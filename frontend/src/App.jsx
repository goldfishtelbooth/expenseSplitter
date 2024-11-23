import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import HomePage from './pages/HomePage';
import GroupsPage from './pages/GroupsPage';
import ParticipantsPage from './pages/ParticipantsPage';
import ExpensesPage from './pages/ExpensesPage';
import NotFoundPage from './pages/NotFoundPage';
import BalancesPage from './pages/BalancesPage';

function App() {
  return (
    <Router>
      <Navbar />
      <div className="container mt-4">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/groups" element={<GroupsPage />} />
          <Route path="/participants" element={<ParticipantsPage />} />
          <Route path="/expenses" element={<ExpensesPage />} />
          <Route path="/balances" element={<BalancesPage />} />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;