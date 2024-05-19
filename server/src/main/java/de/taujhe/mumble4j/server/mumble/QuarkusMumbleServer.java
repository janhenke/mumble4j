package de.taujhe.mumble4j.server.mumble;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import javax.net.ssl.SSLContext;

import de.taujhe.mumble4j.server.MumbleServer;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.Shutdown;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * A bean to wrap a {@link MumbleServer} instance into the Quarkus app lifecycle.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
@Startup
@ApplicationScoped
public class QuarkusMumbleServer implements Closeable
{
	private static final Logger LOGGER = LoggerFactory.getLogger(QuarkusMumbleServer.class);

	private final MumbleServer mumbleServer;

	public QuarkusMumbleServer(final @NotNull SSLContext sslContext)
	{
		try
		{
			this.mumbleServer = MumbleServer.open(sslContext);
		}
		catch (final IOException e)
		{
			LOGGER.error("Failed to open MumbleServer", e);
			throw new UncheckedIOException(e);
		}
	}

	@Shutdown
	@Override
	public void close()
	{
		mumbleServer.close();
	}
}
