package fr.mickaelbaron.chatjsonwebsocket;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Connecte l'utilisateur à la chatroom demandée et gère l'envoi de messages
 * 
 * @author teulierf
 * @version 1.0.0
 * @see BE-SAVE
 */

@ServerEndpoint(value = "/chat/{role}/{username}/{token}/{chatroom}",
				decoders = ChatMessageDecoder.class,
				encoders = ChatMessageEncoder.class)

public class ChatJSONChatroomEndpoint {

	// Liste des sessions connectées
	private static Map<String, Session> allSessions = new HashMap<>();

	// Liste des noms utilisateurs connectés
	private static Map<String, String> allUsers = new HashMap<>();

	// Liste des chatrooms en cours
	private static Map<String, String> allChatRooms = new HashMap<>();

	//Stocker les infos des utilisateurs pour constrcution du type Chatmessage
	private static Map<String, String> userRoles = new ConcurrentHashMap<>();
	private static Map<String, String> userNames = new ConcurrentHashMap<>();
	private static Map<String, String> chatIds = new ConcurrentHashMap<>();
	
	private static ChatDAO chatDAO = new ChatDAO();

    @OnOpen
    public void onOpen(Session session, @PathParam("chatroom") String chatRoom, @PathParam("username") String userName, @PathParam("role") String role, @PathParam("token") String token)
            throws IOException {
        
    	System.out.println("ChatroomEndpoint.onOpen()");

        //Numéro de la connection
        System.out.println("Connection number:" + session.getOpenSessions().size());
        allChatRooms.put(session.getId(), chatRoom);
           
        //Renvoie l'utilisateur s'il existe ou NULL sinon, grace au username
        ChatUtilisateur utilisateurExistant = getUtilisateurParUserId(userName);
        
        //Verification de token  
        if (!JwtUtil.validateToken(token)) {
            System.out.println("Token invalide");
            session.close();
            return;
        }
        
		//Verification des paramètres:
	    if (userName == null || chatRoom == null) {
	        System.out.println("Username ou chatroom ne peut pas être null");
	        session.close();
	        return;
	    }
        
        // Vérification si le rôle est soit "demandeur" soit "admin"
        if (!"demandeur".equals(role) && !"admin".equals(role)) {
            System.out.println("Rôle non autorisé");
            session.close();
            return;
        }
        
        // Admin connexion possible à toutes les conversations
        if ("admin".equals(utilisateurExistant.getRole())) {
        	
        	//Ajout à la liste des actifs
            allUsers.put(session.getId(), userName);
            allSessions.put(userName, session);
            System.out.println("Connection Admin:" + utilisateurExistant.getRole() + ", " + utilisateurExistant.getUserId() + ", " + chatRoom);
            
            //Recupération des messsages:
            List<ChatMessage> chatMessages = ChatDAO.getChatHistory(chatRoom);
            broadcastHistory(chatMessages, session);
            
        //Demandeur, mais verification de la chatroom
        } else {
        	
            // La chatroom existante et dans sa liste, connectez l'utilisateur à cette chatroom
        	if (utilisateurExistant.getChatrooms().contains(chatRoom)) {
        		
        		System.out.println("Chatroom dans la liste demandeur");
        		allUsers.put(session.getId(), userName);
                allSessions.put(userName, session);
                System.out.println("Connection Demandeur à chatroom existante:" + utilisateurExistant.getRole() + ", " + utilisateurExistant.getUserId() + ", " + chatRoom);
                
                //Recupération des messsages:
                List<ChatMessage> chatMessages = ChatDAO.getChatHistory(chatRoom);
                broadcastHistory(chatMessages, session);
                
            
	        // La chatroom non existante mais est utilisée par un autre demandeur   
            } else if (ChatDAO.getDemandeurparChatroom().containsKey(chatRoom)) {
            	
            	System.out.println("Chatroom deja utilisée par autre demandeur");
                session.close();
             
            // La chatroom n'existe pas et n'est pas utilisée, ajouter et connecter l'utilisateur    
            }else {
            
            //Ajout Chatroom à la liste demandeur
            utilisateurExistant.ajouterChatroom(chatRoom);
            ChatDAO.addDemandeurParChatroom(chatRoom, userName);
            
            allUsers.put(session.getId(), userName);
            allSessions.put(userName, session);
            ChatDAO.getExistingChatrooms().add(chatRoom);
            System.out.println("Connection demandeur à nouvelle chatroom:" + utilisateurExistant.getRole() + ", " + utilisateurExistant.getUserId() + ", " + chatRoom);            
            
            //Recupération des messsages:
            List<ChatMessage> chatMessages = ChatDAO.getChatHistory(chatRoom);
            broadcastHistory(chatMessages, session);
                
            }
        }
            
        //Remplissage des listes info utilisateur pour l'afficher lors de l'envoie du Chatmessage
        userRoles.put(session.getId(), role);
        userNames.put(session.getId(), userName);
        chatIds.put(session.getId(), chatRoom);
        
    }
    
    
    @OnMessage
    public void onMessage(Session session, ChatMessage message) {
        System.out.println("ChatEndpoint.onMessage()");
        
        //Récupération des données utilisateur
        String role = userRoles.get(session.getId());
        String username = userNames.get(session.getId());
        String chat = chatIds.get(session.getId());

        //Création du ChatMessage avec toutes les infos utilisateur
        message.setRole(role);
        message.setUserId(username);
        message.setChatroomId(chat);
        message.setSessionId(session.getId());
        message.setCreated(new Date());
        message.setType("message chat");
        
        this.broadcastObjectMessage(message, allUsers.get(session.getId()), null,
                allChatRooms.get(session.getId()));
        
        //Sauvegarde du message
        chatDAO.saveChatMessage(message.getChatroomId(), message);
        
        //Création de la notification d'envoie de message
        ChatMessage infoMessage = new ChatMessage();
        infoMessage.setType("Notification");
        infoMessage.setChatroomId(chat);
        this.broadcastNotification(infoMessage, session, infoMessage.getChatroomId());
    }

    @OnClose
    public void onClose(Session session) {
        //System.out.println("ChatEndpoint.onClose()");
        
    	System.out.println("Deconnection de :" + allUsers.get(session.getId()));
    	
        //Retirer l'id de connection de la liste des session ouvertes et utilisateurs en cours
        String currentUsername = allUsers.get(session.getId());
        allSessions.remove(currentUsername);
        allUsers.remove(session.getId());
        
        ChatMessage newChatMessage = new ChatMessage();
        newChatMessage.setContent(currentUsername);
        this.broadcastStringMessage(currentUsername + " disconnected!", session, allChatRooms.get(session.getId()));
    }
    

    //Envoyer un ChatMessage
    private void broadcastObjectMessage(ChatMessage message, String user, Session exclude, String currentChatRoom) {
        allSessions.forEach((username, session) -> {
        //Parcourir toute les sessions de allsessions
        	
        	String chatRoom = allChatRooms.get(session.getId());
        	//Recupère la chatroom associée
        	
            try {
                if (!(exclude != null && session.getId().equals(exclude.getId()))) {
                //exclude null ou session differente de exclude
                	
                    if (chatRoom != null && chatRoom.equals(currentChatRoom)) {
                    //Chatroom non nul et chatroom correspond a la chatroom utilisée
                    	
                        session.getBasicRemote().sendObject(message);
                    }
                }
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    }
    
    
    //Envoyer un Messsage String simple
    private void broadcastStringMessage(String message, Session exclude, String currentChatRoom) {
        allSessions.forEach((username, session) -> {
            try {
                if (!(exclude != null && session.getId().equals(exclude.getId()))) {
                    if (allChatRooms.get(session.getId()).equals(currentChatRoom)) {
                        session.getBasicRemote().sendObject(message);
                    }
                }
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    }
    
    
    //Afficher l'historique des messages
    private void broadcastHistory(List<ChatMessage> messages, Session session) {
        messages.forEach(message -> {
            try {
                session.getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    }
    
   
    //Envoyer des notifications
    private void broadcastNotification(ChatMessage notificationMessage, Session senderSession, String chatroom) {
        allSessions.forEach((username, session) -> {
            if (userRoles.get(session.getId()).equals("admin") && !session.equals(senderSession)) {
                try {
                    session.getBasicRemote().sendObject(notificationMessage);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
        ChatDAO.getDemandeurparChatroom().forEach((room, demandeur) -> {
            if (room.equals(chatroom)) {
                Session demandeurSession = allSessions.get(demandeur);
                if (demandeurSession != null && !demandeurSession.equals(senderSession)) {
                    try {
                        demandeurSession.getBasicRemote().sendObject(notificationMessage);
                    } catch (IOException | EncodeException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    
        
    /*Verifier si l'userId fait parti des userId deja existant
	Renvoie Null si n'existe pas sinon renvoie le Chatutilisateur correspondant*/    
    private ChatUtilisateur getUtilisateurParUserId(String userId) {

    	
        return ChatDAO.getExistingUsers().stream()
                .filter(utilisateur -> utilisateur.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    
}

