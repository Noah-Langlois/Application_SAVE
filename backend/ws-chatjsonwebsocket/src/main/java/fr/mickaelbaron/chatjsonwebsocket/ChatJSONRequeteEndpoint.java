package fr.mickaelbaron.chatjsonwebsocket;

import java.io.IOException;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
/**
 * Actualisation de la liste des chatrooms
 * 
 * @author teulierf
 * @version 1.0.0
 * @see BE-SAVE
 */

@ServerEndpoint(value = "/chat/requete/{username}/{token}",
				decoders = ChatMessageDecoder.class,
				encoders = ChatMessageEncoder.class)
public class ChatJSONRequeteEndpoint {
	
	@OnOpen
    public void onOpen(Session session ,@PathParam("username") String userName, @PathParam("token") String token) throws IOException {
		
		System.out.println("ChatRequeteEndpoint.onOpen()");
		
		//Verification des paramètres:
	    if (userName == null) {
	        System.out.println("Username ou role ne peut pas être null");
	        session.close();
	        return;
	    }
	    
		// Verif token
		if (!JwtUtil.validateToken(token)) {
            System.out.println("Token invalide");
            session.close();
            return;
        }

        // Renvoie l'utilisateur s'il existe ou NULL sinon, grace au username
        ChatUtilisateur utilisateurExistant = getUtilisateurParUserId(userName);
        
        
        // L'utilisateur existe déjà
        if (utilisateurExistant != null) {
                                       
            // C'est un admin qui est dans la liste
            if (utilisateurExistant.getRole()=="admin") {
            
                // Envoie de la liste des chatrooms à la VUE
                System.out.println("Envoie de la liste de chatroom");
                ChatMessage infoMessage = new ChatMessage();
                infoMessage.setType("Liste chatrooms");
                infoMessage.setChatrooms(ChatDAO.getExistingChatrooms());
                broadcastListChatroom(infoMessage, session);
                
                                              
            } else if (utilisateurExistant.getRole()=="demandeur"){
               	
                // Envoie de la liste des chatrooms à la VUE
                System.out.println("Envoie de la liste de chatroom");
            	ChatMessage infoMessage = new ChatMessage();
                infoMessage.setType("Liste chatrooms");
                infoMessage.setChatrooms(utilisateurExistant.getChatrooms());
                broadcastListChatroom(infoMessage, session);
                                                           
            } else {
            	System.out.println("Pas de role");
                session.close();
                return;
            	} 
            }
	}
	
	
    /*Verifier si l'userId fait parti des userId deja existant
	Renvoie Null si n'existe pas sinon renvoie le Chatutilisateur correspondant*/    	
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
        	System.out.println("Pb avec l'envoie liste chatroom");
            e.printStackTrace();
        }
    }


	
}
