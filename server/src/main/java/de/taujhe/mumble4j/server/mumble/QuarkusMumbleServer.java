package de.taujhe.mumble4j.server.mumble;

import java.io.IOException;
import java.net.InetSocketAddress;

import de.taujhe.mumble4j.MumbleServer;
import de.taujhe.mumble4j.server.persistence.RegisteredUserRepository;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.Startup;
import io.quarkus.tls.CertificateUpdatedEvent;
import io.quarkus.tls.TlsConfigurationRegistry;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Implementation of {@link MumbleServer}, using the Quarkus framework.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
@Startup
@Singleton
public class QuarkusMumbleServer extends MumbleServer
{
	private static final Logger LOGGER = LoggerFactory.getLogger(QuarkusMumbleServer.class);

	private final RegisteredUserRepository registeredUserRepository;

	@Inject
	public QuarkusMumbleServer(final @NotNull TlsConfigurationRegistry tlsConfigurationRegistry,
	                           @ConfigProperty(name = "mumble.server.port") final int serverPort,
	                           final RegisteredUserRepository registeredUserRepository) throws IOException
	{
		super(new InetSocketAddress(serverPort), tlsConfigurationRegistry.get("mumble").map(tlsConfiguration -> {
			try
			{
				return tlsConfiguration.createSSLContext();
			}
			catch (Exception e)
			{
				LOGGER.error("Failed to create SSLContext", e);
				throw new RuntimeException(e);
			}
		}).orElseThrow(() -> new RuntimeException("Named TLS configuration 'mumble' is required.")));
		this.registeredUserRepository = registeredUserRepository;
	}

	public void onReload(@Observes final @NotNull CertificateUpdatedEvent event)
	{
		if ("mumble".equals(event.name()))
		{
			try
			{
				setSSLContext(event.tlsConfiguration().createSSLContext());
			}
			catch (final Exception e)
			{
				LOGGER.error("IOException while setting SSLContext, shutting down.", e);
				Quarkus.asyncExit();
			}
		}
	}

	@Override
	protected void handleException(final @NotNull IOException e)
	{
		LOGGER.error("", e);
	}

	@Override
	protected boolean isUsernameRegistered(@NotNull final String username)
	{
		return registeredUserRepository.isUsernameRegistered(username);
	}
}
