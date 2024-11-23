package com.example.demo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupDTO {
    private Long id;
    private String name;
    private List<ParticipantDTO> participants = new ArrayList<>(); // Default to empty list;
}
