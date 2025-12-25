import React, { useEffect, useState } from "react";
import axios from "axios";
import api from '../config/axiosConfig'
import '../css/ReceivedApplications.css'
import ResumeCard from "./ResumeCard";

export default function ReceivedApplications() {
  const [applications, setApplications] = useState([]);
  const [jobId, setJobId] = useState("");
  const [loading, setLoading] = useState(false);
  const STATUS_OPTIONS = ["PENDING", "SHORTLISTED", "REJECTED"];
  const [statusMap, setStatusMap] = useState({});
  const [previewUrl, setPreviewUrl] = useState(null);


  const role = sessionStorage.getItem("role");

  useEffect(() => {
  const map = {};
  applications.forEach(app => {
    map[app.id] = app.status; 
  });
  setStatusMap(map);
}, [applications]);


  // 🔐 Fetch applications by jobId
  const fetchByJobId = async () => {
    if (!jobId.trim()) {
      alert("Enter Job ID");
      return;
    }

    try {
      setLoading(true);
      const res = await api.get(`/api/applications/${jobId}`);
      console.log(res)
      setApplications(res.data);
    } catch (err) {
      console.error("Error fetching job applications", err);
    } finally {
      setLoading(false);
    }
  };

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

  const handleStatusChange = async (appId, newStatus) => {
  try {
    setStatusMap(prev => ({
      ...prev,
      [appId]: newStatus
    }));

    await api.put(`/api/applications/${appId}/status`, {
      status: newStatus
    });
    alert("Updated Status : "+newStatus)

  } catch (err) {
    alert("Failed to update status "+err);
  }
};  


  if (role !== "RECRUITER") {
    return <p>Access denied</p>;
  }

  return (
    <div className="app-container">
      <h2>Received Applications</h2>

      <div className="filter-group">
        <input
          type="text"
          placeholder="Enter Job ID"
          value={jobId}
          onChange={(e) => setJobId(e.target.value)}
        />
        <button onClick={fetchByJobId}>Fetch By Job</button>
      </div>

      {loading ? (
        <p>Loading applications...</p>
      ) : applications.length > 0 ? (
        <div className="app-cards">
  {applications.map((app) => (
    
    <div className="app-card" key={app.id}>
      <h3>{app.job.role}</h3>

      <p><strong>Candidate Name :</strong> {app.user.fname+" "+app.user.lname}</p>
      <p><strong>Candidate Email :</strong> {app.user.email}</p>
      <p><strong>Experience(yrs) :</strong> {}</p>
      <p><strong>Resume Score  :</strong> {app.resume.score}</p>
       <div>
        
        {Array.isArray(app.resume.skills) &&
          app.resume.skills.map((skill, i) => (
            <span key={i} className="skill-chip">
              <strong>Skills: </strong>
              {skill.replace(/["[\]]/g, " ").toUpperCase()}
            </span>
          ))}
      </div>
      <div className="status-wrapper">
  <label><strong>Status</strong></label>
  <select
         className="status-select"
          value={statusMap[app.id] || app.status}
          onChange={(e) =>
            handleStatusChange(app.id, e.target.value)
          }
        >
          {STATUS_OPTIONS.map(status => (
            <option key={status} value={status}>
              {status}
            </option>
          ))}
        </select>
         <button   className="preview-btn"
 onClick={() => previewResume(app.resume.id)}>
  View Resume
</button>

{previewUrl && (
  <iframe
    src={previewUrl}
    width="100%"
    height="400px"
    style={{ borderRadius: "10px", marginTop: "10px" }}
  />
)}
</div>
     
    </div>
  ))}
</div>


      ) : (
        <p>No applications found</p>
      )}
    </div>
  );
}
