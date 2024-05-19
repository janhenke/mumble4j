package de.taujhe.mumble4j.server.config;

import java.nio.file.Path;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509ExtendedKeyManager;

import org.eclipse.microprofile.config.Config;
import org.jetbrains.annotations.NotNull;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.pem.util.PemUtils;

/**
 * Producer class for CDI beans which need explicit configuration.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public class BeanProducer
{
	@Produces
	@Singleton
	public SSLContext sslContext(final @NotNull Config config)
	{
		final Path certificateChainPath = config.getValue("quarkus.http.ssl.certificate.files", Path.class);
		final Path privateKeyPath = config.getValue("quarkus.http.ssl.certificate.key-files", Path.class);
		final String keyPassword = config.getValue("quarkus.http.ssl.certificate.key-store-password", String.class);

		final List<String> protocols = config.getValues("quarkus.http.ssl.protocols", String.class);

		final X509ExtendedKeyManager extendedKeyManager = PemUtils.loadIdentityMaterial(certificateChainPath,
		                                                                                privateKeyPath,
		                                                                                keyPassword.toCharArray());
		return SSLFactory.builder()
		                 .withIdentityMaterial(extendedKeyManager)
		                 .withDefaultTrustMaterial()
		                 .withProtocols(protocols.toArray(String[]::new))
		                 .build()
		                 .getSslContext();
	}
}
