package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseDTO {
    private Long id;

    @NotNull
    private Double amount;

    private String description;

    @NotNull
    private Long payerId;

    @NotNull
    private Long groupId;

    @NotNull
    private List<Split> splits;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Split {
        @NotNull
        private Long participantId;

        @NotNull
        private Double amount;
    }
}

