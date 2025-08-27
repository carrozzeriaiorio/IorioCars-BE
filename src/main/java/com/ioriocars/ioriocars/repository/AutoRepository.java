package com.ioriocars.ioriocars.repository;

import com.ioriocars.ioriocars.domain.Auto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoRepository extends JpaRepository<Auto, Long> {

    // Ricerca con filtri, paginata
    Page<Auto> findByMarcaContainingIgnoreCaseAndModelloContainingIgnoreCaseAndAnnoBetweenAndPrezzoBetweenAndKmBetweenAndCarburanteContainingIgnoreCase(
            String marca,
            String modello,
            int annoMin,
            int annoMax,
            double prezzoMin,
            double prezzoMax,
            int kmMin,
            int kmMax,
            String carburante,
            Pageable pageable
    );
}
