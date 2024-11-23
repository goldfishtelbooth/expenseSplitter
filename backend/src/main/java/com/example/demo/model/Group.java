package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "expense_groups") // group and groups are reserved keywords in mysql
@Data
@NoArgsConstructor
@AllArgsConstructor
// Lombok's @Data annotation generates equals and hashCode methods that include all fields by default.
// In bidirectional relationships, this can cause recursive calls leading to ConcurrentModificationException.
// To prevent this, exclude the collections from equals and hashCode.
@EqualsAndHashCode(exclude = {"participants", "expenses"})
@ToString(exclude = {"participants", "expenses"})
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

//    @Column(nullable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
            name = "group_participants",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )

    private Set<Participant> participants = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = { CascadeType.ALL})
    private List<Expense> expenses;

    public Group(String name) {
        this.name = name;
        this.participants = new HashSet<>();
    }
}
