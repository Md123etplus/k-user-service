package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")  // Base path for all endpoints
public class UserController {

    private static final String SUPABASE_URL = "https://aqfwsgieequeqdcwneff.supabase.co";
    private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFxZndzZ2llZXF1ZXFkY3duZWZmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI1OTc0NzksImV4cCI6MjA1ODE3MzQ3OX0.OS5figsWYDiAe5zoutSSPY8HlvGiNeId6i_9RMBibM8";
    
    private final RestTemplate restTemplate = new RestTemplate();

    // ✅ INSCRIPTION D'UN UTILISATEUR
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> requestBody) {
        String email = (String) requestBody.get("email");
        String password = (String) requestBody.get("password");
        String fullName = (String) requestBody.get("full_name");
        String role = (String) requestBody.get("role");

        // Préparer les données pour Supabase Auth API
        Map<String, Object> supabaseRequest = new HashMap<>();
        supabaseRequest.put("email", email);
        supabaseRequest.put("password", password);
        supabaseRequest.put("data", Map.of("full_name", fullName, "role", role));

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", SUPABASE_API_KEY);
        headers.set("Authorization", "Bearer " + SUPABASE_API_KEY);
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(supabaseRequest, headers);
        String supabaseAuthUrl = SUPABASE_URL + "/auth/v1/signup";

        try {
            ResponseEntity<String> response = restTemplate.exchange(supabaseAuthUrl, HttpMethod.POST, entity, String.class);
            return ResponseEntity.ok(response.getBody());  
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur d'inscription: " + e.getMessage());
        }
    }

    // ✅ LOGIN UTILISATEUR
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, Object> requestBody) {
        String email = (String) requestBody.get("email");
        String password = (String) requestBody.get("password");

        // Préparer la requête pour Supabase Auth API
        Map<String, Object> loginRequest = new HashMap<>();
        loginRequest.put("email", email);
        loginRequest.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", SUPABASE_API_KEY);
        headers.set("Authorization", "Bearer " + SUPABASE_API_KEY);
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(loginRequest, headers);
        String supabaseAuthUrl = SUPABASE_URL + "/auth/v1/token?grant_type=password";

        try {
            ResponseEntity<String> response = restTemplate.exchange(supabaseAuthUrl, HttpMethod.POST, entity, String.class);
            return ResponseEntity.ok(response.getBody());  // Retourner la réponse de Supabase (token JWT)
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Échec de la connexion: " + e.getMessage());
        }
    }
}
