import React, { useState } from 'react';
import api from '../services/api';

function GroupForm() {
  const [groupName, setGroupName] = useState('');
  const [participantEmails, setParticipantEmails] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSuccessMessage('');
    setErrorMessage('');

    const participants = participantEmails
      .split(',')
      .map(email => ({ email: email.trim() }));

    const groupData = {
      name: groupName,
      participants: participants,
    };

    try {
      const response = await api.post('/group', groupData);
      setSuccessMessage(`Group "${response.data.name}" created successfully!`);
      setGroupName('');
      setParticipantEmails('');
    } catch (error) {
      console.error('Error creating group:', error);
      setErrorMessage('Failed to create group. Please try again.');
    }
  };

  return (
    <div>
      <h3>Create Group</h3>
      {successMessage && <div className="alert alert-success">{successMessage}</div>}
      {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="groupName" className="form-label">Group Name</label>
          <input
            type="text"
            className="form-control"
            id="groupName"
            value={groupName}
            onChange={(e) => setGroupName(e.target.value)}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="participantEmails" className="form-label">Participant Emails (comma-separated)</label>
          <input
            type="text"
            className="form-control"
            id="participantEmails"
            value={participantEmails}
            onChange={(e) => setParticipantEmails(e.target.value)}
            placeholder="e.g., email1@example.com, email2@example.com"
            required
          />
        </div>
        <button type="submit" className="btn btn-primary">Create Group</button>
      </form>
    </div>
  );
}

export default GroupForm;
