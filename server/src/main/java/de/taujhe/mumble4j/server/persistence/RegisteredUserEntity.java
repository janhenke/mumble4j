package de.taujhe.mumble4j.server.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A user registered with a Mumble server (database entity).
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
@Entity
@Table(name = "registered_user")
public class RegisteredUserEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;

	@Column(name = "username", nullable = false, unique = true)
	private String username;
}
