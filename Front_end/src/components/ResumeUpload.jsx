import React, { useState } from 'react';
import axios from 'axios';
import '../css/ResumeUpload.css'; 
import api from '../config/axiosConfig'

function ResumeUpload({ setResumes }) {
  const [resume, setResume] = useState({
    role : '',
    resumefile: null,
  });

  const handleChange = (e) => {
    const { name, value, files } = e.target;

    if (name === 'resumefile') {
      setResume({ ...resume, resumefile: files[0] }); 
    }else{
      setResume({ ...resume, [name]: value });
    }
  };

  const handleUpload = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append('resume', resume.resumefile);
    formData.append('role', resume.role); 

    try {
      const res = await api.post('/api/resume/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      alert('Resume uploaded.');
      
      if (setResumes) setResumes((prev) => [...prev, res.data]);
    } catch (err) {
      alert('Failed to upload resume.');
      console.error(err);
    }
  };

  return (
    <form onSubmit={handleUpload} className="resume-upload-container">
       <input
        type="text"
        name="role"
        placeholder="Role"
        value={resume.role}
        onChange={handleChange}
        required
      />

      <input
        type="file"
        name="resumefile"
        accept=".pdf,.docx"
        onChange={handleChange}
        required
      />

      
      

      <button type="submit">Upload Resume</button>
    </form>
  );
}

export default ResumeUpload;
