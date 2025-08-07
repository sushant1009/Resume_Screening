from src.Resume_Screening import logger
from dataclasses import dataclass
from pathlib import Path
import urllib.request as request
from src.Resume_Screening.utils.common import read_yaml, create_directory,get_size
from src.Resume_Screening.constants import *
from src.Resume_Screening.entity.config_entity import DataIngestionConfig
import os
import zipfile

class Data_Ingestion:
    def __init__(self, config = DataIngestionConfig):
        self.config = config
    
    def download_file(self):
        if not os.path.exists(self.config.local_data_file):
            filename, headers = request.urlretrieve(
                url = self.config.source_URL,
                filename = self.config.local_data_file
            )
            logger.info(f"{filename} download! with following info: \n{headers}")
        else:
            logger.info(f"File already exists of size: {get_size(Path(self.config.local_data_file))}")



    def extract_zip_file(self):
        unzip_path = self.config.unzip_dir
        os.makedirs(unzip_path, exist_ok=True)
        with zipfile.ZipFile(self.config.local_data_file, 'r') as zip_ref:
            zip_ref.extractall(unzip_path)