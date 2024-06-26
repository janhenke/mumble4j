package de.taujhe.mumble4j.server.mumble;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;

import de.taujhe.mumble4j.server.MumbleServer;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.Shutdown;
import io.quarkus.runtime.Startup;
import io.quarkus.tls.TlsConfiguration;
import io.quarkus.tls.TlsConfigurationRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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

	@Inject
	public QuarkusMumbleServer(final @NotNull TlsConfigurationRegistry tlsConfigurationRegistry,
	                           @ConfigProperty(name = "mumble.server.port") final int serverPort)
	{
		final TlsConfiguration tlsConfiguration = tlsConfigurationRegistry.get("mumble")
		                                                                  .orElseThrow(() -> new RuntimeException(
				                                                                  "Named TLS configuration 'mumble' is required."));

		try
		{
			this.mumbleServer = MumbleServer.open(tlsConfiguration.createSSLContext(),
			                                      new InetSocketAddress(serverPort));
		}
		catch (final IOException e)
		{
			LOGGER.error("Failed to open MumbleServer", e);
			throw new UncheckedIOException(e);
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to create SSLContext", e);
			throw new RuntimeException(e);
		}
	}

	@Shutdown
	@Override
	public void close()
	{
		mumbleServer.close();
	}
}
