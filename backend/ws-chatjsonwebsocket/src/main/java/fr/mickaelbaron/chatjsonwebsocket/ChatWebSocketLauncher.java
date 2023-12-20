package fr.mickaelbaron.chatjsonwebsocket;

import java.util.HashMap;
import java.util.Map;

import org.glassfish.tyrus.server.Server;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 * Launcher du serveur AppliBE-SAVE
 */
public class ChatWebSocketLauncher {

	public static void main(String[] args) {
		final Map<String, Object> serverProperties = new HashMap<>();
		serverProperties.put(Server.STATIC_CONTENT_ROOT, "./static");
		Server server = new Server("0.0.0.0", 8024, "/chatjsonwebsocket", serverProperties, ChatJSONEndpointV2.class, ChatJSONRoomEndpoint.class, ChatJSONFirstEndpoint.class);

		try {
			server.start();

			System.out.println("Tyrus app started available at ws://localhost:8024/chatjsonwebsocket"
					+ "\nHit enter to stop it...");

			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.stop();
		}
	}
}
