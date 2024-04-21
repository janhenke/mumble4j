package de.taujhe.mumble4j.core;

import com.google.protobuf.MessageLite;

import org.jetbrains.annotations.NotNull;

import MumbleProto.Mumble;

/**
 * Mumble version packet.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public class VersionPacket extends AbstractPacket
{
	private final Mumble.Version version;

	VersionPacket(final Mumble.Version version) {this.version = version;}

	@NotNull
	@Override
	protected PacketType getPacketType()
	{
		return PacketType.VERSION;
	}

	@Override
	protected @NotNull MessageLite getMessage()
	{
		return version;
	}
}
