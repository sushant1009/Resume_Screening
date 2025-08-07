import React from 'react';
import { dummyResumes } from '../data/resumes';

function ResumeUpload({ setResumes }) {
  const handleUpload = (e) => {
    e.preventDefault(); 
    setResumes(dummyResumes); 
    alert("Dummy resumes uploaded.");
  };

  return (
    <form onSubmit={handleUpload}>
      <input type="file" multiple accept=".pdf,.docx" />
      <button type="submit">Upload Resumes</button>
    </form>
  );
}

export default ResumeUpload;
