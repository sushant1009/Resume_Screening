import React, { useState } from 'react';
import ResumeCard from '../../ResumeCard';

function ResumeList({ resumes }) {
  const [filter, setFilter] = useState('');

  const filtered = resumes.filter(r =>
    r.skills.some(skill => skill.toLowerCase().includes(filter.toLowerCase()))
  );

  return (
    <div>
      <input
        type="text"
        placeholder="Filter by skill (e.g. react)"
        onChange={(e) => setFilter(e.target.value)}
        style={{ margin: '10px', padding: '5px' }}
      />
      {filtered.map((resume, idx) => (
        <ResumeCard key={idx} resume={resume} />
      ))}
    </div>
  );
}

export default ResumeList;
