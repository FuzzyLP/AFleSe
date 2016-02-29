package ontologies;

import java.util.ArrayList;
import java.util.HashMap;

import com.hp.hpl.jena.rdf.model.RDFNode;

public interface InterfaceOntologyQuery {
			
	public abstract String getQueryString(boolean argumentsSubstituted);
	
	public abstract String [] getVariablesNames();
	
	public abstract String [] getArgumentsNames();
		
	public abstract ArrayList<HashMap<String, RDFNode>> getResults();
	
	public abstract OntologyQueryVarResult[][] getResultsWithInfo() throws OntologiesException;
	
	public abstract String getQueryLogo();
}
