package fr.mickaelbaron.chatjsonwebsocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class ChatMessageEncoder implements Encoder.Text<ChatMessage> {

	@Override
	public void init(EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public String encode(ChatMessage object) throws EncodeException {
		System.out.println("ChatMessageEncoder.encode()");
		
		ObjectMapper mapper = new ObjectMapper(); 
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			
			return "ERROR";
		}
	}

}
