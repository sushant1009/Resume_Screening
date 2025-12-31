import React, { useEffect, useState } from "react";
import api from "../../config/axiosConfig";
import { useParams, useNavigate } from "react-router-dom";
import "../../css/ApplyJob.css";

function ApplyJob() {
  const { jobId } = useParams();
  const navigate = useNavigate();

  const [resumes, setResumes] = useState([]);
  const [selectedResume, setSelectedResume] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchResumes();
  }, []);

  const fetchResumes = async () => {
    try {
      const res = await api.get("/api/resume/me");
      setResumes(res.data);
    } catch (err) {
      alert("Failed to load resumes");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!selectedResume) {
      alert("Please select a resume");
      return;
    }

    try {
        console.log(jobId, selectedResume)
      setLoading(true);
      await api.post("/api/applications/apply", {
        job : {jobId : jobId},
        resumeId: selectedResume
      });

      alert("✅ Application submitted successfully!");
      navigate("/joblist");

    } catch (err) {
      if (err.response?.status === 409) {
        alert("⚠️ Already applied");
      } else {
        alert("❌ Failed to apply");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="apply-container">
      <h2>Apply for Job</h2>

      <form onSubmit={handleSubmit}>

        {/* Resume dropdown */}
        <label>Select Resume</label>
        <select
          value={selectedResume}
          onChange={(e) => setSelectedResume(e.target.value)}
          required
        >
          <option value="">-- Select Resume --</option>

          {resumes.map((resume) => (
            <option key={resume.id} value={resume.id}>
              {resume.fileName} | Score: {resume.score} | Exp: {resume.experience / 12} yrs | <p><strong>File:</strong> {resume.originalFileName}</p>
            </option>
          ))}
        </select>

        <button type="submit" disabled={loading}>
          {loading ? "Submitting..." : "Apply"}
        </button>
      </form>
    </div>
  );
}

export default ApplyJob;
