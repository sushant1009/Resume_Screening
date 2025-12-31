import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import ResumeUpload from './components/Student/ResumeUpload';
import SignupForm from './components/SignupForm';
import LoginForm from './components/LoginForm';
import ResumePage from './components/Student/ResumePage';
import JobPost from './components/Recruiter/JobPost';
import JobList  from './components/Student/JobList'; 
import ReceivedApplications from './components/Recruiter/ReceivedApplications';
import Navigation from './components/Navigation';
import PrivateRoute from './components/PrivateRoute';
import MyApplications from './components/Student/MyApplications';
import ApplyJob from './components/Student/ApplyJob';
import './css/App.css'; 
import Forgetpass from './components/Forgetpass';

function App() {
  

  return (
    <Router>
      <div>
        {/* Role-Based Navigation */}
        <Navigation />

        {/* Routes */}
        <Routes>
          {/* Public Routes */}
          <Route path="/login" element={<LoginForm />} />
          <Route path="/signup" element={<SignupForm />} />
          <Route path="/forgetpass" element={<Forgetpass />} />

          {/* Student Routes */}
          <Route
            path="/resumeupload"
            element={
              <PrivateRoute allowedRoles={["STUDENT"]}>
                <ResumeUpload />
              </PrivateRoute>
            }
          />
          <Route
            path="/getresume"
            element={
              <PrivateRoute allowedRoles={["STUDENT"]}>
                <ResumePage />
              </PrivateRoute>
            }
          />

          {/* Recruiter Routes */}
          <Route
            path="/jobpost"
            element={
              <PrivateRoute allowedRoles={["RECRUITER"]}>
                <JobPost />
              </PrivateRoute>
            }
          />
          
              <Route
                path="/joblist"
                element={
                  <PrivateRoute allowedRoles={["RECRUITER", "STUDENT"]}>
                    <JobList />
                  </PrivateRoute>
                }
              />

              <Route
                path="/apply/:jobId"
                element={
                  <PrivateRoute allowedRoles={["STUDENT"]}>
                    <ApplyJob />
                  </PrivateRoute>
                }
              />

              <Route
              path='/myapplications'
              element={
                <PrivateRoute allowedRoles={["STUDENT"]}>
                <MyApplications/>
                </PrivateRoute>
              }/>
          

          <Route
            path="/receivedapplications"
            element={
              <PrivateRoute allowedRoles={["RECRUITER"]}>
                <ReceivedApplications />
              </PrivateRoute>
            }
          />

          {/* Default Route */}
          <Route path="/" element={<LoginForm />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
