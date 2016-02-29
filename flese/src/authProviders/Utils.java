package authProviders;

public class Utils {

	/**
	 * Returns newLocalUserName if localUserName is null; localUserName otherwise.
	 * 
	 * @param localUserName
	 *            is the name of the user.
	 * @param newLocalUserName
	 *            is the new value proposed for localUserName.
	 * @return newLocalUserName if localUserName is null; localUserName otherwise
	 * @throws LocalUserInfoException
	 * 
	 */
	public static String ifNullThenSetUserNameFrom(String localUserName, String beforeAt, String afterAt,
			String msgForBeforeAt, String msgForAfterAt) {
		final String replaceForAt1 = "_from_";
		final String replaceForAt2 = "_at_";

		if (localUserName == null) {
			if ((beforeAt != null) && (afterAt != null)) {
				if (beforeAt.contains(afterAt)) {
					localUserName = fixLocalUserName(beforeAt, replaceForAt1);
					// LOG.info(msgForBeforeAt);
				} else {
					String auxLocalUserName = beforeAt + "_from_" + afterAt;
					localUserName = fixLocalUserName(auxLocalUserName, replaceForAt2);
					// LOG.info(msgForBeforeAt+ "+ _at_ + "+ msgForAfterAt);
				}
			} else {
				if (beforeAt != null) {
					localUserName = fixLocalUserName(beforeAt, replaceForAt2);
				}
				if (afterAt != null) {
					localUserName = fixLocalUserName(beforeAt, replaceForAt2);
				}
			}
		}
		return localUserName;
	}

	/**
	 * Fixes invalid localUserNames.
	 * 
	 * @param localUserName
	 *            is the name of the user.
	 * @return the fixed localUserName or null if we cannot fix it.
	 * 
	 */
	private static String fixLocalUserName(String newLocalUserName, String replaceForAt) {
		if ((replaceForAt == null) || (replaceForAt.isEmpty()))
			replaceForAt = "_at_";

		// String msg = "fixLocalUserName: ";
		if ((newLocalUserName != null) && (!"".equals(newLocalUserName))) {
			// msg += newLocalUserName + " -> ";
			newLocalUserName = newLocalUserName.replaceAll("\\s", "_");
			// msg += newLocalUserName + " -> ";
			newLocalUserName = newLocalUserName.replaceAll("\\@", replaceForAt);
			// msg += newLocalUserName + " -> ";
			newLocalUserName = newLocalUserName.replaceAll("\\.", "_");
			// msg += newLocalUserName + " ";
			// LOG.info(msg);
		}

		if (null != nullOnlyIfUserNameIsValid(newLocalUserName)) {
			newLocalUserName = null;
		}
		return newLocalUserName;
	}

	/**
	 * Checks if an user name is valid.
	 * 
	 * @param localUserName
	 *            is the name of the user that we are checking.
	 * @return null if the localUserName parameter serves as a valid user name.
	 */
	public static String nullOnlyIfUserNameIsValid(String userName) {

		if (userName == null) {
			return "userName is null";
		}
		if ("".equals(userName)) {
			return "userName is empty";
		}
		if (userName.contains("\\s")) {
			return "userName contains \\s";
		}
		if (userName.contains("\\@")) {
			return "userName contains \\@";
		}
		if (userName.contains("\\.")) {
			return "userName contains \\.";
		}
		if (userName.contains("/")) {
			return "userName contains /.";
		}

		return null;
	}

}
