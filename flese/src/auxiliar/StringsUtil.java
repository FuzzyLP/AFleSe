package auxiliar;

public class StringsUtil {

	public static boolean isEmptyString(String string) {
		if (string == null)
			return true;
		if (string.trim().isEmpty())
			return true;
		return false;
	}
}
