package de.taujhe.mumble4j;

import javax.net.ssl.SSLContext;

import de.taujhe.mumble4j.impl.ClientSession;
import de.taujhe.mumble4j.impl.SessionId;

import org.jspecify.annotations.NullMarked;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import tlschannel.ServerTlsChannel;

/**
 * Base class of Mumble server instances.
 *
 * <p>This class already implements the network side of the Mumble protocol, but derived classes must implement the
 * abstract method related to state handling.</p>
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
@NullMarked
public abstract class MumbleServer implements Closeable
{
	private ServerSocketChannel serverSocketChannel;
	private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
	private final Map<SessionId, ClientSession> clientSessions = new ConcurrentHashMap<>();

	protected MumbleServer(final InetSocketAddress socketAddress, final SSLContext sslContext)
			throws IOException
	{
		// IOException here is fatal, if this fails, this object is useless. Therefore, let the caller handle it
		this.serverSocketChannel = ServerSocketChannel.open().bind(socketAddress);
		executorService.submit(() -> acceptConnection(serverSocketChannel, sslContext));
	}

	private void acceptConnection(final ServerSocketChannel serverSocketChannel,
	                              final SSLContext sslContext)
	{
		try
		{
			final SocketChannel socketChannel = serverSocketChannel.accept();
			final ServerTlsChannel tlsChannel = ServerTlsChannel.newBuilder(socketChannel, sslContext).build();
			final ClientSession clientSession = new ClientSession(executorService, tlsChannel);
			clientSessions.put(clientSession.getSessionId(), clientSession);

			clientSession.sendServerHandshake();
		}
		catch (final IOException e)
		{
			handleException(e);
		}
		executorService.submit(() -> acceptConnection(serverSocketChannel, sslContext));
	}

	protected void setSSLContext(final SSLContext sslContext) throws IOException
	{
		final SocketAddress socketAddress = serverSocketChannel.getLocalAddress();
		serverSocketChannel.close();

		this.serverSocketChannel = ServerSocketChannel.open().bind(socketAddress);
		executorService.submit(() -> acceptConnection(serverSocketChannel, sslContext));
	}

	protected abstract void handleException(final IOException e);

	protected abstract boolean isUsernameRegistered(String username);

	@Override
	public void close() throws IOException
	{
		serverSocketChannel.close();
		executorService.shutdown();
		clientSessions.values().forEach(clientSession -> {
			try
			{
				clientSession.close();
			}
			catch (IOException e)
			{
				handleException(e);
			}
		});
	}
}
