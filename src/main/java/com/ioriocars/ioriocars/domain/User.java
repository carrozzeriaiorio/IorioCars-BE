package com.ioriocars.ioriocars.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data               // genera getter, setter, toString, equals e hashCode
@NoArgsConstructor  // genera costruttore vuoto
@AllArgsConstructor // genera costruttore con tutti i campi
@Builder            // genera pattern builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // usiamo email come identificativo

    @Column(nullable = false)
    private String password; // hashata

    @Column(nullable = false)
    private String role; // "ADMIN"
}

