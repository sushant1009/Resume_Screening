import os

project_name = "resume_screening_ai"

# Define folder structure
folders = [
    f"{project_name}/data/raw",
    f"{project_name}/data/processed",
    f"{project_name}/data/interim",
    f"{project_name}/models",
    f"{project_name}/notebooks",
    f"{project_name}/references",
    f"{project_name}/reports/figures",
    f"{project_name}/src/data",
    f"{project_name}/src/features",
    f"{project_name}/src/models",
    f"{project_name}/src/visualization",
    f"{project_name}/tests"
]

# Define template files to create
files = {
    f"{project_name}/README.md": "# Resume Screening AI\n\nThis project uses machine learning to score and rank resumes.",
    f"{project_name}/requirements.txt": "# Add your dependencies here\npandas\nscikit-learn\n",
    f"{project_name}/.gitignore": "*.pyc\n__pycache__/\n.env\nmodels/\n",
    f"{project_name}/setup.py": f"""from setuptools import find_packages, setup

setup(
    name="{project_name}",
    version="0.1.0",
    packages=find_packages(),
    include_package_data=True,
    description="MLOps-ready Resume Screening AI project.",
)
"""
}

# Create folders
for folder in folders:
    os.makedirs(folder, exist_ok=True)

# Create template files
for file_path, content in files.items():
    with open(file_path, "w") as f:
        f.write(content)

print(f"✅ MLOps project '{project_name}' created successfully.")
