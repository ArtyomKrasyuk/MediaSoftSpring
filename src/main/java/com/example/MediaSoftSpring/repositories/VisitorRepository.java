package com.example.MediaSoftSpring.repositories;

import com.example.MediaSoftSpring.entities.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {
}
