package com.ioriocars.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titolo;          // titolo dell'annuncio

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modello;

    @Column(nullable = false)
    private int anno;

    @Column(nullable = false)
    private double prezzo;

    @Column(nullable = false)
    private int km;                 // chilometraggio

    @Column(nullable = false)
    private String carburante;      // es. "Benzina", "Diesel", "Elettrica"

    @Column(nullable = true)
    private String descrizione;     // descrizione dettagliata

    @Column(nullable = true)
    private String immagine;        // filename o URL dell'immagine
}

