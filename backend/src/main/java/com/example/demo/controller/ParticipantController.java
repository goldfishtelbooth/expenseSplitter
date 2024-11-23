package com.example.demo.controller;

import com.example.demo.dto.ParticipantDTO;
import com.example.demo.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/participant")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping
    public ResponseEntity<List<ParticipantDTO>> getAllParticipants() {
        List<ParticipantDTO> participants = participantService.getAllParticipants();
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantDTO> getParticipantById(@PathVariable Long id) {
        ParticipantDTO participant = participantService.getParticipantById(id);
        return ResponseEntity.ok(participant);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ParticipantDTO>> getParticipantsByGroup(@PathVariable Long groupId) {
        List<ParticipantDTO> participants = participantService.getParticipantsByGroup(groupId);
        return ResponseEntity.ok(participants);
    }

    @PostMapping
    public ResponseEntity<ParticipantDTO> createParticipant(@Valid @RequestBody ParticipantDTO participantDTO) {
        ParticipantDTO createdParticipant = participantService.createParticipant(participantDTO);
        return ResponseEntity.ok(createdParticipant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParticipantDTO> updateParticipant(
            @PathVariable Long id,
            @Valid @RequestBody ParticipantDTO participantDTO) {
        ParticipantDTO updatedParticipant = participantService.updateParticipant(id, participantDTO);
        return ResponseEntity.ok(updatedParticipant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }
}