package filters;

import java.io.File;
import java.io.FilenameFilter;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

/**
 * Creates a filter to get only *.pl files.
 * 
 */
public class OnlyCiaoPrologFilesFilterClass implements FilenameFilter {

	// private static final Log LOG = LogFactory.getLog(OnlyCiaoPrologFilesFilterClass.class);
	@Override
	public boolean accept(File dir, String name) {
		// dir va a ser el directorio contenedor y no el manejador del archivo a filtrar !!!
		// LOG.info("OnlyCiaoPrologFilesFilterClass: accept: dir.AbsolutePath: " + dir.getAbsolutePath());
		return ((dir.isDirectory()) && (dir.canRead()) && (dir.canExecute()) && (name.endsWith(".pl")));
	}

}
