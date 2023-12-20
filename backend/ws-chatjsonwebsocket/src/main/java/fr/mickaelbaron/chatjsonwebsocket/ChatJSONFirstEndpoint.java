package fr.mickaelbaron.chatjsonwebsocket;

import java.io.IOException;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat/{role}/{username}",
decoders = ChatMessageDecoder.class,
encoders = ChatMessageEncoder.class)
public class ChatJSONFirstEndpoint {
	
	@OnOpen
	public void onOpen(Session session, @PathParam("username") String userName) {
		
		ChatUtilisateur utilisateurExistant = getUtilisateurParUserId(userName);
		if (utilisateurExistant != null) {
			ChatMessage infoMessage = new ChatMessage();
            infoMessage.setType("Utilisateur existant");
            broadcastListChatroom(infoMessage, session);
		}
		else {
			ChatMessage infoMessage = new ChatMessage();
            infoMessage.setType("Nouvel utilisateur");
            broadcastListChatroom(infoMessage, session);
		}
	}
	private ChatUtilisateur getUtilisateurParUserId(String userId) {
	    //Verifier si l'userId fait parti des userId deja existant
	    //Renvoie Null si n'existe pas sinon renvoie le Chatutilisateur correspondant
	    	
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
