package de.taujhe.mumble4j.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;

import org.jetbrains.annotations.NotNull;

import MumbleProto.Mumble;

/**
 * Base class for Mumble network packets.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public sealed abstract class MumbleControlPacket permits PingPacket, VersionPacket
{
	/**
	 * Length of the packet header preceding every packet. It consists of
	 * <ul>
	 *     <li>2 byte packet type</li>
	 *     <li>4 byte payload length</li>
	 * </ul>
	 */
	public static final int PACKET_HEADER_LENGTH = 2 + 4;
	/**
	 * Maximum length of the payload according to the protocol specification: 8 MiB - 1
	 */
	public static final int MAX_PAYLOAD_LENGTH = 8 * 1024 * 1024 - 1;
	/**
	 * Maximum length of a packet.
	 */
	public static final int MAX_PACKET_LENGTH = PACKET_HEADER_LENGTH + MAX_PAYLOAD_LENGTH;

	@NotNull
	abstract protected PacketType getPacketType();

	@NotNull
	abstract protected MessageLite getMessage();

	@NotNull
	public static MumbleControlPacket parseNetworkBuffer(final @NotNull ByteBuffer buffer)
			throws InvalidProtocolBufferException
	{
		// The protocol is defined in big endian byte order
		buffer.order(ByteOrder.BIG_ENDIAN);

		// TODO: Handle unknown types better
		final PacketType packetType = PacketType.findByNetworkValue(buffer.getShort()).orElseThrow();
		final int messageLength = buffer.getInt();
		buffer.limit(buffer.position() + messageLength);

		return switch (packetType)
		{
			case VERSION -> new VersionPacket(Mumble.Version.parseFrom(buffer));
			case PING -> new PingPacket(Mumble.Ping.parseFrom(buffer));
			// TODO: Handle remaining types
			default -> throw new IllegalStateException("Unexpected value: " + packetType);
		};
	}

	public void serialize(@NotNull final ByteBuffer buffer)
	{
		final PacketType packetType = getPacketType();
		final MessageLite message = getMessage();

		// The protocol is defined in big endian byte order
		buffer.order(ByteOrder.BIG_ENDIAN);
		buffer.putShort(packetType.getNetworkValue());
		buffer.putInt(message.getSerializedSize());

		message.toByteString().copyTo(buffer);
	}
}
