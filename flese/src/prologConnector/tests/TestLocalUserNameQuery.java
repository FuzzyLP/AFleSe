package prologConnector.tests;

import prologConnector.CiaoPrologConnectorException;
import prologConnector.CiaoPrologTestingQuery;
import prologConnector.PlConnectionEnvelope;
import prologConnector.PlConnectionEnvelopeException;
import CiaoJava.PLAtom;
import CiaoJava.PLStructure;
import CiaoJava.PLTerm;
import CiaoJava.PLVariable;
import auxiliar.LocalUserInfo;
import constants.KConstants;
import filesAndPaths.FilesAndPathsException;
import filesAndPaths.ProgramFileInfo;

public class TestLocalUserNameQuery {

	public static class KCtes {

		public static String programOwner = "appAdm";
		public static String programFileName1 = "db_shopping.pl";
		public static String programFileName2 = "test_username.pl";

		public static String assertLocalUserNamePredicate = "assertLocalUserName";
		public static String isLocalUserNamePredicate = "isUserNameLocalUserNameAux";

	}

	public static void main(String[] args) throws Exception {
		KConstants.PathsMgmt.loadConfig();
		LocalUserInfo localUserInfo = LocalUserInfo.getFakeLocalUserInfo("victorpablosceruelo_at_gmail_com");
		
		test1(localUserInfo);
		// test2(localUserInfo);

		System.exit(0);

	}

	@SuppressWarnings("static-access")
	protected static void test1(LocalUserInfo localUserInfo) throws CiaoPrologConnectorException,
			FilesAndPathsException, PlConnectionEnvelopeException {
		ProgramFileInfo pfi = new ProgramFileInfo(KCtes.programOwner, KCtes.programFileName1);
		// To fill in with values.
		PLStructure query = null;
		PLVariable[] variables = null;
		String[] variablesNames = null;

		variables = new PLVariable[6];
		variables[0] = new PLVariable(); // Kind
		variables[1] = new PLVariable(); // Reason
		variables[2] = new PLVariable(); // Xtmp
		variables[3] = new PLVariable(); // Vtmp
		variables[4] = new PLVariable(); // Cond
		variables[5] = new PLVariable(); // V

		variablesNames = new String[] { "Kind", "Reason", "Xtmp", "Vtmp", "Cond", "V" };

		String localUserName = localUserInfo.getLocalUserName();
		PLTerm argAssert = new PLAtom(localUserName);
		PLTerm[] argsAssert = new PLTerm[] { argAssert };
		PLStructure queryAssert = new PLStructure(KCtes.assertLocalUserNamePredicate, argsAssert);

		PLTerm[] argsCheckUserName = new PLTerm[] { argAssert, variables[0], variables[1] };
		PLStructure queryCheckUserName = new PLStructure(KCtes.isLocalUserNamePredicate, argsCheckUserName);

		PLTerm[] args_expensive = { variables[2], variables[3] };
		PLStructure query_expensive = new PLStructure("expensive", args_expensive);

		PLTerm[] args_rfuzzy_var_truth_value = { variables[3], variables[4], variables[5] };
		PLStructure query_dump_constraints = new PLStructure("rfuzzy_var_truth_value",
				args_rfuzzy_var_truth_value);

		PLTerm[] args_conjunction1 = { queryAssert, queryCheckUserName };
		PLStructure conjunction1 = new PLStructure(",", args_conjunction1);

		PLTerm[] args_conjunction2 = { query_expensive, query_dump_constraints };
		PLStructure conjunction2 = new PLStructure(",", args_conjunction2);

		PLTerm[] args_conjunction3 = { conjunction1, conjunction2 };
		PLStructure conjunction3 = new PLStructure(",", args_conjunction3);

		query = conjunction3;

		// Now the real stuff.
		CiaoPrologTestingQuery ciaoQuery = CiaoPrologTestingQuery.getInstance(pfi);
		ciaoQuery.overrideRealQuery(query, variables, variablesNames);
		PlConnectionEnvelope plConnectionEnvelope = new PlConnectionEnvelope();
		plConnectionEnvelope.runPrologQuery(ciaoQuery, localUserInfo);
	}

	@SuppressWarnings("static-access")
	protected static void test2(LocalUserInfo localUserInfo) throws CiaoPrologConnectorException,
			FilesAndPathsException, PlConnectionEnvelopeException {
		ProgramFileInfo pfi = new ProgramFileInfo(KCtes.programOwner, KCtes.programFileName2);
		// To fill in with values.
		PLStructure query = null;
		PLVariable[] variables = null;
		String[] variablesNames = null;

		variables = new PLVariable[5];
		variables[0] = new PLVariable(); //
		variables[1] = new PLVariable(); //
		variables[2] = new PLVariable(); //
		variables[3] = new PLVariable(); //
		variables[4] = new PLVariable(); //

		variablesNames = new String[] { "Kind", "Reason", "Type2", "Kind2", "Reason2" };

		String localUserName = localUserInfo.getLocalUserName();
		// PLTerm argAssert = new PLStructure("'" + localUserName + "'", new
		// PLTerm[0]);
		// PLTerm argAssert = new PLString("'" + localUserName + "'");
		// PLTerm argAssert = new PLAtom("'" + localUserName + "'");
		PLTerm argAssert = new PLAtom(localUserName);
		PLTerm[] argsAssert = new PLTerm[] { argAssert };
		PLStructure queryAssert = new PLStructure(KCtes.assertLocalUserNamePredicate, argsAssert);

		PLTerm[] argsCheckUserName = new PLTerm[] { argAssert, variables[0], variables[1] };
		PLStructure queryCheckUserName = new PLStructure(KCtes.isLocalUserNamePredicate, argsCheckUserName);

		PLTerm[] args_test = { variables[2], variables[3], variables[4] };
		PLStructure query_test = new PLStructure("test", args_test);

		PLTerm[] args_conjunction1 = { queryAssert, queryCheckUserName };
		PLStructure conjunction1 = new PLStructure(",", args_conjunction1);

		PLTerm[] args_conjunction2 = { conjunction1, query_test };
		PLStructure conjunction2 = new PLStructure(",", args_conjunction2);

		query = conjunction2;

		// Now the real stuff.
		CiaoPrologTestingQuery ciaoQuery = CiaoPrologTestingQuery.getInstance(pfi);
		ciaoQuery.overrideRealQuery(query, variables, variablesNames);
		PlConnectionEnvelope plConnectionEnvelope = new PlConnectionEnvelope();
		plConnectionEnvelope.runPrologQuery(ciaoQuery, localUserInfo);
	}

}
