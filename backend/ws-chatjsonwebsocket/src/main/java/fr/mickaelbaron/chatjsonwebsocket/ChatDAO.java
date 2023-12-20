package fr.mickaelbaron.chatjsonwebsocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatDAO {

    // Simule une base de données en utilisant une structure de données en mémoire
    private static ConcurrentHashMap<String, List<ChatMessage>> chatMessagesByChatroom = new ConcurrentHashMap<>();
    private static Map<String, String> hashedPasswordsByUser = new HashMap<>();

    public void saveChatMessage(String chatroom, ChatMessage message) {
        // Sauvegarder le message dans la "base de données"
        chatMessagesByChatroom.computeIfAbsent(chatroom, k -> new ArrayList<>()).add(message);
    }

    public List<ChatMessage> getChatHistory(String chatroom) {
        // Récupérer l'historique des messages pour une chatroom donnée
        return chatMessagesByChatroom.getOrDefault(chatroom, new ArrayList<>());
    }

    public static void saveHashedPassword(String username, String hashedPassword) {
        // Sauvegarder le mot de passe haché dans la "base de données"
        hashedPasswordsByUser.put(username, hashedPassword);
    }

    public static String getHashedPasswordByUsername(String username) {
        // Récupérer le mot de passe haché pour un utilisateur donné
        return hashedPasswordsByUser.get(username);
    }
}

