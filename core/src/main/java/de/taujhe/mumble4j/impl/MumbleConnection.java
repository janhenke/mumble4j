package de.taujhe.mumble4j.impl;

import de.taujhe.mumble4j.packet.MumbleControlPacket;

import org.jspecify.annotations.NullMarked;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import tlschannel.TlsChannel;

/**
 * An active Mumble network connection. This handles the details of the network protocol.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
@NullMarked
public abstract class MumbleConnection implements Closeable
{
	/// Default port number for a mumble server.
	public static final int DEFAULT_PORT = 64738;

	private final ExecutorService executor;
	private final TlsChannel tlsChannel;

	protected MumbleConnection(final ExecutorService executor, final TlsChannel tlsChannel)
	{
		this.executor = executor;
		this.tlsChannel = tlsChannel;

		executor.execute(this::readPacket);
	}

	protected abstract void acceptPacket(final MumbleControlPacket packet);

	protected abstract void handleException(final IOException e);

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

	private void writePacket(final MumbleControlPacket packet)
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

	public void sendPacket(final MumbleControlPacket packet)
	{
		executor.execute(() -> writePacket(packet));
	}

	@Override
	public void close() throws IOException
	{
		tlsChannel.close();
	}
}
