import React from 'react';
import GroupForm from '../components/GroupForm';
import GroupList from '../components/GroupList';

function GroupsPage() {
  return (
    <div>
      <GroupForm />
      <hr />
      <GroupList />
    </div>
  );
}

export default GroupsPage;
