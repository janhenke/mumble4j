package de.taujhe.mumble4j.packet;

import com.google.protobuf.MessageLite;

import org.jetbrains.annotations.NotNull;

import MumbleProto.Mumble;

/**
 * Mumble {@code Version} packet.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 * @see PacketType#VERSION
 */
public final class VersionPacket extends MumbleControlPacket
{
	private final Mumble.Version version;

	public VersionPacket(final Mumble.Version version)
	{
		this.version = version;
	}

	public static int packVersionV1(final int majorVersion, final int minorVersion, final int patchVersion)
	{
		// Legacy versions: These versions are kept around for backward compatibility, but
		// have since been replaced by other version formats.
		//
		// Mumble legacy version format (v1) is an uint32:
		// major   minor  patch
		// 0xFFFF  0xFF   0xFF
		// (big-endian)

		int result = 0;
		result |= (majorVersion & 0xFFFF) << 16;
		result |= (minorVersion & 0xFF) << 8;
		// 0xFF patch version is expected for any value greater
		if (patchVersion > 0xFF)
		{
			result |= 0xFF;
		}
		else
		{
			result |= patchVersion & 0xFF;
		}

		return result;
	}

	public static long packVersionV2(final int majorVersion, final int minorVersion, final int patchVersion)
	{
		// The mumble version format (v2) is an uint64:
		// major   minor   patch   reserved/unused
		// 0xFFFF  0xFFFF  0xFFFF  0xFFFF
		// (big-endian)

		long result = 0;
		result |= (long) (majorVersion & 0xFFFF) << 48;
		result |= (long) (minorVersion & 0xFFFF) << 32;
		result |= (long) (patchVersion & 0xFFFF) << 16;

		return result;
	}

	@NotNull
	@Override
	protected PacketType getPacketType()
	{
		return PacketType.VERSION;
	}

	@Override
	protected @NotNull MessageLite getMessage()
	{
		return version;
	}

	@Override
	public String toString()
	{
		return version.toString();
	}
}
