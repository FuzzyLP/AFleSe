package ontologies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public abstract class AbstractOntologyQuery implements InterfaceOntologyQuery {

	private String queryString;
	private HashMap<String, OntologyQueryArgument> queryArguments;
	private String substitutedQueryString;
	private ArrayList<HashMap<String, RDFNode>> results;

	private final static String defaultServiceEndpoint = "http://dbpedia.org/sparql";

	protected AbstractOntologyQuery(String serviceEndPoint, String queryString, HashMap<String, OntologyQueryArgument> args) {
		this.queryString = queryString;
		this.queryArguments = args;
		this.substitutedQueryString = null;
		this.results = new ArrayList<HashMap<String, RDFNode>>();

		setQueryArguments(args);
		query(serviceEndPoint);
	}

	public final String getQueryString(boolean argumentsSubstituted) {
		if (argumentsSubstituted) {
			return this.substitutedQueryString == null ? "" : this.substitutedQueryString;
		}
		return this.queryString == null ? "" : this.queryString;
	}

	private final void setQueryArguments(HashMap<String, OntologyQueryArgument> args) {
		if (args == null) {
			args = new HashMap<String, OntologyQueryArgument>();
		}

		String tmpQuery = getQueryString(false);
		ParameterizedSparqlString parametrizedQuery = new ParameterizedSparqlString();

		// Create the Parameterized String
		// ParameterizedSparqlString queryString = new
		// ParameterizedSparqlString();
		parametrizedQuery.setCommandText(tmpQuery);

		Set<String> keys = args.keySet();

		for (String key : keys) {
			OntologyQueryArgument argValue = args.get(key);
			setQueryArgument(key, argValue, parametrizedQuery);
		}
		// queryString.setLiteral("url", serviceEndPoint);
		// queryString.toString();

		this.substitutedQueryString = parametrizedQuery.toString();
	}

	private void setQueryArgument(String key, OntologyQueryArgument argValue, ParameterizedSparqlString parametrizedQuery) {
		System.out.println("Setting args: arg0: " + key + " arg1: " + argValue.toString());

		if (argValue != null) {
			if (argValue.isNode()) {
				RDFNode value = argValue.getRDFNode();
				parametrizedQuery.setParam(key, value);
			} else {
				Resource resource = ResourceFactory.createResource(argValue.toString());
				parametrizedQuery.setParam(key, resource);
				// NO: parametrizedQuery.setLiteral(key, argValue.toString());
			}
		}
	}

	private void query(String serviceEndPoint) {

		if ((serviceEndPoint == null) || ("".equals(serviceEndPoint))) {
			serviceEndPoint = defaultServiceEndpoint;
		}

		String queryToBeSend = getQueryString(true);
		System.out.println("Sending query: " + queryToBeSend);
		if ((queryToBeSend != null) && (!"".equals(queryToBeSend))) {

			Query query = QueryFactory.create(queryToBeSend);
			QueryExecution qe = QueryExecutionFactory.sparqlService(serviceEndPoint, query);

			ResultSet rs = null;
			try {
				rs = qe.execSelect();
				while (rs.hasNext()) {
					QuerySolution qs = rs.next();
					HashMap<String, RDFNode> result = new HashMap<String, RDFNode>();

					String[] varsNames = getVariablesNames();
					for (String varName : varsNames) {
						RDFNode value = qs.get(varName);
						result.put(varName, value);
					}

					results.add(result);
				}
			} catch (Exception e) {
				rs = null;
				System.out.println(e.getMessage());
			} finally {
				qe.close();
			}

			if (rs != null) {
				System.out.println(ResultSetFormatter.asText(rs));
			}
		}

		// //Create the Parameterized String
		// ParameterizedSparqlString queryString = new
		// ParameterizedSparqlString();
		// queryString.Namespaces.AddNamespace("ex", new
		// Uri("http://example.org/ns#"));
		// queryString.CommandText = "SELECT * WHERE { ?s ex:property @value }";
		//
		// //Inject a Value for the parameter
		// queryString.SetUri("value", new Uri("http://example.org/value"));
		//
		// //When we call ToString() we get the full command text with
		// namespaces appended as PREFIX
		// //declarations and any parameters replaced with their declared values
		// Console.WriteLine(queryString.ToString());
	}

	public final ArrayList<HashMap<String, RDFNode>> getResults() {
		return results;
	}

	public final OntologyQueryVarResult[][] getResultsWithInfo() throws OntologiesException {
		OntologyQueryVarResult[][] arrayResults = new OntologyQueryVarResult[this.results.size()][];

		int size = this.results.size();
		for (int i = 0; i < size; i++) {
			HashMap<String, RDFNode> resultIn = this.results.get(i);
			Set<String> keys = resultIn.keySet();
			OntologyQueryVarResult[] result = new OntologyQueryVarResult[keys.size()];
			int j = 0;

			for (String key : keys) {
				RDFNode node = resultIn.get(key);
				OntologyQueryVarResult ontologyQueryVarResult = new OntologyQueryVarResult(key, node);
				result[j] = ontologyQueryVarResult;
				j++;
			}

			arrayResults[i] = result;
		}

		return arrayResults;
	}

	protected static String getServiceEndPointKey(String serviceEndPoint) {
		if ((serviceEndPoint == null) || ("".equals(serviceEndPoint))) {
			return defaultServiceEndpoint;
		}
		return serviceEndPoint;
	}

	protected static String getQueryStringKey(String querystring) {
		if ((querystring == null) || ("".equals(querystring))) {
			return "null";
		}
		return querystring;
	}

	protected static String getArgumentsKey(HashMap<String, OntologyQueryArgument> args) {
		if (args == null) {
			return "null";
		}
		if (args.size() == 0) {
			return "null";
		}

		Set<String> keysSet = args.keySet();
		String[] keys = keysSet.toArray(new String[keysSet.size()]);
		Arrays.sort(keys);

		StringBuilder sbKey = new StringBuilder();
		for (int i = 0; i < keys.length; i++) {
			sbKey.append(keys[i]);
			sbKey.append("=");
			OntologyQueryArgument arg = args.get(keys[i]);
			String argString = (arg == null) ? "null" : arg.toString();
			sbKey.append(argString);
			if (i + 1 < keys.length) {
				sbKey.append(":");
			}
		}
		String keyString = sbKey.toString();
		return keyString;
	}

	protected HashMap<String, OntologyQueryArgument> getQueryArguments() {
		return this.queryArguments;
	}

	@Override
	public String getQueryLogo() {
		HashMap<String, OntologyQueryArgument> args = getQueryArguments();
		StringBuilder logoSB = new StringBuilder();

		String subLogo = getQuerySubLogo();
		if (subLogo != null) {
			logoSB.append(subLogo);
			logoSB.append(" ");
		}

		if (args != null) {
			Collection<OntologyQueryArgument> valuesCol = args.values();
			for (OntologyQueryArgument ontologyQueryArgument : valuesCol) {
				if (ontologyQueryArgument != null) {
					String argStr = ontologyQueryArgument.getStringArg();
					if (argStr != null) {
						argStr = adequateUrlString(argStr);
						logoSB.append(argStr);
						logoSB.append(" ");
					}
				}
			}
		}

		return logoSB.toString();
	}

	private String adequateUrlString(String argStr) {
		if ((argStr == null) || ("".equals(argStr))) {
			return "";
		}

		int index = argStr.lastIndexOf("/");
		if ((index > -1) && (index + 1 < argStr.length())) {
			argStr = argStr.substring(index + 1);
		}
		return argStr;
	}

	abstract protected String getQuerySubLogo();

	// .
	// .
	// .
	// .
}
