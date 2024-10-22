package de.taujhe.mumble4j.server.persistence;

import org.jetbrains.annotations.NotNull;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Implementation of RegisteredUserRepository with a database backend.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
@ApplicationScoped
public class RegisteredUserRepository implements PanacheRepositoryBase<RegisteredUserEntity, Integer>
{
	public boolean isUsernameRegistered(final @NotNull String username)
	{
		return count("username", username) > 0;
	}
}
