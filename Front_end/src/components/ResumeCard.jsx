import { useState } from "react";
import React from "react";
import "../css/ResumeCard.css";
import api from '../config/axiosConfig'

const ResumeCard = ({ resume }) => {

  const [resumes, setResumes] = useState([]);

  const [previewUrl, setPreviewUrl] = useState(null);

const previewResume = async (resumeId) => {
  try {
    const res = await api.get(`/api/resume/preview/${resumeId}`, {
      responseType: "blob"
    });
    console.log(res)

    const pdfBlob = new Blob([res.data], { type: "application/pdf" });
    const url = URL.createObjectURL(pdfBlob);

    setPreviewUrl(url);
  } catch (err) {
    alert("Failed to load resume preview"+err);
  }
};

// const deleteResume = async (resumeId) => {
//   if (!window.confirm("Are you sure you want to delete this resume?")) return;

//   try {
//     await api.delete(`/api/resume/${resumeId}`);

//     // Remove from UI instantly
//     setResumes(prev => prev.filter(r => r.id !== resumeId));

//     alert("🗑 Resume deleted successfully");
//   } catch (err) {
//     alert("❌ Failed to delete resume"+err);
//     console.error(err);
//   }
// };
const handleDelete = async (resumeId) => {
  if (!window.confirm("Are you sure you want to delete this resume?")) return;

  try {
    await api.delete(`/api/resume/${resumeId}`);

    // ✅ Instantly refresh UI
    setResumes(prev =>
      prev.filter(resume => resume.id !== resumeId)
    );

    alert("Resume deleted successfully");
  } catch (err) {
    alert("Failed to delete resume");
    console.error(err);
  }
};



  return (
    <div className="resume-card">
      {/* Header */}
      <div className="resume-header">
        <span className="id"><strong>Id:</strong> {resume.id}</span>
     
        <span className="score-badge"><strong>Score: </strong>{resume.score}</span>
      </div>

      {/* File Info */}
      <p className="file-name">
        <strong>File Name:</strong> {resume.originalFileName}
      </p>

      {/* Skills */}
      <div className="skills-section">
        
        {Array.isArray(resume.skills) &&
          resume.skills.map((skill, i) => (
            <span key={i} className="skill-chip">
              <strong>Skills: </strong>
              {skill.replace(/["[\]]/g, " ")}
            </span>
          ))}
      </div>

     
      <div className="meta">
        <span><strong>Role:</strong> {resume.role}</span>
        <span><strong>Experience:</strong>{resume.experience ? Math.floor(resume.experience / 12) : 0} yrs</span>
      </div>

      <button   className="preview-btn"
 onClick={() => previewResume(resume.id)}>
  Preview Resume
</button>

{previewUrl && (
  <iframe
    src={previewUrl}
    width="100%"
    height="400px"
    style={{ borderRadius: "10px", marginTop: "10px" }}
  />
)}

  <button
  className="delete-btn"
  onClick={() => handleDelete(resume.id)}
>
  Delete
</button>



    </div>
  );
};

export default ResumeCard;
