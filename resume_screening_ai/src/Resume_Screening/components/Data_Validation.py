from src.Resume_Screening import logger
from src.Resume_Screening.entity.config_entity import DataValidationConfig
from src.Resume_Screening.utils.common import read_yaml, get_size, create_directory
from pathlib import Path
import os
import sys
import pandas as pd

class Data_Validation :
    def __init__(self, config : DataValidationConfig):
        self.config = config 
        
    def validate_columns(self) -> bool:
        validation_status = None
        data = pd.read_csv(self.config.unzip_data_dir)
        all_cols = list(data.columns)
        all_schema = self.config.all_schema.keys()
        for col in all_cols:
            if col not in all_schema:
                validation_status = False 
                print(f"{col} is not present {validation_status}")
                  
            else:
                validation_status = True
        
        with open(self.config.status_file, 'w') as f:
                    f.write(f"Validation Status : {validation_status}")
        
        return validation_status