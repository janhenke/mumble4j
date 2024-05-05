package de.taujhe.mumble4j.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Properties;

import org.jetbrains.annotations.NotNull;

/**
 * Access to embedded release information.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
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

	@NotNull
	public OffsetDateTime getBuildTime()
	{
		return OffsetDateTime.parse(properties.getProperty("git.build.time"));
	}

	@NotNull
	public String getBuildVersion()
	{
		return properties.getProperty("git.build.version");
	}

	@NotNull
	public String getGitShortCommitId()
	{
		return properties.getProperty("git.commit.id.abbrev");
	}

	@NotNull
	public String getGitCommitId()
	{
		return properties.getProperty("git.commit.id.full");
	}
}
