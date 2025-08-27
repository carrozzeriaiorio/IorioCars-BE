package com.ioriocars.service;

import com.ioriocars.domain.Auto;
import com.ioriocars.repository.AutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutoService {

    @Autowired
    private AutoRepository autoRepository;

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
