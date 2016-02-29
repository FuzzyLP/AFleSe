package storeHouse;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CacheStoreHouse {

	final static protected Log LOG = LogFactory.getLog(CacheStoreHouse.class);

	private static HashMap<String, HashMap<String, HashMap<String, HashMap<String, Object>>>> storeHouse = new HashMap<String, HashMap<String, HashMap<String, HashMap<String, Object>>>>();

	/**
	 * This method allows to store in the storeHouse an object.
	 * 
	 * @param keyLevel1
	 * @param keyLevel2
	 * @param keyLevel3
	 * @param keyLevel4
	 * @param object
	 * @throws CacheStoreHouseException
	 */
	public static void store(@SuppressWarnings("rawtypes") Class keyLevel1, String keyLevel2,
			String keyLevel3, String keyLevel4, Object object, boolean debug) throws CacheStoreHouseException {

		CacheStoreHouseKey key = new CacheStoreHouseKey(keyLevel1, keyLevel2, keyLevel3, keyLevel4);

		store(key, object, debug);
	}

	/**
	 * This method allow to retrieve from the storeHouse an object
	 * 
	 * @param keyLevel1
	 * @param keyLevel2
	 * @param keyLevel3
	 * @param keyLevel4
	 * @return returns the stored object.
	 * @throws CacheStoreHouseException
	 */
	public static Object retrieve(@SuppressWarnings("rawtypes") Class keyLevel1, String keyLevel2,
			String keyLevel3, String keyLevel4, boolean debug) throws CacheStoreHouseException {

		CacheStoreHouseKey key = new CacheStoreHouseKey(keyLevel1, keyLevel2, keyLevel3, keyLevel4);
		Object retrieved = retrieve(key, debug);

		return retrieved;
	}

	/**
	 * Removes the object stored in the storeHouse
	 * 
	 * @param keyLevel1
	 * @param keyLevel2
	 * @param keyLevel3
	 * @param keyLevel4
	 * @throws CacheStoreHouseException
	 */
	public static void remove(@SuppressWarnings("rawtypes") Class keyLevel1, String keyLevel2,
			String keyLevel3, String keyLevel4, boolean debug) throws CacheStoreHouseException {

		CacheStoreHouseKey key = new CacheStoreHouseKey(keyLevel1, keyLevel2, keyLevel3, keyLevel4);

		store(key, null, debug);
	}

	// --------------
	// --------------
	// --------------

	private synchronized static void store(CacheStoreHouseKey key, Object object, boolean debug)
			throws CacheStoreHouseException {

		HashMap<String, HashMap<String, HashMap<String, Object>>> storeHouseL2 = null;
		HashMap<String, HashMap<String, Object>> storeHouseL3 = null;
		HashMap<String, Object> storeHouseL4 = null;

		// Retrieve or create the level 2 storeHouse.
		storeHouseL2 = storeHouse.get(key.getKeyLevel1(true));
		if ((storeHouseL2 == null) || (key.resetLevel2())) {
			storeHouseL2 = null;
			if (!key.resetLevel2())
				storeHouseL2 = new HashMap<String, HashMap<String, HashMap<String, Object>>>();
			storeHouse.put(key.getKeyLevel1(true), storeHouseL2);

			if (key.resetLevel2()) {
				if (debug)
					LOG.info("Removed L2 cache. Key: " + key.getDebugMsg());
				return;
			}
		}

		// Retrieve or create the level 3 storeHouse.
		storeHouseL3 = storeHouseL2.get(key.getKeyLevel2(true));
		if ((storeHouseL3 == null) || (key.resetLevel3())) {
			storeHouseL3 = null;
			if (!key.resetLevel3())
				storeHouseL3 = new HashMap<String, HashMap<String, Object>>();
			storeHouseL2.put(key.getKeyLevel2(true), storeHouseL3);

			if (key.resetLevel3()){
				if (debug)
					LOG.info("Removed L3 cache. Key: " + key.getDebugMsg());
				return;
			}
		}

		// Retrieve or create the level 4 storeHouse.
		storeHouseL4 = storeHouseL3.get(key.getKeyLevel3(true));
		if ((storeHouseL4 == null) || (key.resetLevel4())) {
			storeHouseL4 = null;
			if (!key.resetLevel4())
				storeHouseL4 = new HashMap<String, Object>();
			storeHouseL3.put(key.getKeyLevel3(true), storeHouseL4);

			if (key.resetLevel4()){
				if (debug)
					LOG.info("Removed L4 cache. Key: " + key.getDebugMsg());
				return;
			}
		}

		// Save the object in the level 4 storeHouse.
		storeHouseL4.put(key.getKeyLevel4(true), object);
		
		if (object == null) {
			if (debug)
				LOG.info("Removed object from cache. Key: " + key.getDebugMsg());
		}
		else {
			if (debug)
				LOG.info("Stored object in cache with Key: " + key.getDebugMsg());
		}
	}

	/**
	 * Retrieves the object in the cache (if any).
	 * 
	 * @param key
	 * @return
	 * @throws CacheStoreHouseException
	 */
	private static Object retrieve(CacheStoreHouseKey key, boolean debug) throws CacheStoreHouseException {

		HashMap<String, HashMap<String, HashMap<String, Object>>> storeHouseL2 = storeHouse.get(key
				.getKeyLevel1(true));
		if (storeHouseL2 == null)
			return null;

		HashMap<String, HashMap<String, Object>> storeHouseL3 = storeHouseL2.get(key.getKeyLevel2(true));
		if (storeHouseL3 == null)
			return null;

		HashMap<String, Object> storeHouseL4 = storeHouseL3.get(key.getKeyLevel3(true));
		if (storeHouseL4 == null)
			return null;

		Object object = storeHouseL4.get(key.getKeyLevel4(true));
		if (object == null)
			return null;

		if (debug)
			LOG.info("Object retrieved from cache using Key: " + key.getDebugMsg());
		return object;
	}

}

// END OF FILE