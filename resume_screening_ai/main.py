from src.Resume_Screening import logger
from src.Resume_Screening.pipeline.Stage_01_Data_Ingestion import DataIngestionTrainingPipeline
from src.Resume_Screening.pipeline.Stage_02_Data_Validation import DataValidationTrainingPipeline
from src.Resume_Screening.pipeline.Stage_03_Data_Transformation import DataTransformationTrainingPipeline
from src.Resume_Screening.pipeline.Stage_04_Model_Training import ModelTrainingPipeline

STAGE_NAME = "Data Ingestion"
try:
   logger.info(f">>>>>> stage {STAGE_NAME} started <<<<<<") 
   data_ingestion = DataIngestionTrainingPipeline()
   data_ingestion.main()
   logger.info(f">>>>>> stage {STAGE_NAME} completed <<<<<<\n\nx==========x")
except Exception as e:
    logger.exception(e)
    raise e
 

STAGE_NAME = "Data Validation"
try:
   logger.info(f">>>>>> stage {STAGE_NAME} started <<<<<<")
   data_validation = DataValidationTrainingPipeline()
   data_validation.main()
   logger.info(f">>>>>> stage {STAGE_NAME} completed <<<<<<\n\nx==========x")
except Exception as e:
   logger.exception(e)
   raise e

STAGE_NAME = "Data Transformation"
try:
   logger.info(f">>>>>> stage {STAGE_NAME} started <<<<<<")
   data_transformation = DataTransformationTrainingPipeline()
   data_transformation.main()
   logger.info(f">>>>>> stage {STAGE_NAME} completed <<<<<<\n\nx==========x")
except Exception as e:
   logger.exception(e)
   raise e

STAGE_NAME = "Model Training"
try:
   logger.info(f">>>>>> stage {STAGE_NAME} started <<<<<<")
   model_training = ModelTrainingPipeline()
   model_training.main()
   logger.info(f">>>>>> stage {STAGE_NAME} completed <<<<<<\n\nx==========x")
except Exception as e:
   logger.exception(e)
   raise e