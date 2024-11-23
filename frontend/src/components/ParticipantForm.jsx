import React, { useState } from 'react';
import api from '../services/api';

function ParticipantForm() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');

  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);

    try {
      const response = await api.post('/participant', {
        name,
        email,
      });
      setSuccess(`Participant "${response.data.name}" created successfully!`);
      setName('');
      setEmail('');
    } catch (err) {
      console.error('Error creating participant:', err);
      setError('Failed to create participant. Please try again.');
    }
  };

  return (
    <div>
      <h3>Create Participant</h3>
      {error && <div className="alert alert-danger">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label htmlFor="participantName" className="form-label mb-2">Participant Name</label>
          <input
            type="text"
            className="form-control"
            id="participantName"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </div>
        <div className="mb-4">
          <label htmlFor="participantEmail" className="form-label mb-2">Participant Email</label>
          <input
            type="email"
            className="form-control"
            id="participantEmail"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <button type="submit" className="btn btn-primary">Create Participant</button>
      </form>
    </div>
  );
}

export default ParticipantForm;