from src.Resume_Screening import logger
from dataclasses import dataclass
from pathlib import Path
import urllib.request as request
from src.Resume_Screening.utils.common import read_yaml, create_directory,get_size
from src.Resume_Screening.constants import *
from src.Resume_Screening.entity.config_entity import DataTransformationConfig
from src.Resume_Screening.constants.skills import skills_list
from sklearn.model_selection import train_test_split


import pandas as pd
import numpy as np
import re
import nltk
from nltk.corpus import stopwords 
import os
import zipfile


class DataTransformation:
    def __init__(self, config : DataTransformationConfig):
        self.config = config
        self.skills_list = skills_list or []
        nltk.download('stopwords')
        self.stop_words = set(stopwords.words('english'))
    
    def load_data(self) :
        df = pd.read_csv(self.config.data_path)
        return df
    
    def clean_text(self,text):
        text = text.lower()
        text = re.sub(r'[^a-zA-Z ]', '', text)  
        text = " ".join([word for word in text.split() if word not in self.stop_words])
        return text
    
    def extract_skills_from_cleaned_text(self, cleaned_text, skills_list):
        found_skills = []
        for skill in skills_list:
            if skill in cleaned_text:
                found_skills.append(skill)
        return found_skills
    
    def extract_experience_in_months(self, text):
        text = text.lower()

        # Extract years
        year_matches = re.findall(r'(\d+(?:\.\d+)?)\s*(?:\+)?\s*(?:years?|yrs?|yr)', text)
        total_years = sum(float(y) for y in year_matches)

        # Extract months
        month_matches = re.findall(r'(\d+(?:\.\d+)?)\s*(?:months?|mos?)', text)
        total_months = sum(float(m) for m in month_matches)

        return round(total_years * 12 + total_months)

    def extract_skills(self, text):
        found_skills = []
        text = text.lower()
        for skill in self.skills_list:
            if skill.lower() in text:
                found_skills.append(skill)
        return list(set(found_skills)) 
    
    def compute_skill_match(self, df, category_column='Category', skill_column='extracted_skills'):
    
        role_skill_map = (
            df.groupby(category_column)[skill_column]
            .sum()  # Combine all skill lists
            .apply(lambda x: list(set(x)))  # Remove duplicates
            .to_dict()
        )
        return role_skill_map
    
    def get_matching_skills(self,row,role_skill_map):
        role_skills = set(role_skill_map.get(row['Category'], []))
        resume_skills = set(row['extracted_skills'])
        match_count = len(role_skills.intersection(resume_skills))
        total_role_skills = len(role_skills)
        match_percent = (match_count / total_role_skills * 100) if total_role_skills > 0 else 0
        return pd.Series([match_count, round(match_percent, 2)])
    
    def get_matching_skills(self,row,role_skill_map):
        role_skills = set(role_skill_map.get(row['Category'], []))
        resume_skills = set(row['extracted_skills'])
        match_count = len(role_skills.intersection(resume_skills))
        total_role_skills = len(role_skills)
        match_percent = (match_count / total_role_skills * 100) if total_role_skills > 0 else 0
        return pd.Series([match_count, round(match_percent, 2)])
    
    
    def compute_resume_score(self, row, role_skill_map, max_exp_months=140, skill_weight=0.7, exp_weight=0.3):
        role_skills = set(role_skill_map.get(row['Category'], []))
        resume_skills = set(row['extracted_skills'])

        match_count = len(role_skills.intersection(resume_skills))
        total_role_skills = len(role_skills)
        skill_match_percent = (match_count / total_role_skills * 100) if total_role_skills > 0 else 0

        exp_months = row.get('experience_months', 0)
        exp_score = min(exp_months / max_exp_months * 100, 100)

        final_score = (skill_match_percent * skill_weight) + (exp_score * exp_weight)

        return pd.Series([
            match_count,
            round(skill_match_percent, 2),
            round(exp_score, 2),
            round(final_score, 2)
        ])
    
    def train_test_splitting(self):
        data = pd.read_csv(self.config.transformed_data_path)
        data = data.drop(columns=['Category', 'Resume' ,'extracted_skills','cleaned_text'], errors='ignore')
        data['shortlisted'] = (
    (data['experience_score'] >= 0.6) & 
    (data['resume_score'] >= 0.6) & 
    (data['role_similarity_score'] >= 0.7)
).astype(int)
        train, test = train_test_split(data, test_size=0.20, random_state=42)
        
        train.to_csv(os.path.join(self.config.root_dir, "train.csv"), index = False)
        test.to_csv(os.path.join(self.config.root_dir, "test.csv"), index = False)
        
        logger.info("Splitted data into Training and Testing Sets")
        logger.info(f"Train data shape : {train.shape}")
        logger.info(f"Test data shape : {test.shape}")
    
