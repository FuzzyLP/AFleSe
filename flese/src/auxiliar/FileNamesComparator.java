package auxiliar;

import java.io.File;
import java.util.Comparator;

public class FileNamesComparator {

	public static final Comparator<File> PERSONALIZED = new PersonalizedComparator();

	private static class PersonalizedComparator implements Comparator<File>, java.io.Serializable {

		// use serialVersionUID from JDK 1.2.2 for interoperability
		private static final long serialVersionUID = 1L;

		public int compare(File o1, File o2) {
			//Comparator<String> comparador = .PERSONALIZED;
			StringsComparator.PersonalizedComparator comparador = new StringsComparator.PersonalizedComparator();
			int result = comparador.compare(o1.getName(), o2.getName());
			return result;
		}
	}
}
