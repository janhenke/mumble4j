package de.taujhe.mumble4j.impl;

import org.jspecify.annotations.NullMarked;

/**
 * Identifier of a Mumble session.
 *
 * @param value The numeric value of the session id.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
@NullMarked
public record SessionId(int value)
{}
