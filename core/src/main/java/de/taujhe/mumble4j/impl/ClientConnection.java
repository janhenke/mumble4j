package de.taujhe.mumble4j.impl;

import java.io.Closeable;
import java.io.IOException;

import de.taujhe.mumble4j.MumbleServer;
import de.taujhe.mumble4j.ServerState;

import org.jetbrains.annotations.NotNull;

import tlschannel.async.AsynchronousTlsChannel;

/**
 * Holding the state of an active client connection.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see MumbleServer
 */
public class ClientConnection implements Closeable
{
	@NotNull
	private final AsynchronousTlsChannel tlsChannel;
	@NotNull
	private final ServerState serverState;

	public ClientConnection(final @NotNull AsynchronousTlsChannel tlsChannel,
	                        final @NotNull ServerState serverState)
	{
		this.tlsChannel = tlsChannel;
		this.serverState = serverState;
	}

	@Override
	public void close() throws IOException
	{
		tlsChannel.close();
	}
}
