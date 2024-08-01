package com.osroyale.util.generic;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds generic attributes.
 * 
 * @author Michael | Chex
 */
public class GenericAttributes {

	/** The map of generic attributes for an entity. */
	private final Map<Object, Object> genericAttributes = new HashMap<>();

	public <K, V> void put(K key, V value) {
		genericAttributes.put(key, value);
	}

	/**
	 * Sets a generic attribute.
	 * 
	 * @param <K>
	 *            The key type.
	 * @param <E>
	 *            The return value class type.
	 * @param key
	 *            The key.
	 * @param attribute
	 *            The value associated with this key.
	 */
	public <K, E> void set(K key, E attribute) {
		genericAttributes.put(key, attribute);
	}

	/**
	 * Modifies a generic attribute.
	 * @param <K>	The key type.
	 * @param <E>	The return value class type.
	 * @param key	The key.
	 * @param value	The value to set.
	 */
	public <K, E> void modify(K key, E value) {
		genericAttributes.replace(key, value);
	}

	/**
	 * Removes a generic attribute.
	 * 
	 * @param <K>
	 *            The key type.
	 * @param key
	 *            The key.
	 */
	public <K> void remove(K key) {
		genericAttributes.remove(key);
	}

	/**
	 * Checks if a key is in the list of generic attribute.
	 * 
	 * @param <K>
	 *            The key type.
	 * @param key
	 *            The key.
	 * @return {@code True} if the generic attributes contains a value with this
	 *         key.
	 */
	public <K> boolean has(K key) {
		return genericAttributes.containsKey(key);
	}

	/**
	 * Gets a generic attribute.
	 * 
	 * @param <K>
	 *            The key type.
	 * @param <E>
	 *            The return value class type.
	 * @param key
	 *            The key.
	 * @return The value associated with the key, or null.
	 */
	@SuppressWarnings("unchecked")
	public <K, E> E get(K key) {
		try {
			return (E) genericAttributes.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets a generic attribute.
	 *
	 * @param <K>
	 *            The key type.
	 * @param key
	 *            The key.
	 * @return The value associated with the key, or null.
	 */
	@SuppressWarnings("unchecked")
	public <K> Object getObject(K key) {
		try {
			return genericAttributes.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets a generic attribute casted to a specific class type.
	 * 
	 * @param <K>
	 *            The key type.
	 * @param <E>
	 *            The return value class type.
	 * @param key
	 *            The key.
	 * @param type
	 *            The class for the return type.
	 * @return The value associated with the key, or null.
	 */
	public <K, E> E get(K key, Class<? extends E> type) {
		Object attribute = genericAttributes.get(key);
		try {
			return type.cast(attribute);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the state of a key.
	 * 
	 * @param <K>
	 *            The key type.
	 * @param key
	 *            The key to check.
	 * @return {@code True} if the value of the key is equal to
	 *         {@code Boolean.TRUE}.
	 */
	public <K> boolean is(K key) {
		return has(key) && Boolean.TRUE == get(key, Boolean.class);
	}
}