package de.taujhe.mumble4j.packet;

import java.nio.ByteBuffer;
import java.util.List;

import com.google.protobuf.MessageLite;

import org.jetbrains.annotations.NotNull;

import MumbleProto.Mumble;

/**
 * Mumble {@code Channel State} packet.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see PacketType#CHANNEL_STATE
 */
public final class ChannelState extends MumbleControlPacket
{
	private final Mumble.ChannelState channelState;

	public ChannelState(final Mumble.ChannelState channelState)
	{
		this.channelState = channelState;
	}

	public int getChannelId()
	{
		return channelState.getChannelId();
	}

	public int getParent()
	{
		return channelState.getParent();
	}

	public @NotNull String getName()
	{
		return channelState.getName();
	}

	public @NotNull List<Integer> getLinks()
	{
		return channelState.getLinksList();
	}

	public boolean isTemporary()
	{
		return channelState.getTemporary();
	}

	public @NotNull String getDescription()
	{
		return channelState.getDescription();
	}

	public @NotNull List<Integer> getLinksAdd()
	{
		return channelState.getLinksAddList();
	}

	public @NotNull List<Integer> getLinksRemove()
	{
		return channelState.getLinksRemoveList();
	}

	public int getMaxUsers()
	{
		return channelState.getMaxUsers();
	}

	public boolean isEnterRestricted()
	{
		return channelState.getIsEnterRestricted();
	}

	public boolean isCanEnter()
	{
		return channelState.getCanEnter();
	}

	public int getPosition()
	{
		return channelState.getPosition();
	}

	public @NotNull ByteBuffer getDescriptionHash()
	{
		return channelState.getDescriptionHash().asReadOnlyByteBuffer();
	}

	@Override
	protected @NotNull PacketType getPacketType()
	{
		return PacketType.CHANNEL_STATE;
	}

	@Override
	protected @NotNull MessageLite getMessage()
	{
		return channelState;
	}

	@Override
	public String toString()
	{
		return channelState.toString();
	}
}
