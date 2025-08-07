import React from 'react';
import { scoreResume } from '../utils/scoring';

function ResumeCard({ resume }) {
  const score = scoreResume(resume);

  // Function to pick border color
  const getBorderColor = (score) => {
    const numericScore = parseInt(score);
    if (numericScore >= 80) return 'green';      
    if (numericScore >= 50) return 'orange';      
    return 'red';                              
  };

  const cardStyle = {
    border: `2px solid ${getBorderColor(score)}`,
    marginBottom: '10px',
    padding: '10px',
    borderRadius: '8px',
    backgroundcolor :'#faf7e40a'
  };

  return (
    <div style={cardStyle}>
      <h3>{resume.name}</h3>
      <p><b>Experience:</b> {resume.experience} years</p>
      <p><b>Skills:</b> {resume.skills.join(', ')}</p>
      <p><b>Score:</b> {score}/100</p>
      <button>Shortlist</button>
    </div>
  );
}

export default ResumeCard;