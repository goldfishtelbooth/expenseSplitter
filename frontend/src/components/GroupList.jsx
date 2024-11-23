import React, { useEffect, useState } from 'react';
import api from '../services/api';

function GroupList() {
  const [groups, setGroups] = useState([]);

  useEffect(() => {
    fetchGroups();
  }, []);

  const fetchGroups = async () => {
    try {
      const response = await api.get('/group');
      setGroups(response.data);
    } catch (error) {
      console.error('Error fetching groups:', error);
    }
  };

  return (
    <div>
      <h3>Groups</h3>
      <ul className="list-group">
        {groups.map(group => (
          <li key={group.id} className="list-group-item">
            <h4>{group.name}</h4>
            <p>Members:</p>
            <ul>
              {group.participants.map((participant) => (
                <li key={participant.id}>{participant.name} ({participant.email})</li>
              ))}
            </ul>
        </li>
        ))}
      </ul>
    </div>
  );
}

export default GroupList;
