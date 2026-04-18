package de.taujhe.mumble4j.server.config;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import java.time.Clock;

/**
 * Producer class for CDI beans which need explicit configuration.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
@Singleton
public class BeanProducer
{
	@Produces
	Clock clock()
	{
		return Clock.systemUTC();
	}
}
