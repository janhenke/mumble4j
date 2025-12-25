package de.taujhe.mumble4j.server.persistence;

import org.jetbrains.annotations.NotNull;

import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

/**
 * Implementation of RegisteredUserRepository with a database backend.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
@Repository
public interface RegisteredUserRepository extends BasicRepository<RegisteredUserEntity, Long>
{
	@Query("select count(r) from RegisteredUserEntity r where r.username = :username")
	long countByUsername(@NotNull String username);
}
