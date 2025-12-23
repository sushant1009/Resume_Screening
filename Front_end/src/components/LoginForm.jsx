import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import '../css/LoginForm.css';

const LoginForm = () => {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    username: "",
    password: ""
  });

  const [error, setError] = useState("");

  // Handle input changes
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // Handle login form submit
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      // Call backend login API
      const res = await axios.post(
        "http://localhost:8080/api/auth/login",
        form,
        {
          headers: { "Content-Type": "application/json" }
        }
      );

      // Backend must return { token: "JWT_TOKEN", role: "USER/RECRUITER" }
      const { token, role } = res.data;

      if (!token || !role) {
        setError("Invalid backend response");
        return;
      }

      // Map backend role to frontend role
      const mappedRole = role === "USER" ? "STUDENT" : role;

      // ✅ Store JWT and role in sessionStorage
      sessionStorage.setItem("token", token);
      sessionStorage.setItem("role", mappedRole);

      // Redirect based on role
      if (mappedRole === "RECRUITER") navigate("/joblist");
      else if (mappedRole === "STUDENT") navigate("/resumeupload");

    } catch (err) {
      console.log(err)
       if (!err.response) {
    setError("Server is under maintainance. Please try again later.");
  } else if (err.response.status === 403) {
   setError("Invalid username or password");
  } else {
    alert("Something went wrong. Please try again.");
  }
    }
  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h2>Login</h2>

        <input
          type="text"
          name="username"
          placeholder="Username"
          value={form.username}
          onChange={handleChange}
          required
        />

        <input
          type="password"
          name="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          required
        />

        <button type="submit">Login</button>

        {error && <p style={{ color: "red", marginTop: "10px" }}>{error}</p>}
      </form>
    </div>
  );
};

export default LoginForm;
