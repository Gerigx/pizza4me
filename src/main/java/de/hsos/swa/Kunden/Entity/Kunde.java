package de.hsos.swa.Kunden.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.management.relation.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
// security
import io.quarkus.security.jpa.Username;

// https://quarkus.io/guides/security-jpa
// https://quarkus.io/guides/security-getting-started-tutorial

@Entity
@Table(name="Kunde")
@UserDefinition
public class Kunde {
    
    @Id
    @GeneratedValue
    private Long id;

    // Geschäftsdaten
    @NotBlank(message = "Vorname darf nicht leer sein")
    @Size(max = 50, message = "Vorname darf maximal 50 Zeichen lang sein")
    @Column(name = "vorname", nullable = false)
    private String vorname;

    @NotBlank(message = "Nachname darf nicht leer sein")
    @Size(max = 50, message = "Nachname darf maximal 50 Zeichen lang sein")
    @Column(name = "nachname", nullable = false)
    private String nachname;

    @Valid
    @Embedded
    private Adresse adresse;

    @Column(name = "orders")
    private List<Long> orders = new ArrayList<>();

    // Security/Login Daten
    @Username
    @NotBlank(message = "Username darf nicht leer sein")
    @Size(min = 3, max = 30, message = "Username muss zwischen 3 und 30 Zeichen lang sein")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Password
    @NotBlank(message = "Password darf nicht leer sein")
    @Size(min = 8, message = "Password muss mindestens 8 Zeichen lang sein")
    @Column(name = "password", nullable = false)
    private String password;

    @Roles
    @NotBlank(message = "Role darf nicht leer sein")
    private String role;

    // Konstruktoren
    public Kunde() {
    }

    public Kunde(String vorname, String nachname, String username, String password, String role) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.username = username;
        this.password = BcryptUtil.bcryptHash(password); 
        this.role = role;
    }

    // Utility Methoden für User Management
    public static Kunde createNewKunde(String vorname, String nachname, String username, String password, String role) {
        return new Kunde(vorname, nachname, username, password, role);
    }

    public void changePassword(String newPassword) {
        this.password = BcryptUtil.bcryptHash(newPassword);
    }

    public void addOrder(Long orderId) {
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }
        this.orders.add(orderId);
    }

    public boolean hasRole(String roleName) {
        return this.role != null && Objects.equals(this.role, roleName);
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getFullName() {
        return vorname + " " + nachname;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public List<Long> getOrders() {
        return orders != null ? new ArrayList<>(orders) : new ArrayList<>();
    }

    public void setOrders(List<Long> orders) {
        this.orders = orders != null ? new ArrayList<>(orders) : new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Kein Getter für Password aus Security-Gründen!
    public void setPassword(String password) {
        this.password = BcryptUtil.bcryptHash(password);
    }

    public boolean verifyPassword(String password) {
    // TESTCODE: Frischen Hash erstellen und sofort prüfen
        if (password.equals("password123")) {
            String testHash = BcryptUtil.bcryptHash(password);
            boolean selfTest = BcryptUtil.matches(password, testHash);
            System.out.println("Self-test (should be true): " + selfTest);
        
        if (!selfTest) {
            System.out.println("PROBLEM: Quarkus BCrypt funktioniert nicht mit sich selbst!");
        }
    }
    
    return BcryptUtil.matches(password, this.password);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // equals und hashCode basierend auf username (unique identifier)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kunde kunde = (Kunde) o;
        return Objects.equals(username, kunde.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "Kunde{" +
                "id=" + id +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", username='" + username + '\'' +
                ", role=" + (role != null ? role : "none") +
                '}';
    }
}
