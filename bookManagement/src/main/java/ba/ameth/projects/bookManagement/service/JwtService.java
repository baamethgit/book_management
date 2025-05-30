package ba.ameth.projects.bookManagement.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // CLÉ SECRÈTE pour signer les tokens
    // POURQUOI ? Pour que personne ne puisse créer de faux tokens
    // Cette clé doit être gardée secrète et être assez longue
    private static final String SECRET_KEY = "mySecretKeyThatIsLongEnoughForHS256AlgorithmAndShouldBeKeptSecret";

    // DURÉE DE VIE du token (24 heures)
    // POURQUOI ? Pour limiter les risques si un token est volé
    private static final int JWT_EXPIRATION = 86400000; // 24h en millisecondes

    // MÉTHODE PRINCIPALE : Créer un token pour un utilisateur
    // QUAND L'UTILISER ? Après une connexion réussie
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // On met l'email dans le token (pas sensible)
        claims.put("email", userDetails.getUsername());
        return createToken(claims, userDetails.getUsername());
    }

    // CRÉER LE TOKEN avec toutes les infos
    // POURQUOI privée ? Seule generateToken() doit l'appeler
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)                    // Infos custom (email)
                .setSubject(subject)                  // Sujet principal (email)
                .setIssuedAt(new Date(System.currentTimeMillis()))           // Date de création
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))  // Date d'expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)         // Signature avec notre clé
                .compact();                           // Convertir en string
    }

    // GÉNÉRER LA CLÉ DE SIGNATURE
    // POURQUOI ? JWT doit être signé pour être sécurisé
    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);  // Crée une clé HMAC
    }

    // EXTRAIRE L'EMAIL du token
    // QUAND L'UTILISER ? Dans le filtre, pour savoir qui est connecté
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // EXTRAIRE LA DATE D'EXPIRATION
    // QUAND L'UTILISER ? Pour vérifier si le token est encore valide
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // EXTRAIRE UNE INFO SPÉCIFIQUE du token
    // POURQUOI générique ? Pour pouvoir extraire n'importe quelle info
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // EXTRAIRE TOUTES LES INFOS du token
    // POURQUOI privée ? Les autres méthodes publiques sont plus spécifiques
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())  // Utilise notre clé pour vérifier la signature
                .build()
                .parseClaimsJws(token)           // Parse et vérifie le token
                .getBody();                      // Récupère le contenu
    }

    // VÉRIFIER SI LE TOKEN EST EXPIRÉ
    // POURQUOI privée ? Utilisée seulement dans validateToken()
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // VALIDER COMPLÈTEMENT UN TOKEN
    // QUAND L'UTILISER ? Dans le filtre, à chaque requête protégée
    // VÉRIFIE : 1) L'email correspond 2) Le token n'est pas expiré
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}