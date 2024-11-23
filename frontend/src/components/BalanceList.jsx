import React, { useState, useEffect } from 'react';
import api from '../services/api';

function BalanceList() {
  const [balances, setBalances] = useState({}); // Initialize as an empty object
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchBalances();
  }, []);

  const fetchBalances = async () => {
    try {
      const response = await api.get('/expense/balances');
      setBalances(response.data || {}); // Ensure `balances` is always an object
    } catch (err) {
      setError('Failed to fetch balances');
      console.error(err);
    }
  };

  return (
    <div>
      <h3>Total Balances</h3>
      {error && <div className="alert alert-danger">{error}</div>}
      <ul className="list-group">
        {Object.entries(balances).map(([participant, amount]) => (
          <li key={participant} className="list-group-item">
            {participant}: ${amount.toFixed(2)}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default BalanceList;
