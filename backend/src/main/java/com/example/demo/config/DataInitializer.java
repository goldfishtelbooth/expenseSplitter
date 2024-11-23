package com.example.demo.config;

import com.example.demo.model.Expense;
import com.example.demo.model.ExpenseSplit;
import com.example.demo.model.Group;
import com.example.demo.model.Participant;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.ParticipantRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ParticipantRepository participantRepository;
    private final GroupRepository groupRepository;
    private final ExpenseRepository expenseRepository;

    @PostConstruct
    public void initData() {
        // Create participants
        Participant alice = new Participant();
        alice.setName("Alice");
        alice.setEmail("alice@example.com");

        Participant bob = new Participant();
        bob.setName("Bob");
        bob.setEmail("bob@example.com");

        Participant charlie = new Participant();
        charlie.setName("Charlie");
        charlie.setEmail("charlie@example.com");

        participantRepository.saveAll(Arrays.asList(alice, bob, charlie));

        // Create a group
        Group lunchGroup = new Group();
        lunchGroup.setName("Lunch Group");
        lunchGroup.getParticipants().addAll(Arrays.asList(alice, bob, charlie));
        groupRepository.save(lunchGroup);

        Group alaskaTrip = new Group();
        alaskaTrip.setName("Alaska Trip");
        alaskaTrip.getParticipants().addAll(Arrays.asList(alice, charlie));
        groupRepository.save(alaskaTrip);

        // Create an expense
        Expense lunchExpense = new Expense();
        lunchExpense.setDescription("Lunch with team");
        lunchExpense.setAmount(120.5);
        lunchExpense.setPayer(alice); // Alice paid for this
        lunchExpense.setGroup(lunchGroup);

        // Create splits for the expense
        ExpenseSplit split1 = new ExpenseSplit();
        split1.setParticipant(alice);
        split1.setAmount(40.17);
        split1.setExpense(lunchExpense);

        ExpenseSplit split2 = new ExpenseSplit();
        split2.setParticipant(bob);
        split2.setAmount(40.17);
        split2.setExpense(lunchExpense);

        ExpenseSplit split3 = new ExpenseSplit();
        split3.setParticipant(charlie);
        split3.setAmount(40.16);
        split3.setExpense(lunchExpense);

        List<ExpenseSplit> splits = new ArrayList<>();
        splits.add(split1);
        splits.add(split2);
        splits.add(split3);

        lunchExpense.setSplits(splits);

        // Save the expense
        expenseRepository.save(lunchExpense);

        Expense tripExpense = new Expense();
        tripExpense.setDescription("Alaska trip tickets");
        tripExpense.setAmount(500.0);
        tripExpense.setPayer(charlie); // Charlie paid for this
        tripExpense.setGroup(alaskaTrip);

        ExpenseSplit tripSplit1 = new ExpenseSplit();
        tripSplit1.setParticipant(alice);
        tripSplit1.setAmount(300.0);
        tripSplit1.setExpense(tripExpense);

        ExpenseSplit tripSplit2 = new ExpenseSplit();
        tripSplit2.setParticipant(charlie);
        tripSplit2.setAmount(200.0);
        tripSplit2.setExpense(tripExpense);

        tripExpense.setSplits(Arrays.asList(tripSplit1, tripSplit2));
        expenseRepository.save(tripExpense);
    }
}
