package com.annb.quizz.repository;

import com.annb.quizz.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository  extends JpaRepository<Room, String> {
    Optional<Room> findByCode(String codee);
}
