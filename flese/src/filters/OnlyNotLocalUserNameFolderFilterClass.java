package filters;

import java.io.File;
import java.io.FilenameFilter;

public class OnlyNotLocalUserNameFolderFilterClass implements FilenameFilter {

	private String userName = "";

	/**
	 * Creates a filter to get only directories not owned by the owner.
	 * 
	 * @param owner is the owner of the directories that are *NOT* 
	 * returned when the filter is executed. 
	 * 
	 */
	public OnlyNotLocalUserNameFolderFilterClass(String newUserName) {
		super();
		userName = newUserName;
	}
	
	
	@Override
	public boolean accept(File dir, String name) {
		return ((dir.isDirectory()) && (dir.canRead()) && (dir.canExecute()) && 
				(! name.equals(userName)) && (! name.equals(".")) && (! name.equals(userName)));
	}

}
