package managers;

import prologConnector.CiaoPrologConnectorException;
import prologConnector.CiaoPrologNormalQuery;
import prologConnector.CiaoPrologProgramIntrospectionQuery;
import prologConnector.CiaoPrologQueryAnswer;
import prologConnector.CiaoPrologTestingQuery;
import prologConnector.PlConnectionEnvelopeException;
import prologConnector.ProgramIntrospection;
import storeHouse.RequestStoreHouseException;
import auxiliar.NextStep;
import constants.KConstants;
import constants.KUrls;
import conversors.QueryConversorException;
import filesAndPaths.FilesAndPathsException;
import filesAndPaths.ProgramFileInfo;

public class QueriesManager extends AbstractManager {

	public QueriesManager() {
		super();
	}

	@Override
	public String methodToInvokeIfMethodRequestedIsNotAvailable() {
		return "selectProgramFile";
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void selectProgramFile() throws FilesAndPathsException, RequestStoreHouseException, FileSharingException {
		ProgramFileInfo[] filesList = FilesManagerAux.list(requestStoreHouse);
		resultsStoreHouse.setFilesList(filesList);

		// Forward to the jsp page.
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Queries.SelectProgramFilePage, ""));
	}

	public void programFileActions() throws FilesAndPathsException, RequestStoreHouseException {
		ProgramFileInfo programFileInfo = requestStoreHouse.getProgramFileInfo();
		resultsStoreHouse.setProgramFileInfo(programFileInfo);
		// Forward to the jsp page.
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Queries.ProgramFileActionsPage, ""));
	}

	public void selectQueryStartType() throws Exception {
		CiaoPrologProgramIntrospectionQuery ciaoPrologProgramIntrospectionQuery = CiaoPrologProgramIntrospectionQuery
				.getInstance(requestStoreHouse);
		ProgramIntrospection programIntrospection = ciaoPrologProgramIntrospectionQuery.getProgramIntrospection();
		resultsStoreHouse.setCiaoPrologProgramIntrospection(programIntrospection);
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Queries.SelectQueryStartTypePage, ""));
	}

	public void selectQuery() throws Exception {
		CiaoPrologProgramIntrospectionQuery ciaoPrologProgramIntrospectionQuery = CiaoPrologProgramIntrospectionQuery
				.getInstance(requestStoreHouse);
		ProgramIntrospection programIntrospection = ciaoPrologProgramIntrospectionQuery.getProgramIntrospection();
		resultsStoreHouse.setCiaoPrologProgramIntrospection(programIntrospection);
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Queries.SelectQueryPage, ""));
	}

	public void selectQueryAddLine() throws Exception {
		CiaoPrologProgramIntrospectionQuery ciaoPrologProgramIntrospectionQuery = CiaoPrologProgramIntrospectionQuery
				.getInstance(requestStoreHouse);
		ProgramIntrospection programIntrospection = ciaoPrologProgramIntrospectionQuery.getProgramIntrospection();
		resultsStoreHouse.setCiaoPrologProgramIntrospection(programIntrospection);
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Queries.SelectQueryAddLinePage, ""));
	}

	public void selectQueryAddAggr() throws Exception {
		CiaoPrologProgramIntrospectionQuery ciaoPrologProgramIntrospectionQuery = CiaoPrologProgramIntrospectionQuery
				.getInstance(requestStoreHouse);
		ProgramIntrospection programIntrospection = ciaoPrologProgramIntrospectionQuery.getProgramIntrospection();
		resultsStoreHouse.setCiaoPrologProgramIntrospection(programIntrospection);
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Queries.SelectQueryAddAggrPage, ""));
	}

	public void selectNegation() throws Exception {
		CiaoPrologProgramIntrospectionQuery ciaoPrologProgramIntrospectionQuery = CiaoPrologProgramIntrospectionQuery
				.getInstance(requestStoreHouse);
		ProgramIntrospection programIntrospection = ciaoPrologProgramIntrospectionQuery.getProgramIntrospection();
		resultsStoreHouse.setCiaoPrologProgramIntrospection(programIntrospection);
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Queries.SelectNegationPage, ""));
	}

	public void selectModifier() throws Exception {
		CiaoPrologProgramIntrospectionQuery ciaoPrologProgramIntrospectionQuery = CiaoPrologProgramIntrospectionQuery
				.getInstance(requestStoreHouse);
		ProgramIntrospection programIntrospection = ciaoPrologProgramIntrospectionQuery.getProgramIntrospection();
		resultsStoreHouse.setCiaoPrologProgramIntrospection(programIntrospection);
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Queries.SelectModifierPage, ""));
	}

	public void selectOperator() throws Exception {
		CiaoPrologProgramIntrospectionQuery ciaoPrologProgramIntrospectionQuery = CiaoPrologProgramIntrospectionQuery
				.getInstance(requestStoreHouse);
		ProgramIntrospection programIntrospection = ciaoPrologProgramIntrospectionQuery.getProgramIntrospection();
		resultsStoreHouse.setCiaoPrologProgramIntrospection(programIntrospection);
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Queries.SelectOperatorPage, ""));
	}

	public void selectValue() throws Exception {
		CiaoPrologProgramIntrospectionQuery ciaoPrologProgramIntrospectionQuery = CiaoPrologProgramIntrospectionQuery
				.getInstance(requestStoreHouse);
		ProgramIntrospection programIntrospection = ciaoPrologProgramIntrospectionQuery.getProgramIntrospection();
		resultsStoreHouse.setCiaoPrologProgramIntrospection(programIntrospection);
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Queries.SelectValuePage, ""));
	}

	public void evaluate() throws Exception {
		CiaoPrologQueryAnswer[] queryAnswers = new CiaoPrologQueryAnswer[0];
		String[] queryVariablesNames = new String[0];
		try {
			CiaoPrologNormalQuery query = CiaoPrologNormalQuery.getInstance(requestStoreHouse);
			queryAnswers = query.getQueryAnswers();
			queryVariablesNames = query.getVariablesNames();
		} catch (QueryConversorException e) {
			queryAnswers = new CiaoPrologQueryAnswer[0];
			queryVariablesNames = new String[0];
			String msg = e.getMessage();
			resultsStoreHouse.addResultMessage(msg);
		} catch (CiaoPrologConnectorException e) {
			queryAnswers = new CiaoPrologQueryAnswer[0];
			queryVariablesNames = new String[0];
			String msg = e.getMessage();
			resultsStoreHouse.addResultMessage(msg);
		} catch (PlConnectionEnvelopeException e) {
			queryAnswers = new CiaoPrologQueryAnswer[0];
			queryVariablesNames = new String[0];
			String msg = e.getMessage();
			resultsStoreHouse.addResultMessage(msg);
		}
		resultsStoreHouse.setCiaoPrologQueryAnswers(queryAnswers);
		resultsStoreHouse.setCiaoPrologQueryVariablesNames(queryVariablesNames);
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Queries.EvaluatePage, ""));
	}

	public void test() throws Exception {
		CiaoPrologTestingQuery query = CiaoPrologTestingQuery.getInstance(requestStoreHouse.getProgramFileInfo());
		CiaoPrologQueryAnswer[] queryAnswers = query.getQueryAnswers();
		resultsStoreHouse.setCiaoPrologQueryAnswers(queryAnswers);
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Queries.EvaluatePage, ""));
	}

}
