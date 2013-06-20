package com.mimolet.android.util;

import java.util.HashMap;

/**
 * A local storage of objects stored by id.
 */
public final class Registry {

	protected static HashMap<Object, Object> map = new HashMap<Object, Object>();

	/**
	 * Returns the object with the given id.
	 * 
	 * @param key
	 *            the identifier
	 * @return the object or <code>null</code> if no match
	 */
	@SuppressWarnings("unchecked")
	public static <X> X get(Object key) {
		return (X) map.get(key);
	}

	/**
	 * Returns a map of all registered objects.
	 * 
	 * @return the object map
	 */
	public static HashMap<Object, Object> getAll() {
		return map;
	}

	/**
	 * Registers an object.
	 * 
	 * @param id
	 *            the identifier
	 * @param obj
	 *            the object to be registered
	 */
	public static void register(Object key, Object value) {
		map.put(key, value);
	}

	/**
	 * Unregisters an object.
	 * 
	 * @param id
	 *            the identifier
	 */
	public static void unregister(Object key) {
		map.remove(key);
	}

	/**
	 * Unregisters all registered objects.
	 */
	public static void unregisterAll() {
		map.clear();
	}

	private Registry() {
	}

}