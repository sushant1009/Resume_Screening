import React, { useState } from "react";
import axios from "axios";
import "../css/SignupForm.css";
import api from "../config/axiosConfig";
import { useNavigate } from "react-router-dom";

const SignupForm = () => {
  const [form, setForm] = useState({
    fname: "",
    lname: "",
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
    companyName: ""
  });

  const navigate = useNavigate();
  const [role, setRole] = useState("STUDENT");
  const [errors, setErrors] = useState({});
  const [success, setSuccess] = useState("");

  // Validation for each field dynamically
  const validateField = (name, value) => {
    let error = "";

    switch (name) {
      case "fname":
        if (!value.trim()) error = "First name is required";
        break;
      case "lname":
        if (!value.trim()) error = "Last name is required";
        break;
      case "username":
        if (!value.trim()) error = "Username is required";
        break;
      case "email":
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value))
          error = "Invalid email format";
        break;
      case "password":
        if (value.length < 6)
          error = "Password must be at least 6 characters long";
        break;
      case "confirmPassword":
        if (value !== form.password) error = "Passwords do not match";
        break;
      case "companyName":
        if (role === "RECRUITER" && !value.trim())
          error = "Company name is required";
        break;
      default:
        break;
    }

    setErrors((prev) => ({ ...prev, [name]: error }));
  };

  // Handle input change dynamically
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
    validateField(name, value);
  };

  // Final validation before submit
  const validateForm = () => {
    let valid = true;
    Object.entries(form).forEach(([name, value]) => {
      validateField(name, value);
      if (errors[name]) valid = false;
    });
    return valid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    try {
      let endpoint =
        role === "STUDENT"
          ? "/api/auth/signup/user"
          : "/api/auth/signup/recruiter";

      const res = await api.post(endpoint, form);
      if(res.status === 200)
      {
        setSuccess("🎉 Signup successful! Redirecting to login...");
      setForm({
        fname: "",
        lname: "",
        username: "",
        email: "",
        password: "",
        confirmPassword: "",
        companyName: ""
      });
      navigate('/login')
      }
      else if (res.status === 409) {
    const { field, message } = res.data;
    setErrors({ [field]: message });
  }
      setRole("STUDENT");
      setErrors({});
      setTimeout(() => setSuccess(""), 3000); // Clear message after 3s
      } 
      catch (err) {
        
    if(err.response.data.message =="Duplicate value")
    {
        alert("Username or Email Already exists");
    }
    else{
      alert("Something went wrong");
    }
  
    }
  };

  return (
    <div className="signup-container">
      <form className="signup-form" onSubmit={handleSubmit}>
        <h2>Create Account</h2>

        {success && <p className="success">{success}</p>}
        {errors.api && <p className="error">{errors.api}</p>}

        <input
          name="fname"
          placeholder="First Name"
          value={form.fname}
          onChange={handleChange}
        />
        {errors.fname && <p className="error">{errors.fname}</p>}

        <input
          name="lname"
          placeholder="Last Name"
          value={form.lname}
          onChange={handleChange}
        />
        {errors.lname && <p className="error">{errors.lname}</p>}

        <input
          name="username"
          placeholder="Username"
          value={form.username}
          onChange={handleChange}
        />
        {errors.username && <p className="error">{errors.username}</p>}

        <input
          type="email"
          name="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
        />
        {errors.email && <p className="error">{errors.email}</p>}

        <input
          type="password"
          name="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
        />
        {errors.password && <p className="error">{errors.password}</p>}

        <input
          type="password"
          name="confirmPassword"
          placeholder="Confirm Password"
          value={form.confirmPassword}
          onChange={handleChange}
        />
        {errors.confirmPassword && (
          <p className="error">{errors.confirmPassword}</p>
        )}

        <select
          name="role"
          value={role}
          onChange={(e) => setRole(e.target.value)}
        >
          <option value="STUDENT">Student</option>
          <option value="RECRUITER">Recruiter</option>
        </select>

        {role === "RECRUITER" && (
          <>
            <input
              name="companyName"
              placeholder="Company Name"
              value={form.companyName}
              onChange={handleChange}
            />
            {errors.companyName && (
              <p className="error">{errors.companyName}</p>
            )}
          </>
        )}

        <button type="submit" disabled={Object.values(errors).some((e) => e)}>
          Sign Up
        </button>
      </form>
    </div>
  );
};

export default SignupForm;
