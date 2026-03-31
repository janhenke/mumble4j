package de.taujhe.mumble4j.packet;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.protobuf.MessageLite;

import org.jspecify.annotations.NullMarked;

import MumbleProto.Mumble;

/**
 * Mumble {@code Authenticate} packet.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see PacketType#AUTHENTICATE
 */
@NullMarked
public final class AuthenticatePacket extends MumbleControlPacket
{
	/**
	 * Type of the mumble client.
	 *
	 * @author Jan Henke (Jan.Henke@taujhe.de)
	 */
	public enum ClientType
	{
		REGULAR(0), BOT(1);

		private final int protocolValue;

		ClientType(final int protocolValue)
		{
			this.protocolValue = protocolValue;
		}

		public int getProtocolValue()
		{
			return protocolValue;
		}

		public static Optional<ClientType> findByProtocolValue(final int protocolValue)
		{
			return Arrays.stream(values()).filter(clientType -> protocolValue == clientType.protocolValue).findAny();
		}
	}

	private final Mumble.Authenticate authenticate;

	public AuthenticatePacket(final Mumble.Authenticate authenticate)
	{
		this.authenticate = authenticate;
	}

	public String getUsername()
	{
		return authenticate.getUsername();
	}

	public String getPassword()
	{
		return authenticate.getPassword();
	}

	public List<String> getTokens()
	{
		return authenticate.getTokensList();
	}

	public List<Integer> getCeltVersions()
	{
		return authenticate.getCeltVersionsList();
	}

	public boolean isOpus()
	{
		return authenticate.getOpus();
	}

	public ClientType getClientType()
	{
		return ClientType.findByProtocolValue(authenticate.getClientType()).orElseThrow();
	}

	@Override
	protected PacketType getPacketType()
	{
		return PacketType.AUTHENTICATE;
	}

	@Override
	protected MessageLite getMessage()
	{
		return authenticate;
	}

	@Override
	public String toString()
	{
		return authenticate.toString();
	}
}
