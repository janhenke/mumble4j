package de.taujhe.mumble4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.SSLContext;

import de.taujhe.mumble4j.impl.ClientContext;
import de.taujhe.mumble4j.server.RegisteredUserRepository;

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

	private final AtomicInteger sessionIdGenerator = new AtomicInteger(1);

	private final ServerSocketChannel serverSocketChannel;
	private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
	private final Set<ClientContext> clientContexts = ConcurrentHashMap.newKeySet();

	private final RegisteredUserRepository registeredUserRepository;

	@NotNull
	public static MumbleServer open(final @NotNull InetSocketAddress socketAddress,
	                                final @NotNull SSLContext sslContext,
	                                final @NotNull RegisteredUserRepository registeredUserRepository) throws IOException
	{
		return new MumbleServer(socketAddress, sslContext, registeredUserRepository);
	}

	private MumbleServer(final @NotNull InetSocketAddress socketAddress,
	                     final @NotNull SSLContext sslContext,
	                     final @NotNull RegisteredUserRepository registeredUserRepository) throws IOException
	{
		this.serverSocketChannel = ServerSocketChannel.open().bind(socketAddress);
		this.registeredUserRepository = registeredUserRepository;

		Thread.ofVirtual().start(() -> {
			while (true)
			{
				try
				{
					final ClientContext clientContext = new ClientContext(
							sessionIdGenerator.incrementAndGet(),
					        ServerTlsChannel.newBuilder(serverSocketChannel.accept(), sslContext).build()
					);
					clientContexts.add(clientContext);
					clientContext.sendServerHandshake();
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
