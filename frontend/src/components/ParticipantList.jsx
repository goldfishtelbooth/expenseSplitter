import React, { useEffect, useState } from 'react';
import api from '../services/api';

function ParticipantList() {
  const [participants, setParticipants] = useState([]);

  useEffect(() => {
    fetchParticipants();
  }, []);

  const fetchParticipants = async () => {
    try {
      const response = await api.get('/participant');
      setParticipants(response.data);
    } catch (error) {
      console.error('Error fetching participants:', error);
    }
  };

  return (
    <div>
      <h2>Participants</h2>
      <ul className="list-group">
        {participants.map(participant => (
          <li key={participant.id} className="list-group-item">
            {participant.name} ({participant.email})
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ParticipantList;
