package prologConnector;

import logs.LogsManager;
import auxiliar.LocalUserInfo;
import storeHouse.CacheStoreHouse;
import storeHouse.CacheStoreHouseException;
import storeHouse.RequestStoreHouse;
import CiaoJava.PLStructure;
import CiaoJava.PLTerm;
import CiaoJava.PLVariable;
import constants.KConstants;
import filesAndPaths.FilesAndPathsException;
import filesAndPaths.ProgramFileInfo;

public class CiaoPrologProgramIntrospectionQuery extends CiaoPrologQueryAbstract {

	ProgramIntrospection programIntrospection = null;

	public boolean isOfType(String type) {
		return CiaoPrologQueryAbstract.Constants.ProgramIntrospectionQuery.equals(type);
	}
	
	private CiaoPrologProgramIntrospectionQuery(ProgramFileInfo programFileInfo) throws CiaoPrologConnectorException, FilesAndPathsException {
		super(programFileInfo);

		// Prepare the query structure.
		// rfuzzy_introspection(PClass, PName, PArity, PType).
		PLVariable[] variables = new PLVariable[5];
		variables[0] = new PLVariable(); // predicateType
		variables[1] = new PLVariable(); // predicateName
		variables[2] = new PLVariable(); // predicateArity
		variables[3] = new PLVariable(); // predicateOrigin
		variables[4] = new PLVariable(); // predicateType
		PLTerm[] args = { variables[0], variables[1], variables[2], variables[3], variables[4] };
		PLStructure query = new PLStructure("rfuzzy_introspection", args);

		String[] variablesNames = { KConstants.ProgramIntrospectionFields.predicateName,
				KConstants.ProgramIntrospectionFields.predicateArity, KConstants.ProgramIntrospectionFields.predicateTypes,
				KConstants.ProgramIntrospectionFields.predicateOrigins, KConstants.ProgramIntrospectionFields.predicateMoreInfo };

		setRealQuery(query, variables, variablesNames);

		isProgramIntrospectionQuery = true;
	}

	public static synchronized CiaoPrologProgramIntrospectionQuery getInstance(RequestStoreHouse requestStoreHouse) throws CacheStoreHouseException,
			FilesAndPathsException, CiaoPrologConnectorException, PlConnectionEnvelopeException {
		String fullPath = requestStoreHouse.getProgramFileInfo().getProgramFileFullPath();
		LocalUserInfo localUserInfo = requestStoreHouse.getSession().getLocalUserInfo();
		
		Object o = CacheStoreHouse.retrieve(CiaoPrologProgramIntrospectionQuery.class, fullPath, fullPath, fullPath, true);
		CiaoPrologProgramIntrospectionQuery query = (CiaoPrologProgramIntrospectionQuery) o;
		if (query == null) {
			query = new CiaoPrologProgramIntrospectionQuery(requestStoreHouse.getProgramFileInfo());
			PlConnectionEnvelope.runPrologQuery(query, localUserInfo);
			CacheStoreHouse.store(CiaoPrologProgramIntrospectionQuery.class, fullPath, fullPath, fullPath, query, true);
		} else {
			LogsManager.logQuery(query, localUserInfo);
		}

		return query;
	}

	public static void clearCacheInstancesFor(ProgramFileInfo programFileInfo) throws FilesAndPathsException, CacheStoreHouseException {
		String fullPath = programFileInfo.getProgramFileFullPath();
		CacheStoreHouse.store(CiaoPrologProgramIntrospectionQuery.class, fullPath, null, null, null, true);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public ProgramIntrospection getProgramIntrospection() {
		// if (programIntrospection != null) return programIntrospection;
		return this.programIntrospection;
	}

	@Override
	public void adequationOfQueryAnswers() {
		programIntrospection = new ProgramIntrospection(getProgramFileInfo());
		CiaoPrologQueryAnswer answer = null;
		CiaoPrologQueryAnswer [] queryAnswers = getQueryAnswers();
		for (int i = 0; i < queryAnswers.length; i++) {
			answer = queryAnswers[i];
			programIntrospection.addAnswerInfo(answer);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
