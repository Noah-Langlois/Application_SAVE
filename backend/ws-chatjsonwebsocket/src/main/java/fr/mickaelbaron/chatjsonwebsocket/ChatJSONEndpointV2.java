package fr.mickaelbaron.chatjsonwebsocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

/**
 * @author Florine
 * Serveur pour l'application BE-SAVE
 */
@ServerEndpoint(value = "/chat/{role}/{username}/{chatroom}",
				decoders = ChatMessageDecoder.class,
				encoders = ChatMessageEncoder.class)

public class ChatJSONEndpointV2 {

	// Liste des sessions connectées
	private static Map<String, Session> allSessions = new HashMap<>();

	// Liste des noms utilisateurs connectés
	private static Map<String, String> allUsers = new HashMap<>();

	// Liste des chatrooms en cours
	private static Map<String, String> allChatRooms = new HashMap<>();
	
	//Liste des demandeurs + IdChatroom, pour unique demandeur par chatroom
	private static Map<String, String> demandeursParChatRoom = new HashMap<>();
	
	//Liste des utilisateur existant
	private static List<ChatUtilisateur> existingUsers = Collections.synchronizedList(new ArrayList<>());
	
	//Liste des chatroom existante
	private static List<String> existingChatroom = Collections.synchronizedList(new ArrayList<>());

	//Stocker les infos des utilisateurs pour constrcution du type Chatmessage
	private static Map<String, String> userRoles = new ConcurrentHashMap<>();
	private static Map<String, String> userNames = new ConcurrentHashMap<>();
	private static Map<String, String> chatIds = new ConcurrentHashMap<>();
	
	private static ChatDAO chatDAO = new ChatDAO();

    @OnOpen
    public void onOpen(Session session, @PathParam("chatroom") String chatRoom, @PathParam("username") String userName, @PathParam("role") String role)
            throws IOException {
        
    	System.out.println("ChatEndpoint.onOpen()");

        //Numéro de la connection
        System.out.println("Connection number:" + session.getOpenSessions().size());
        allChatRooms.put(session.getId(), chatRoom);
           
        //Renvoie l'utilisateur s'il existe ou NULL sinon, grace au username
        ChatUtilisateur utilisateurExistant = getUtilisateurParUserId(userName);
        
        //Les différents cas:
        
        if (utilisateurExistant != null) {
        // L'utilisateur existe déjà
        	
            if ("admin".equals(utilisateurExistant.getRole())) {
            // Si admin connexion possible a toutes les conversations
            	
                allUsers.put(session.getId(), userName);
                allSessions.put(userName, session);
                System.out.println("Utilisateur existant:" + utilisateurExistant.getRole() + ", " + utilisateurExistant.getUserId() + ", " + chatRoom);
                this.broadcastStringMessage(userName + " reconnected!", session, chatRoom);
                
                //Recupération des messsages:
                List<ChatMessage> chatMessages = chatDAO.getChatHistory(chatRoom);
                broadcastHistory(chatMessages, session);
                
                
                } else {
                //Demandeur, mais verification de la chatroom
            	
            	if (utilisateurExistant.getChatrooms().contains(chatRoom)) {
                // La chatroom est dans sa liste, connectez l'utilisateur à cette chatroom
            		
            		System.out.println("Chatroom dans la liste demandeur");
            		allUsers.put(session.getId(), userName);
                    allSessions.put(userName, session);
                    System.out.println("Utilisateur existant:" + utilisateurExistant.getRole() + ", " + utilisateurExistant.getUserId() + ", " + chatRoom);
                    this.broadcastStringMessage(userName + " reconnected!", session, chatRoom);
                    
                    //Recupération des messsages:
                    List<ChatMessage> chatMessages = chatDAO.getChatHistory(chatRoom);
                    broadcastHistory(chatMessages, session);
                    
                    
                    
                } else if (demandeursParChatRoom.containsKey(chatRoom)) {
                 // La chatroom est utilisée par un autre utilisateur
                	
                    String existingUser = demandeursParChatRoom.get(chatRoom);
                    
                    if ("demandeur".equals(getUtilisateurParUserId(existingUser).getRole())) {
                        // C'est un demandeur, refusez la connexion
                    	
                    	System.out.println("Chatroom deja utilisée par autre demandeur");
                        session.close();
                        } 
                    
                    else {
                    //C'est un admin alors connexion possible
                    	
                    	allUsers.put(session.getId(), userName);
                        allSessions.put(userName, session);
                        System.out.println("Utilisateur existant:" + utilisateurExistant.getRole() + ", " + utilisateurExistant.getUserId() + ", " + chatRoom);
                        this.broadcastStringMessage(userName + " connected!", session, chatRoom);
                        //Recupération des messsages:
                        List<ChatMessage> chatMessages = chatDAO.getChatHistory(chatRoom);
                        broadcastHistory(chatMessages, session);
                        
                    	}
                    
                    }else {
                    // La chatroom n'est pas dans sa liste, ajouter et connecter l'utilisateur
                    	
                    utilisateurExistant.ajouterChatroom(chatRoom);
                    demandeursParChatRoom.put(chatRoom, userName);
                    allUsers.put(session.getId(), userName);
                    allSessions.put(userName, session);
                    existingChatroom.add(chatRoom);
                    System.out.println("Utilisateur existant :" + utilisateurExistant.getRole() + ", " + utilisateurExistant.getUserId() + ", " + chatRoom);
                    this.broadcastStringMessage(userName + " reconnected!", session, chatRoom);
                    
                    
                    //Recupération des messsages:
                    List<ChatMessage> chatMessages = chatDAO.getChatHistory(chatRoom);
                    broadcastHistory(chatMessages, session);
                    
                }
            }
            
        } else {
          // L'utilisateur n'existe pas, créer un nouvel utilisateur
        	
            ChatUtilisateur nouvelUtilisateur = new ChatUtilisateur();
            nouvelUtilisateur.setUserId(userName);
            nouvelUtilisateur.setRole(role);

            if ("admin".equals(nouvelUtilisateur.getRole())) {
            // Si c'est un nouvel admin, alors toutes les chatroom autorisées
            	
            	allUsers.put(session.getId(), userName);
                allSessions.put(userName, session);
                System.out.println("Nouvel utilisateur :" + nouvelUtilisateur.getRole() + ", " + nouvelUtilisateur.getUserId() + ", " + chatRoom);
                this.broadcastStringMessage(userName + " connected!", session, chatRoom);
                existingUsers.add(nouvelUtilisateur);
                //Recupération des messsages:
                List<ChatMessage> chatMessages = chatDAO.getChatHistory(chatRoom);
                broadcastHistory(chatMessages, session);
                
                
                
            } else {
            //C'est un demandeur
            	
            	if (existingChatroom.contains(chatRoom)) {
                // La chatroom existe déjà, refusez la connexion
            		
            		System.out.println("Vous etes nouveau mais Chatroom deja prise");
                    session.close();
                    
                } else {
                // La chatroom n'existe pas, ajoutez-la et connectez le nouvel utilisateur
                	
                    nouvelUtilisateur.ajouterChatroom(chatRoom);
                    demandeursParChatRoom.put(chatRoom, userName);
                    allUsers.put(session.getId(), userName);
                    allSessions.put(userName, session);
                    existingChatroom.add(chatRoom);
                    System.out.println("Nouvel utilisateur :" + nouvelUtilisateur.getRole() + ", " + nouvelUtilisateur.getUserId() + ", " + chatRoom);
                    this.broadcastStringMessage(userName + " connected!", session, chatRoom);
                    existingUsers.add(nouvelUtilisateur);
                    List<ChatMessage> chatMessages = chatDAO.getChatHistory(chatRoom);
                    broadcastHistory(chatMessages, session);
                    
                }
            }
        }

        //Remplissage des listes info utilisateur pour l'afficher lors de l'envoie du Chatmessage
        userRoles.put(session.getId(), role);
        userNames.put(session.getId(), userName);
        chatIds.put(session.getId(), chatRoom);
        
    }
    
    @OnMessage
    public void onMessage(Session session, byte[] message) {
        System.out.println("ChatEndpoint.onMessageForByteArray()");
        this.broadcastBinaryMessage(ByteBuffer.wrap(message), null, allChatRooms.get(session.getId()));
    }
    
    @OnMessage
    public void onMessage(Session session, ChatMessage message) {
        System.out.println("ChatEndpoint.onMessage()");
        
        //Récupération des données utilisateur
        String role = userRoles.get(session.getId());
        String username = userNames.get(session.getId());
        String chat = chatIds.get(session.getId());

        //Création du Chatmessage avec toutes les infos
        message.setRole(role);
        message.setUserId(username);
        message.setChatroomId(chat);
        message.setSessionId(session.getId());
        message.setType("message chat");
        
        this.broadcastObjectMessage(message, allUsers.get(session.getId()), null,
                allChatRooms.get(session.getId()));
        
        chatDAO.saveChatMessage(message.getChatroomId(), message);

    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("ChatEndpoint.onClose()");
        
        //Retirer l'id de connction de la liste des session ouvertes et utilisateurs en cours
        String currentUsername = allUsers.get(session.getId());
        allSessions.remove(currentUsername);
        allUsers.remove(session.getId());
        
        //Retirer l'association chatroom/demandeur de la liste pour laisser la place
        demandeursParChatRoom.values().removeIf(username -> username.equals(currentUsername));
        ChatMessage newChatMessage = new ChatMessage();
        newChatMessage.setContent(currentUsername);
        this.broadcastStringMessage(currentUsername + " disconnected!", session, allChatRooms.get(session.getId()));
    }

    //Definition des Broadcast en fonction des types de messages à envoyer
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
    private void broadcastBinaryMessage(ByteBuffer message, Session exclude, String currentChatRoom) {
        allSessions.forEach((username, session) -> {
            try {
                if (!(exclude != null && session.getId().equals(exclude.getId()))) {
                    if (allChatRooms.get(session.getId()).equals(currentChatRoom)) {
                        Future<Void> sendBinary = session.getAsyncRemote().sendBinary(message);
                        sendBinary.get(1, TimeUnit.MILLISECONDS);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                System.out.println("Too long!");
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
    	
        return existingUsers.stream()
                .filter(utilisateur -> utilisateur.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    
    public static List<String> getExisting() {
    	return existingChatroom;
    }
    
    public static List<ChatUtilisateur> getExisting2() {
    	return existingUsers;
    }
}
