package de.taujhe.mumble4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLContext;

import de.taujhe.mumble4j.impl.ClientConnection;

import org.jetbrains.annotations.NotNull;

/**
 * Mumble server instance.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public final class MumbleServer implements Closeable
{
	public static final int DEFAULT_PORT = 64738;

	@NotNull
	private final SSLContext sslContext;
	@NotNull
	private final ServerState serverState;
	@NotNull
	private final InetSocketAddress socketAddress;

	private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

	private final Set<ClientConnection> clientConnections = ConcurrentHashMap.newKeySet();

	@NotNull
	public MumbleServer open(final @NotNull SSLContext sslContext, final @NotNull ServerState serverState)
	{
		return new MumbleServer(new InetSocketAddress(DEFAULT_PORT), sslContext, serverState);
	}

	@NotNull
	public MumbleServer open(final @NotNull InetSocketAddress socketAddress,
	                         final @NotNull SSLContext sslContext,
	                         final @NotNull ServerState serverState)
	{
		return new MumbleServer(socketAddress, sslContext, serverState);
	}

	private MumbleServer(final @NotNull InetSocketAddress socketAddress,
	                     final @NotNull SSLContext sslContext,
	                     final @NotNull ServerState serverState)
	{
		this.socketAddress = socketAddress;
		this.sslContext = sslContext;
		this.serverState = serverState;

		executorService.submit(() -> {
//			final SocketChannel socketChannel = ServerSocketChannel.open().bind(socketAddress).accept();
//			clientConnections.add(new ClientConnection(tlsChannel, serverState));
		});
	}

	@Override
	public void close()
	{
		executorService.shutdown();
		clientConnections.forEach(clientConnection -> {
			try
			{
				clientConnection.close();
			}
			catch (IOException e)
			{
				// TODO: Handle this properly
				throw new RuntimeException(e);
			}
		});
	}
}
