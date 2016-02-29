package storeHouse;

import programAnalysis.ProgramAnalysis;
import prologConnector.CiaoPrologNormalQuery;
import prologConnector.CiaoPrologProgramIntrospectionQuery;
import filesAndPaths.FilesAndPathsException;
import filesAndPaths.ProgramFileInfo;

public class CacheStoreHouseCleaner {

	public static void clean(ProgramFileInfo programFileInfo) throws FilesAndPathsException, CacheStoreHouseException {
		
		ProgramAnalysis.clearCacheInstancesFor(programFileInfo);
		CiaoPrologProgramIntrospectionQuery.clearCacheInstancesFor(programFileInfo);
		CiaoPrologNormalQuery.clearCacheInstancesFor(programFileInfo);

	}

}
