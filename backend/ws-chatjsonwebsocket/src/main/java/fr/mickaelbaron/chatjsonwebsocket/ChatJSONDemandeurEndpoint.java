package fr.mickaelbaron.chatjsonwebsocket;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Gestion de l'authentification et envoie la liste des chatrooms autorisées
 * 
 * @author teulierf
 * @version 3.1.0
 * @see BE-SAVE
 */

@ServerEndpoint(value = "/chat/demandeur/{username}/{password}",
				decoders = ChatMessageDecoder.class,
				encoders = ChatMessageEncoder.class)

public class ChatJSONDemandeurEndpoint {
			
	@OnOpen
    public void onOpen(Session session, @PathParam("username") String userName, @PathParam("password") String password) throws IOException {
        System.out.println("ChatDemandeurEndpoint.onOpen()");

        String hashedPasswordFromDatabase = ChatDAO.getHashedPasswordByUsername(userName);

        //Renvoie l'utilisateur s'il existe ou NULL sinon, grace au username
        ChatUtilisateur demandeurExistant = getDemandeurParUserId(userName);

        
        //Les différents cas:
        
        // L'utilisateur existe déjà
        if (demandeurExistant != null) {
        
        	// Vérifier le mot de passe
            if (hashedPasswordFromDatabase == null || !BCrypt.checkpw(password, hashedPasswordFromDatabase)) {
                
            	// Mot de passe incorrect, refuser l'accès
            	System.out.println("Mauvais mot de passe!");
                session.close();
                return;
            }
            
            System.out.println("Bon mot de passe!");
            System.out.println("Utilisateur existant:" + demandeurExistant.getRole() + ", " + demandeurExistant.getUserId());
        	
            //Generation du token
            String token = JwtUtil.generateToken(userName, "demandeur");
            ChatMessage tokenMessage = new ChatMessage();
            tokenMessage.setType("Token");
            tokenMessage.setContent(token);
            broadcastToken(tokenMessage, session);

               	
            //Envoie de la liste des chatrooms à la VUE
        	ChatMessage infoMessage = new ChatMessage();
            infoMessage.setType("Liste chatrooms");
            infoMessage.setChatrooms(demandeurExistant.getChatrooms());
            broadcastListChatroom(infoMessage, session);

            
            //Envoyer les notifications pour messages non lu
            System.out.println("check unread messages");
            checkForUnreadMessages(demandeurExistant, session);
            
            //Stocker la date de connection
            ChatDAO.updateLastLoginTime(userName);
                                        
        }   
         else {
        	// L'utilisateur n'existe pas, créer un nouvel utilisateur
      		
        	//Ajout nouvel utilisateur
            ChatUtilisateur nouvelUtilisateur = new ChatUtilisateur();
            nouvelUtilisateur.setUserId(userName);
            nouvelUtilisateur.setRole("demandeur");
            ChatDAO.getExistingUsers().add(nouvelUtilisateur);
            ChatDAO.getDemandeur().add(nouvelUtilisateur);
            
            //Enregistrement du mdp
            String hashedPassword = PasswordHashing.hashPassword(password);
            ChatDAO.saveHashedPassword(userName, hashedPassword);
            System.out.println("Enregistrement du mot de passe");
            System.out.println("Nouvel utilisateur:" + nouvelUtilisateur.getRole() + ", " + nouvelUtilisateur.getUserId());
            
            //Generation du token
            String token = JwtUtil.generateToken(userName, "demandeur");
            ChatMessage tokenMessage = new ChatMessage();
            tokenMessage.setType("Token");
            tokenMessage.setContent(token);
            broadcastToken(tokenMessage, session);
            
            //Stocker la date de connection
            ChatDAO.updateLastLoginTime(userName);
            
            //Envoie de la liste des chatrooms à la vue
            ChatMessage infoMessage = new ChatMessage();
            infoMessage.setType("Liste chatrooms");
            infoMessage.setChatrooms(nouvelUtilisateur.getChatrooms());
            broadcastListChatroom(infoMessage, session);
        	}
          
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
    
    
    /*Verifier si l'userId fait parti des demandeur deja existant
    Renvoie Null si n'existe pas sinon renvoie le Chatutilisateur correspondant*/
    private ChatUtilisateur getDemandeurParUserId(String userId) {

    	
        return ChatDAO.getDemandeur().stream()
                .filter(utilisateur -> utilisateur.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    
    private void broadcastUnreadNotification(ChatMessage notificationMessage, Session session) {
    	try {
            session.getBasicRemote().sendObject(notificationMessage);
            System.out.println("Envoie de la notif unread");
        } catch (IOException | EncodeException e) {
        	System.out.println("Pb avec unread");
            e.printStackTrace();
        }
    }
    
    private void broadcastToken(ChatMessage tokenmessage, Session session) {
    	try {
            session.getBasicRemote().sendObject(tokenmessage);
            System.out.println("Envoie du token");
        } catch (IOException | EncodeException e) {
        	System.out.println("Pb envoie token");
            e.printStackTrace();
        }
    }
    
    public void checkForUnreadMessages(ChatUtilisateur utilisateur, Session session) {
    	Date lastLogin = ChatDAO.getLastLoginTime().getOrDefault(utilisateur.getUserId(), new Date());
    	System.out.println("Heure connection:" + lastLogin);
    	if (utilisateur.getRole()=="demandeur") {
	    	for (String chatroom : utilisateur.getChatrooms()) {
	    		List<ChatMessage> messages = ChatDAO.getChatHistory(chatroom);
	    		
	    		for (ChatMessage message : messages) {
	    			System.out.println(message.getCreated());
	    			if (message.getCreated().after(lastLogin)) {
	    				ChatMessage infoMessage = new ChatMessage();
	    		        infoMessage.setType("Notification");
	    		        infoMessage.setChatroomId(message.getChatroomId());
	    		        broadcastUnreadNotification(infoMessage, session);    				
	    			}
	    			else {
	    				System.out.println("Pas de message non lu");
	    			}
	    		}
	    	}
    	} else {
    		for (String chatroom : ChatDAO.getExistingChatrooms()) {
	    		List<ChatMessage> messages = ChatDAO.getChatHistory(chatroom);
	    		
	    		for (ChatMessage message : messages) {
	    			System.out.println(message.getCreated());
	    			if (message.getCreated().after(lastLogin)) {
	    				ChatMessage infoMessage = new ChatMessage();
	    		        infoMessage.setType("Notification");
	    		        infoMessage.setChatroomId(message.getChatroomId());
	    		        broadcastUnreadNotification(infoMessage, session);    				
	    			}
	    			else {
	    				System.out.println("Pas de message non lu");
	    				}
	    			}   	
    			}
    		}
    	
    	}
    }
    		

