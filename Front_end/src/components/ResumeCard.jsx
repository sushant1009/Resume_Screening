// src/components/ResumeCard.jsx
import React from "react";

const ResumeCard = ({ resume }) => {
  return (
    <div
      style={{
        border: "1px solid #ddd",
        borderRadius: "10px",
        padding: "15px",
        boxShadow: "0px 4px 8px rgba(0,0,0,0.1)",
        backgroundColor: "#fff"
      }}
    >
      <h3>{resume.username}</h3>
      <p><strong>File:</strong> {resume.fileName}</p>
      <p><strong>Score:</strong> {resume.score}</p>
      <p><strong>Role:</strong> {resume.role}</p>

      {/* Resume preview */}
      {resume.previewUrl ? (
        <iframe
          src={resume.previewUrl}
          title="Resume Preview"
          width="100%"
          height="200px"
          style={{ border: "none" }}
        />
      ) : (
        <pre
          style={{
            backgroundColor: "#f7f7f7",
            padding: "10px",
            borderRadius: "5px",
            maxHeight: "200px",
            overflowY: "auto"
          }}
        >
          {resume.filePath || "No preview available"}
        </pre>
      )}
    </div>
  );
};

export default ResumeCard;
