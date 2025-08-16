import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import ResumeUpload from './components/ResumeUpload';
import SignupForm from './components/SignupForm';
import LoginForm from './components/LoginForm';
import ResumePage from './components/ResumePage';
import { dummyResumes } from './data/resumes';
import './css/App.css'; 

function App() {
  const [resumes, setResumes] = useState(dummyResumes);

  return (
    <Router>
      <div>
        {/* Navigation Links */}
        <nav style={{ textAlign: "center", marginTop: "20px" }}>
          <Link to="/login" style={{ marginRight: "20px" }}>Login</Link>
          <Link to="/signup" style={{ marginRight: "20px" }}>Signup</Link>
          <Link to="/resumeupload" style={{ marginRight: "20px" }}>Upload Resume</Link>
          <Link to="/getresume" style={{ marginRight: "20px" }}>Get Resume</Link>
        </nav>

        {/* Routes */}
        <Routes>
          <Route path="/login" element={<div className='container'><LoginForm /></div>} />
          <Route path="/signup" element={<div className='container'><SignupForm /></div>} />
          <Route path="/resumeupload" element={<div className='container'><ResumeUpload /></div>} />
          <Route path="/getresume" element={<div className='container'><ResumePage /></div>} />
          <Route path="/" element={<LoginForm />} /> {/* Default route */}
        </Routes>
      </div>
    </Router>
  );
}

export default App;
