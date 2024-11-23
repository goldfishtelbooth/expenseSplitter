import React, { useState, useEffect } from 'react';
import api from '../services/api';

function ExpenseForm() {
  const [amount, setAmount] = useState('');
  const [description, setDescription] = useState('');
  const [payer, setPayer] = useState('');
  const [splits, setSplits] = useState([]);
  const [participants, setParticipants] = useState([]);
  const [groups, setGroups] = useState([]);
  const [selectedGroup, setSelectedGroup] = useState('');
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  useEffect(() => {
    fetchGroups();
  }, []);

  const fetchGroups = async () => {
    try {
      const response = await api.get('/group'); // Fetch all groups
      setGroups(response.data);
    } catch (error) {
      console.error('Error fetching groups:', error);
    }
  };

  const handleGroupChange = async (groupId) => {
    setSelectedGroup(groupId);
    try {
      const response = await api.get(`/participant/group/${groupId}`);
      const participants = response.data;

      setParticipants(participants);
      setSplits(
        participants.map((participant) => ({
          participantId: participant.id,
          amount: 0,
        }))
      );
      setPayer('');
    } catch (error) {
      console.error('Error fetching participants:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);

    try {
      const response = await api.post('/expense', {
        amount: parseFloat(amount),
        description,
        payerId: parseInt(payer),
        groupId: parseInt(selectedGroup),
        splits,
      });
      setSuccess(`Expense "${response.data.description}" created successfully!`);
      setAmount('');
      setDescription('');
      setPayer('');
      setSplits([]);
      setSelectedGroup('');
    } catch (err) {
      console.error('Error creating expense:', err);
      setError('Failed to create expense. Please try again.');
    }
  };

  const handleSplitChange = (participantId, value) => {
    const updatedSplits = splits.map((split) =>
      split.participantId === participantId
        ? { ...split, amount: parseFloat(value) || 0 }
        : split
    );
    setSplits(updatedSplits);
  };

  return (
    <div>
      <h3>Create Expense</h3>
      {error && <div className="alert alert-danger">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label htmlFor="group" className="form-label">Group</label>
          <select
            id="group"
            className="form-select"
            value={selectedGroup}
            onChange={(e) => handleGroupChange(e.target.value)}
            required
          >
            <option value="" disabled>Select a group</option>
            {groups.map((group) => (
              <option key={group.id} value={group.id}>{group.name}</option>
            ))}
          </select>
        </div>

        {selectedGroup && (
          <>
            <div className="mb-4">
              <label htmlFor="payer" className="form-label">Payer</label>
              <select
                id="payer"
                className="form-select"
                value={payer}
                onChange={(e) => setPayer(e.target.value)}
                required
              >
                <option value="" disabled>Select a payer</option>
                {participants.map((participant) => (
                  <option key={participant.id} value={participant.id}>
                    {participant.name} ({participant.email})
                  </option>
                ))}
              </select>
            </div>

            <div className="mb-4">
              <label htmlFor="amount" className="form-label">Amount</label>
              <input
                type="number"
                id="amount"
                className="form-control"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                required
              />
            </div>

            <div className="mb-4">
              <label className="form-label">Splits</label>
              {splits.map((split, index) => (
                <div key={index} className="input-group mb-2">
                  <span className="input-group-text">
                    {participants.find((p) => p.id === split.participantId)?.name || 'Unknown'}
                  </span>
                  <input
                    type="number"
                    className="form-control"
                    placeholder="Amount"
                    value={split.amount}
                    onChange={(e) =>
                      handleSplitChange(split.participantId, e.target.value)
                    }
                    required
                  />
                </div>
              ))}
            </div>
          </>
        )}

        <div className="mb-4">
          <label htmlFor="description" className="form-label">Description</label>
          <input
            type="text"
            id="description"
            className="form-control"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          />
        </div>

        

        <button type="submit" className="btn btn-primary">Create Expense</button>
      </form>
    </div>
  );
}

export default ExpenseForm;
