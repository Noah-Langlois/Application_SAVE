package fr.mickaelbaron.chatjsonwebsocket;

import org.mindrot.jbcrypt.BCrypt;

/**
 * hashing des mots de passe
 * @author teulierf
 * @see BE-SAVE
 */

public class PasswordHashing {
    public static String hashPassword(String plainPassword) {
        // Générer un sel (salt) aléatoire
        String salt = BCrypt.gensalt();

        // Hasher le mot de passe avec le sel
        return BCrypt.hashpw(plainPassword, salt);
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        // Vérifier le mot de passe en le comparant au haché stocké
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}

