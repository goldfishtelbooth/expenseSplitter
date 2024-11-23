package com.example.demo.repository;

import com.example.demo.model.Group;
import com.example.demo.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>{
    Optional<Participant> findByEmail(String email);
    // find all participants whose emails are in the provided list.
    boolean existsByEmail(String email);

}
