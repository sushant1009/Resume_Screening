import React, { useState } from "react";
import axios from "axios";
import '../css/LoginForm.css';

const LoginForm = () => {
  const [form, setForm] = useState({
    username: "",
    password: ""
  });

  const [role, setRole] = useState("STUDENT")

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };
  

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if(role == "STUDENT")
      {
  const res = await axios.post(
    "http://localhost:8080/api/auth/login/user",
    form
    // { withCredentials: true } // allow browser to store cookie
  );
  alert("Login successful!");
  console.log(res.data);
  }
  else if(role == "RECRUITER")
      {
  const res = await axios.post(
    "http://localhost:8080/api/auth/login/recruiter",
    form
    // { withCredentials: true } // allow browser to store cookie
  );
  alert("Login successful!");
  console.log(res.data);
  }
} catch (err) {
  alert("Invalid credentials");
  console.error(err);
}

  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h2>Login</h2>
          <select name="role" value={role} onChange={(e)=>setRole(e.target.value)} required>
            <option value="STUDENT">Student</option>
            <option value="RECRUITER">Recruiter</option>
          </select>
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
      </form>
    </div>
  );
};

export default LoginForm;
