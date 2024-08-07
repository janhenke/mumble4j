package de.taujhe.mumble4j.packet;

import com.google.protobuf.MessageLite;

import org.jetbrains.annotations.NotNull;

import MumbleProto.Mumble;

/**
 * Mumble {@code Ping} packet.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see PacketType#PING
 */
public final class PingPacket extends MumbleControlPacket
{
	private final Mumble.Ping ping;

	public PingPacket(final @NotNull Mumble.Ping ping)
	{
		this.ping = ping;
	}

	public long getTimestamp()
	{
		return ping.getTimestamp();
	}

	@Override
	protected @NotNull PacketType getPacketType()
	{
		return PacketType.PING;
	}

	@Override
	protected @NotNull MessageLite getMessage()
	{
		return ping;
	}

	@Override
	public String toString()
	{
		return ping.toString();
	}
}
