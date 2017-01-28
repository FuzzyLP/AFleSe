package constants;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KConstants {

	public static final class Application {
		public static final String servletName = "/Servlet";
		public static final String testModeSubUrl = "fleseTest";
		public static final String httpsPrefix = "https://";
		public static final String httpPrefix = "http://";
		public static final String doGetMethod = "doGet";
		public static final String doPostMethod = "doPost";

		public static final String BackupsFolder = "FleSeProgramsBackups";
		public static final String LogsFolder = "FleSeLogs";
		public static final String LogsDefaultName = "FleSeUnknownLogs_";
		public static final String LogsFileForQueries = "FleSeQueriesLog_";
		public static final String LogsFileForSignedUsers = "FleSeSignedUsersLog_";

		public static final String AppBugsEmail = "vpablos@babel.ls.fi.upm.es";
	}

	public static final class Titles {
		public static final String FirstLine = "<span class='underline'>Fle</span>xible";
		public static final String SecondLine = "<span class='underline'>Se</span>arches in Databases";
	}

	public static final class Managers {
		public static final String managersPackage = "managers";
		public static final String managerSuffix = "Manager";
	}

	public static class Values {
		public static final String True = "true";
		public static final String False = "false";
	}

	public static class Request {
		// Objects stored in the request.
		public static final String resultsStoreHouse = "resultsStoreHouse";

		// General parameters
		public static final String managerParam = "manager";
		public static final String operationParam = "op";
		public static final String fileNameParam = "fileName";
		public static final String fileOwnerParam = "fileOwner";
		public static final String providerId = "id";
		public static final String isAjaxParam = "ajax";
		public static final String programParam = "chooseProgram";
		public static final String mode = "mode";
		public static final String modeBasic = "basic";
		public static final String modeAdvanced = "advanced";
		public static final String modeEditingDefault = "editing default";

		// Lines Counters
		public static final String linesCounterParam = "linesCount";
		public static final String lineNumberParam = "lineNum";
		public static final String lineIdParam = "lineId";

		// Query parameters.
		public static final String databaseParam = "db";
		public static final String predicateParam = "pred";
		public static final String aggregatorParam = "queryLines.selectAggregator";
		public static final String negationParam = "negation";
		public static final String quantifierParam = "quantifier";
		public static final String operatorParam = "operator";
		public static final String valueParam = "value";
		
		//Ontologies parameters.
		public static final String url = "url";
		public static final String serviceEndPoint = "serviceEndPoint";
		public static final String divIdPrefix = "divIdPrefix";
		
	}

	public static class PathsMgmt {
		public static String[] programFilesValidPaths = new String[5];
		public static String plServerPath = "";
		public static String ciaocPath = "";
		public static boolean stateErrorConfigFile = false;
		public static boolean stateErrorConfigFile2 = false;
		public static String reasonError2 = "";
		
		
		public static void loadConfig() {

		ClassLoader loader = KConstants.class.getClassLoader();
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
			input = loader.getResourceAsStream("config.properties");
			prop.load(input);
			// get the property values and load it into the constants
			if (!prop.getProperty("programFilesValidPathsFromTemp").equals(""))
			{
				programFilesValidPaths[0] = System.getProperty("java.io.tmpdir") + (prop.getProperty("programFilesValidPathsFromTemp"));
			}
				programFilesValidPaths[1] = (prop.getProperty("programFilesValidPaths"));
			programFilesValidPaths[2] = (prop.getProperty("programFilesValidPaths2"));
			programFilesValidPaths[3] = (prop.getProperty("programFilesValidPaths3"));
			programFilesValidPaths[4] = (prop.getProperty("programFilesValidPaths4"));
			
			plServerPath = prop.getProperty("plServerPath");
			ciaocPath = prop.getProperty("ciaocPath");
	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			stateErrorConfigFile = true;
			ex.printStackTrace();
		}
		
		finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		}
	}

	public static class Communications {
		public static final int BUFSIZE = 4096;
		public static final int maxFileSize = 50000 * 1024;
		public static final int maxMemSize = 50000 * 1024;
	}

	public static class CiaoPrologQuery {
		public static final long maximumNumberOfRetries = 9223372036854775807L;
		public static final long maximumNumberOfAnswers = 9223372036854775807L;

	}

	public static class PlConnectionsPool {
		public static final int maxNumOfConnections = 10;
	}

	public static class Session {
		public static final String swAppInTestingMode = "swAppInTestingMode";
		public static final String authProvider = "authProvider";
		public static final String authProviderId = "authProviderId";
		public static final String localUserInfo = "localUserInfo";
		public static final String registryStoreHouse = "registryStoreHouse";
	}

	public static class NextStep {
		public static final int none = 0;
		public static final int forward_to = 1;
		public static final int redirect_to = 2;
		public static final int redirect_to_with_session = 3;
		public static final int invalidAction = 4;
	}

	public static class Fuzzifications {
		public final static String DEFAULT_DEFINITION = "default definition";
		public final static String PREVIOUS_DEFAULT_DEFINITION = "previous default definition";
		public static final String predDefined = "predDefined";
		public static final String predNecessary = "predNecessary";
		public static final String database = "database";
	}

	public static class ProgramIntrospectionFields {
		public static final String predicateName = "predicateName";
		public static final String predicateArity = "predicateArity";
		public static final String predicateTypes = "predicateTypes";
		public static final String predicateOrigins = "predicateOrigins";
		public static final String predicateMoreInfo = "predicateMoreInfo";
	}

	public static class PrologTypes {
		public static final String rfuzzy_any_type = "rfuzzy_any_type";
		public static final String rfuzzy_truth_value_type = "rfuzzy_truth_value_type";

		public static final String rfuzzy_enum_type = "rfuzzy_enum_type";
		public static final String rfuzzy_string_type = "rfuzzy_string_type";

		public static final String rfuzzy_number_type = "rfuzzy_number_type";
		public static final String rfuzzy_integer_type = "rfuzzy_integer_type";
		public static final String rfuzzy_float_type = "rfuzzy_float_type";

	}

	public static class PredicateOrigins {
		public static final String framework = "framework";
		public static final String db_field = "db_field";
		public static final String type = "type";
		public static final String negation = "negation";
		public static final String modifier = "modifier";
		public static final String aggregator = "aggregator";
		public static final String similarity = "similarity";
		public static final String value = "value";
		public static final String function = "function";
		public static final String rule = "rule";
		public static final String defaults_to = "defaults_to";
		public static final String synonym_of = "synonym_of";
		public static final String antonym_of = "antonym_of";
	}
	
	public static class MoreInfoTypes {
		public static final String fuzzyRule = "fuzzy_rule";
		public static final String database = "database";
		public static final String enumTypeValues = "rfuzzy_enum_type_values";
		public static final String dbField = "rfuzzy_db_field";
		public static final String definedOperators = "defined_operators";
		public static final String similarityClause = "rfuzzy_similarity_clause";
	}

	public static class ProgramAnalysis {
		public static final String markerForIf = "if";
		public static final String markerForWithCredibility = "with_credibility";
		public static final String markerForOnlyForUser = "only_for_user";
		public static final String markerForDefaultsTo = "defaults_to";
		public static final String markerForRule = "rule";
		public static final String markerForFunction = "function";
		public static final String markerForValue = "value";
	}
	
	public static class Ontologies {
		public static final String defaultServiceEndpoint = "http://dbpedia.org/sparql";
	}

	public static class JavaScriptScripts {
		public static final String jsRegex = "<script[^>]*>[^<]*</script>";
		public static final String jsStart = "<script type=\"text/javascript\">";
		public static final String jsEnd = "</script>";
	}

	public static class JspsDivsIds {
		public static final String auxAndInvisibleSection = "auxAndInvisibleSection";
		public static final String mainSecDivId = "mainSecDiv";
		public static final String msgsSecDivId = "msgsSecDiv";

		public static final String programFileActionsContainerId = "programFileActionsContainer";
		public static final String chooseQueryStartTypeContainerId = "chooseQueryStartTypeContainerId";
		public static final String queryStartContainerId = "queryStartContainer";
		public static final String selectQueryDivId = "selectQueryDiv";
		public static final String runQueryDivId = "runQueryDiv";
		public static final String queryLinesContainerId = "queryLinesContainer";
		public static final String searchOrPersonalizeTableId = "searchOrPersonalizeTable";
		public static final String queryLinesTableId = "queryLinesTable";
		public static final String queryLinesAggregatorTableId = "queryLinesAggregatorTable";

		public static final String filesListDiv = "filesListDiv";
		public static final String fileViewContentsDiv = "fileViewContentsDiv";
		public static final String fileUploadDiv = "fileUploadDiv";
		public static final String uploadFormId = "uploadForm";
		public static final String uploadStatusDivId = "uploadStatus";
		public static final String uploadFormTargetiFrameId = "uploadFormTargetiFrame";

		public static final String personalizationFunctionUnderModificationDivId = "personalizationFunctionUnderModificationDivId";

		public static final String fuzzificationGraphicDivId = "fuzzificationGraphicDiv";
		public static final String fuzzificationValuesAndButtonDivId = "fuzzificationValuesAndButtonDiv";
		public static final String fuzzificationSaveStatusDivId = "fuzzificationSaveStatus";
		public static final String fuzzificationBarValueDivId = "fuzzificationBarValueDivId";
		
		public static final String ontologyStartDivId = "ontologyStartDivId";
		public static final String ontologyUrlFieldId = "ontologyUrlFieldId";
		public static final String ontologyQueryResultsDivId = "ontologyQueryResultsDivId";
		
		public static final String fuzzificationUpdateStatusDivId = "fuzzificationUpdateStatus";
	}

	public static class AppMsgs {
		public static final String welcomeToFleSe = "Welcome to the FleSe application. Enjoy !!!";
		public static final String errorTryingToAuthenticateUser = "The social authentication provider chosen says you are not authenticated. Sorry.";
		public static final String exception1 = "Ups! An exception occurred.";
		public static final String exception2 = "You can press the key F5 and try again or send a bug report to ";
		public static final String exception3 = " with the following info: ";
		public static final String exception4 = "Cannot find the configuration file.";
		public static final String exception5 = "Move the file to the resource folder and you can press the key F5 and try again.";
		public static final String exception6 = "Cannot load the configuration file: ";
		public static final String exception7 = "Check and correct the configuration file and you can press the key F5 and try again.";
		public static final String errorSessionNull1 = "Your session has expired. You need to sign in again.";
	}
}

// **********************************
// **********************************
// **********************************

