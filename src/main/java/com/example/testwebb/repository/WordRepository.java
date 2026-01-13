package com.example.testwebb.repository;

import com.example.testwebb.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {



}
