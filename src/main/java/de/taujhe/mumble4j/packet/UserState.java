package de.taujhe.mumble4j.packet;

import com.google.protobuf.MessageLite;

import org.jspecify.annotations.NullMarked;

import MumbleProto.Mumble;

/**
 * Mumble {@code User State} packet.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see PacketType#USER_STATE
 */
@NullMarked
public final class UserState extends MumbleControlPacket
{
	private final Mumble.UserState userState;

	public UserState(final Mumble.UserState userState)
	{
		this.userState = userState;
	}

	@Override
	protected PacketType getPacketType()
	{
		return PacketType.USER_STATE;
	}

	@Override
	protected MessageLite getMessage()
	{
		return userState;
	}

	@Override
	public String toString()
	{
		return userState.toString();
	}
}
