package fr.mickaelbaron.chatjsonwebsocket;

import java.util.HashMap;
import java.util.Map;

import org.glassfish.tyrus.server.Server;

/**
 * Launcher du serveur AppliBE-SAVE
 * @author Mickael BARON (baron.mickael@gmail.com)
 * @see BE-SAVE
 */
public class ChatWebSocketLauncher {

	public static void main(String[] args) {
		final Map<String, Object> serverProperties = new HashMap<>();
		serverProperties.put(Server.STATIC_CONTENT_ROOT, "./static");
		Server server = new Server("0.0.0.0", 8024, "/chatjsonwebsocket", serverProperties,
				ChatJSONEndpointV2.class, ChatJSONDemandeurEndpoint.class, ChatJSONAdminEndpoint.class,
				ChatJSONFirstEndpoint.class, ChatJSONRequeteEndpoint.class, ChatJSONSuperAdminEndpoint.class,
				ChatJSONSuperAdminEndpoint.class, ChatJSONSuperAdminVueEndpoint.class);

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
