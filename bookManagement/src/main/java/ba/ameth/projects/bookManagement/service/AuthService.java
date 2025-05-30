package ba.ameth.projects.bookManagement.service;

import ba.ameth.projects.bookManagement.dto.AuthResponse;
import ba.ameth.projects.bookManagement.dto.LoginRequest;
import ba.ameth.projects.bookManagement.dto.RegisterRequest;
import ba.ameth.projects.bookManagement.entities.User;
import ba.ameth.projects.bookManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // Créer un nouvel utilisateur
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encoder le mot de passe


        // Sauvegarder en base
        User savedUser = userRepository.save(user);

        // Générer le token JWT
        String token = jwtService.generateToken(savedUser);

        // Retourner la réponse
        return new AuthResponse(token, savedUser.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        // Vérifier email/password avec Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Récupérer l'utilisateur
        User user = (User) authentication.getPrincipal();

        // Générer le token
        String token = jwtService.generateToken(user);

        // Retourner la réponse
        return new AuthResponse(token, user.getEmail());
    }
}