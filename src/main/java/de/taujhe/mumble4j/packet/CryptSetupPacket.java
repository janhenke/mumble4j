package de.taujhe.mumble4j.packet;

import com.google.protobuf.MessageLite;

import org.jspecify.annotations.NullMarked;

import MumbleProto.Mumble;
import java.nio.ByteBuffer;

/**
 * Mumble Crypt Setup packet.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see PacketType#CRYPT_SETUP
 */
@NullMarked
public final class CryptSetupPacket extends MumbleControlPacket
{
	private final Mumble.CryptSetup cryptSetup;

	public CryptSetupPacket(final Mumble.CryptSetup cryptSetup)
	{
		this.cryptSetup = cryptSetup;
	}

	public ByteBuffer getKey()
	{
		return cryptSetup.getKey().asReadOnlyByteBuffer();
	}

	public ByteBuffer getClientNonce()
	{
		return cryptSetup.getClientNonce().asReadOnlyByteBuffer();
	}

	public ByteBuffer getServerNonce()
	{
		return cryptSetup.getServerNonce().asReadOnlyByteBuffer();
	}

	@Override
	protected PacketType getPacketType()
	{
		return PacketType.CRYPT_SETUP;
	}

	@Override
	protected MessageLite getMessage()
	{
		return cryptSetup;
	}

	@Override
	public String toString()
	{
		return cryptSetup.toString();
	}
}
