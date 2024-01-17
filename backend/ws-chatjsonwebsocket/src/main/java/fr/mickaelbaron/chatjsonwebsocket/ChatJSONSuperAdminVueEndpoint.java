package fr.mickaelbaron.chatjsonwebsocket;

import java.io.IOException;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

/**
 * envoie list admin pour SuperAdmin
 * 
 * @author teulierf
 * @version 3.1.0
 * @see BE-SAVE
 */

@ServerEndpoint(value = "/chat/SuperAdmin/{token}/List",
				decoders = ChatMessageDecoder.class,
				encoders = ChatMessageEncoder.class)

public class ChatJSONSuperAdminVueEndpoint {
			
	@OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        System.out.println("ChatSuperAdminListEndpoint.onOpen()");
        
        if (!JwtUtil.validateToken(token)) {
            System.out.println("Token invalide");
            session.close();
            return;
        }
        
        //Envoie de la liste des chatrooms à la VUE
        ChatMessage infoMessage = new ChatMessage();
        infoMessage.setType("Liste admins");
        infoMessage.setAdm(ChatDAO.getAdmin());
        broadcastListChatroom(infoMessage, session);
           
	}
	
    private void broadcastListChatroom(ChatMessage message, Session session) {
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
        	System.out.println("Pb avec l'envoie liste chatroom");
            e.printStackTrace();
        }
    }

    
}