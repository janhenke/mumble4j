package de.taujhe.mumble4j.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.NotNull;

/**
 * Default implementation of {@link RegisteredUserRepository}.
 *
 * <p>This implementation does not persist any state, everything is kept in memory only.</p>
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public class InMemoryRegisteredUserRepository implements RegisteredUserRepository
{
	private final AtomicInteger userIdGenerator = new AtomicInteger(1);
	private final List<RegisteredUser> users = new ArrayList<>();

	@Override
	public boolean isUsernameRegistered(final @NotNull String username)
	{
		return users.stream().anyMatch(registeredUser -> username.equals(registeredUser.username()));
	}
}
