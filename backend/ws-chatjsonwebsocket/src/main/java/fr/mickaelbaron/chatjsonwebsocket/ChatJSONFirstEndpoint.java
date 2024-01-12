package fr.mickaelbaron.chatjsonwebsocket;

import java.io.IOException;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Notifie interface si utilisateur connu ou pas avant connection
 * 
 * @author teulierf
 * @version 1.0.0
 * @see BE-SAVE
 */

@ServerEndpoint(value = "/chat/{role}/{username}",
decoders = ChatMessageDecoder.class,
encoders = ChatMessageEncoder.class)
public class ChatJSONFirstEndpoint {
	
	@OnOpen
	public void onOpen(Session session, @PathParam("role") String role, @PathParam("username") String userName) throws IOException {
		
		ChatUtilisateur utilisateurExistant = getUtilisateurParUserId(userName);
		if (utilisateurExistant != null) {
		//Utilisateur existe
			
			if (utilisateurExistant.getRole().equals(role)) {
			//Role ok
			ChatMessage infoMessage = new ChatMessage();
            infoMessage.setType("Utilisateur existant");
            broadcastListChatroom(infoMessage, session);}
			
			else {
			//Mauvais role renseigne.
				System.out.println("Admin pas dans la liste");
                session.close();
                return;
			}
		}
		else {
		//Utilisateur pas dans la liste donc nouveau
			
			if ("admin".equals(role)) {
				System.out.println("Ajout d'admin uniquement par SuperAdmin");
                session.close();
                return;
			}
			else {
				ChatMessage infoMessage = new ChatMessage();
				infoMessage.setType("Nouvel utilisateur");
				broadcastListChatroom(infoMessage, session);
			}
		}
	}
	
	//Verifier si l'userId fait parti des userId deja existant
    //Renvoie Null si n'existe pas sinon renvoie le Chatutilisateur correspondant
	private ChatUtilisateur getUtilisateurParUserId(String userId) {
	    
	    	
	        return ChatDAO.getExistingUsers().stream()
	                .filter(utilisateur -> utilisateur.getUserId().equals(userId))
	                .findFirst()
	                .orElse(null);
	    }
	//Afficher la liste des chatroom
    private void broadcastListChatroom(ChatMessage message, Session session) {
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }
    
}
