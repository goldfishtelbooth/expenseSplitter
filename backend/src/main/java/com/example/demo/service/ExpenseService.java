package com.example.demo.service;

import com.example.demo.dto.ExpenseDTO;
import com.example.demo.dto.ExpenseDTO.Split;
import com.example.demo.model.Expense;
import com.example.demo.model.ExpenseSplit;
import com.example.demo.model.Participant;
import com.example.demo.model.Group;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ParticipantRepository participantRepository;
    private final GroupRepository groupRepository;

    // Create an Expense
    public void addExpense(ExpenseDTO expenseDTO) {
        Group group = groupRepository.findById(expenseDTO.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + expenseDTO.getGroupId()));

        Participant payer = participantRepository.findById(expenseDTO.getPayerId())
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + expenseDTO.getPayerId()));

        List<ExpenseSplit> splits = new ArrayList<>();
        for (Split split : expenseDTO.getSplits()) {
            Participant participant = participantRepository.findById(split.getParticipantId())
                    .orElseThrow(() -> new RuntimeException("Participant not found with id: " + split.getParticipantId()));
            ExpenseSplit expenseSplit = new ExpenseSplit();
            expenseSplit.setParticipant(participant);
            expenseSplit.setAmount(split.getAmount());
            splits.add(expenseSplit);
        }

        Expense expense = new Expense();
        expense.setAmount(expenseDTO.getAmount());
        expense.setDescription(expenseDTO.getDescription());
        expense.setGroup(group);
        expense.setPayer(payer);
        expense.setSplits(splits);

        for (ExpenseSplit split : splits) {
            split.setExpense(expense);
        }

        expenseRepository.save(expense);
    }

    // Retrieve all Expenses
    public List<ExpenseDTO> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Retrieve an Expense by ID
    public ExpenseDTO getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));
        return convertToDTO(expense);
    }

    // Update an Expense
    public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        Group group = groupRepository.findById(expenseDTO.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + expenseDTO.getGroupId()));

        Participant payer = participantRepository.findById(expenseDTO.getPayerId())
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + expenseDTO.getPayerId()));

        List<ExpenseSplit> splits = new ArrayList<>();
        for (Split split : expenseDTO.getSplits()) {
            Participant participant = participantRepository.findById(split.getParticipantId())
                    .orElseThrow(() -> new RuntimeException("Participant not found with id: " + split.getParticipantId()));
            ExpenseSplit expenseSplit = new ExpenseSplit();
            expenseSplit.setParticipant(participant);
            expenseSplit.setAmount(split.getAmount());
            splits.add(expenseSplit);
        }

        expense.setAmount(expenseDTO.getAmount());
        expense.setDescription(expenseDTO.getDescription());
        expense.setGroup(group);
        expense.setPayer(payer);
        expense.setSplits(splits);

        for (ExpenseSplit split : splits) {
            split.setExpense(expense);
        }

        expenseRepository.save(expense);
        return convertToDTO(expense);
    }

    // Delete an Expense
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new RuntimeException("Expense not found with id: " + id);
        }
        expenseRepository.deleteById(id);
    }

    // Helper Method to Convert Expense to ExpenseDTO
    private ExpenseDTO convertToDTO(Expense expense) {
        return ExpenseDTO.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .payerId(expense.getPayer().getId())
                .groupId(expense.getGroup().getId())
                .splits(expense.getSplits().stream()
                        .map(split -> new ExpenseDTO.Split(split.getParticipant().getId(), split.getAmount()))
                        .collect(Collectors.toList()))
                .build();
    }

    public Map<String, Double> calculateTotalBalances() {
        Map<Long, Double> participantBalances = new HashMap<>();

        // Aggregate net balances for all participants
        for (Expense expense : expenseRepository.findAll()) {
            expense.getSplits().forEach(split -> {
                participantBalances.put(
                        split.getParticipant().getId(),
                        participantBalances.getOrDefault(split.getParticipant().getId(), 0.0) - split.getAmount()
                );
            });
            participantBalances.put(
                    expense.getPayer().getId(),
                    participantBalances.getOrDefault(expense.getPayer().getId(), 0.0) + expense.getAmount()
            );
        }

        // Map participant IDs to names and include balances
        Map<String, Double> participantBalancesByName = new HashMap<>();
        for (Map.Entry<Long, Double> entry : participantBalances.entrySet()) {
            participantRepository.findById(entry.getKey()).ifPresent(participant -> {
                participantBalancesByName.put(participant.getName(), entry.getValue());
            });
        }

        return participantBalancesByName;
    }

    public List<String> calculateSettlements() {
        Map<Long, Double> participantBalances = new HashMap<>();

        // Aggregate net balances for all participants
        for (Expense expense : expenseRepository.findAll()) {
            expense.getSplits().forEach(split -> {
                participantBalances.put(
                        split.getParticipant().getId(),
                        participantBalances.getOrDefault(split.getParticipant().getId(), 0.0) - split.getAmount()
                );
            });
            participantBalances.put(
                    expense.getPayer().getId(),
                    participantBalances.getOrDefault(expense.getPayer().getId(), 0.0) + expense.getAmount()
            );
        }

        // Convert balances to priority queues for minimal transactions
        PriorityQueue<Map.Entry<Long, Double>> debtQueue = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue)
        );
        PriorityQueue<Map.Entry<Long, Double>> creditQueue = new PriorityQueue<>(
                (a, b) -> Double.compare(b.getValue(), a.getValue())
        );

        for (Map.Entry<Long, Double> entry : participantBalances.entrySet()) {
            if (entry.getValue() < 0) {
                debtQueue.add(Map.entry(entry.getKey(), -entry.getValue()));
            } else if (entry.getValue() > 0) {
                creditQueue.add(Map.entry(entry.getKey(), entry.getValue()));
            }
        }

        List<String> settlements = new ArrayList<>();

        while (!debtQueue.isEmpty() && !creditQueue.isEmpty()) {
            Map.Entry<Long, Double> debtor = debtQueue.poll();
            Map.Entry<Long, Double> creditor = creditQueue.poll();

            double settlementAmount = Math.min(debtor.getValue(), creditor.getValue());

            // Resolve participant names using the repository
            String debtorName = participantRepository.findById(debtor.getKey())
                    .map(Participant::getName)
                    .orElse("Unknown");
            String creditorName = participantRepository.findById(creditor.getKey())
                    .map(Participant::getName)
                    .orElse("Unknown");

            settlements.add(debtorName + " owes " + creditorName + " $" + String.format("%.2f", settlementAmount));

            if (debtor.getValue() > settlementAmount) {
                debtQueue.add(Map.entry(debtor.getKey(), debtor.getValue() - settlementAmount));
            }
            if (creditor.getValue() > settlementAmount) {
                creditQueue.add(Map.entry(creditor.getKey(), creditor.getValue() - settlementAmount));
            }
        }

        return settlements;
    }
}
