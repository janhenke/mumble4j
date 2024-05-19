package de.taujhe.mumble4j.server;

/**
 * Listener for server events.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see MumbleServer
 */
public interface MumbleServerEventListener
{
	void clientConnected(String client);

	void clientDisconnected(String client);
}
