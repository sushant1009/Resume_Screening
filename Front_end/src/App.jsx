import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import ResumeUpload from './components/ResumeUpload';
import ResumeList from './components/ResumeList';
import SignupForm from './components/SingUpForm';
import LoginForm from './components/LoginForm';
import { dummyResumes } from './data/resumes';

function App() {
  const [resumes, setResumes] = useState(dummyResumes);

  return (
    <Router>
      <div>
        {/* Navigation Links */}
        <nav style={{ textAlign: "center", marginTop: "20px" }}>
          <Link to="/login" style={{ marginRight: "20px" }}>Login</Link>
          <Link to="/signup">Signup</Link>
        </nav>

        {/* Routes */}
        <Routes>
          <Route path="/login" element={<LoginForm />} />
          <Route path="/signup" element={<SignupForm />} />
          <Route path="/" element={<LoginForm />} /> {/* Default route */}
        </Routes>
      </div>
    </Router>
  );
}

export default App;
