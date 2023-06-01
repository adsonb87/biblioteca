package com.spring.biblioteca.repository;

import com.spring.biblioteca.model.Papel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PapelRepository extends JpaRepository<Papel, Long> {

    Papel findByPapel(String papel);
}
