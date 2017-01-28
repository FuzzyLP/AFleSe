package managers;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import storeHouse.CacheStoreHouseCleaner;
import storeHouse.CacheStoreHouseException;
import storeHouse.RequestStoreHouse;
import storeHouse.RequestStoreHouseException;
import auxiliar.CastingsClass;
import auxiliar.LocalUserInfo;
import constants.KConstants;
import filesAndPaths.FilesAndPathsException;
import filesAndPaths.PathsMgmt;
import filesAndPaths.PathsUtils;
import filesAndPaths.ProgramFileInfo;
import filters.OnlyCiaoPrologFilesFilterClass;
import filters.OnlyLocalUserNameFolderFilterClass;
import filters.OnlyNotLocalUserNameFolderFilterClass;
import managers.FileSharingException;

public class FilesManagerAux {
	private static HashMap<String,HashMap<String,Boolean>>sharingState = new HashMap<String,HashMap<String,Boolean>>();
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static ProgramFileInfo[] listMyFiles(RequestStoreHouse requestStoreHouse) throws FilesAndPathsException,
			RequestStoreHouseException, FileSharingException {
		return listAux(requestStoreHouse, true);
	}

	public static ProgramFileInfo[] list(RequestStoreHouse requestStoreHouse) throws FilesAndPathsException, RequestStoreHouseException, FileSharingException {
		return listAux(requestStoreHouse, false);
	}

	public static ProgramFileInfo[] listAux(RequestStoreHouse requestStoreHouse, boolean onlyMine) throws FilesAndPathsException,
			RequestStoreHouseException, FileSharingException {

		LocalUserInfo localUserInfo = requestStoreHouse.getSession().getLocalUserInfo();

		PathsMgmt pathsMgmt = new PathsMgmt();
		File dir = new File(pathsMgmt.getProgramFilesPath());

		ArrayList<ProgramFileInfo> currentList = new ArrayList<ProgramFileInfo>();

		FilenameFilter filter;
		String[] subDirs;

		// We list first the localUserName program files.
		filter = (FilenameFilter) new OnlyLocalUserNameFolderFilterClass(localUserInfo.getLocalUserName());
		subDirs = dir.list(filter);

		if (subDirs != null) {
			for (int i = 0; i < subDirs.length; i++) {
				// Get filename of file or directory
				currentList = listProgramFilesInSubDir(subDirs[i], pathsMgmt, currentList);
			}
		}

		if (!onlyMine) {
			// We list in second (and last) place the other program files.
			filter = (FilenameFilter) new OnlyNotLocalUserNameFolderFilterClass(localUserInfo.getLocalUserName());
			subDirs = dir.list(filter);

			if (subDirs != null) {
				for (int i = 0; i < subDirs.length; i++) {
					// Get filename of file or directory
					currentList = listProgramFilesInSubDir(subDirs[i], pathsMgmt, currentList);
				}
			}
		}

		ProgramFileInfo [] array = currentList.toArray(new ProgramFileInfo[currentList.size()]);
		Arrays.sort(array);
		try{
			for (ProgramFileInfo p: array)
			{
				if (sharingState.containsKey(p.getFileOwner()))
				{
					if ((!sharingState.get(p.getFileOwner()).containsKey(p.getFileName())))
					{
						sharingState.get(p.getFileOwner()).put(p.getFileName(), true);
					}
				} else {
					HashMap<String,Boolean> h = new HashMap<String,Boolean>();
					h.put(p.getFileName(),true);
					sharingState.put(p.getFileOwner(), h);
				}
			}
		}
		catch (Exception e)
		{
			throw new FileSharingException(e.getMessage());
		}
		return array;
	}
	
	public static void changeSharingState(ProgramFileInfo p) throws Exception
	{
		try{
			sharingState.get(p.getFileOwner()).put(p.getFileName(), !p.getSharingState());
		}
		catch (Exception e)
		{
			throw new FileSharingException(e.getMessage());
		}
		
	}
	
	public static boolean getSharingState(ProgramFileInfo p) throws Exception
	{
		try{
			return (sharingState.get(p.getFileOwner()).get(p.getFileName()));
		}
		catch (Exception e)
		{
			throw new FileSharingException(e.getMessage());
		}
	}
	
	public static void removeSharingFile(ProgramFileInfo p) throws Exception
	{
		try{
			if( sharingState.get(p.getFileOwner()) != null )
				sharingState.get(p.getFileOwner()).remove(p.getFileName());
		}
		catch (Exception e)
		{
			throw new FileSharingException(e.getMessage());
		}
	}
	
	public static boolean sharedfiles(String s) throws Exception
	{
		try{
			if (sharingState.containsKey(s))
			{
				return true;
			}
			
			for (HashMap<String,Boolean> h : sharingState.values())
			{
				if (h.containsValue(true))
				{
					return true;
				}
			}
			return false;
		}
		catch (Exception e)
		{
			throw new FileSharingException(e.getMessage());
		}
	}

	/**
	 * Gets a list with the existing program files.
	 * 
	 * @param subDir
	 *            is the full path of the subdirectory we are listing.
	 * @return the program files list.
	 * @throws FilesAndPathsException
	 * @throws LocalUserInfoException
	 */
	private static ArrayList<ProgramFileInfo> listProgramFilesInSubDir(String subDir, PathsMgmt pathsMgmt,
			ArrayList<ProgramFileInfo> currentList) throws FilesAndPathsException {

		if ((subDir == null) || ("".equals(subDir)) || (KConstants.Application.BackupsFolder.equals(subDir))
				|| (KConstants.Application.LogsFolder.equals(subDir))) {
			return currentList;
		}

		String realPathSubDir = PathsUtils.concatPathsStrings(pathsMgmt.getProgramFilesPath(), subDir);

		File dir = new File(realPathSubDir);
		FilenameFilter filter = (FilenameFilter) new OnlyCiaoPrologFilesFilterClass();
		String[] files = dir.list(filter);

		ProgramFileInfo programFileInfo;
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				try {
					programFileInfo = new ProgramFileInfo(subDir, files[i]);
				} catch (FilesAndPathsException e) {
					e.printStackTrace();
					programFileInfo = null;
				}

				if (programFileInfo != null)
					currentList.add(programFileInfo);
			}
		}
		return currentList;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static String[] uploadFileAux(RequestStoreHouse requestStoreHouse) throws FilesAndPathsException, RequestStoreHouseException,
			FileUploadException, CacheStoreHouseException {
		ArrayList<String> ret = new ArrayList<String>();
		String doMethod = requestStoreHouse.getDoMethod();

		if ((doMethod == null) || (!(KConstants.Application.doPostMethod.equals(doMethod)))) {
			return uploadFileAuxReturn(ret, "Uploads are only allowed using http post method.");
		}

		// Check that we have a file upload request
		boolean isMultipart = requestStoreHouse.requestIsMultipartContent();
		if (!isMultipart) {
			return uploadFileAuxReturn(ret, "We cannot upload because the content of the request is not multipart.");
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(KConstants.Communications.maxMemSize);
		// Location to save data that is larger than maxMemSize.
		// factory.setRepository(new File("/tmp/uploads"));
		PathsMgmt pathsMgmt = new PathsMgmt();
		String repositoryLocation = pathsMgmt.getProgramFilesPath();
		factory.setRepository(new File(repositoryLocation));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax(KConstants.Communications.maxFileSize);

		// Get the path where we are going to upload the file.
		LocalUserInfo localUserInfo = requestStoreHouse.getSession().getLocalUserInfo();
		String fileOwner = localUserInfo.getLocalUserName();
		if ((fileOwner == null) || ("".equals(fileOwner))) {
			return uploadFileAuxReturn(ret, "ERROR: fileOwner cannot be null nor empty string.");
		}

		// Parse the request to get file items.
		List<FileItem> fileItems = CastingsClass.castList(FileItem.class, upload.parseRequest(requestStoreHouse.getRequest()));

		for (int i = 0; i < fileItems.size(); i++) {
			// FileItem fileItem = (FileItem)i.next();
			FileItem fileItem = fileItems.get(i);
			if (!fileItem.isFormField()) {
				// Get the uploaded file parameters
				// String fieldName = fi.getFieldName();
				String fileName = fileItem.getName();
				if (fileName == null) {
					ret.add("The name of the program file to upload is null.");
				} else {
					if ("".equals(fileName)) {
						ret.add("The name of the program file to upload is an empty string.");
					} else {
						if (!fileName.endsWith(".pl")) {
							ret.add("The name of the program file to upload must have the .pl extension.");
						} else {

							while (fileName.lastIndexOf("\\") >= 0) {
								fileName = fileName.substring(fileName.lastIndexOf("\\"));
							}
							while (fileName.lastIndexOf("/") >= 0) {
								fileName = fileName.substring(fileName.lastIndexOf("/"));
							}

							// String fileNameReal = "";
							// String contentType = fi.getContentType();
							// boolean isInMemory = fi.isInMemory();
							// long sizeInBytes = fi.getSize();
							// Write the file

							ProgramFileInfo programFileInfo = new ProgramFileInfo(fileOwner, fileName);
							if (programFileInfo.existsFile(false)) {
								ret.add("You cannot upload two files with the same name. Use a different name for the new one.");
							} else {
								File file = programFileInfo.createFile(false);

								boolean writeWasOk = false;
								try {
									fileItem.write(file);
									writeWasOk = true;
								} catch (Exception e) {
									e.printStackTrace();
									ret.add(e.getMessage());
									writeWasOk = false;
								}

								if (writeWasOk) {
									int resultCompilation =0;
									try{
										String FilePath = programFileInfo.getProgramFileFullPath();
										Process compilationProcess = Runtime.getRuntime().exec(pathsMgmt.getCiaocPath() + " " + FilePath);
										compilationProcess.waitFor();
										resultCompilation = compilationProcess.exitValue();
									}
									catch (Exception e)
									{
										resultCompilation = -1;
									}
									finally
									{
										if( resultCompilation == 0 )
											ret.add("Successfully uploaded file " + fileName);
										else
										{
											try
											{
												programFileInfo.remove();
											}
											catch (Exception e)
											{
											}
											ret.add("The file uploaded could not be compiled.");
										}
										CacheStoreHouseCleaner.clean( programFileInfo );
									}	
								}
							}
						}
					}
				}
			}
		}
		return uploadFileAuxReturn(ret, null);
	}

	private static String[] uploadFileAuxReturn(ArrayList<String> accumulator, String newMsg) {
		String[] result = null;
		if (accumulator == null) {
			if ((newMsg != null) && (!("".equals(newMsg)))) {
				result = new String[1];
				result[0] = newMsg;
			} else {
				result = new String[0];
			}
		} else {
			if ((newMsg != null) && (!("".equals(newMsg)))) {
				accumulator.add(newMsg);
			}
			result = accumulator.toArray(new String[accumulator.size()]);
		}
		return result;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

}

// // EOF
