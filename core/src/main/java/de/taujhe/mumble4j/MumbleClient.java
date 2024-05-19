package de.taujhe.mumble4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;

import de.taujhe.mumble4j.impl.Mumble4JVersion;
import de.taujhe.mumble4j.impl.MumbleConnection;
import de.taujhe.mumble4j.packet.MumbleControlPacket;
import de.taujhe.mumble4j.packet.VersionPacket;

import org.jetbrains.annotations.NotNull;

import tlschannel.ClientTlsChannel;

/**
 * Class representing a mumble network client.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public final class MumbleClient implements Closeable
{
	private final MumbleConnection mumbleConnection;

	@NotNull
	public static MumbleClient connect(final @NotNull InetSocketAddress address) throws IOException
	{
		try
		{
			return connect(address, SSLContext.getDefault());
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new IllegalStateException("Not suitable TLS implementation is available.", e);
		}
	}

	@NotNull
	public static MumbleClient connect(final @NotNull InetSocketAddress address, final @NotNull SSLContext sslContext)
			throws IOException
	{
		return new MumbleClient(address, sslContext);
	}

	private MumbleClient(final @NotNull InetSocketAddress socketAddress, final @NotNull SSLContext sslContext)
			throws IOException
	{
		final SocketChannel socketChannel = SocketChannel.open(socketAddress);
		mumbleConnection = new MumbleConnection(ClientTlsChannel.newBuilder(socketChannel, sslContext).build(),
		                                        this::handleMumblePacket,
		                                        this::handleConnectionException);

		// start protocol handshake
		final Mumble4JVersion mumble4JVersion = new Mumble4JVersion();
		// the numeric version is the Mumble version we are compatible with, not the mumble4j version
		mumbleConnection.sendPacket(new VersionPacket(1,
		                                              5,
		                                              629,
		                                              "mumble4j-"
				                                              + mumble4JVersion.getBuildVersion()
				                                              + "+"
				                                              + mumble4JVersion.getGitShortCommitId(),
		                                              System.getProperty("os.name"),
		                                              System.getProperty("os.version")));
	}

	private void handleMumblePacket(final @NotNull MumbleControlPacket packet)
	{
		switch (packet)
		{
			case VersionPacket versionPacket -> handleVersionPacket(versionPacket);
		}
	}

	private void handleVersionPacket(final @NotNull VersionPacket versionPacket)
	{

	}

	private void handleConnectionException(final @NotNull IOException e)
	{

	}

	@Override
	public void close() throws IOException
	{
		mumbleConnection.close();
	}
}
