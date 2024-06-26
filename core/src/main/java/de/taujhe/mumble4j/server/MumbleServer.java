package de.taujhe.mumble4j.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLContext;

import de.taujhe.mumble4j.impl.ClientContext;

import org.jetbrains.annotations.NotNull;

import tlschannel.ServerTlsChannel;

/**
 * Mumble server instance.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public final class MumbleServer implements Closeable
{
	/**
	 * Default port number for a mumble server.
	 */
	public static final int DEFAULT_PORT = 64738;

	private final ServerSocketChannel serverSocketChannel;

	private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

	private final Set<ClientContext> clientContexts = ConcurrentHashMap.newKeySet();

	@NotNull
	public static MumbleServer open(final @NotNull SSLContext sslContext,
	                                final @NotNull InetSocketAddress socketAddress) throws IOException
	{
		return new MumbleServer(sslContext, socketAddress);
	}

	private MumbleServer(final @NotNull SSLContext sslContext, final @NotNull InetSocketAddress socketAddress)
			throws IOException
	{
		this.serverSocketChannel = ServerSocketChannel.open().bind(socketAddress);

		Thread.ofVirtual().start(() -> {
			while (true)
			{
				try
				{
					clientContexts.add(new ClientContext(ServerTlsChannel.newBuilder(serverSocketChannel.accept(),
					                                                                 sslContext).build()));
				}
				catch (IOException e)
				{
					// TODO: Handle this properly
					throw new RuntimeException(e);
				}
			}
		});
	}

	@Override
	public void close()
	{
		executorService.shutdown();
		clientContexts.forEach(clientContext -> {
			try
			{
				clientContext.close();
			}
			catch (IOException e)
			{
				// TODO: Handle this properly
				throw new RuntimeException(e);
			}
		});
	}
}
