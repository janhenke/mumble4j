package de.taujhe.mumble4j.packet;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.protobuf.MessageLite;

import org.jetbrains.annotations.NotNull;

import MumbleProto.Mumble;

/**
 * Mumble {@code Authenticate} packet.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see PacketType#AUTHENTICATE
 */
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

		public static @NotNull Optional<ClientType> findByProtocolValue(final int protocolValue)
		{
			return Arrays.stream(values()).filter(clientType -> protocolValue == clientType.protocolValue).findAny();
		}
	}

	private final Mumble.Authenticate authenticate;

	public AuthenticatePacket(final @NotNull Mumble.Authenticate authenticate)
	{
		this.authenticate = authenticate;
	}

	public @NotNull String getUsername()
	{
		return authenticate.getUsername();
	}

	public @NotNull String getPassword()
	{
		return authenticate.getPassword();
	}

	public @NotNull List<String> getTokens()
	{
		return authenticate.getTokensList();
	}

	public @NotNull List<Integer> getCeltVersions()
	{
		return authenticate.getCeltVersionsList();
	}

	public boolean isOpus()
	{
		return authenticate.getOpus();
	}

	public @NotNull ClientType getClientType()
	{
		return ClientType.findByProtocolValue(authenticate.getClientType()).orElseThrow();
	}

	@Override
	protected @NotNull PacketType getPacketType()
	{
		return PacketType.AUTHENTICATE;
	}

	@Override
	protected @NotNull MessageLite getMessage()
	{
		return authenticate;
	}

	@Override
	public String toString()
	{
		return authenticate.toString();
	}
}
