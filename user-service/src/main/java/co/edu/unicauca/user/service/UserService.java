package co.edu.unicauca.user.service;

import co.edu.unicauca.user.dto.LoginResponseDTO;
import co.edu.unicauca.user.model.User;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private final Map<String, User> usuariosPorUsername = new ConcurrentHashMap<>();

    public UserService() {
        // Datos semilla (en memoria) para la demostración del taller.
        usuariosPorUsername.put("docente1", new User(1L, "docente1", User.hash("Docente123!"),
                "Santiago Vidal Martínez", "docente1@unicauca.edu.co", List.of("DOCENTE", "AUTOR")));
        usuariosPorUsername.put("admin1", new User(2L, "admin1", User.hash("Admin123!"),
                "Administrador Saber PRO", "admin1@unicauca.edu.co", List.of("ADMIN")));
        usuariosPorUsername.put("revisor1", new User(3L, "revisor1", User.hash("Revisor123!"),
                "Revisor Par Académico", "revisor1@unicauca.edu.co", List.of("REVISOR")));
    }

    public LoginResponseDTO login(String username, String password) {
        User user = usuariosPorUsername.get(username);
        if (user == null || !user.validatePassword(password)) {
            return LoginResponseDTO.failure("Usuario o contraseña incorrectos.");
        }
        String token = Base64.getEncoder().encodeToString(
                (user.getUsername() + ":" + UUID.randomUUID()).getBytes());
        return LoginResponseDTO.success(user.getId(), user.getUsername(), user.getFullName(), user.getRoles(), token);
    }
}
