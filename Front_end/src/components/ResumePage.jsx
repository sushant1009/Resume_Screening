import React, { useState, useEffect } from "react";
import ResumeCard from "./ResumeCard";
import api from "../config/axiosConfig";
import "../css/ResumePage.css";

const ResumesPage = () => {
  const [resumes, setResumes] = useState([]);
  const [loading, setLoading] = useState(false);

  const role = sessionStorage.getItem("role");

  useEffect(() => {
    fetchResumes();
  }, []);

  const fetchResumes = async () => {
    try {
      setLoading(true);
      let response;

      if (role === "STUDENT") {
        response = await api.get("/api/resume/me");
        console.log(response);
        // alert(response.data)
        setResumes(response.data); // ✅ FIXED
        
      } 
      else if (role === "RECRUITER") {
        response = await api.get("/api/resume/all");
        setResumes(response.data);
      }
    } catch (err) {
      console.error("Error fetching resumes:", err.response?.data || err);
    } finally {
      setLoading(false);
    }
  };

  

  return (
    <div className="resume-container">
      <h1>{role === "STUDENT" ? "My Resumes" : "All Resumes"}</h1>

      {loading ? (
        <p className="no-data">Loading resumes...</p>
      ) : (
        <div className="resume-grid">
          {resumes.length > 0 ? (
            resumes.map((resume) => (
              <ResumeCard
                key={resume._id || resume.id}
                resume={resume}
              />
            ))
          ) : (
            <p className="no-data">No resumes found.</p>
          )}
        </div>

      )}

    </div>
  );
};

export default ResumesPage;
