import React, { useState } from "react";
import api from "../../config/axiosConfig";
import "../../css/JobPost.css";

function JobPost() {

  const [form, setForm] = useState({
    role: "",
    jobDescription: "",
    salary,
    skills: "",
    minQualification:"",
    minExperience:"",
    location: "",
    noOpenings: "",
    deadline: "",
  });
  const [loading, setLoading] = useState(false);


  const [errors, setErrors] = useState({});

  // ---------- Handle Change ----------
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  // ---------- Validation ----------
  const validate = () => {
    const newErrors = {};

    if (!form.role.trim())
      newErrors.role = "Job title is required";

    if (!form.noOpenings || form.noOpenings <= 0)
      newErrors.noOpenings = "Enter a valid number of openings";

    if (!form.jobDescription.trim())
      newErrors.jobDescription = "Job description is required";
     if (!form.minQualification.trim())
      newErrors.minQualification = "Qualification is required";
     if (!form.minExperience.trim())
      newErrors.minExperience = "Experience is required";

    if (!form.skills.trim())
      newErrors.skills = "At least one skill is required";

    if (!form.location.trim())
      newErrors.location = "Location is required";

    if (!form.deadline) {
      newErrors.deadline = "Application deadline is required";
    } else {
      const today = new Date();
      today.setHours(0, 0, 0, 0);

      const selectedDate = new Date(form.deadline);
      selectedDate.setHours(0, 0, 0, 0);

      if (selectedDate <= today) {
        newErrors.deadline = "Deadline must be a future date";
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // ---------- Submit ----------
  const handleSubmit = async (e) => {
    e.preventDefault();
 
    if (!validate()) return;

    const payload = {
      ...form,
      noOpenings: Number(form.noOpenings),
      skills: form.skills.split(",")
    };
    

    try {
      setLoading(true);   
      await api.post("/api/jobs/create", payload);
      alert("Job posted successfully!");
      setForm({
        role: "",
        jobDescription: "",
        salary,
        skills: "",
        minQualification:"",
        minExperience:"",
        location: "",
        noOpenings: "",
        deadline: "",
      });

      setErrors({});
    } catch (err) {
      console.error(err);
      alert("Failed to create job");
    } finally {
    setLoading(false);            
  }
  };

  return (
    <div className="form-container">
      <h2>Post a Job</h2>

      <form onSubmit={handleSubmit}>

        <input
          type="text"
          name="role"
          placeholder="Job Title"
          value={form.role}
          onChange={handleChange}
        />
        {errors.role && <p className="error">{errors.role}</p>}

        <input
          type="number"
          name="noOpenings"
          placeholder="Number of Openings"
          min={1}
          value={form.noOpenings}
          onChange={handleChange}
        />
        {errors.noOpenings && <p className="error">{errors.noOpenings}</p>}
        <input
          type="number"
          name="salary"
          placeholder="Salary"
          min={0}
          value={form.salary}
          onChange={handleChange}
        />
        <textarea
          name="jobDescription"
          placeholder="Job Description"
          value={form.jobDescription}
          onChange={handleChange}
        />
        {errors.jobDescription && <p className="error">{errors.jobDescription}</p>}

        <input
          type="text"
          name="skills"
          placeholder="Required Skills (comma separated)"
          value={form.skills}
          onChange={handleChange}
        />
        {errors.skills && <p className="error">{errors.skills}</p>}

        <input
          type="text"
          name="minQualification"
          placeholder="Minimum Required Qualification"
          value={form.minQualification}
          onChange={handleChange}
        />
        {errors.minQualification && <p className="error">{errors.minQualification}</p>}


        <input
          type="text"
          name="minExperience"
          placeholder="Minimum Required Experience(yrs.)"
          value={form.minExperience}
          onChange={handleChange}
        />
        {errors.minExperience && <p className="error">{errors.minExperience}</p>}

        <input
        type="text"
        name="location"
        placeholder="Job Location"
        value={form.location}
        onChange={handleChange}
       />
       {errors.location && <p className="error">{errors.location}</p>}


        <input
          type="date"
          name="deadline"
          min={new Date().toISOString().split("T")[0]}
          value={form.deadline}
          onChange={handleChange}
        />
        {errors.deadline && <p className="error">{errors.deadline}</p>}

        <button type="submit" disabled={loading}>
        {loading ? <span className="spinner"></span> : "Post Job"}
        </button>
      </form>
    </div>
  );
}

export default JobPost;
