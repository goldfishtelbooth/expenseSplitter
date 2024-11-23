package com.example.demo.service;

import com.example.demo.dto.ParticipantDTO;
import com.example.demo.model.Group;
import com.example.demo.model.Participant;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final GroupRepository groupRepository;

    // Fetch all participants and map to DTOs
    public List<ParticipantDTO> getAllParticipants() {
        List<Participant> participants = participantRepository.findAll();
        return participants.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ParticipantDTO getParticipantById(Long id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + id));
        return convertToDTO(participant);
    }

    public ParticipantDTO createParticipant(ParticipantDTO participantDTO) {
        // Check if participant with the same email already exists
        if (participantRepository.existsByEmail(participantDTO.getEmail())) {
            throw new RuntimeException("Participant with email " + participantDTO.getEmail() + " already exists.");
        }

        Participant participant = new Participant();
        participant.setName(participantDTO.getName());
        participant.setEmail(participantDTO.getEmail());

        Participant savedParticipant = participantRepository.save(participant);
        return convertToDTO(savedParticipant);
    }

    public ParticipantDTO updateParticipant(Long id, ParticipantDTO participantDTO) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + id));

        participant.setName(participantDTO.getName());
        participant.setEmail(participantDTO.getEmail());

        Participant updatedParticipant = participantRepository.save(participant);
        return convertToDTO(updatedParticipant);
    }

    public void deleteParticipant(Long id) {
        if (!participantRepository.existsById(id)) {
            throw new RuntimeException("Participant not found with id: " + id);
        }
        participantRepository.deleteById(id);
    }

    // Helper method to convert Participant to ParticipantDTO
    private ParticipantDTO convertToDTO(Participant participant) {
        ParticipantDTO dto = new ParticipantDTO();
        dto.setId(participant.getId());
        dto.setName(participant.getName());
        dto.setEmail(participant.getEmail());
        return dto;
    }

    public List<ParticipantDTO> getParticipantsByGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
        return group.getParticipants().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}