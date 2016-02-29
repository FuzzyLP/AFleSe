package ontologies;

import java.util.HashMap;

import storeHouse.CacheStoreHouse;
import storeHouse.CacheStoreHouseException;

public class OntologyPropertiesQuery extends AbstractOntologyQuery {

	private static final String queryPrefixLine01 = "PREFIX owl: <http://www.w3.org/2002/07/owl#>";
	private static final String queryPrefixLine02 = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>";
	private static final String queryPrefixLine03 = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
	private static final String queryPrefixLine04 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
	private static final String queryPrefixLine05 = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>";
	private static final String queryPrefixLine06 = "PREFIX dc: <http://purl.org/dc/elements/1.1/>";
	private static final String queryPrefixLine07 = "PREFIX : <http://dbpedia.org/resource/>";
	private static final String queryPrefixLine08 = "PREFIX dbpedia2: <http://dbpedia.org/property/>";
	private static final String queryPrefixLine09 = "PREFIX dbpedia: <http://dbpedia.org/>";
	private static final String queryPrefixLine10 = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>";

	private static final String queryEndLine01 = "SELECT ?property ?hasValue ?isValueOf";
	private static final String queryEndLine02 = "WHERE { ";
	private static final String queryEndLine03 = "         { ?url ?property ?hasValue }";
	private static final String queryEndLine04 = "      UNION ";
	private static final String queryEndLine05 = "         { ?isValueOf ?property ?url }";
	private static final String queryEndLine06 = "      }";
	// @url = <http://dbpedia.org/ontology/Disease>
	// @url = <http://dbpedia.org/resource/Air_Force_blue>

	private static final String queryString = queryPrefixLine01 + queryPrefixLine02 + queryPrefixLine03 + queryPrefixLine04
			+ queryPrefixLine05 + queryPrefixLine06 + queryPrefixLine07 + queryPrefixLine08 + queryPrefixLine09 + queryPrefixLine10
			+ queryEndLine01 + queryEndLine02 + queryEndLine03 + queryEndLine04 + queryEndLine05 + queryEndLine06;

	private static final String nameVar1 = "property";
	private static final String nameVar2 = "hasValue";
	private static final String nameVar3 = "isValueOf";

	public static final String nameArg1 = "url";

	private OntologyPropertiesQuery(String serviceEndPoint, HashMap<String, OntologyQueryArgument> args) {
		super(serviceEndPoint, queryString, args);
	}

	public static AbstractOntologyQuery getInstance(String serviceEndPoint, HashMap<String, OntologyQueryArgument> args)
			throws CacheStoreHouseException {
		String serviceEndPointKey = getServiceEndPointKey(serviceEndPoint);
		String queryStringKey = getQueryStringKey(queryString);
		String argumentsKey = getArgumentsKey(args);
		Object o = CacheStoreHouse.retrieve(OntologyPropertiesQuery.class, serviceEndPointKey, queryStringKey, argumentsKey, true);
		OntologyPropertiesQuery query = (OntologyPropertiesQuery) o;
		if (query == null) {
			query = new OntologyPropertiesQuery(serviceEndPoint, args);
			CacheStoreHouse.store(OntologyPropertiesQuery.class, serviceEndPointKey, queryStringKey, argumentsKey, query, true);
		}
		return query;
	}

	@Override
	public String[] getVariablesNames() {
		String[] varsNames = new String[3];
		varsNames[0] = nameVar1;
		varsNames[1] = nameVar2;
		varsNames[2] = nameVar3;
		return varsNames;
	}

	@Override
	public String[] getArgumentsNames() {
		String[] argsNames = new String[1];
		argsNames[0] = nameArg1;
		return argsNames;
	}

	protected String getQuerySubLogo() {
		return "properties of ";
	}
}
