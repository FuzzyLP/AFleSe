package urls;

import java.lang.reflect.Field;
import java.util.ArrayList;

import storeHouse.CacheStoreHouse;
import storeHouse.CacheStoreHouseException;
import constants.KUrls;

public class UrlsMaps {

	private static boolean loaded = false;

	private static String getManager(UrlMap urlMap) {
		String manager = urlMap.getManager(true);
		return manager;
	}

	private static String getOp(UrlMap urlMap) {
		String op = urlMap.getOp(true);
		return op;
	}

	public static UrlMap getUrlMap(UrlMap urlMap) throws UrlMapException {
		String manager = getManager(urlMap);
		String op = getOp(urlMap);

		if (!loaded) {
			load();
		}

		try {
			urlMap = (UrlMap) CacheStoreHouse.retrieve(UrlsMaps.class, manager, manager, op, false);
		} catch (CacheStoreHouseException e) {
			e.printStackTrace();
			urlMap = null;
		}
		if (urlMap == null)
			throw new UrlMapException("urlMap cannot be null.");
		return urlMap;
	}

	/**
	 * This predicate uses Java reflection to get all the urls defined in it.
	 * 
	 * @return
	 */
	public static final UrlMap[] retrieveDefinedUrls() {
		Class<?>[] subClasses = KUrls.class.getClasses();
		ArrayList<UrlMap> fullList = new ArrayList<UrlMap>();
		Field[] urlMapList = null;

		for (int i = 0; i < subClasses.length; i++) {
			urlMapList = null;
			urlMapList = subClasses[i].getFields();

			for (int j = 0; j < urlMapList.length; j++) {
				Object objectDefined;
				try {
					objectDefined = urlMapList[j].get(null);
				} catch (IllegalArgumentException e) {
					objectDefined = null;
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					objectDefined = null;
					e.printStackTrace();
				}
				UrlMap value = null;
				if (objectDefined != null) {
					value = (UrlMap) ((objectDefined instanceof UrlMap) ? objectDefined : null);
				}
				if (value != null) {
					fullList.add(value);
				}
			}
		}

		return fullList.toArray(new UrlMap[fullList.size()]);
	}

	/**
	 * This predicate loads all the defined pages in KUrls so that we ca access dynamically to them.
	 */
	private static synchronized void load() {

		if (!loaded) {
			UrlMap[] pagesList = retrieveDefinedUrls();

			for (int i = 0; i < pagesList.length; i++) {
				UrlMap page = pagesList[i];
				try {
					storeMapping(page);
				} catch (CacheStoreHouseException e) {
					e.printStackTrace();
				}
			}
			loaded = true;
		}
	}

	private static void storeMapping(UrlMap urlMap) throws CacheStoreHouseException {
		String manager = getManager(urlMap);
		String op = getOp(urlMap);

		CacheStoreHouse.store(UrlsMaps.class, manager, manager, op, urlMap, true);
	}
}

/* --- */
