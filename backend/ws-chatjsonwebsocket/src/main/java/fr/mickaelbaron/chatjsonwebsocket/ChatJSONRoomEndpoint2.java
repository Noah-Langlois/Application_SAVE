package fr.mickaelbaron.chatjsonwebsocket;

import java.io.IOException;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

/**
 * @author Florine
 * Serveur pour l'application BE-SAVE
 */
@ServerEndpoint(value = "/chat/{role}/{username}",
				decoders = ChatMessageDecoder.class,
				encoders = ChatMessageEncoder.class)

public class ChatJSONRoomEndpoint2 {
	
		
		
	@OnOpen
    public void onOpen(Session session, @PathParam("username") String userName, @PathParam("role") String role) throws IOException {
        System.out.println("ChatEndpoint.onOpen()");
        
        //Renvoie l'utilisateur s'il existe ou NULL sinon, grace au username
        ChatUtilisateur utilisateurExistant = getUtilisateurParUserId(userName);
        
        //Les différents cas:
        
        if (utilisateurExistant != null) {
        // L'utilisateur existe déjà
        	
            if ("admin".equals(utilisateurExistant.getRole())) {
            // Si admin connexion possible a toutes les conversations
            	
                //Envoie de la liste des chatrooms à la vue
                ChatMessage infoMessage = new ChatMessage();
                infoMessage.setType("Liste chatrooms");
                infoMessage.setChatrooms(ChatDAO.getExistingChatrooms());
                broadcastListChatroom(infoMessage, session);
                
                
                } else {
                    //Envoie de la liste des chatrooms à la vue
                    ChatMessage infoMessage = new ChatMessage();
                    infoMessage.setType("Liste chatrooms");
                    infoMessage.setChatrooms(utilisateurExistant.getChatrooms());
                    broadcastListChatroom(infoMessage, session);
                                        
                }} 
            
         else {
          // L'utilisateur n'existe pas, créer un nouvel utilisateur
        	
            ChatUtilisateur nouvelUtilisateur = new ChatUtilisateur();
            nouvelUtilisateur.setUserId(userName);
            nouvelUtilisateur.setRole(role);

            if ("admin".equals(nouvelUtilisateur.getRole())) {
            // Si c'est un nouvel admin, alors toutes les chatroom autorisées
      
                
                //Envoie de la liste des chatrooms à la vue
                ChatMessage infoMessage = new ChatMessage();
                infoMessage.setType("Liste chatrooms");
                infoMessage.setChatrooms(ChatDAO.getExistingChatrooms());
                broadcastListChatroom(infoMessage, session);
                
                
            } else {
            //C'est un demandeur
            	
            	
                    
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
	
    private ChatUtilisateur getUtilisateurParUserId(String userId) {
    //Verifier si l'userId fait parti des userId deja existant
    //Renvoie Null si n'existe pas sinon renvoie le Chatutilisateur correspondant
    	
        return ChatDAO.getExistingUsers().stream()
                .filter(utilisateur -> utilisateur.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    
     

}
