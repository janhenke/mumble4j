package de.taujhe.mumble4j.packet;

import com.google.protobuf.MessageLite;

import org.jetbrains.annotations.NotNull;

import MumbleProto.Mumble;

/**
 * Mumble {@code Server Sync} packet.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see PacketType#SERVER_SYNC
 */
public final class ServerSync extends MumbleControlPacket
{
	private final Mumble.ServerSync serverSync;

	public ServerSync(final Mumble.ServerSync serverSync)
	{
		this.serverSync = serverSync;
	}

	public int getSession()
	{
		return serverSync.getSession();
	}

	public int getMaxBandwidth()
	{
		return serverSync.getMaxBandwidth();
	}

	public @NotNull String getWelcomeText()
	{
		return serverSync.getWelcomeText();
	}

	public int getPermissions()
	{
		// according to the comment in the protocol definition,
		// this is supposed to be an int and must not exceed the size of an integer.
		return (int) serverSync.getPermissions();
	}

	@Override
	protected @NotNull PacketType getPacketType()
	{
		return PacketType.SERVER_SYNC;
	}

	@Override
	protected @NotNull MessageLite getMessage()
	{
		return serverSync;
	}

	@Override
	public String toString()
	{
		return serverSync.toString();
	}
}
