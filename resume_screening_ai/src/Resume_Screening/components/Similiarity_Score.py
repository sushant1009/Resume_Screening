from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity
import re
import nltk
from nltk.corpus import stopwords 

model = SentenceTransformer('all-MiniLM-L6-v2')

def compute_role_similarity(resume_text, role_title):
    resume_emb = model.encode([resume_text], convert_to_tensor=True)
    role_emb = model.encode([role_title], convert_to_tensor=True)
    similarity = cosine_similarity(resume_emb, role_emb)
    return float(similarity[0][0]*100) 

def clean_text(text):
    nltk.download('stopwords')
    stop_words = set(stopwords.words('english'))
    text = text.lower()
    text = re.sub(r'[^a-zA-Z ]', '', text)  
    text = " ".join([word for word in text.split() if word not in stop_words])
    return text