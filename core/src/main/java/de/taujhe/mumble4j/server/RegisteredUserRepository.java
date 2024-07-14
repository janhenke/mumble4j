package de.taujhe.mumble4j.server;

import org.jetbrains.annotations.NotNull;

/**
 * Repository for registered users in a mumble server instance.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public interface RegisteredUserRepository
{
	boolean isUsernameRegistered(@NotNull String username);
}
