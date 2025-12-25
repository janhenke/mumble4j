package de.taujhe.mumble4j.impl;

import de.taujhe.mumble4j.packet.MumbleControlPacket;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import tlschannel.TlsChannel;

/**
 * An active Mumble network connection. This handles the details of the network protocol.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public abstract class MumbleConnection implements Closeable
{
	/// Default port number for a mumble server.
	public static final int DEFAULT_PORT = 64738;

	private final Executor executor;
	private final TlsChannel tlsChannel;

	protected MumbleConnection(final Executor executor, final TlsChannel tlsChannel)
	{
		this.executor = executor;
		this.tlsChannel = tlsChannel;

		executor.execute(this::readPacket);
	}

	protected abstract void acceptPacket(final @NotNull MumbleControlPacket packet);

	protected abstract void handleException(final @NotNull IOException e);

	private void readPacket()
	{
		final ByteBuffer buffer = ByteBuffer.allocate(MumbleControlPacket.MAX_PACKET_LENGTH);
		try
		{
			// blocks until ready
			tlsChannel.read(buffer);
			executor.execute(this::readPacket);
			acceptPacket(MumbleControlPacket.parseNetworkBuffer(buffer));
		}
		catch (final IOException e)
		{
			handleException(e);
		}
	}

	private void writePacket(final @NotNull MumbleControlPacket packet)
	{
		final ByteBuffer buffer = ByteBuffer.allocate(MumbleControlPacket.MAX_PACKET_LENGTH);
		try
		{
			packet.serialize(buffer);
			buffer.rewind();
			// blocks until ready
			tlsChannel.write(buffer);
		}
		catch (IOException e)
		{
			handleException(e);
		}
	}

	public void sendPacket(final @NotNull MumbleControlPacket packet)
	{
		executor.execute(() -> writePacket(packet));
	}

	@Override
	public void close() throws IOException
	{
		tlsChannel.close();
	}
}
