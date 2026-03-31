package de.taujhe.mumble4j.packet;

import com.google.protobuf.MessageLite;

import org.jspecify.annotations.NullMarked;

import MumbleProto.Mumble;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Mumble {@code Channel State} packet.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see PacketType#CHANNEL_STATE
 */
@NullMarked
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

	public String getName()
	{
		return channelState.getName();
	}

	public List<Integer> getLinks()
	{
		return channelState.getLinksList();
	}

	public boolean isTemporary()
	{
		return channelState.getTemporary();
	}

	public String getDescription()
	{
		return channelState.getDescription();
	}

	public List<Integer> getLinksAdd()
	{
		return channelState.getLinksAddList();
	}

	public List<Integer> getLinksRemove()
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

	public ByteBuffer getDescriptionHash()
	{
		return channelState.getDescriptionHash().asReadOnlyByteBuffer();
	}

	@Override
	protected PacketType getPacketType()
	{
		return PacketType.CHANNEL_STATE;
	}

	@Override
	protected MessageLite getMessage()
	{
		return channelState;
	}

	@Override
	public String toString()
	{
		return channelState.toString();
	}
}
