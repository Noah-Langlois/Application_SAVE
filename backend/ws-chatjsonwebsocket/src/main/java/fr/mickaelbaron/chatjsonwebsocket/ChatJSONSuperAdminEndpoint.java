package fr.mickaelbaron.chatjsonwebsocket;

import java.io.IOException;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Ajout ou Suppression d'admin par le SuperAdmin
 * 
 * @author teulierf
 * @version 4.1.0
 * @see BE-SAVE
 */

@ServerEndpoint(value = "/chat/SuperAdmin/{token}/{AjoutouSuppr}/{username}",
				decoders = ChatMessageDecoder.class,
				encoders = ChatMessageEncoder.class)

public class ChatJSONSuperAdminEndpoint {
			
	@OnOpen
    public void onOpen(Session session, @PathParam("token") String token, @PathParam("AjoutouSuppr") String Requete, @PathParam("username") String nameAdmin) throws IOException {
        
		System.out.println("ChatSuperAdminEndpoint.onOpen()");
		
		// Verification des paramètres
	    if (nameAdmin == null || Requete == null) {
	        System.out.println("NameAdmin ou Requete ne peut pas être null");
	        session.close();
	        return;
	    }
        
        ChatUtilisateur SuperAdmin = getAdminParUserId("SuperAdmin");
        
        System.out.println("requete demandée : " + Requete);
        
        //Verification du Token du SuperAdmin
        if (!JwtUtil.validateToken(token)) {
            System.out.println("Token invalide");
            session.close();
            return;
        }
        
        //Requete type ajout d'admin
        if ("Ajout".equals(Requete)) {
	        
	        //Création du ChatUtilisateur de type admin
	        ChatUtilisateur newAdmin = new ChatUtilisateur();
	        newAdmin.setUserId(nameAdmin);
	        newAdmin.setRole("admin");
	        //ChatDAO.getExistingUsers().add(newAdmin);
	        
	        //Ajout à la base
	        ChatDAO.addValidAdmin(SuperAdmin, newAdmin);
	        System.out.println("Nouvel Admin Ajouté");
	    
	    //Requête type suppresion d'admin
        } else if ("Suppr".equals(Requete)) {
        	
        	ChatUtilisateur adminSuppr = getAdminParUserId(nameAdmin);
        	
        	if (adminSuppr != null) {
        	//Admin existant
        		
	        	ChatDAO.removeExistingAdmin(SuperAdmin, adminSuppr);
	        	System.out.println("Admin Supprimé");
	        	
        	} else if (ChatDAO.getValidAdmin().contains(nameAdmin)) {
        	//Admin	jamais connecté
        		
        		ChatDAO.removeValidAdmin(SuperAdmin, nameAdmin);
	        	System.out.println("Admin Supprimé");
	        	
        	} else {
        		throw new IllegalStateException("Admin inconnu!");
        	}
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