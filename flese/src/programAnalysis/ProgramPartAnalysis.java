package programAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import auxiliar.Dates;
import constants.KConstants;

public class ProgramPartAnalysis {
	final Log LOG = LogFactory.getLog(ProgramPartAnalysis.class);

	private ArrayList<String> programSubPartLines = null;
	private ArrayList<String> programSubPartComments = null;

	private String programSubPartLineAcc = null;
	private String head = null;
	private String predDefined = null;
	private String predNecessary = null;
	private String body = null;
	private String fuzzyBody = null;
	private float value = -1;
	private HashMap<String, String> functionPoints = null;
	private String ruleBody = null;
	private String ruleAggregator = null;
	private float defaults_to = -1;
	private String if_condition = null;
	private String with_credibility = null;
	private String only_for_user = null;
	private boolean partIsIncomplete = false;

	private static final String whiteSpacesPattern = "[\\s]*";
	// private static final String anythingPattern = "[\\s\\S]*";
	private static final String termNamePattern = "([0-9a-zA-Z_-]+){1}";
	private static final String predicatePattern = whiteSpacesPattern + termNamePattern + "[\\(]{1}" + whiteSpacesPattern + termNamePattern
			+ whiteSpacesPattern + "[\\)]{1}" + whiteSpacesPattern;

	ProgramPartAnalysis() {
		this.programSubPartLineAcc = "";
		this.programSubPartLines = new ArrayList<String>();
		this.programSubPartComments = new ArrayList<String>();
	}

	public String parse(String programLineIn) throws Exception {
		LOG.info("programLineIn: " + programLineIn);
		if (programLineIn == null)
			throw new Exception("program line is null");
		String programSubPartLine = null;

		// If there is a comment, save it.
		int index = programLineIn.indexOf("%");
		if (index > -1) {
			programSubPartComments.add(programLineIn.substring(index));
			programLineIn = removeSpacesBeforeAndAfter(programLineIn.substring(0, index));
		} else
			programSubPartComments.add(null);

		// If the line contains more than 1 clause, split it.
		index = programLineIn.indexOf(".");
		if (index + 1 != programLineIn.length()) {
			while ((index > 0) && (!dotDenotesClauseEnd(index, programLineIn))) {
				index = programLineIn.indexOf(".", index + 1);
			}
		}

		if (index > -1) { // We have 1 end of clause delimiter.
			// It contains at least 1 clause.
			programSubPartLine = programLineIn.substring(0, index);
			if ((index + 1) < programLineIn.length())
				programLineIn = programLineIn.substring(index + 1);
			else
				programLineIn = "";
			partIsIncomplete = false;
		} else {
			if ("".equals(programSubPartLineAcc)) { // It is the beggining of a
													// line.
				if ("".equals(programLineIn)) { // The line contains only a
												// comment.
					partIsIncomplete = false;
				} else { // The line contains something but it is incomplete.
					partIsIncomplete = true;
				}
			} else { // We are inside a splitted subPart.
				partIsIncomplete = true;

			}
			// The subpart line is the full line (without comment).
			programSubPartLine = programLineIn;
			programLineIn = "";
		}

		programSubPartLines.add(programSubPartLine);
		programSubPartLineAcc += programSubPartLine;

		head = null;
		if (!partIsIncomplete) {
			parseProgramSubPartLineAcc(programSubPartLineAcc);
		}

		// Avoid problems when the line is formed by just white spaces.
		programLineIn = removeSpacesBeforeAndAfter(programLineIn);
		if ("".equals(programLineIn))
			programLineIn = null;

		return programLineIn;
	}

	private void parseProgramSubPartLineAcc(String programSubPartLineAcc) throws Exception {
		int index;

		String msg = "programSubPartLineAcc: " + programSubPartLineAcc + " \n";

		index = programSubPartLineAcc.indexOf(":-");
		if (index > -1) {
			head = removeSpacesBeforeAndAfter(programSubPartLineAcc.substring(0, index));
			body = removeSpacesBeforeAndAfter(programSubPartLineAcc.substring(index + 2));
			programSubPartLineAcc = "";
		}

		index = programSubPartLineAcc.indexOf(":~");
		if (index > -1) {
			head = removeSpacesBeforeAndAfter(programSubPartLineAcc.substring(0, index));
			fuzzyBody = removeSpacesBeforeAndAfter(programSubPartLineAcc.substring(index + 2));

			parseHead();
			parseFuzzyBody();
			programSubPartLineAcc = "";
		}

		if ((head == null) && (!("".equals(programSubPartLineAcc)))) {
			head = removeSpacesBeforeAndAfter(programSubPartLineAcc);
		}

		msg += " head: " + head;
		msg += " body: " + body;
		msg += " fuzzyBody: " + fuzzyBody + "\n";
		LOG.info(msg);
	}

	private boolean dotDenotesClauseEnd(int index, String programLineIn) throws Exception {
		if (index < 0)
			throw new Exception("Index out of range. index: " + index + " programLineIn: " + programLineIn);
		if (!(index < programLineIn.length()))
			throw new Exception("Index out of range. index: " + index + " programLineIn: " + programLineIn);
		if (programLineIn.charAt(index) != '.')
			throw new Exception("Character at index is not a dot. index: " + index + " programLineIn: " + programLineIn);

		return ((!dotIsDecimalMarker(index, programLineIn)) && (!dotIsPartOfClpqrOperator(index, programLineIn)));
	}

	private boolean dotIsDecimalMarker(int index, String programLineIn) {
		int subStringBegins = index - 1;
		while ((subStringBegins >= 0) && (!isDelimiter(programLineIn.charAt(subStringBegins)))) {
			subStringBegins--;
		}
		if (subStringBegins <= 0)
			subStringBegins = 0;
		if (isDelimiter(programLineIn.charAt(subStringBegins)))
			subStringBegins++;

		int subStringEnds = index + 1;
		while ((subStringEnds < programLineIn.length()) && (!isDelimiter(programLineIn.charAt(subStringEnds)))) { // Problem
																													// is
																													// here
																													// !!!
			subStringEnds++;
		}
		if (subStringEnds >= programLineIn.length())
			subStringEnds = programLineIn.length();

		String subString = programLineIn.substring(subStringBegins, subStringEnds);
		Float number = -1.0f;
		boolean result = false;

		// LOG.info("subString: " + subString);
		if (subString.length() < 3)
			result = false;
		else {
			try {
				number = Float.parseFloat(subString);
				result = true;

			} catch (Exception e) {
				number = -1.0f;
				result = false;
			}
		}
		LOG.info("It is " + result + " that dot is decimal marker. SubString: " + subString + " Number: " + number);
		return result;
	}

	private boolean dotIsPartOfClpqrOperator(int index, String programLineIn) {

		String before = null;
		if (index + 1 < programLineIn.length())
			before = programLineIn.substring(0, index + 1);
		else
			before = programLineIn;
		String after = programLineIn.substring(index);

		boolean result = ((before.endsWith(".=.")) || (before.endsWith(".<>.")) || (before.endsWith(".>.")) || (before.endsWith(".<."))
				|| (before.endsWith(".>=.")) || (before.endsWith(".=<.")) || (after.startsWith(".=.")) || (after.startsWith(".<>."))
				|| (after.startsWith(".>.")) || (after.startsWith(".<.")) || (after.startsWith(".>=.")) || (after.startsWith(".=<.")));

		LOG.info("It is " + result + " that dot is part of CLPQ/R operator. SubString: " + programLineIn);
		return result;
	}

	private boolean isDelimiter(char character) {
		if (character == ' ')
			return true;
		if (character == ',')
			return true;
		if (character == '.')
			return true;
		if (character == ')')
			return true;
		if (character == '(')
			return true;
		if (character == ']')
			return true;
		if (character == '[')
			return true;
		return false;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private String removeSpacesBeforeAndAfter(String input) {
		input = removeSpacesBefore(input);
		input = removeSpacesAfter(input);
		// LOG.info("input: " + input);
		return input;
	}

	private String removeSpacesBefore(String input) {
		if (input == null)
			return input;
		while ((!"".equals(input)) && (input.charAt(0) == ' ')) {
			input = input.substring(1);
		}
		// LOG.info("input: " + input);
		return input;
	}

	private String removeSpacesAfter(String input) {
		if (input == null)
			return input;
		while ((!"".equals(input)) && (input.charAt(input.length() - 1) == ' ')) {
			input = input.substring(0, input.length() - 1);
		}
		// LOG.info("input: " + input);
		return input;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private String removeSimpleCommas(String input) {
		String tmp = null;
		
		while (input.contains("'")) {
			int index = input.indexOf("'");
			tmp = input.substring(0, index) + input.substring(index + 1, input.length());
			input = tmp;
		}
		return input;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * if (programPart.getFunctionAnalized() != null) { if
	 * ((localUserName.equals(fileOwner)) ||
	 * ((programPart.getFunctionAnalized().getPredOwner() != null) &&
	 * ((programPart.getFunctionAnalized().getPredOwner().equals(localUserName))
	 * ||
	 * (programPart.getFunctionAnalized().getPredOwner().equals(DEFAULT_DEFINITION
	 * ))))) { addToFunctionsOrderedList(programPart.getFunctionAnalized()); } }
	 */

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public String[] getLines() {
		String[] lines = new String[programSubPartLines.size()];
		for (int i = 0; i < programSubPartLines.size(); i++) {
			lines[i] = "";
			if ((programSubPartLines.get(i) != null) && (!"".equals(programSubPartLines.get(i)))) {
				lines[i] += programSubPartLines.get(i);
				if (i + 1 >= programSubPartLines.size())
					lines[i] += ".";
			}
			if ((programSubPartComments.get(i) != null) && (!"".equals(programSubPartComments.get(i)))) {
				if ("".equals(lines[i]))
					lines[i] += programSubPartComments.get(i);
				else
					lines[i] += " " + programSubPartComments.get(i);
			}
		}
		return lines;
	}

	public boolean partIsIncomplete() {
		return partIsIncomplete;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void parseHead() throws Exception {
		LOG.info("parseHead. head: " + head);
		if (head == null)
			throw new Exception("head is null.");

		if (!head.matches(predicatePattern)) {
			LOG.info("Head does not match predicate pattern. Head: " + head);
			predDefined = null;
			predNecessary = null;
		} else {
			predDefined = head.replaceAll(predicatePattern, "$1") + "(" + head.replaceAll(predicatePattern, "$2") + ")";
		}

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void parseFuzzyBody() throws Exception {
		LOG.info("parseFuzzyBody. fuzzyBody: " + fuzzyBody);
		if (fuzzyBody == null)
			throw new Exception("fuzzy body is null.");

		fuzzyBody = removeSpacesBeforeAndAfter(fuzzyBody);

		int indexStart = fuzzyBody.indexOf("(");

		if (indexStart == -1)
			throw new Exception("fuzzy body contains no functionality.");

		String functionality = removeSpacesBeforeAndAfter(fuzzyBody.substring(0, indexStart));
		String arguments = removeSpacesBeforeAndAfter(fuzzyBody.substring(indexStart + 1));
		String[] parsed = null;

		LOG.info("\n" + "functionality: " + functionality + "\n" + "arguments: " + arguments);

		// For values.
		if (KConstants.ProgramAnalysis.markerForValue.equals(functionality)) {
			parsed = parseUntilExtraPar(arguments, '(');
			try {
				value = Float.parseFloat(parsed[1]);
			} catch (Exception e) {
				throw new Exception("Erroneous number for value. line: " + arguments);
			}
			arguments = parsed[0];
		}

		// For functions.
		if (KConstants.ProgramAnalysis.markerForFunction.equals(functionality)) {
			parsed = parseUntilExtraPar(arguments, '(');
			arguments = parsed[0];

			parsed = parseUntilComma(parsed[1]);
			predNecessary = parsed[1];

			String functionPointsString = removeSpacesBeforeAndAfter(parsed[0]);
			if (functionPointsString.indexOf("[") != 0) {
				throw new Exception("Erroneous format for function points. Not starting with [.");
			}
			functionPointsString = functionPointsString.substring(1);
			parsed = parseUntilExtraPar(functionPointsString, '[');

			if (!"".equals(removeSpacesBeforeAndAfter(parsed[0]))) {
				throw new Exception("Erroneous format for function points. Not ending with ].");
			}
			parseFunctionPoints(parsed[1]);

		}

		// For rules.
		if (KConstants.ProgramAnalysis.markerForRule.equals(functionality)) {
			parsed = parseUntilExtraPar(arguments, '(');
			arguments = parsed[0];
			ruleAggregator = null;
			ruleBody = parsed[1];

			try {
				parsed = parseUntilComma(parsed[1]);
				ruleAggregator = parsed[1];
				ruleBody = parsed[0];
			} catch (Exception e) {
				LOG.info("ruleBody has no aggregator. ruleBody: " + ruleBody);
			}
		}

		// For defaults.
		if (KConstants.ProgramAnalysis.markerForDefaultsTo.equals(functionality)) {
			parsed = parseUntilExtraPar(arguments, '(');
			try {
				defaults_to = Float.parseFloat(parsed[1]);
			} catch (Exception e) {
				throw new Exception("Erroneous number for defaults_to. line: " + arguments);
			}
			arguments = parsed[0];
		}

		arguments = removeSpacesBeforeAndAfter(arguments);
		LOG.info("arguments: " + arguments);

		while ((arguments != null) && (arguments.length() > 0)) {
			arguments = saveExtraOption(arguments);
			arguments = removeSpacesBeforeAndAfter(arguments);
		}
		LOG.info("arguments: " + arguments);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	void parseFunctionPoints(String input) throws Exception {
		// LOG.info("input: " + input);

		boolean morePoints = true;
		int indexStart, indexMiddle, indexEnd;
		String pointsPairString = "";
		StringBuilder logMsg = new StringBuilder();

		while (morePoints) {
			indexEnd = input.indexOf(")");
			if (indexEnd > -1) {

				pointsPairString = input.substring(0, indexEnd);
				if ((indexEnd + 1) < input.length()) {
					input = input.substring(indexEnd + 1);
				} else
					input = "";
				// LOG.info("pointsPairString: " + pointsPairString);

				indexStart = pointsPairString.indexOf("(");
				if (indexStart > -1) {
					pointsPairString = pointsPairString.substring(indexStart + 1);

					indexMiddle = pointsPairString.indexOf(",");
					if (indexMiddle > -1) {
						String pointX = pointsPairString.substring(0, indexMiddle);
						String pointY = pointsPairString.substring(indexMiddle + 1);

						if (functionPoints == null) {
							functionPoints = new HashMap<String, String>();
						}
						FunctionPoint functionPoint = new FunctionPoint(pointX, pointY);
						// LOG.info("functionPoint " +
						// functionPoint.toString());
						functionPoints.put(functionPoint.getCoordinate1(), functionPoint.getCoordinate2());
						logMsg.append(functionPoint.toString());
					} else
						throw new Exception("no comma between point coordinates.");
				} else
					throw new Exception("no parenthesis starting point coordinates.");
			} else {
				// Finished reading the points sequence.
				morePoints = false;
			}
		}

		LOG.info(logMsg.toString());

		if (!"".equals(removeSpacesBeforeAndAfter(input))) {
			throw new Exception("Not parsed text: " + input);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private String saveExtraOption(String argumentsIn) throws Exception {
		if (argumentsIn == null)
			return argumentsIn;

		LOG.info("arguments: " + argumentsIn);
		String argumentsOut = null;
		String marker = null;
		
		marker = KConstants.ProgramAnalysis.markerForIf;
		if (argumentsIn.startsWith(marker)) {
			argumentsOut = removeSpacesBeforeAndAfter(argumentsIn.substring(marker.length()));
			if (argumentsOut.indexOf("(") == 0)
				argumentsOut = argumentsOut.substring(1);
			String[] parsed = parseUntilExtraPar(argumentsOut, '(');
			if_condition = parsed[1];
			LOG.info("if_condition: " + if_condition);
			argumentsOut = parsed[0];
		}
		
		marker = KConstants.ProgramAnalysis.markerForWithCredibility;
		if (argumentsIn.startsWith(marker)) {
			argumentsOut = removeSpacesBeforeAndAfter(argumentsIn.substring(marker.length()));
			if (argumentsOut.indexOf("(") == 0)
				argumentsOut = argumentsOut.substring(1);
			String[] parsed = parseUntilExtraPar(argumentsOut, '(');
			with_credibility = parsed[1];
			LOG.info("with_credibility: " + with_credibility);
			argumentsOut = parsed[0];
		}
		
		marker = KConstants.ProgramAnalysis.markerForOnlyForUser;
		if (argumentsIn.startsWith(marker)) {
			argumentsOut = removeSpacesBeforeAndAfter(argumentsIn.substring(marker.length()));
			argumentsOut = removeSimpleCommas(argumentsOut);
			for (int i = 0; i < argumentsOut.length(); i++) {
				if (isDelimiter(argumentsOut.charAt(i))) {
					throw new Exception("Not a valid user name: " + argumentsOut);
				}
			}
			only_for_user = argumentsOut;
			LOG.info("only_for_user: " + only_for_user);
			argumentsOut = "";
		}

		if ((!"".equals(argumentsOut)) && (argumentsIn.equals(argumentsOut))) {
			throw new Exception("argumentsIn.equals(argumentsOut)");
		}
		return argumentsOut;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private String[] parseUntilExtraPar(String input, char kind) throws Exception {
		if (input == null)
			throw new Exception("input is null.");
		// if (input == null) return new String [] {input, input};

		if ((kind != '[') && (kind != '('))
			throw new Exception("kind is not valid. kind: " + kind);

		int index = 0;
		int openPars1 = 0;
		int openPars2 = 0;
		char current;

		if (kind == '(')
			openPars1++;
		if (kind == '[')
			openPars2++;

		while ((index < input.length()) && ((openPars1 > 0) || (openPars2 > 0))) {
			current = input.charAt(index);
			if (current == ')')
				openPars1--;
			if (current == '(')
				openPars1++;
			if (current == ']')
				openPars2--;
			if (current == '[')
				openPars2++;
			if ((openPars1 > 0) || (openPars2 > 0))
				index++;
		}

		if (index >= input.length()) {
			throw new Exception("input has mismatched paranthesis. input: " + input);
		} else {
			if ((openPars1 < 0) || (openPars2 < 0)) {
				throw new Exception("input has mismatched paranthesis. input: " + input);
			} else {
				if (index == 0) {
					throw new Exception("input has nothing before parenthesis. input: " + input);
				} else {
					String inputParsed = input.substring(0, index);

					if (index + 1 < input.length()) {
						input = input.substring(index + 1);
					} else {
						input = "";
					}
					return new String[] { input, inputParsed };
				}
			}
		}
	}

	private String[] parseUntilComma(String input) throws Exception {
		if (input == null)
			throw new Exception("input is null.");

		int index = input.indexOf(",");
		if (index < 0) {
			throw new Exception("input has not a comma. input: " + input);
		} else {
			if (index == 0) {
				throw new Exception("input has no string before comma. input: " + input);
			} else {
				String inputParsed = input.substring(0, index);
				if (index + 1 < input.length()) {
					input = input.substring(index + 1);
				} else {
					input = "";
				}
				return new String[] { input, inputParsed };
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public String getKeyForHashMap() {
		return this.getPredDefined() + "_" + this.getPredNecessary() + "_" + this.getPredOwner();
	}

	public void setPredDefined(String predDefined) {
		this.predDefined = predDefined;
	}

	public String getPredDefined() {
		return predDefined;
	}

	public void setPredNecessary(String predNecessary) {
		this.predNecessary = predNecessary;
	}

	public String getPredNecessary() {
		return predNecessary;
	}

	public void setPredOwner(String predOwner) {
		if ((predOwner != null) && (!KConstants.Fuzzifications.DEFAULT_DEFINITION.equals(predOwner)) && (!"".equals(predOwner))) {
			only_for_user = predOwner;
		}
	}

	public String getPredOwner() {
		if ((only_for_user == null) || ("".equals(only_for_user)))
			return KConstants.Fuzzifications.DEFAULT_DEFINITION;
		else
			return only_for_user;
	}

	public String getHead() {
		return head;
	}

	public String getOnly_for_user(){
		return only_for_user;
	}
	
	public String getBody() {
		return body;
	}

	public float getValue() {
		return value;
	}

	public String getRuleBody() {
		return ruleBody;
	}

	public String getRuleAggregator() {
		return ruleAggregator;
	}

	public float getDefaultsTo() {
		return defaults_to;
	}

	public String getIfCondition() {
		return if_condition;
	}

	public String getWithCredibility() {
		return with_credibility;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public boolean isFunction() {
		return (functionPoints != null);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public HashMap<String, String> getFunctionPoints() {
		if (functionPoints == null) {
			return new HashMap<String, String>();
		}
		return functionPoints;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void updateFunction(String[][] params) throws ProgramAnalysisException {
		programSubPartLines = new ArrayList<String>();
		String line = this.predDefined + " :~ function(" + this.predNecessary + ", [";

		functionPoints = new HashMap<String, String>();
		FunctionPoint functionPoint = null;

		for (int i = 0; i < params.length; i++) {
			functionPoint = new FunctionPoint(params[i][0], params[i][1]);
			functionPoints.put(functionPoint.getCoordinate1(), functionPoint.getCoordinate2());
			line += " (" + params[i][0] + ", " + params[i][1] + ")";
			if (i + 1 < params.length)
				line += ",";
		}
		line += " ])";
		if ((only_for_user != null) && (!KConstants.Fuzzifications.DEFAULT_DEFINITION.equals(only_for_user)))
			line += " only_for_user '" + only_for_user + "'";
		programSubPartLines.add(line);

		programSubPartComments = new ArrayList<String>();
		programSubPartComments.add(" % Program line generated by FleSe on " + Dates.getStringOfCurrentDate());

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
