package ba.ameth.projects.bookManagement.configuration;

import ba.ameth.projects.bookManagement.service.CustomUserDetailsService;
import ba.ameth.projects.bookManagement.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    private CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // MÉTHODE PRINCIPALE : S'exécute à CHAQUE requête
    // POURQUOI OncePerRequestFilter ? Pour éviter les doublons
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // ÉTAPE 1 : Récupérer le header Authorization
        // FORMAT ATTENDU : "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // ÉTAPE 2 : Vérifier si le header existe et a le bon format
        // POURQUOI cette vérification ? Toutes les requêtes n'ont pas de token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // PAS DE TOKEN → on continue sans authentifier
            filterChain.doFilter(request, response);
            return;
        }

        // ÉTAPE 3 : Extraire le token (enlever "Bearer ")
        // "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." → "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        jwt = authHeader.substring(7);

        // ÉTAPE 4 : Extraire l'email du token
        // POURQUOI ? Pour identifier l'utilisateur connecté
        userEmail = jwtService.extractUsername(jwt);

        // ÉTAPE 5 : Vérifier si on a un email ET s'il n'y a pas déjà d'authentification
        // POURQUOI cette double vérification ?
        // - userEmail != null : le token contient un email valide
        // - SecurityContextHolder vide : pas déjà authentifié dans cette requête
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // ÉTAPE 6 : Charger l'utilisateur depuis la base de données
            // POURQUOI ? Pour avoir les infos complètes et vérifier qu'il existe toujours
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // ÉTAPE 7 : Valider le token
            // VÉRIFIE : email correspond + token pas expiré
            if (jwtService.validateToken(jwt, userDetails)) {

                // ÉTAPE 8 : Créer l'objet d'authentification pour Spring Security
                // POURQUOI UsernamePasswordAuthenticationToken ?
                // C'est le format que Spring Security comprend
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,                    // L'utilisateur
                        null,                          // Pas de mot de passe (on utilise JWT)
                        userDetails.getAuthorities()   // Les rôles/permissions
                );

                // Ajouter des détails sur la requête
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ÉTAPE 9 : DIRE À SPRING SECURITY "CET UTILISATEUR EST CONNECTÉ"
                // À partir de maintenant, Spring Security considère cette requête comme authentifiée
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // ÉTAPE 10 : Continuer vers le prochain filtre/controller
        // POURQUOI toujours faire ça ? La requête doit continuer son chemin
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/");
    }


}