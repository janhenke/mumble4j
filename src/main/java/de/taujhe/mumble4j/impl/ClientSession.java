package de.taujhe.mumble4j.impl;

import com.google.protobuf.ByteString;

import de.taujhe.mumble4j.MumbleServer;
import de.taujhe.mumble4j.packet.AuthenticatePacket;
import de.taujhe.mumble4j.packet.ChannelState;
import de.taujhe.mumble4j.packet.CryptSetupPacket;
import de.taujhe.mumble4j.packet.MumbleControlPacket;
import de.taujhe.mumble4j.packet.PingPacket;
import de.taujhe.mumble4j.packet.ServerSync;
import de.taujhe.mumble4j.packet.UserState;
import de.taujhe.mumble4j.packet.VersionPacket;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MumbleProto.Mumble;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import tlschannel.ServerTlsChannel;

/**
 * Holding the state of an active client connection.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see MumbleServer
 */
@NullMarked
public class ClientSession extends MumbleConnection
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientSession.class);

	private final SessionId sessionId;

	public ClientSession(final ExecutorService executor, final ServerTlsChannel tlsChannel, final SessionId sessionId)
	{
		super(executor, tlsChannel);
		this.sessionId = sessionId;
	}

	public SessionId getSessionId()
	{
		return sessionId;
	}

	public void sendServerHandshake() throws IOException
	{
		// start protocol handshake
		final Mumble4JVersion mumble4JVersion = new Mumble4JVersion();
		// the numeric version is the Mumble version we are compatible with, not the mumble4j version
		sendPacket(new VersionPacket(Mumble.Version.newBuilder()
		                                           .setVersionV1(VersionPacket.packVersionV1(1, 5, 629))
		                                           .setVersionV2(VersionPacket.packVersionV2(1, 5, 629))
		                                           .setRelease("mumble4j-"
				                                                       + mumble4JVersion.getBuildVersion()
				                                                       + "+"
				                                                       + mumble4JVersion.getGitShortCommitId())
		                                           .setOs(System.getProperty("os.name"))
		                                           .setOsVersion(System.getProperty("os.version"))
		                                           .build()));
		sendPacket(new CryptSetupPacket(Mumble.CryptSetup.newBuilder()
		                                                 .setKey(ByteString.copyFrom("123456", StandardCharsets.UTF_8))
		                                                 .setServerNonce(ByteString.copyFrom("abcdef",
		                                                                                     StandardCharsets.UTF_8))
		                                                 .build()));
		sendPacket(new ChannelState(Mumble.ChannelState.newBuilder()
		                                               .setChannelId(0)
		                                               .setParent(0)
		                                               .setName("Root")
		                                               .setDescription("Root channel")
		                                               .build()));
		sendPacket(new UserState(Mumble.UserState.newBuilder()
		                                         .setSession(sessionId.value())
		                                         .setActor(0)
		                                         .setName("Foo")
		                                         .setChannelId(0)
		                                         .build()));
		sendPacket(new ServerSync(Mumble.ServerSync.newBuilder().setSession(sessionId.value()).setWelcomeText("Hi!").build()));
	}

	@Override
	protected void acceptPacket(final MumbleControlPacket packet)
	{
		switch (packet)
		{
			case VersionPacket versionPacket -> handleControlPacket(versionPacket);
			case AuthenticatePacket authenticatePacket -> handleControlPacket(authenticatePacket);
			case PingPacket pingPacket -> handleControlPacket(pingPacket);
			case ServerSync serverSync -> handleControlPacket(serverSync);
			case ChannelState channelState -> handleControlPacket(channelState);
			case UserState userState -> handleControlPacket(userState);
			case CryptSetupPacket cryptSetupPacket -> handleControlPacket(cryptSetupPacket);
		}
	}

	private void handleControlPacket(final VersionPacket versionPacket)
	{
		LOGGER.debug(versionPacket.toString());
	}

	private void handleControlPacket(final AuthenticatePacket authenticatePacket)
	{
		LOGGER.debug(authenticatePacket.toString());
	}

	private void handleControlPacket(final PingPacket pingPacket)
	{
		LOGGER.trace(pingPacket.toString());
		sendPacket(new PingPacket(Mumble.Ping.newBuilder().setTimestamp(pingPacket.getTimestamp()).build()));
	}

	private void handleControlPacket(final ServerSync serverSync)
	{
		LOGGER.debug(serverSync.toString());
	}

	private void handleControlPacket(final ChannelState channelState)
	{
		LOGGER.debug(channelState.toString());
	}

	private void handleControlPacket(final UserState userState)
	{
		LOGGER.debug(userState.toString());
	}

	private void handleControlPacket(final CryptSetupPacket cryptSetupPacket)
	{
		LOGGER.debug(cryptSetupPacket.toString());
	}

	@Override
	protected void handleException(final IOException e)
	{
		LOGGER.debug("Connection error", e);
	}
}
