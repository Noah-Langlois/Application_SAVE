package fr.mickaelbaron.chatjsonwebsocket;

import java.util.Date;

/**
 * @author Florine
 * Definition de la classe ChatMesssage
 * rassemblant toutes les infos necessaire à l'affichage et au stockage
 * Appli BE-SAVE
 */
public class ChatMessage {
	
	//Attributs constituant l'ensemble du type ChatMessage
	private String content;
	
	private Date created;
	
	private String browser;
	
	private String role;
	
	private String userId;
	
	private String sessionId;
	
	private String chatroomId;
	
	//Constructeur
	public ChatMessage() {
	}
	
	//Ensemble des Getters et Setters pour initialiser les attributs
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getChatroomId() {
		return chatroomId;
	}

	public void setChatroomId(String chatroomId) {
		this.chatroomId = chatroomId;
	}

	//Redefinition de la methode toString
	@Override
	public String toString() {
		return "ChatMessage [content=" + content + ", created=" + created + ", browser=" + browser + "userId=" + userId + "role=" + role + "session=" + sessionId + "chatroomId=" + chatroomId +" ]";
	}
}
