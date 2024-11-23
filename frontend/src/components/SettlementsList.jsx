import React, { useState, useEffect } from 'react';
import api from '../services/api';

function SettlementsList() {
  const [settlements, setSettlements] = useState([]); // Initialize as an empty array
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchSettlements();
  }, []);

  const fetchSettlements = async () => {
    try {
      const response = await api.get('/expense/settlements'); // Adjust API endpoint if needed
      setSettlements(response.data || []); // Ensure `settlements` is always an array
    } catch (err) {
      setError('Failed to fetch settlements');
      console.error(err);
    }
  };

  return (
    <div>
      <h3>Settlements</h3>
      {error && <div className="alert alert-danger">{error}</div>}
      <ul className="list-group">
        {settlements.map((settlement, index) => (
          <li key={index} className="list-group-item">
            {settlement}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default SettlementsList;
