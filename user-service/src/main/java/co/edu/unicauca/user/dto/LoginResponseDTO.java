package co.edu.unicauca.user.dto;

import java.util.List;

public class LoginResponseDTO {

    private boolean authenticated;
    private String message;
    private Long userId;
    private String username;
    private String fullName;
    private List<String> roles;
    /**
     * Identificador simple de sesión (NO es un JWT). Tal como lo indica el
     * enunciado del taller, en esta versión no se implementan tokens de
     * acceso reales; se simula con un identificador opaco que Question
     * Service exige en el encabezado Authorization.
     */
    private String token;

    public static LoginResponseDTO failure(String message) {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.authenticated = false;
        dto.message = message;
        return dto;
    }

    public static LoginResponseDTO success(Long userId, String username, String fullName, List<String> roles, String token) {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.authenticated = true;
        dto.message = "Autenticación exitosa.";
        dto.userId = userId;
        dto.username = username;
        dto.fullName = fullName;
        dto.roles = roles;
        dto.token = token;
        return dto;
    }

    public boolean isAuthenticated() { return authenticated; }
    public String getMessage() { return message; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public List<String> getRoles() { return roles; }
    public String getToken() { return token; }
}
