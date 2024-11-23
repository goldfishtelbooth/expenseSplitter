import React from 'react';
import ExpenseForm from '../components/ExpenseForm';
import ExpenseList from '../components/ExpenseList';

function ExpensesPage() {
  return (
    <div>
      <ExpenseForm />
      <hr />
      <ExpenseList />
    </div>
  );
}

export default ExpensesPage;