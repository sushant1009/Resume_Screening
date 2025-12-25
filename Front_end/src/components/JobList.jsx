import React, { useEffect, useState } from "react";
import axios from "axios";
import api from "../config/axiosConfig";
import { useNavigate } from "react-router-dom";
import '../css/JobList.css';

function JobList() {
  const [jobs, setJobs] = useState([]);
  const [filter, setFilter] = useState("");
  const [application, setApplication] = useState({
    user :{
      username : null,
    },
    job :{
      jobId : null,
    }
  })
  useEffect(() => {
    fetchJobs();
  }, []);

  const navigate = useNavigate();

function applyForJob(id) {
  navigate(`/apply/${id}`);
}


  const fetchJobs = async () => {
    try {
     const res = await api.get("/api/jobs/");
      console.log(res.data);
      setJobs(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const filteredJobs = jobs.filter((job) =>
    job.role.toLowerCase().includes(filter.toLowerCase())
  );
  
//   async function applyForJob(id) {
  

//   const newApp = {
//     job: { jobId: id }
//   };

//   setApplication(newApp);

//   try {
//   const res = await api.post("/api/applications/apply", newApp);

//   alert("✅ Application submitted successfully!");

// } catch (err) {

//   if (!err.response) {
//     // Server not running / network error
//     alert("❌ Server is not reachable. Please try again later.");
//     return;
//   }

//   const status = err.response.status;

//   if (status === 401) {
//     alert("⚠️ Please login to apply for the job.");
//   }
//   else if (status === 403) {
//     alert("You are not authorized to apply for this job.");
//   }
//   else if (status === 404) {
//     alert("Job not found.");
//   }
//   else if (status === 409) {
//     alert("⚠️ You have already applied for this job.");
//   }
//   else if (status === 500) {
//     alert("Something went wrong on the server.");
//   }
//   else {
//     alert("Failed to apply. Please try again.");
//   }

//   console.error("Apply job error:", err);
// }

// }
  return (
    <div className="job-list-container">
      <h2>Available Jobs</h2>
      <input
        type="text"
        placeholder="Filter by Job Title..."
        value={filter}
        onChange={(e) => setFilter(e.target.value)}
        className="filter-input"
      />

      <div className="job-cards">
        {filteredJobs.map((job) => (
          <div className="job-card" key={job.jobId}>
            <h3>{job.title}</h3>
            <h2>{job.recruiter.companyName}</h2>
             <p><strong>Id:</strong> {job.jobId}</p>
            <p><strong>Role:</strong> {job.role}</p>
             <p><strong>Salary(INR):</strong> {job.salary == null ? 0 :job.salary}</p>
            <p><strong>No. Openings:</strong> {job.noOpenings}</p>
            <p><strong>Required Experience:</strong> {job.minExperience}</p>
            <p><strong>Required Qualification:</strong> {job.minQualification}</p>
            <p><strong>Description:</strong> {job.jobDescription}</p>
            <p><strong>Skills:</strong> <i>{Array.isArray(job.skills) ? job.skills.join(", ") : job.skills}</i></p>
            <p><strong>Location:</strong> {job.location}</p>
             <p><strong>Deadline:</strong>  {new Date(job.deadline).toLocaleDateString()}</p>
            <button onClick={() => applyForJob(job.jobId)}>Apply</button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default JobList;
