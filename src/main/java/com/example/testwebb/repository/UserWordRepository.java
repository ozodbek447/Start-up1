package com.example.testwebb.repository;

import com.example.testwebb.entity.UserWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWordRepository extends JpaRepository<UserWord, Long> {


    boolean existsByUserId(Long userId);
    Optional<UserWord> findByUserId(Long userId);
}
