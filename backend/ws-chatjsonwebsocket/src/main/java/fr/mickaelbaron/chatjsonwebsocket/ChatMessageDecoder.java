package fr.mickaelbaron.chatjsonwebsocket;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;

/**
 * Decodage des message JSON
 * 
 * @author Mickael BARON (baron.mickael@gmail.com)
 * @see BE-SAVE
 */
public class ChatMessageDecoder implements Decoder.Text<ChatMessage> {

	@Override
	public void init(EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public ChatMessage decode(String s) throws DecodeException {
		System.out.println("ChatMessageDecoder.decode() - Received JSON : " + s);
		
		ObjectMapper mapper = new ObjectMapper(); 
		try {
			ChatMessage readValue = mapper.readValue(s, ChatMessage.class);
			if (readValue.getCreated() == null) {
				readValue.setCreated(new Date());
			}
			return readValue;
		} catch (IOException e) {
			e.printStackTrace();
			
			return null;
		}
	}

	@Override
	public boolean willDecode(String s) {
		return (s != null);
	}
}
