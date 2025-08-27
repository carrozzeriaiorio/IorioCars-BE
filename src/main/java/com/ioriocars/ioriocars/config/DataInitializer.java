package com.ioriocars.ioriocars.config;

import com.ioriocars.ioriocars.domain.Auto;
import com.ioriocars.ioriocars.domain.User;
import com.ioriocars.ioriocars.repository.AutoRepository;
import com.ioriocars.ioriocars.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, AutoRepository autoRepository) {
        return args -> {

            // ----------- CREAZIONE ADMIN DI DEFAULT -----------
            if (userRepository.findAll().isEmpty()) {
                User admin = User.builder()
                        .email("carrozzeriaiorio@gmail.com")
                        .password(new BCryptPasswordEncoder().encode("iorio00"))
                        .role("ADMIN")
                        .build();
                userRepository.save(admin);
            }

            // ----------- CREAZIONE AUTO DI ESEMPIO -----------
            if (autoRepository.findAll().isEmpty()) {
                Auto auto1 = Auto.builder()
                        .titolo("Fiat Panda 2020")
                        .marca("Fiat")
                        .modello("Panda")
                        .anno(2020)
                        .prezzo(8500)
                        .km(15000)
                        .carburante("Benzina")
                        .descrizione("Auto compatta ideale per città")
                        .immagine(null) // puoi aggiungere filename reale se vuoi
                        .build();

                Auto auto2 = Auto.builder()
                        .titolo("Volkswagen Golf 2018")
                        .marca("Volkswagen")
                        .modello("Golf")
                        .anno(2018)
                        .prezzo(12000)
                        .km(45000)
                        .carburante("Diesel")
                        .descrizione("Compatta familiare affidabile")
                        .immagine(null)
                        .build();

                autoRepository.saveAll(List.of(auto1, auto2));
            }
        };
    }
}

