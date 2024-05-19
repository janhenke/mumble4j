package de.taujhe.mumble4j.impl;

import java.io.Closeable;
import java.io.IOException;

import de.taujhe.mumble4j.packet.MumbleControlPacket;
import de.taujhe.mumble4j.packet.VersionPacket;
import de.taujhe.mumble4j.server.MumbleServer;

import org.jetbrains.annotations.NotNull;

import tlschannel.ServerTlsChannel;

/**
 * Holding the state of an active client connection.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see MumbleServer
 */
public class ClientContext implements Closeable
{
	private final MumbleConnection mumbleConnection;

	public ClientContext(final @NotNull ServerTlsChannel tlsChannel)
	{
		mumbleConnection = new MumbleConnection(tlsChannel, this::handleMumblePacket, this::handleConnectionException);
	}

	@Override
	public void close() throws IOException
	{
		mumbleConnection.close();
	}

	private void handleMumblePacket(final @NotNull MumbleControlPacket packet)
	{
		switch (packet)
		{
			case VersionPacket versionPacket -> handleVersionPacket(versionPacket);
		}
	}

	private void handleVersionPacket(final @NotNull VersionPacket versionPacket)
	{
		System.out.println(versionPacket);
	}

	private void handleConnectionException(final @NotNull IOException e)
	{

	}
}
