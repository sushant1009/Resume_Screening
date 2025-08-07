export function scoreResume(resume) {
  const keywords = ['react', 'node', 'python', 'ml'];
  const matched = resume.skills.filter(skill =>
    keywords.includes(skill.toLowerCase())
  );
  return Math.min((matched.length / keywords.length) * 100, 100).toFixed(0);
}
