package fr.mickaelbaron.chatjsonwebsocket;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;

/**
 * Class gestion des tokens
 * @author teulierf
 * @version 1.0
 */

public class JwtUtil {

    private static final String KEY = "secret"; // Utilisez une clé plus sécurisée dans la production

    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS256, KEY)
                .compact();
    }
    // Ajoutez d'autres méthodes utiles si nécessaire
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
