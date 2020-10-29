package cz.janakdom.backend.dao;

import cz.janakdom.backend.model.database.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportDao extends JpaRepository<Report, Integer> {
    Optional<Report> findByHash(String hash);
    void deleteByHash(String hash);
}