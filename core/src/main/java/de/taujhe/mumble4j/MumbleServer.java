package de.taujhe.mumble4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.SSLContext;

import de.taujhe.mumble4j.impl.ClientContext;

import org.jetbrains.annotations.NotNull;

import tlschannel.ServerTlsChannel;

/**
 * Base class of Mumble server instances.
 *
 * <p>This class already implements the network side of the Mumble protocol, but derived classes must implement the
 * abstract method related to state handling.</p>
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public abstract class MumbleServer implements Closeable
{
	/**
	 * Default port number for a mumble server.
	 */
	public static final int DEFAULT_PORT = 64738;

	private final AtomicInteger sessionIdGenerator = new AtomicInteger(1);

	private ServerSocketChannel serverSocketChannel;
	private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
	private final Set<ClientContext> clientContexts = ConcurrentHashMap.newKeySet();

	protected MumbleServer(final @NotNull InetSocketAddress socketAddress, final @NotNull SSLContext sslContext)
			throws IOException
	{
		// IOException here is fatal, if this fails, this object is useless. Therefore, let the caller handle it
		this.serverSocketChannel = ServerSocketChannel.open().bind(socketAddress);
		executorService.submit(() -> acceptConnection(serverSocketChannel, sslContext));
	}

	private void acceptConnection(final @NotNull ServerSocketChannel serverSocketChannel,
	                              final @NotNull SSLContext sslContext)
	{
		try
		{
			final SocketChannel socketChannel = serverSocketChannel.accept();
			final ServerTlsChannel tlsChannel = ServerTlsChannel.newBuilder(socketChannel, sslContext).build();
			final ClientContext clientContext = new ClientContext(sessionIdGenerator.incrementAndGet(), tlsChannel);
			clientContexts.add(clientContext);

			clientContext.sendServerHandshake();
		}
		catch (final IOException e)
		{
			handleException(e);
		}
		executorService.submit(() -> acceptConnection(serverSocketChannel, sslContext));
	}

	protected void setSSLContext(final @NotNull SSLContext sslContext) throws IOException
	{
		final SocketAddress socketAddress = serverSocketChannel.getLocalAddress();
		serverSocketChannel.close();

		this.serverSocketChannel = ServerSocketChannel.open().bind(socketAddress);
		executorService.submit(() -> acceptConnection(serverSocketChannel, sslContext));
	}

	protected abstract void handleException(final @NotNull IOException e);

	protected abstract boolean isUsernameRegistered(@NotNull String username);

	@Override
	public void close() throws IOException
	{
		serverSocketChannel.close();
		executorService.shutdown();
		clientContexts.forEach(clientContext -> {
			try
			{
				clientContext.close();
			}
			catch (IOException e)
			{
				handleException(e);
			}
		});
	}
}
