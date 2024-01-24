package fr.mickaelbaron.chatjsonwebsocket;

import java.util.Date;
import java.util.List;

/**
 * Modelise les messages info/chat envoyé à l'interface
 * 
 * @author teulierf
 * @version 1.0.0
 * @see BE-SAVE
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
	
	private String type;
	
	private List<String> chatrooms;
	
	private List<String> adminsValid;
	
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
	
	public String getType() {
		return type;
	}
	
	public void setType (String type) {
		this.type=type;
	}
	
    public List<String> getChatrooms() {
        return chatrooms;
    }

    public void setChatrooms(List<String> chatrooms) {
        this.chatrooms = chatrooms;
    }
    
    public List<String> getAdm() {
        return adminsValid;
    }

    public void setAdm(List<String> adm) {
        this.adminsValid = adm;
    }
    

	//Redefinition de la methode toString
	@Override
	public String toString() {
		return "ChatMessage [content=" + content + ", created=" + created + ", browser=" + browser + "userId=" + userId + "role=" + role + "session=" + sessionId + "chatroomId=" + chatroomId +" ]";
	}
}
