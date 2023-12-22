package fr.mickaelbaron.chatjsonwebsocket;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Gestion de l'authentification et envoie la liste des chatrooms autorisées
 * 
 * @author Florine
 * @version 3.0.0
 * @see BE-SAVE
 */

@ServerEndpoint(value = "/chat/{role}/{username}/{password}",
				decoders = ChatMessageDecoder.class,
				encoders = ChatMessageEncoder.class)

public class ChatJSONRoomEndpoint {
	
		
		
	@OnOpen
    public void onOpen(Session session, @PathParam("username") String userName, @PathParam("role") String role, @PathParam("password") String password) throws IOException {
        System.out.println("ChatEndpoint.onOpen()");

        String hashedPasswordFromDatabase = ChatDAO.getHashedPasswordByUsername(userName);

        //Renvoie l'utilisateur s'il existe ou NULL sinon, grace au username
        ChatUtilisateur utilisateurExistant = getUtilisateurParUserId(userName);
        
        if ("SuperAdmin".equals(userName) && "admin".equals(role)) {
        	ChatUtilisateur newAdmin = new ChatUtilisateur();
            newAdmin.setUserId("NewAdmin");
            newAdmin.setRole("admin");
            ChatDAO.addAdminWithPassword(utilisateurExistant, newAdmin, "NouveauMotDePasse123");
        }
        
        //Les différents cas:
        
        if (utilisateurExistant != null) {
        	// L'utilisateur existe déjà
        	
        	// Vérifier le mot de passe
            if (hashedPasswordFromDatabase == null || !BCrypt.checkpw(password, hashedPasswordFromDatabase)) {
                // Mot de passe incorrect, refuser l'accès
            	System.out.println("Mauvais mot de passe!");
                session.close();
                return;
            }
            System.out.println("Bon mot de passe!");
            System.out.println("Utilisateur existant:" + utilisateurExistant.getRole() + ", " + utilisateurExistant.getUserId());
                	
            if ((getAdminParUserId(userName)) != null) {
            // c'est un admin qui est dans la liste
            	
                //Envoie de la liste des chatrooms à la vue
                ChatMessage infoMessage = new ChatMessage();
                infoMessage.setType("Liste chatrooms");
                infoMessage.setChatrooms(ChatDAO.getExistingChatrooms());
                broadcastListChatroom(infoMessage, session);
                                
                } else if ("demandeur".equals(role)){
                    //Envoie de la liste des chatrooms à la vue
                    ChatMessage infoMessage = new ChatMessage();
                    infoMessage.setType("Liste chatrooms");
                    infoMessage.setChatrooms(utilisateurExistant.getChatrooms());
                    broadcastListChatroom(infoMessage, session);
                                        
                } else {
                	System.out.println("Admin pas dans la liste");
                    session.close();
                    return;
                	} 
                }
            
         else {
          // L'utilisateur n'existe pas, créer un nouvel utilisateur
        	
        	if ("admin".equals(role)) {
        		System.out.println("Admin pas dans la liste");
                session.close();
                return;
        	}
        	else {
            ChatUtilisateur nouvelUtilisateur = new ChatUtilisateur();
            nouvelUtilisateur.setUserId(userName);
            nouvelUtilisateur.setRole(role);
            ChatDAO.getExistingUsers().add(nouvelUtilisateur);
            
            String hashedPassword = PasswordHashing.hashPassword(password);
            ChatDAO.saveHashedPassword(userName, hashedPassword);
            System.out.println("Enregistrement du mot de passe");
            System.out.println("Nouvel utilisateur:" + nouvelUtilisateur.getRole() + ", " + nouvelUtilisateur.getUserId());
                               
            //Envoie de la liste des chatrooms à la vue
            ChatMessage infoMessage = new ChatMessage();
            infoMessage.setType("Liste chatrooms");
            infoMessage.setChatrooms(nouvelUtilisateur.getChatrooms());
            broadcastListChatroom(infoMessage, session);
        	}
          }
          
    }
	
    //Afficher la liste des chatroom
    private void broadcastListChatroom(ChatMessage message, Session session) {
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }
    
	 /*Verifier si l'userId fait parti des userId deja existant
    renvoie Null si n'existe pas sinon renvoie le Chatutilisateur correspondant*/
    private ChatUtilisateur getUtilisateurParUserId(String userId) {

    	
        return ChatDAO.getExistingUsers().stream()
                .filter(utilisateur -> utilisateur.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    
    /*Verifier si l'userId fait parti des admins deja existant
    Renvoie Null si n'existe pas sinon renvoie le Chatutilisateur correspondant*/
    private ChatUtilisateur getAdminParUserId(String userId) {

    	
        return ChatDAO.getAdmin().stream()
                .filter(utilisateur -> utilisateur.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    
     

}
