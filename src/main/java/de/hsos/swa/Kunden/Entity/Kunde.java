package de.hsos.swa.Kunden.Entity;

import java.util.List;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Kunde {
    @Id
    @GeneratedValue
    private long id;

    private List<Long> orders;

    private String vorname;
    private String nachname;

    @Embedded
    private Adresse adresse;
}
