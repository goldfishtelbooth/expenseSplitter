import React, { useEffect, useState } from 'react';
import api from '../services/api';

function ExpenseList() {
  const [expenses, setExpenses] = useState([]);
  const [participants, setParticipants] = useState({});

  useEffect(() => {
    fetchExpenses();
    fetchParticipants();
  }, []);

  const fetchExpenses = async () => {
    try {
      const response = await api.get('/expense');
      setExpenses(response.data);
    } catch (error) {
      console.error('Error fetching expenses:', error);
    }
  };

  const fetchParticipants = async () => {
    try {
      const response = await api.get('/participant');
      const participantsMap = response.data.reduce((acc, participant) => {
        acc[participant.id] = participant.name;
        return acc;
      }, {});
      setParticipants(participantsMap);
    } catch (error) {
      console.error('Error fetching participants:', error);
    }
  };

  const getParticipantName = (id) => participants[id] || 'Unknown';

  return (
    <div>
      <h3>Expenses</h3>
      <ul className="list-group">
        {expenses.map((expense) => (
          <li key={expense.id} className="list-group-item">
            <div>
              <strong>{expense.description}</strong> - ${expense.amount.toFixed(2)} paid by{' '}
              {expense.payerId ? getParticipantName(expense.payerId) : 'Unknown'}
            </div>
            {expense.splits && expense.splits.length > 0 && (
              <div className="mt-2">
                <strong>Split Details:</strong>
                <ul className="list-unstyled">
                  {expense.splits.map((split, index) => (
                    <li key={index}>
                      Participant name: {getParticipantName(split.participantId)}, Amount: $
                      {split.amount.toFixed(2)}
                    </li>
                  ))}
                </ul>
              </div>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ExpenseList;
