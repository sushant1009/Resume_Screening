from flask import Flask, request, jsonify
import os
from pdfminer.high_level import extract_text
import sys
import json
import re
from src.Resume_Screening.components.Similiarity_Score import compute_role_similarity, clean_text
from src.Resume_Screening.constants.skills import skills_list

app = Flask(__name__)

# -------------------
# Your existing analysis functions here
# -------------------
def analyze_resume(text, role):
   cleaned_text = clean_text(text)
   score = compute_role_similarity(text, role)
   return score

def extract_experience(text):
        text = text.lower()

        # Extract years
        year_matches = re.findall(r'(\d+(?:\.\d+)?)\s*(?:\+)?\s*(?:years?|yrs?|yr)', text)
        total_years = sum(float(y) for y in year_matches)

        # Extract months
        month_matches = re.findall(r'(\d+(?:\.\d+)?)\s*(?:months?|mos?)', text)
        total_months = sum(float(m) for m in month_matches)

        return round(total_years  + (total_months/12))
    
def extract_skills_from_cleaned_text(cleaned_text, skills_list):
        found_skills = []
        for skill in skills_list:
            if skill in cleaned_text:
                found_skills.append(skill)
        return found_skills
    

# Extract text from PDF
def extract_pdf_text(pdf_path):
    try:
        text = extract_text(pdf_path)
        return text.strip()
    except Exception as e:
        return f"Error extracting text: {str(e)}"

@app.route("/process_resume", methods=["POST"])
def process_resume():
    if "file" not in request.files:
        return jsonify({"error": "No file uploaded"}), 400
    if "role" not in request.form:
        return jsonify({"error": "No file uploaded"}), 400

    file = request.files["file"]
    role = request.form["role"]
    file_path = os.path.join("uploads", file.filename)
    os.makedirs("uploads", exist_ok=True)
    file.save(file_path)

    # Step 1: Extract text
    extracted_text = extract_pdf_text(file_path)
    
    cleaned_text = clean_text(extracted_text)

    # Step 2: Analyze resume (your functions)
    resume_score = analyze_resume(cleaned_text, role)
    experience = extract_experience(cleaned_text)
    skills = extract_skills_from_cleaned_text(cleaned_text,skills_list)
    skills = [s.replace("'", "") for s in skills]
    print(skills)
    # Step 3: Return response
    return jsonify({
        "experience" : experience,
        "score": resume_score,
        "skills" : skills
    })

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001)
