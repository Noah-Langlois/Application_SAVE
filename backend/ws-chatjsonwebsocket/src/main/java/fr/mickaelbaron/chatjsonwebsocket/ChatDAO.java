package fr.mickaelbaron.chatjsonwebsocket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ChatDAO {

    // Simule une base de données en utilisant une structure de données en mémoire
    private static ConcurrentHashMap<String, List<ChatMessage>> chatMessagesByChatroom = new ConcurrentHashMap<>();

    public void saveChatMessage(String chatroom, ChatMessage message) {
        // Sauvegarder le message dans la "base de données"
        chatMessagesByChatroom.computeIfAbsent(chatroom, k -> new ArrayList<>()).add(message);
    }

    public List<ChatMessage> getChatHistory(String chatroom) {
        // Récupérer l'historique des messages pour une chatroom donnée
        return chatMessagesByChatroom.getOrDefault(chatroom, new ArrayList<>());
    }
}
