from src.Resume_Screening.config.configuration import ConfigurationManager
from src.Resume_Screening.components.Data_Validation import Data_Validation
from src.Resume_Screening import logger



STAGE_NAME = "Data Validation stage"

class DataValidationTrainingPipeline:
    def __init__(self):
        pass
    
    def main(self):
        config = ConfigurationManager()
        data_validation_config = config.get_data_validation_config()
        data_validation = Data_Validation(config = data_validation_config)
        data_validation.validate_columns()