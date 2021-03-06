package cz.janakdom.backend.dao;

import cz.janakdom.backend.model.database.DamageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DamageTypeDao extends JpaRepository<DamageType, Integer> {
    Optional<DamageType> findByName(String name);
    List<DamageType> findAllByIsDeletedFalse();
}
