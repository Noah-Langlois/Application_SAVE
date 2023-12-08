package fr.mickaelbaron.chatjsonwebsocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
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
@ServerEndpoint(value = "/chat/{role}/{chatroom}/{username}",
				decoders = ChatMessageDecoder.class,
				encoders = ChatMessageEncoder.class)
public class ChatJSONEndpoint {

	// Liste des sessions connectées
	private static Map<String, Session> allSessions = new HashMap<>();

	// Liste des noms utilisateurs connectés
	private static Map<String, String> allUsers = new HashMap<>();

	// Liste des chatroom en cours
	private static Map<String, String> allChatRooms = new HashMap<>();
	
	//Liste des demandeurs + IdChatroom, pour unique demandeur par chatroom
	private static Map<String, String> demandeursParChatRoom = new HashMap<>();
	
	
	//Stocker les infos des utilisateurs pour constrcution du type Chatmessage
	private static Map<String, String> userRoles = new ConcurrentHashMap<>();
	private static Map<String, String> userNames = new ConcurrentHashMap<>();
	private static Map<String, String> chatIds = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("chatroom") String chatRoom, @PathParam("username") String userName, @PathParam("role") String role)
            throws IOException {
        System.out.println("ChatEndpoint.onOpen()");
        
//        //Affichage des paramètres
//        System.out.println("PathParameters:");
//        session.getPathParameters().forEach((c, v) -> {
//            System.out.println(c + " " + v);
//        });
//
//        System.out.println("QueryParameters:");
//        System.out.println(session.getQueryString());
//
//        System.out.println("Path and Query Parameters:");
//        session.getRequestParameterMap().forEach((k, v) -> {
//            System.out.println(k + " " + v);
//        });
        
        
        //Numéro de la connection
        System.out.println("Connection number:" + session.getOpenSessions().size());
        
        //Cas où le nom utilisateur est deja utilisé
        if (allSessions.containsKey(userName)) {
            session.close();
        } else {
        	//L'utilisateur peut se connecter
            allChatRooms.put(session.getId(), chatRoom);
            if ("admin".equals(role)) {
            	//Cas ou l'utilisateur est un admin
                allUsers.put(session.getId(), userName);
                allSessions.put(userName, session);
                this.broadcastStringMessage(userName + " connected!", session, chatRoom);
            } else {
            	//La session est deja attribuée a un utilisateur
                if (demandeursParChatRoom.containsKey(chatRoom)) {
                    session.close();
                } else {
                	//Le demandeur peut se connecter
                    demandeursParChatRoom.put(chatRoom, userName);
                    allUsers.put(session.getId(), userName);
                    allSessions.put(userName, session);
                    this.broadcastStringMessage(userName + " connected!", session, chatRoom);
                }
            }
        }

        // Créer un nouvel objet ChatMessage et définir les propriétés
        ChatMessage newChatMessage = new ChatMessage();
        newChatMessage.setRole(role);
        newChatMessage.setUserId(userName);
        newChatMessage.setSessionId(session.getId());
        newChatMessage.setChatroomId(chatRoom);
        
        //Remplissage des listes info utilisateur pour l'afficher lors de l'envoi du Chatmessage
        userRoles.put(session.getId(), role);
        userNames.put(session.getId(), userName);
        chatIds.put(session.getId(), chatRoom);
        
        // Utiliser cet objet pour diffuser un message
        this.broadcastObjectMessage(newChatMessage, userName, null, allChatRooms.get(session.getId()));
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
        
        this.broadcastObjectMessage(message, allUsers.get(session.getId()), null,
                allChatRooms.get(session.getId()));
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
    
    private void broadcastObjectMessage(ChatMessage message, String user, Session exclude, String currentChatRoom) {
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
}