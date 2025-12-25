package de.taujhe.mumble4j.packet;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;

import org.jetbrains.annotations.NotNull;

import MumbleProto.Mumble;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Base class for Mumble network packets.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public sealed abstract class MumbleControlPacket
		permits VersionPacket, AuthenticatePacket, PingPacket, ServerSync, ChannelState, UserState, CryptSetupPacket
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
		buffer.rewind();

		// TODO: Handle unknown types better
		final PacketType packetType = PacketType.findByNetworkValue(buffer.getShort()).orElseThrow();
		final int messageLength = buffer.getInt();
		buffer.limit(buffer.position() + messageLength);

		return switch (packetType)
		{
			// TODO: Handle remaining types
			case VERSION -> new VersionPacket(Mumble.Version.parseFrom(buffer));
			case AUTHENTICATE -> new AuthenticatePacket(Mumble.Authenticate.parseFrom(buffer));
			case PING -> new PingPacket(Mumble.Ping.parseFrom(buffer));
			case SERVER_SYNC -> new ServerSync(Mumble.ServerSync.parseFrom(buffer));
			case CHANNEL_STATE -> new ChannelState(Mumble.ChannelState.parseFrom(buffer));
			case USER_STATE -> new UserState(Mumble.UserState.parseFrom(buffer));
			case CRYPT_SETUP -> new CryptSetupPacket(Mumble.CryptSetup.parseFrom(buffer));
			case UDP_TUNNEL, REJECT, CHANNEL_REMOVE, USER_REMOVE, BAN_LIST, TEXT_MESSAGE, PERMISSION_DENIED, ACL,
			     QUERY_USERS, CONTEXT_ACTION_MODIFY, CONTEXT_ACTION, USER_LIST, VOICE_TARGET, PERMISSION_QUERY,
			     CODEC_VERSION, USER_STATS, REQUEST_BLOB, SERVER_CONFIG, SUGGEST_CONFIG ->
					throw new IllegalStateException("Unexpected value: " + packetType);
		};
	}

	public void serialize(@NotNull final ByteBuffer buffer)
	{
		final PacketType packetType = getPacketType();
		final MessageLite message = getMessage();
		final int serializedSize = message.getSerializedSize();

		// The protocol is defined in big endian byte order
		buffer.order(ByteOrder.BIG_ENDIAN);
		buffer.putShort(packetType.getNetworkValue());
		buffer.putInt(serializedSize);
		buffer.limit(buffer.position() + serializedSize);

		message.toByteString().copyTo(buffer);
	}
}
