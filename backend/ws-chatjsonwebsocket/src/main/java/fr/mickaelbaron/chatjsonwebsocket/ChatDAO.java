package fr.mickaelbaron.chatjsonwebsocket;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Modelise la base de donnee
 * 
 * @author teulierf
 * @version 1.0.0
 * @see BE-SAVE
 */
public class ChatDAO {
	
/////////////////////////////////DATA//////////////////////////////////////////////////////////

    // Simule une base de données en utilisant une structure de données en mémoire
    private static ConcurrentHashMap<String, List<ChatMessage>> chatMessagesByChatroom = new ConcurrentHashMap<>();
    private static Map<String, String> hashedPasswordsByUser = new HashMap<>();
    
    //Liste des utilisateur/demandeur/admin existant
  	private static List<ChatUtilisateur> existingUsers = Collections.synchronizedList(new ArrayList<>());
  	private static List<ChatUtilisateur> existingAdmin = Collections.synchronizedList(new ArrayList<>()); 
  	private static List<ChatUtilisateur> existingDemandeur = Collections.synchronizedList(new ArrayList<>()); 
  	
	//Liste des demandeurs + IdChatroom, pour unique demandeur par chatroom
	private static Map<String, String> demandeursParChatRoom = new HashMap<>();
  	
  	//Liste des chatroom existante
  	private static List<String> existingChatroom = Collections.synchronizedList(new ArrayList<>());
  	
  	//Map dernière connection utilisateur : Clé: username, valeur: date + heure connection
  	private static ConcurrentHashMap<String, Date> lastLoginTime = new ConcurrentHashMap<>();

  	
  	
  	
//////////////////////////////////GETERS & SETERS//////////////////////////////////////////////////////////
  	
    public void saveChatMessage(String chatroom, ChatMessage message) {
        // Sauvegarder le message dans la "base de données"
        chatMessagesByChatroom.computeIfAbsent(chatroom, k -> new ArrayList<>()).add(message);
    }

    public static List<ChatMessage> getChatHistory(String chatroom) {
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
    
    public static List<String> getExistingChatrooms() {
    	return existingChatroom;
    }
    public static List<ChatUtilisateur> getExistingUsers() {
    	return existingUsers;
    }
    
    public static List<ChatUtilisateur> getAdmin() {
    	return existingAdmin;
    }
    
    public static List<ChatUtilisateur> getDemandeur() {
    	return existingDemandeur;
    }
    
    public static Map<String, String> getDemandeurparChatroom() {
    	return demandeursParChatRoom;
    }
    
    public static void addDemandeurParChatroom(String chatroom, String username) {
    	demandeursParChatRoom.put(chatroom, username);
    }
    
    public static void updateLastLoginTime(String username) {
    	//Enregistrement d'une date de nouvelle connection
    	lastLoginTime.put(username,  new Date());
    }
    
    public static ConcurrentHashMap<String, Date> getLastLoginTime() {
    	return lastLoginTime;
    }

    
///////////////////////////////////SUPER ADMIN //////////////////////////////////////////////////////////
    
    static {
        ChatUtilisateur superAdmin = new ChatUtilisateur();
        superAdmin.setUserId("SuperAdmin");
        superAdmin.setRole("admin");
        existingUsers.add(superAdmin);
        existingAdmin.add(superAdmin);
        // Définir le mot de passe pour le SuperAdmin (à remplacer par un mot de passe sécurisé)
        String superAdminPassword = "Papapawww";
        String hashedPassword = PasswordHashing.hashPassword(superAdminPassword);
        ChatDAO.saveHashedPassword(superAdmin.getUserId(), hashedPassword);
    }
    
    
    public static void addAdminWithPassword(ChatUtilisateur superAdmin, ChatUtilisateur newAdmin, String newPassword) {
        // Vérifier si l'utilisateur qui veut ajouter un administrateur est bien le SuperAdmin
        if ("SuperAdmin".equals(superAdmin.getUserId()) && "admin".equals(superAdmin.getRole())) {
            // Définir le mot de passe pour le nouvel administrateur
            String hashedPassword = PasswordHashing.hashPassword(newPassword);
            ChatDAO.saveHashedPassword(newAdmin.getUserId(), hashedPassword);

            // Ajouter le nouvel administrateur à la liste
            existingAdmin.add(newAdmin);
        } else {
            // L'utilisateur n'a pas les droits pour ajouter un administrateur
            throw new IllegalStateException("Seul le SuperAdmin peut ajouter des administrateurs.");
        }
    }
}

