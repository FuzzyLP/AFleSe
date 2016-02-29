package filters;

import java.io.File;
import java.io.FilenameFilter;


/**
 * Creates a filter to filter out any directory not owned by the owner.
 * 
 */
public class OnlyLocalUserNameFolderFilterClass implements FilenameFilter {

	private String owner = "";
	
	/**
	 * Creates a filter to filter out any directory not owned by the owner.
	 * 
	 * @param owner is the owner of the directories that are returned when 
	 * the filter is executed. 
	 * 
	 */
	public OnlyLocalUserNameFolderFilterClass(String owner) {
		super();
		this.owner = owner;
	}
	
	@Override
	public boolean accept(File dir, String name) {
		return ((dir.isDirectory()) && (dir.canRead()) && (dir.canExecute()) && (name.equals(owner)));
	}

}
