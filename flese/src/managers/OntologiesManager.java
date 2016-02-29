package managers;

import java.util.ArrayList;
import java.util.HashMap;

import ontologies.InterfaceOntologyQuery;
import ontologies.OntologyInstancesQuery;
import ontologies.OntologyPropertiesQuery;
import ontologies.OntologyQueryArgument;
import ontologies.OntologyRootQuery;
import auxiliar.NextStep;

import com.hp.hpl.jena.rdf.model.RDFNode;

import constants.KConstants;
import constants.KUrls;

public class OntologiesManager extends AbstractManager {

	public OntologiesManager() {
	}

	@Override
	public String methodToInvokeIfMethodRequestedIsNotAvailable() {
		return "start";
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void start() throws Exception {
		// Forward to the jsp page.
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Ontologies.StartPage, ""));
	}

	public void main() throws Exception {
		// Forward to the jsp page.
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Ontologies.MainPage, ""));
	}

	public void mainQuery() throws Exception {

		String serviceEndPoint = requestStoreHouse.getRequestParameter(KConstants.Request.serviceEndPoint);

		InterfaceOntologyQuery query = OntologyRootQuery.getInstance(serviceEndPoint, null);

		this.resultsStoreHouse.resetOntologyQueryResults();
		this.resultsStoreHouse.addOntologyQueryResults(query);

		// Forward to the jsp page.
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Ontologies.MainQueryPage, ""));
	}

	public void instancesQuery() throws Exception {

		String serviceEndPoint = requestStoreHouse.getRequestParameter(KConstants.Request.serviceEndPoint);
		String urlValue = requestStoreHouse.getRequestParameter(KConstants.Request.url);

		HashMap<String, OntologyQueryArgument> args = new HashMap<String, OntologyQueryArgument>();
		OntologyQueryArgument value = OntologyQueryArgument.getInstance(urlValue);

		args.put(OntologyInstancesQuery.nameArg1, value);

		InterfaceOntologyQuery query = OntologyInstancesQuery.getInstance(serviceEndPoint, args);

		this.resultsStoreHouse.resetOntologyQueryResults();
		this.resultsStoreHouse.addOntologyQueryResults(query);

		// Forward to the jsp page.
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Ontologies.InstancesQueryPage, ""));
	}

	public void propertiesQuery() throws Exception {

		String serviceEndPoint = requestStoreHouse.getRequestParameter(KConstants.Request.serviceEndPoint);
		String urlValue = requestStoreHouse.getRequestParameter(KConstants.Request.url);

		HashMap<String, OntologyQueryArgument> args = new HashMap<String, OntologyQueryArgument>();
		OntologyQueryArgument value = OntologyQueryArgument.getInstance(urlValue);

		args.put(OntologyInstancesQuery.nameArg1, value);

		InterfaceOntologyQuery query = OntologyPropertiesQuery.getInstance(serviceEndPoint, args);

		this.resultsStoreHouse.resetOntologyQueryResults();
		this.resultsStoreHouse.addOntologyQueryResults(query);

		// Forward to the jsp page.
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Ontologies.PropertiesQueryPage, ""));
	}

	public void test() throws Exception {
		String serviceEndPoint = requestStoreHouse.getRequestParameter(KConstants.JspsDivsIds.ontologyUrlFieldId);

		InterfaceOntologyQuery classesQuery = OntologyRootQuery.getInstance(serviceEndPoint, null);

		this.resultsStoreHouse.resetOntologyQueryResults();
		this.resultsStoreHouse.addOntologyQueryResults(classesQuery);

		ArrayList<HashMap<String, RDFNode>> classesQueryResults = classesQuery.getResults();
		int i = 0;
		for (HashMap<String, RDFNode> classesQueryResult : classesQueryResults) {
			if (i < 1) {
				HashMap<String, OntologyQueryArgument> args = new HashMap<String, OntologyQueryArgument>();
				RDFNode value = classesQueryResult.get(OntologyRootQuery.nameVar1);

				args.put(OntologyInstancesQuery.nameArg1, OntologyQueryArgument.getInstance(value));

				InterfaceOntologyQuery instancesQuery = OntologyInstancesQuery.getInstance(serviceEndPoint, args);
				this.resultsStoreHouse.addOntologyQueryResults(instancesQuery);
			}
			i++;
		}

		// Forward to the jsp page.
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Ontologies.MainQueryPage, ""));
	}
}
