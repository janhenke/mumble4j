package de.taujhe.mumble4j.packet;

import java.nio.ByteBuffer;

import com.google.protobuf.MessageLite;

import org.jetbrains.annotations.NotNull;

import MumbleProto.Mumble;

/**
 * Mumble Crypt Setup packet.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see PacketType#CRYPT_SETUP
 */
public final class CryptSetupPacket extends MumbleControlPacket
{
	private final Mumble.CryptSetup cryptSetup;

	public CryptSetupPacket(final @NotNull Mumble.CryptSetup cryptSetup)
	{
		this.cryptSetup = cryptSetup;
	}

	public @NotNull ByteBuffer getKey()
	{
		return cryptSetup.getKey().asReadOnlyByteBuffer();
	}

	public @NotNull ByteBuffer getClientNonce()
	{
		return cryptSetup.getClientNonce().asReadOnlyByteBuffer();
	}

	public @NotNull ByteBuffer getServerNonce()
	{
		return cryptSetup.getServerNonce().asReadOnlyByteBuffer();
	}

	@Override
	protected @NotNull PacketType getPacketType()
	{
		return PacketType.CRYPT_SETUP;
	}

	@Override
	protected @NotNull MessageLite getMessage()
	{
		return cryptSetup;
	}

	@Override
	public String toString()
	{
		return cryptSetup.toString();
	}
}
