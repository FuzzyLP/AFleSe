package storeHouse;

public class CacheStoreHouseKey {

	private String keyLevel1 = null;
	private String keyLevel2 = null;
	private String keyLevel3 = null;
	private String keyLevel4 = null;

	protected CacheStoreHouseKey(@SuppressWarnings("rawtypes") Class keyLevel1, String keyLevel2,
			String keyLevel3, String keyLevel4) throws CacheStoreHouseException {

		boolean reset = false;

		this.keyLevel1 = getKeyFromClassName(keyLevel1);
		if ("".equals(this.keyLevel1))
			reset = true;

		this.keyLevel2 = reset ? "" : getKey(keyLevel2);
		if ("".equals(this.keyLevel2))
			reset = true;

		this.keyLevel3 = reset ? "" : getKey(keyLevel3);
		if ("".equals(this.keyLevel3))
			reset = true;

		this.keyLevel4 = reset ? "" : getKey(keyLevel4);
		if ("".equals(this.keyLevel4))
			reset = true;
	}

	/**
	 * Gets the key from the class object
	 * 
	 * @param keyLevel1
	 * @return returns the key
	 * @throws CacheStoreHouseException
	 */
	private String getKeyFromClassName(@SuppressWarnings("rawtypes") Class keyLevel1)
			throws CacheStoreHouseException {
		if (keyLevel1 == null)
			throw new CacheStoreHouseException("keyLevel1 cannot be null.");
		String className = keyLevel1.getName();
		if (className == null)
			throw new CacheStoreHouseException("keyLevel1.getName cannot be null.");
		if ("".equals(className))
			throw new CacheStoreHouseException("keyLevel1.getName cannot be empty string.");
		return getKey(className);
	}

	private String getKey(String input) {
		if (input == null) {
			input = "";
		}

		return input.toUpperCase();
	}

	public String getKeyLevel1(boolean throwExceptionIfNull) throws CacheStoreHouseException {
		if (throwExceptionIfNull) {
			if ((this.keyLevel1 == null) || ("".equals(this.keyLevel1))) {
				throw new CacheStoreHouseException("keyLevel1 has an invalid value.");
			}
		}
		return this.keyLevel1;
	}

	public String getKeyLevel2(boolean throwExceptionIfNull) throws CacheStoreHouseException {
		if (throwExceptionIfNull) {
			if ((this.keyLevel2 == null) || ("".equals(this.keyLevel2))) {
				throw new CacheStoreHouseException("keyLevel2 has an invalid value.");
			}
		}
		return this.keyLevel2;
	}

	public String getKeyLevel3(boolean throwExceptionIfNull) throws CacheStoreHouseException {
		if (throwExceptionIfNull) {
			if ((this.keyLevel3 == null) || ("".equals(this.keyLevel3))) {
				throw new CacheStoreHouseException("keyLevel3 has an invalid value.");
			}
		}
		return this.keyLevel3;
	}

	public String getKeyLevel4(boolean throwExceptionIfNull) throws CacheStoreHouseException {
		if (throwExceptionIfNull) {
			if ((this.keyLevel4 == null) || ("".equals(this.keyLevel4))) {
				throw new CacheStoreHouseException("keyLevel4 has an invalid value.");
			}
		}
		return this.keyLevel4;
	}

	public boolean resetLevel1() throws CacheStoreHouseException {
		return ("".equals(getKeyLevel1(false)));
	}

	public boolean resetLevel2() throws CacheStoreHouseException {
		return ("".equals(getKeyLevel2(false)));
	}

	public boolean resetLevel3() throws CacheStoreHouseException {
		return ("".equals(getKeyLevel3(false)));
	}

	public boolean resetLevel4() throws CacheStoreHouseException {
		return ("".equals(getKeyLevel4(false)));
	}

	public String getDebugMsg() throws CacheStoreHouseException {
		StringBuilder debugMsg = new StringBuilder();

		debugMsg.append("CacheStoreHouseKey: -");
		debugMsg.append(getKeyLevel1(false));
		debugMsg.append("- -");
		debugMsg.append(getKeyLevel2(false));
		debugMsg.append("- -");
		debugMsg.append(getKeyLevel3(false));
		debugMsg.append("- -");
		debugMsg.append(getKeyLevel4(false));
		debugMsg.append("-");
		return debugMsg.toString();
	}

}

// EOF