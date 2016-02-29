package prologConnector;

import logs.LogsManager;
import auxiliar.LocalUserInfo;
import storeHouse.CacheStoreHouse;
import storeHouse.CacheStoreHouseException;
import storeHouse.RequestStoreHouse;
import storeHouse.RequestStoreHouseException;
import conversors.ConversorToPrologQuery;
import conversors.QueryConversorException;
import filesAndPaths.FilesAndPathsException;
import filesAndPaths.ProgramFileInfo;

public class CiaoPrologNormalQuery extends CiaoPrologQueryAbstract {

	public boolean isOfType(String type) {
		return CiaoPrologQueryAbstract.Constants.NormalQuery.equals(type);
	}

	private CiaoPrologNormalQuery(ProgramFileInfo programFileInfo) throws CiaoPrologConnectorException {
		super(programFileInfo);
	}

	public static CiaoPrologNormalQuery getInstance(RequestStoreHouse requestStoreHouse) throws CacheStoreHouseException,
			FilesAndPathsException, CiaoPrologConnectorException, PlConnectionEnvelopeException, FilesAndPathsException,
			QueryConversorException, RequestStoreHouseException {

		String fullPath = requestStoreHouse.getProgramFileInfo().getProgramFileFullPath();
		LocalUserInfo localUserInfo = requestStoreHouse.getSession().getLocalUserInfo();
		String key1 = localUserInfo.getLocalUserName();

		ConversorToPrologQuery conversor = new ConversorToPrologQuery(requestStoreHouse);
		String key2 = conversor.getQueryComplexInfoString();

		Object o = CacheStoreHouse.retrieve(CiaoPrologNormalQuery.class, fullPath, key1, key2, true);
		CiaoPrologNormalQuery query = (CiaoPrologNormalQuery) o;
		if (query == null) {
			query = new CiaoPrologNormalQuery(requestStoreHouse.getProgramFileInfo());
			query.setRealQuery(conversor.getConvertedQuery(), conversor.getListOfVariables(), conversor.getListOfNamesForVariables());
			PlConnectionEnvelope.runPrologQuery(query, localUserInfo);
			CacheStoreHouse.store(CiaoPrologNormalQuery.class, fullPath, key1, key2, query, true);
		}
		else {
			LOG.info("Answers retrieved from cache ");
			LogsManager.logQuery(query, localUserInfo);
		}
		return query;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void clearCacheInstancesFor(ProgramFileInfo programFileInfo) throws FilesAndPathsException, CacheStoreHouseException {
		String fullPath = programFileInfo.getProgramFileFullPath();

		CacheStoreHouse.store(CiaoPrologNormalQuery.class, fullPath, null, null, null, true);
	}

	@Override
	public void adequationOfQueryAnswers() {
		// TODO Auto-generated method stub
		
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
