package com.ioriocars.ioriocars.service;

import com.ioriocars.ioriocars.domain.Auto;
import com.ioriocars.ioriocars.repository.AutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutoService {

    @Autowired
    private AutoRepository autoRepository;

    // Paginazione e filtri
    public Page<Auto> findFiltered(String marca, String modello,
                                   int annoMin, int annoMax,
                                   double prezzoMin, double prezzoMax,
                                   int kmMin, int kmMax,
                                   String carburante,
                                   int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return autoRepository.findByMarcaContainingIgnoreCaseAndModelloContainingIgnoreCaseAndAnnoBetweenAndPrezzoBetweenAndKmBetweenAndCarburanteContainingIgnoreCase(
                marca, modello, annoMin, annoMax, prezzoMin, prezzoMax, kmMin, kmMax, carburante, pageable
        );
    }

    public List<Auto> getAll() {
        return autoRepository.findAll();
    }

    public Optional<Auto> getById(Long id) {
        return autoRepository.findById(id);
    }

    public Auto create(Auto auto) {
        return autoRepository.save(auto);
    }

    public Auto update(Long id, Auto updatedAuto) {
        Optional<Auto> opt = autoRepository.findById(id);
        if (opt.isPresent()) {
            Auto auto = opt.get();
            auto.setTitolo(updatedAuto.getTitolo());
            auto.setMarca(updatedAuto.getMarca());
            auto.setModello(updatedAuto.getModello());
            auto.setAnno(updatedAuto.getAnno());
            auto.setPrezzo(updatedAuto.getPrezzo());
            auto.setKm(updatedAuto.getKm());
            auto.setCarburante(updatedAuto.getCarburante());
            auto.setDescrizione(updatedAuto.getDescrizione());
            auto.setImmagine(updatedAuto.getImmagine());
            return autoRepository.save(auto);
        }
        return null;
    }

    public void delete(Long id) {
        autoRepository.deleteById(id);
    }
}
