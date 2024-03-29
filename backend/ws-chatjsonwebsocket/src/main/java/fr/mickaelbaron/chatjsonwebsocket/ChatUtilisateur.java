package fr.mickaelbaron.chatjsonwebsocket;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelise l'utilisateur de l'application
 * 
 * @author teulierf
 * @version 2.0.0
 * @see BE-SAVE
 */
public class ChatUtilisateur {
	
	//Attributs
	private String userId;
	private String name;
	private String role;
	private List<String> chatrooms;
	
	//Constructeur
	public ChatUtilisateur() {
		chatrooms = new ArrayList<>();
	}
	
	//Getters et Setters
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public List<String> getChatrooms() {
		return chatrooms;
	}
	
	public void ajouterChatroom(String chatroom) {
		chatrooms.add(chatroom);
	}
	
	public void supprimerChatroom(String chatroom) {
		chatrooms.remove(chatroom);
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public String getName() {
		return name;
	}
	

}
