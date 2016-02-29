package auxiliar;

import java.util.Comparator;

public class StringsComparator {

	public static final Comparator<String> PERSONALIZED = new PersonalizedComparator();

	public static class PersonalizedComparator implements Comparator<String>, java.io.Serializable {

		// use serialVersionUID from JDK 1.2.2 for interoperability
		private static final long serialVersionUID = 1L;

		public int compare(String o1, String o2) {
			int result = 0;
			boolean valid = true;
			try {
				result = compareDouble(o1, o2);
			} catch (NumberFormatException e) {
				result = 0;
				valid = false;
			} catch (NullPointerException e) {
				result = 0;
				valid = false;
			}

			if (!valid) {
				result = compareStrings(o1, o2);
			}
			return result;
		}

		public int compareDouble(String o1, String o2) throws NumberFormatException, NullPointerException {
			Double o1d = Double.parseDouble(o1);
			Double o2d = Double.parseDouble(o2);
			if (o1d < o2d) {
				return -1;
			}
			if (o1d > o2d) {
				return +1;
			}
			return 0;
		}

		public int compareStrings(String o1, String o2) {
			if (o1 == null) {
				if (o2 == null) {
					return 0;
				} else {
					return -1;
				}
			} else {
				if (o2 == null) {
					return +1;
				}
			}

			char car1 = 'a';
			char car2 = 'a';
			int minLength = getMinLength(o1.length(), o2.length());
			// int maxLength = getMaxLength(o1.length(), o2.length());

			int i = 0;
			int retVal = 0;
			while ((i < minLength) && (retVal == 0)) {
				car1 = o1.charAt(i);
				car2 = o2.charAt(i);

				if (car1 != car2) {
					if (car1 < car2) {
						retVal = -1;
					}
					if (car1 > car2) {
						retVal = +1;
					}
				}
				i++;
			}

			if (retVal == 0) {
				if (o1.length() < o2.length()) {
					retVal = -1;
				}
				if (o1.length() > o2.length()) {
					retVal = +1;
				}
			}

			return retVal;
		}

		private int getMinLength(int i, int j) {
			if (i > j) {
				return j;
			}
			return i;
		}

		@SuppressWarnings("unused") private int getMaxLength(int i, int j) {
			if (i > j) {
				return i;
			}
			return j;
		}
	}

}
