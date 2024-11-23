import React from 'react';
import BalanceList from '../components/BalanceList';
import SettlementsList from '../components/SettlementsList';

function BalancesPage() {
  return (
    <div>
      <BalanceList />
      <hr />
      <SettlementsList />
    </div>
  );
}

export default BalancesPage;
