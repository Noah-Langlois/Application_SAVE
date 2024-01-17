package fr.mickaelbaron.chatjsonwebsocket;

import java.io.IOException;
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

@ServerEndpoint(value = "/chat/SuperAdmin/{token}/{AjoutouSuppr}/{username}",
				decoders = ChatMessageDecoder.class,
				encoders = ChatMessageEncoder.class)

public class ChatJSONSuperAdminEndpoint {
			
	@OnOpen
    public void onOpen(Session session, @PathParam("token") String token, @PathParam("AjoutouSuppr") String Requete, @PathParam("username") String nameAdmin) throws IOException {
        System.out.println("ChatSuperAdminEndpoint.onOpen()");
        ChatUtilisateur SuperAdmin = getAdminParUserId("SuperAdmin");
        System.out.println("ajout: " + Requete);
        
        if (!JwtUtil.validateToken(token)) {
            System.out.println("Token invalide");
            session.close();
            return;
        }
        if ("Ajout".equals(Requete)) {
	        
	        //Création du Chatutilisateur de type admin
	        ChatUtilisateur newAdmin = new ChatUtilisateur();
	        newAdmin.setUserId(nameAdmin);
	        newAdmin.setRole("admin");
	        ChatDAO.getExistingUsers().add(newAdmin);
	        
	        //Ajout à la base
	        ChatDAO.addAdminWithPassword(SuperAdmin,newAdmin);
	        System.out.println("Nouvel Admin Ajouté");
	        
        } else if ("Suppr".equals(Requete)) {
        	
        	ChatUtilisateur adminSuppr = getAdminParUserId(nameAdmin);
        	ChatDAO.removeAdminWithPassword(SuperAdmin, adminSuppr);
        	System.out.println("Admin Supprimé");
        }
        else {
        	throw new IllegalStateException("Requête inconnue!");
        }
	}
	
	//Récuperation du ChatUtilisateur correspondant au userId
    private ChatUtilisateur getAdminParUserId(String userId) {

    	
        return ChatDAO.getAdmin().stream()
                .filter(utilisateur -> utilisateur.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
    
}