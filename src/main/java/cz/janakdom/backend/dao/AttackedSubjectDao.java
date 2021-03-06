package cz.janakdom.backend.dao;

import cz.janakdom.backend.model.database.AttackedSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttackedSubjectDao extends JpaRepository<AttackedSubject, Integer> {
    List<AttackedSubject> findAllByIsDeletedFalse();
    Optional<AttackedSubject> findByName(String name);
}
