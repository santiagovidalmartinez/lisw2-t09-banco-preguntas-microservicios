package co.edu.unicauca.user.model;

import java.util.List;

/**
 * Entidad de usuario del Banco de Preguntas Saber PRO.
 * Repositorio en memoria: cada microservicio gestiona su propia persistencia
 * (Database per Service); en este taller se simplifica con datos semilla.
 */
public class User {

    private final Long id;
    private final String username;
    private final String passwordHash; // simulación simple (texto plano hasheado trivialmente)
    private final String fullName;
    private final String email;
    private final List<String> roles;

    public User(Long id, String username, String passwordHash, String fullName, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
    }

    public boolean validatePassword(String rawPassword) {
        return this.passwordHash.equals(hash(rawPassword));
    }

    public static String hash(String rawPassword) {
        // Simulación simple de hashing (NO usar en producción). Suficiente para el alcance del taller.
        return Integer.toHexString(rawPassword.hashCode());
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public List<String> getRoles() { return roles; }
}
