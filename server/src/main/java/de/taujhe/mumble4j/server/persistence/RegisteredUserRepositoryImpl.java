package de.taujhe.mumble4j.server.persistence;

import de.taujhe.mumble4j.server.RegisteredUserRepository;

import org.jetbrains.annotations.NotNull;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Implementation of {@link RegisteredUserRepository} with a database backend.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
@ApplicationScoped
public class RegisteredUserRepositoryImpl
		implements RegisteredUserRepository, PanacheRepositoryBase<RegisteredUserEntity, Integer>
{
	@Override
	public boolean isUsernameRegistered(final @NotNull String username)
	{
		return count("username", username) > 0;
	}
}
