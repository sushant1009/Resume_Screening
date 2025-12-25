import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../css/Navigation.css";

const Navigation = () => {
  const role = sessionStorage.getItem("role");
  const [open, setOpen] = useState(false);
  const navigate = useNavigate();

  const logout = () => {
    sessionStorage.clear();
    navigate("/login");
  };

  return (
    <nav className="navbar">
      {/* Logo */}
      <div className="nav-logo">ResumePortal</div>

      {/* Hamburger (Mobile) */}
      <div className="hamburger" onClick={() => setOpen(!open)}>
        ☰
      </div>

      {/* Links */}
      <div className={`nav-links ${open ? "open" : ""}`}>
        {!role && (
          <>
            <Link to="/login" onClick={() => setOpen(false)}>Login</Link>
            <Link to="/signup" onClick={() => setOpen(false)}>Signup</Link>
          </>
        )}

        {role === "STUDENT" && (
          <>
            <Link to="/resumeupload" onClick={() => setOpen(false)}>Upload Resume</Link>
            <Link to="/getresume" onClick={() => setOpen(false)}>My Resume</Link>
            <Link to="/joblist" onClick={() => setOpen(false)}>Jobs</Link>
            <button className="logout-btn" onClick={logout}>
              ⏻ Logout
            </button>
          </>
        )}

        {role === "RECRUITER" && (
          <>
            <Link to="/jobpost" onClick={() => setOpen(false)}>Post Job</Link>
            <Link to="/joblist" onClick={() => setOpen(false)}>Jobs</Link>
            <Link to="/receivedapplications" onClick={() => setOpen(false)}>
              Applications
            </Link>
            <button className="logout-btn" onClick={logout}>
              ⏻ Logout
            </button>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navigation;
