from src.Resume_Screening.config.configuration import ConfigurationManager
from src.Resume_Screening.components.Data_Ingestion import Data_Ingestion
from src.Resume_Screening import logger

STAGE_NAME = "Data Ingestion"

class DataIngestionTrainingPipeline:
    def __init__(self):
        pass
    
    def main(self):
        config = ConfigurationManager()
        data_ingestion_config = config.get_data_ingestion_config()
        data_ingestion = Data_Ingestion(config=data_ingestion_config)
        data_ingestion.download_file()
        data_ingestion.extract_zip_file()