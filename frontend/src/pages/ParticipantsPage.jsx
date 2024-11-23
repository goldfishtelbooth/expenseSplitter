import React from 'react';
import ParticipantForm from '../components/ParticipantForm';
import ParticipantList from '../components/ParticipantList';

function ParticipantsPage() {
  return (
    <div>
      <ParticipantForm />
      <hr />
      <ParticipantList />
    </div>
  );
}

export default ParticipantsPage;