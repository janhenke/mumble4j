package de.taujhe.mumble4j.impl;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import de.taujhe.mumble4j.packet.MumbleControlPacket;

import org.jetbrains.annotations.NotNull;

import tlschannel.TlsChannel;

/**
 * An active Mumble network connection. This handles the details of the network protocol.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public class MumbleConnection implements Closeable
{
	private final TlsChannel tlsChannel;
	private final Consumer<MumbleControlPacket> packetConsumer;
	private final Consumer<IOException> exceptionConsumer;

	private final BlockingQueue<MumbleControlPacket> writeQueue = new LinkedBlockingQueue<>();

	private final AtomicBoolean running = new AtomicBoolean(true);
	private final Thread readThread;
	private final Thread writeThread;

	public MumbleConnection(@NotNull final TlsChannel tlsChannel,
	                        @NotNull final Consumer<MumbleControlPacket> packetConsumer,
	                        @NotNull final Consumer<IOException> exceptionConsumer)
	{
		this.tlsChannel = tlsChannel;
		this.packetConsumer = packetConsumer;
		this.exceptionConsumer = exceptionConsumer;

		readThread = Thread.ofVirtual().start(this::readPackets);
		writeThread = Thread.ofVirtual().start(this::processWriteQueue);
	}

	private void readPackets()
	{
		final ByteBuffer buffer = ByteBuffer.allocateDirect(MumbleControlPacket.MAX_PACKET_LENGTH);

		while (running.get())
		{
			try
			{
				// blocks until ready
				tlsChannel.read(buffer);
				buffer.rewind();
				packetConsumer.accept(MumbleControlPacket.parseNetworkBuffer(buffer));
				buffer.rewind();
				buffer.limit(MumbleControlPacket.MAX_PACKET_LENGTH);
			}
			catch (final IOException e)
			{
				exceptionConsumer.accept(e);
			}
		}
	}

	private void processWriteQueue()
	{
		final ByteBuffer buffer = ByteBuffer.allocateDirect(MumbleControlPacket.MAX_PACKET_LENGTH);

		while (running.get())
		{
			try
			{
				// blocks until ready
				final MumbleControlPacket packet = writeQueue.take();

				packet.serialize(buffer);
				buffer.rewind();
				tlsChannel.write(buffer);
				buffer.rewind();
			}
			catch (InterruptedException e)
			{
				// end the loop
				break;
			}
			catch (IOException e)
			{
				exceptionConsumer.accept(e);
			}
		}
	}

	public boolean sendPacket(final @NotNull MumbleControlPacket packet)
	{
		return writeQueue.offer(packet);
	}

	@Override
	public void close() throws IOException
	{
		tlsChannel.close();
		running.set(false);
		readThread.interrupt();
		writeThread.interrupt();
	}
}
