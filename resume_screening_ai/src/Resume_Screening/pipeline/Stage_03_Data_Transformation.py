from src.Resume_Screening.config.configuration import ConfigurationManager
from src.Resume_Screening.components.Data_Transformation import DataTransformation
from src.Resume_Screening import logger
from src.Resume_Screening.components.Similiarity_Score import compute_role_similarity
from src.Resume_Screening.constants.skills import skills_list


STAGE_NAME = "Data Transformation stage"

class DataTransformationTrainingPipeline:
    def __init__(self):
        pass
    
    def main(self):
        try:
            config = ConfigurationManager()
            data_transformation_config = config.get_transformation_config()
            data_transformation = DataTransformation(config=data_transformation_config)
            data = data_transformation.load_data()
            data['cleaned_text'] = data['Resume'].apply(data_transformation.clean_text)
            data['extracted_skills'] = data['cleaned_text'].apply(lambda x: data_transformation.extract_skills_from_cleaned_text(x, skills_list))
            data['experience_months'] = data['Resume'].apply(data_transformation.extract_experience_in_months)
            role_skill_map =  data_transformation.compute_skill_match(data)
            data[['matching_skill_count', 'matching_skill_percent']] = data.apply( lambda row: data_transformation.get_matching_skills(row, role_skill_map),
            axis=1)
            data[['matching_skill_count', 'matching_skill_percent', 'experience_score', 'resume_score']] = data.apply(data_transformation.compute_resume_score,axis=1,role_skill_map=role_skill_map
        )
            data['role_similarity_score'] =  data.apply(
    lambda row: compute_role_similarity(row['Resume'], row['Category']),
    axis=1
)         
           
            data.to_csv(data_transformation_config.transformed_data_path, index=False) 
            data_transformation.train_test_splitting()

        except Exception as e :
            raise e