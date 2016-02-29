package programAnalysis;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProgramPartLine {
	
	final Log LOG = LogFactory.getLog(ProgramPartLine.class);
	

	
	private String programLine = null;
	private String predDefined = null;
	private String predNecessary = null;
	private String predOwner = "";
	private ArrayList <FunctionPoint> functionPoints = null;
	private String msg = null;
	
	private static String fuzzificationInitialSubstring = "rfuzzy_fuzzification(";
	private static String functionInitialSubstring = "function([";
	
	public ProgramPartLine (String line) throws Exception {
		if (programLine == null) throw new Exception("line is null");
		
		if ("".equals(line)) throw new Exception("line is empty string");
		if (! line.startsWith(fuzzificationInitialSubstring)) 
			throw new Exception("line is not a fuzzification definition. line: " +line);
		
		line = line.substring(fuzzificationInitialSubstring.length());
		
		// String line;
		String whiteSpaces        = "[\\s]*";
		String anythingPattern    = "[\\s\\S]*";
		String termNamePattern    = "[0-9a-zA-Z_-]+";
		String predicatePattern   = whiteSpaces + termNamePattern + "[\\(]{1}" + whiteSpaces + 
									termNamePattern + whiteSpaces + "[\\)]{1}" + whiteSpaces;
		// String prologIfPattern    = whiteSpaces + "[:]{1}[-]{1}" + whiteSpaces;
		
		String mainPattern   = "^("+predicatePattern+"){1}[,]{1}("+predicatePattern+"){1}"+"(" + anythingPattern + "){1}$";
		
		if (! line.matches(mainPattern)) throw new Exception("line does not match pattern. line: " + line);

		predDefined       = line.replaceAll(mainPattern, "$1");
		predNecessary     = line.replaceAll(mainPattern, "$2");
		String predOthers = line.replaceAll(mainPattern, "$3");

		msg = "";
		msg += ("\npredDefined: " + predDefined + "\npredNecessary: " + predNecessary + "\npredOthers: " + predOthers);
		
		while((predOthers != null) && (predOthers.startsWith(" "))) predOthers = predOthers.substring(1);
		
		// detect pred owner.
		String ownerPattern = "^("+termNamePattern+"){1}" + "(" + anythingPattern + "){1}$";
		if ((predOthers != null) && (predOthers.startsWith(","))) {
			predOthers = predOthers.substring(1);
			while((predOthers != null) && (predOthers.startsWith(" "))) predOthers = predOthers.substring(1);
			predOwner = predOthers.replaceAll(ownerPattern, "$1");
			predOthers = predOthers.replaceAll(ownerPattern, "$2");
		}
		else {
			predOwner = "";
		}
		
		// detect )
		while((predOthers != null) && (predOthers.startsWith(" "))) predOthers = predOthers.substring(1);
		if ((predOthers != null) && (predOthers.startsWith(")"))) predOthers = predOthers.substring(1);
		
		// detect :-
		while((predOthers != null) && (predOthers.startsWith(" "))) predOthers = predOthers.substring(1);
		if (predOthers.startsWith(":-")) {
			predOthers = predOthers.substring(2);
		}
		
		while((predOthers != null) && (predOthers.startsWith(" "))) predOthers = predOthers.substring(1);

		msg += ("\npredOwner: " + predOwner + "\npredOthers: " + predOthers);
		if ((predOthers == null) || (! predOthers.startsWith(functionInitialSubstring))) {
			LOG.info(msg);
			throw new Exception("line does not contain a function definition. line: " + line);
		}
		predOthers = predOthers.substring(functionInitialSubstring.length());
		
		String floatPattern       = whiteSpaces + "([\\d\\.]+){1}" + whiteSpaces;
		String coordinatesPattern = "[\\(]{1}"+floatPattern+"[,]{1}"+floatPattern+"[\\)]{1}" + 
									"(" + anythingPattern + "){1}$";
		
		FunctionPoint pointCoordinates;
		functionPoints = new ArrayList<FunctionPoint>();
		
		// Identify the function points.
		msg += ("function detected."+"\npredOthers: " + predOthers);
		while((predOthers != null) && (predOthers.startsWith(" "))) predOthers = predOthers.substring(1);
		while ((predOthers != null) && (! ("".equals(predOthers))) && (predOthers.startsWith("("))) {
					
			if (predOthers.matches(coordinatesPattern)) {
				pointCoordinates = new FunctionPoint(	predOthers.replaceAll(coordinatesPattern, "$1"), 
														predOthers.replaceAll(coordinatesPattern, "$2"));
				functionPoints.add(pointCoordinates);
				msg += ("\nPoint coordinates:" + pointCoordinates.toString());
				predOthers = predOthers.replaceAll(coordinatesPattern, "$3");
			}
			while ((predOthers != null) && (predOthers.startsWith(" ") || predOthers.startsWith(","))) {
				predOthers = predOthers.substring(1);
			}
			msg += ("\npredOthers: -" + predOthers + "-");
		}
		if ((predOthers != null) && (predOthers.startsWith("]"))) predOthers = predOthers.substring(1);
		while ((predOthers != null) && (predOthers.startsWith(" "))) predOthers = predOthers.substring(1);
		if ((predOthers != null) && (predOthers.startsWith(")"))) predOthers = predOthers.substring(1);
		while ((predOthers != null) && (predOthers.startsWith(" "))) predOthers = predOthers.substring(1);
		if ((predOthers != null) && (predOthers.startsWith("."))) predOthers = predOthers.substring(1);
		while ((predOthers != null) && (predOthers.startsWith(" "))) predOthers = predOthers.substring(1);

		if ((predOthers != null) && (! ("".equals(predOthers))) && (predOthers.length()>0)) {
			LOG.info(msg);
			LOG.info("Erroneous end: " + predOthers);
			throw new Exception("line does not end as expected. line: " + line);
		}
	}

	public String getPredDefined() {
		return predDefined;
	}

	public String getPredNecessary() {
		return predNecessary;
	}
	
	public String getPredOwner() {
		if ((predOwner == null) || (predOwner != null) && ("".equals(predOwner))) return "default definition";
		else return predOwner;
	}

	public String getFunctionInJavaScript() throws Exception {
		if (functionPoints == null) throw new Exception("functionPoints is null.");
		
		Iterator <FunctionPoint> iterator = functionPoints.iterator();
		FunctionPoint element = null;
		String result = "";
		
		if (functionPoints.size() > 0) {
			result += "new Array(";
		}
		
		if (iterator != null) {
			while (iterator.hasNext()) {
				element = iterator.next();
				result += element.toJavaScript();
				
				if (iterator.hasNext()) result += ", ";
			}
		}
		
		if (functionPoints.size() > 0) {
			result += ")";
		}
		
		return result;
	}
	
	
	
}
