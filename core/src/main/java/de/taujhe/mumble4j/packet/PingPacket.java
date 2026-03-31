package de.taujhe.mumble4j.packet;

import com.google.protobuf.MessageLite;

import org.jspecify.annotations.NullMarked;

import MumbleProto.Mumble;

/**
 * Mumble {@code Ping} packet.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see PacketType#PING
 */
@NullMarked
public final class PingPacket extends MumbleControlPacket
{
	private final Mumble.Ping ping;

	public PingPacket(final Mumble.Ping ping)
	{
		this.ping = ping;
	}

	public long getTimestamp()
	{
		return ping.getTimestamp();
	}

	@Override
	protected PacketType getPacketType()
	{
		return PacketType.PING;
	}

	@Override
	protected MessageLite getMessage()
	{
		return ping;
	}

	@Override
	public String toString()
	{
		return ping.toString();
	}
}
