package auxiliar;

import java.io.IOException;

import org.apache.commons.logging.Log;

public class RunCommand {

	public static boolean run(String commandFullPath, Log LOG) {

		Runtime rt = Runtime.getRuntime();
		Process pr = null;
		try {
			if (LOG != null)
				LOG.info("Executing command " + commandFullPath);
			pr = rt.exec(commandFullPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (pr != null)
			return true;
		return false;
	}
}
