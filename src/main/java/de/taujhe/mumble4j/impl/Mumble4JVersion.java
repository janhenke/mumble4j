package de.taujhe.mumble4j.impl;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Properties;

/**
 * Access to embedded release information.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
@NullMarked
public class Mumble4JVersion
{
	private final Properties properties = new Properties();

	public Mumble4JVersion() throws IOException
	{
		try (InputStream inputStream = this.getClass().getResourceAsStream("/git.properties"))
		{
			properties.load(inputStream);
		}
	}

	public OffsetDateTime getBuildTime()
	{
		return OffsetDateTime.parse(properties.getProperty("git.build.time"));
	}

	public String getBuildVersion()
	{
		return properties.getProperty("git.build.version");
	}

	public String getGitShortCommitId()
	{
		return properties.getProperty("git.commit.id.abbrev");
	}

	public String getGitCommitId()
	{
		return properties.getProperty("git.commit.id.full");
	}
}
