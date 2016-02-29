package constants;

import urls.UrlMap;

public class KUrls {

	public static class Pages {
		public static final UrlMap Empty = getUrlMap("", "emptyPage", null, null, "");
		public static final UrlMap Index = getUrlMap("", "indexPage", null, null, "index.jsp");
		public static final UrlMap Exception = getUrlMap("", "exception", null, null, "WEB-INF/errorException.jsp");
		public static final UrlMap NullSession = getUrlMap("", "nullSession", null, null, "WEB-INF/errorNullSession.jsp");
	}

	public static class Auth {
		public static String manager = "AuthManager";
		public static final UrlMap SocialAuthCallback = getUrlMap("", "", null, null, ""); // No manager nor op.
		
		public static final UrlMap ProvidersPage = getUrlMap(manager, "providersPage", null, null, "WEB-INF/authentication/providers.jsp");
		public static final UrlMap Providers = getUrlMap(manager, "providers", ProvidersPage, KUrls.Pages.Exception, "");

		public static final UrlMap SignInPage = getUrlMap(manager, "signInPage", null, null, "WEB-INF/authentication/signedIn.jsp");
		public static final UrlMap SignIn = getUrlMap(manager, "signIn", SignInPage, KUrls.Pages.Exception, "");

		public static final UrlMap SignOutPage = getUrlMap(manager, "signOutPage", null, null, "WEB-INF/authentication/signedOut.jsp");
		public static final UrlMap SignOut = getUrlMap(manager, "signOut", SignOutPage, KUrls.Pages.Exception, "");

		public static final UrlMap AboutPage = getUrlMap(manager, "aboutPage", null, null, "WEB-INF/authentication/about.jsp");
		public static final UrlMap About = getUrlMap(manager, "about", AboutPage, KUrls.Pages.Exception, "");

		public static final UrlMap LogsPage = getUrlMap(manager, "logsPage", null, null, "WEB-INF/authentication/logs.jsp");
		public static final UrlMap Logs = getUrlMap(manager, "logs", LogsPage, KUrls.Pages.Exception, "");

		public static final UrlMap CallsRegistryPage = getUrlMap(manager, "callsRegistryPage", null, null, "WEB-INF/authentication/callsRegistry.jsp");
		public static final UrlMap CallsRegistry = getUrlMap(manager, "callsRegistry", CallsRegistryPage, KUrls.Pages.Exception, "");
	}

	public static class User {
		public static String manager = "UserManager";

		public static final UrlMap OptionsPage = getUrlMap(manager, "optionsPage", null, null, "WEB-INF/user/userOptions.jsp");
		public static final UrlMap Options = getUrlMap(manager, "options", OptionsPage, KUrls.Pages.Exception, "");
	}

	public static class Files {
		public static String manager = "FilesManager";
		public static final UrlMap ListMyFilesPage = getUrlMap(manager, "listMyFilesPage", null, null, "WEB-INF/files/list.jsp");
		public static final UrlMap ListMyFiles = getUrlMap(manager, "listMyFiles", ListMyFilesPage, KUrls.Pages.Exception, "");

		public static final UrlMap ListPage = getUrlMap(manager, "listPage", null, null, "WEB-INF/files/list.jsp");
		public static final UrlMap List = getUrlMap(manager, "list", ListPage, KUrls.Pages.Exception, "");

		public static final UrlMap UploadDivPage = getUrlMap(manager, "uploadDivPage", null, null, "WEB-INF/files/uploadDiv.jsp");
		public static final UrlMap UploadDiv = getUrlMap(manager, "uploadDiv", UploadDivPage, KUrls.Pages.Exception, "");

		public static final UrlMap UploadPage = getUrlMap(manager, "uploadPage", null, null, "WEB-INF/files/upload.jsp");
		public static final UrlMap Upload = getUrlMap(manager, "upload", UploadPage, UploadPage, "");

		public static final UrlMap ViewPage = getUrlMap(manager, "viewPage", null, null, "WEB-INF/files/view.jsp");
		public static final UrlMap View = getUrlMap(manager, "view", ViewPage, KUrls.Pages.Exception, "");

		public static final UrlMap DownloadPage = getUrlMap(manager, "downloadPage", null, null, "WEB-INF/files/download.jsp");
		public static final UrlMap Download = getUrlMap(manager, "download", DownloadPage, KUrls.Pages.Exception, "");

		public static final UrlMap RemovePage = getUrlMap(manager, "removePage", null, null, "WEB-INF/files/remove.jsp");
		public static final UrlMap Remove = getUrlMap(manager, "remove", RemovePage, KUrls.Pages.Exception, "");

		public static final UrlMap ChangeStatePage = getUrlMap(manager, "ChangeStatePage", null, null, "WEB-INF/files/changeState.jsp");
		public static final UrlMap ChangeState = getUrlMap(manager, "changeState", ChangeStatePage, KUrls.Pages.Exception, "");
		}

	public static class Ontologies{
		public static String manager = "OntologiesManager";
		
		public static final UrlMap StartPage = getUrlMap(manager, "startPage", null, null, "WEB-INF/ontologies/start.jsp");
		public static final UrlMap Start = getUrlMap(manager, "start", StartPage, KUrls.Pages.Exception, "");

		public static final UrlMap MainPage = getUrlMap(manager, "mainPage", null, null, "WEB-INF/ontologies/main.jsp");
		public static final UrlMap Main = getUrlMap(manager, "main", MainPage, KUrls.Pages.Exception, "");

		public static final UrlMap MainQueryPage = getUrlMap(manager, "mainQueryPage", null, null, "WEB-INF/ontologies/mainQuery.jsp");
		public static final UrlMap MainQuery = getUrlMap(manager, "mainQuery", MainQueryPage, KUrls.Pages.Exception, "");

		public static final UrlMap InstancesQueryPage = getUrlMap(manager, "instancesQueryPage", null, null, "WEB-INF/ontologies/instancesQuery.jsp");
		public static final UrlMap InstancesQuery = getUrlMap(manager, "instancesQuery", InstancesQueryPage, KUrls.Pages.Exception, "");

		public static final UrlMap PropertiesQueryPage = getUrlMap(manager, "propertiesQueryPage", null, null, "WEB-INF/ontologies/propertiesQuery.jsp");
		public static final UrlMap PropertiesQuery = getUrlMap(manager, "propertiesQuery", PropertiesQueryPage, KUrls.Pages.Exception, "");

	}
	
	public static class Queries {
		public static String manager = "QueriesManager";

		public static final UrlMap SelectProgramFilePage = getUrlMap(manager, "", null, null, "WEB-INF/queries/selectProgramFile.jsp");
		public static final UrlMap SelectProgramFile = getUrlMap(manager, "selectProgramFile", SelectProgramFilePage, KUrls.Pages.Exception, "");

		public static final UrlMap ProgramFileActionsPage = getUrlMap(manager, "", null, null, "WEB-INF/queries/programFileActions.jsp");
		public static final UrlMap ProgramFileActions = getUrlMap(manager, "programFileActions", ProgramFileActionsPage, KUrls.Pages.Exception, "");

		public static final UrlMap SelectQueryStartTypePage = getUrlMap(manager, "", null, null, "WEB-INF/queries/selectQueryStartType.jsp");
		public static final UrlMap SelectQueryStartType = getUrlMap(manager, "selectQueryStartType", SelectQueryStartTypePage, KUrls.Pages.Exception, "");

		public static final UrlMap SelectQueryPage = getUrlMap(manager, "", null, null, "WEB-INF/queries/selectQuery.jsp");
		public static final UrlMap SelectQuery = getUrlMap(manager, "selectQuery", SelectQueryPage, KUrls.Pages.Exception, "");

		public static final UrlMap SelectQueryAddLinePage = getUrlMap(manager, "", null, null, "WEB-INF/queries/selectQueryAddLine.jsp");
		public static final UrlMap SelectQueryAddLine = getUrlMap(manager, "selectQueryAddLine", SelectQueryAddLinePage, KUrls.Pages.Exception, "");

		public static final UrlMap SelectQueryAddAggrPage = getUrlMap(manager, "", null, null, "WEB-INF/queries/selectQueryAddAggr.jsp");
		public static final UrlMap SelectQueryAddAggr = getUrlMap(manager, "selectQueryAddAggr", SelectQueryAddAggrPage, KUrls.Pages.Exception, "");

		public static final UrlMap SelectNegationPage = getUrlMap(manager, "", null, null, "WEB-INF/queries/selectNegation.jsp");
		public static final UrlMap SelectNegation = getUrlMap(manager, "selectNegation", SelectNegationPage, KUrls.Pages.Exception, "");

		public static final UrlMap SelectModifierPage = getUrlMap(manager, "", null, null, "WEB-INF/queries/selectModifier.jsp");
		public static final UrlMap SelectModifier = getUrlMap(manager, "selectModifier", SelectModifierPage, KUrls.Pages.Exception, "");

		public static final UrlMap SelectOperatorPage = getUrlMap(manager, "", null, null, "WEB-INF/queries/selectOperator.jsp");
		public static final UrlMap SelectOperator = getUrlMap(manager, "selectOperator", SelectOperatorPage, KUrls.Pages.Exception, "");

		public static final UrlMap SelectValuePage = getUrlMap(manager, "", null, null, "WEB-INF/queries/selectValue.jsp");
		public static final UrlMap SelectValue = getUrlMap(manager, "selectValue", SelectValuePage, KUrls.Pages.Exception, "");

		public static final UrlMap EvaluatePage = getUrlMap(manager, "", null, null, "WEB-INF/queries/evaluateQuery.jsp");
		public static final UrlMap Evaluate = getUrlMap(manager, "evaluate", EvaluatePage, KUrls.Pages.Exception, "");
		
		public static final UrlMap TestQueryPage = getUrlMap(manager, "", null, null, "WEB-INF/queries/evaluateQuery.jsp");
		public static final UrlMap TestQuery = getUrlMap(manager, "test", TestQueryPage, KUrls.Pages.Exception, "");

	}

	public static class Fuzzifications {
		public static String manager = "FuzzificationsManager";

		public static final UrlMap ListPage = getUrlMap(manager, "", null, null, "WEB-INF/fuzzifications/list.jsp");
		public static final UrlMap List = getUrlMap(manager, "list", ListPage, KUrls.Pages.Exception, "");

		public static final UrlMap EditPage = getUrlMap(manager, "", null, null, "WEB-INF/fuzzifications/edit.jsp");
		public static final UrlMap Edit = getUrlMap(manager, "edit", EditPage, KUrls.Pages.Exception, "");

		public static final UrlMap SavePage = getUrlMap(manager, "", null, null, "WEB-INF/fuzzifications/save.jsp");
		public static final UrlMap Save = getUrlMap(manager, "save", SavePage, KUrls.Pages.Exception, "");
		
		public static final UrlMap Update = getUrlMap(manager, "selectQueryStartType", Queries.SelectQueryStartTypePage, KUrls.Pages.Exception, "");
	}
	
	private static UrlMap getUrlMap(String manager, String op, UrlMap nextPage, UrlMap exceptionPage, String currentUrl) {
		return UrlMap.getUrlMap(manager, op, nextPage, exceptionPage, currentUrl);
	}

}
