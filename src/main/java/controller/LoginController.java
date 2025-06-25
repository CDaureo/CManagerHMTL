package controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Map;
import java.util.HashMap;

@RestController
public class LoginController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        Map<String, Object> response = new HashMap<>();

        try {
            // Verifica si el usuario existe
            String userSql = "SELECT password FROM usuarios WHERE username = ?";
            String storedPassword = jdbcTemplate.queryForObject(userSql, String.class, username);

            if (storedPassword == null) {
                response.put("success", false);
                response.put("message", "El usuario no existe");
            } else if (!storedPassword.equals(password)) {
                response.put("success", false);
                response.put("message", "Contraseña incorrecta");
                response.put("resetPassword", true); // Para que el frontend borre el campo
            } else {
                response.put("success", true);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al iniciar sesión");
        }
        return response;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        Map<String, Object> response = new HashMap<>();

        try {
            // Verifica si el usuario ya existe
            String checkSql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";
            Integer exists = jdbcTemplate.queryForObject(checkSql, Integer.class, username);
            if (exists != null && exists > 0) {
                response.put("success", false);
                response.put("message", "El usuario ya existe");
                return response;
            }

            // Inserta el nuevo usuario
            String sql = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
            jdbcTemplate.update(sql, username, password);
            response.put("success", true);
        } catch (Exception e) {
            e.printStackTrace(); // Esto mostrará el error real en la consola
            response.put("success", false);
            response.put("message", "Error al registrar usuario: " + e.getMessage());
        }
        return response;
    }
}