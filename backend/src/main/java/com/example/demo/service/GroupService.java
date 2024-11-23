package com.example.demo.service;

import com.example.demo.dto.GroupDTO;
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
public class GroupService {
    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;

    // Fetch all groups and map to DTOs
    public List<GroupDTO> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public GroupDTO createGroup(GroupDTO groupDTO) {
        // Validate group name
        if (groupDTO.getName() == null || groupDTO.getName().isBlank()) {
            throw new IllegalArgumentException("Group name cannot be null or blank");
        }

        Group group = new Group();
        group.setName(groupDTO.getName());

        // Process participants
        if (groupDTO.getParticipants() != null && !groupDTO.getParticipants().isEmpty()) {
            groupDTO.getParticipants().forEach(participantDTO -> {
                // Find the participant by email
                Participant participant = participantRepository.findByEmail(participantDTO.getEmail())
                        .orElseGet(() -> {
                            // If participant does not exist, create a new one
                            Participant newParticipant = new Participant();
                            newParticipant.setEmail(participantDTO.getEmail());
                            newParticipant.setName(participantDTO.getName() != null && !participantDTO.getName().isBlank()
                                    ? participantDTO.getName()
                                    : participantDTO.getEmail()); // Default name to email
                            return participantRepository.save(newParticipant);
                        });

                // Add the participant to the group
                group.getParticipants().add(participant);
            });
        }

        Group savedGroup = groupRepository.save(group);

        return convertToDTO(savedGroup);
    }



    // Helper method to convert Group to GroupDTO
    private GroupDTO convertToDTO(Group group) {
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setParticipants(group.getParticipants().stream().map(this::convertParticipantToDTO).toList());
        return dto;
    }

    private ParticipantDTO convertParticipantToDTO(Participant participant) {
        ParticipantDTO dto = new ParticipantDTO();
        dto.setId(participant.getId());
        dto.setName(participant.getName());
        dto.setEmail(participant.getEmail());
        return dto;
    }
}