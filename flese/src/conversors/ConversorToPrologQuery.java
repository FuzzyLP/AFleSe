package conversors;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import prologConnector.CiaoPrologConnectorException;
import prologConnector.CiaoPrologProgramIntrospectionQuery;
import prologConnector.CiaoPrologTermInJava;
import prologConnector.PlConnectionEnvelopeException;
import prologConnector.PredicateInfo;
import prologConnector.moreInfo.PredMoreInfoInterface;
import storeHouse.CacheStoreHouseException;
import storeHouse.RequestStoreHouse;
import storeHouse.RequestStoreHouseException;
import CiaoJava.PLAtom;
import CiaoJava.PLFloat;
import CiaoJava.PLInteger;
import CiaoJava.PLStructure;
import CiaoJava.PLTerm;
import CiaoJava.PLVariable;
import constants.KConstants;
import filesAndPaths.FilesAndPathsException;

public class ConversorToPrologQuery {

	final Log LOG = LogFactory.getLog(ConversorToPrologQuery.class);

	public class ConversionInput {
		public int lineIndex = 0;
		public String database = null;
		public String aggregator = null;

		// Parameters to be retrieved, converted and saved:
		// quantifier0, quantifier1, predicate, rfuzzyComputeOperator,
		// rfuzzyComputeValue, aggregator;
		public String quantifier0 = null;
		public String quantifier1 = null;
		public String predicate = null;
		public String operator = null;
		public String value = null;

	}

	public class PrologSubQuery {
		public ConversionInput input = new ConversionInput();

		public PLStructure subQuery = null;
		public String SubQuerySimpleInfoString = null;
		public String SubQueryComplexInfoString = null;
		public PLVariable resultVariable = null;
	}

	private ArrayList<PrologSubQuery> subQueries = null;
	private PrologSubQuery initialSubQuery = null;
	private PLVariable outputVariable = null;
	private String outputVariableName = "Truth Value";
	private PLVariable[] showVariables = null;
	private String[] showVariablesNames = null;
	private PLStructure finalQuery = null;

	private String queryComplexInfoString = null;
	private String querySimpleInfoString = null;
	private CiaoPrologProgramIntrospectionQuery ciaoPrologIntrospectionQuery = null;
	private RequestStoreHouse requestStoreHouse = null;

	public ConversorToPrologQuery(RequestStoreHouse requestStoreHouse) throws QueryConversorException,
			CacheStoreHouseException, FilesAndPathsException, CiaoPrologConnectorException,
			PlConnectionEnvelopeException, FilesAndPathsException, RequestStoreHouseException {

		this.requestStoreHouse = requestStoreHouse;
		ciaoPrologIntrospectionQuery = CiaoPrologProgramIntrospectionQuery.getInstance(requestStoreHouse);

		String queryLinesCounterString = requestStoreHouse
				.getRequestParameter(KConstants.Request.linesCounterParam);

		int queryLinesCounter = Integer.parseInt(queryLinesCounterString);

		String msg = "";

		// Parameters to be retrieved and saved:
		// quantifier0, quantifier1, predicate, rfuzzyComputeOperator,
		// rfuzzyComputeValue, aggregator;

		PrologSubQuery prologSubQuery = new PrologSubQuery();
		ConversionInput input = prologSubQuery.input;
		input.lineIndex = 0;
		input.database = requestStoreHouse.getRequestParameter(KConstants.Request.databaseParam);
		input.aggregator = requestStoreHouse.getRequestParameter(KConstants.Request.aggregatorParam);
		testConversionInput(prologSubQuery);

		for (int i = 0; i < queryLinesCounter; i++) {
			prologSubQuery = new PrologSubQuery();
			input = prologSubQuery.input;
			String lineHead = "queryLine[" + i + "].";

			input.lineIndex = i + 1;
			input.database = requestStoreHouse.getRequestParameter(KConstants.Request.databaseParam);
			input.aggregator = requestStoreHouse.getRequestParameter(KConstants.Request.aggregatorParam);

			input.quantifier0 = requestStoreHouse.getRequestParameter(lineHead
					+ KConstants.Request.negationParam);
			input.quantifier1 = requestStoreHouse.getRequestParameter(lineHead
					+ KConstants.Request.quantifierParam);
			input.predicate = requestStoreHouse.getRequestParameter(lineHead
					+ KConstants.Request.predicateParam);
			input.operator = requestStoreHouse.getRequestParameter(lineHead
					+ KConstants.Request.operatorParam);
			input.value = requestStoreHouse.getRequestParameter(lineHead + KConstants.Request.valueParam);

			testConversionInput(prologSubQuery);
			// We only initialize the list if we really need it.
			if (subQueries == null)
				subQueries = new ArrayList<PrologSubQuery>();
			subQueries.add(prologSubQuery);
		}
		LOG.info(msg);
		queryConvert();
	}

	public void testConversionInput(PrologSubQuery prologSubQuery) throws QueryConversorException,
			RequestStoreHouseException, CiaoPrologConnectorException {
		ConversionInput input = prologSubQuery.input;
		if ("".equals(input.database))
			throw new QueryConversorException("You must say what you are looking for.");

		String msg = "";
		msg += ("\n  fp: " + input.quantifier0 + "(" + input.quantifier1 + "(" + input.predicate + "))");
		msg += ("\n  cp: " + input.predicate + " " + input.operator + " " + input.value);
		msg += ("\n  aggregator: " + input.aggregator);
		msg += ("\n  database: " + input.database);
		LOG.info(msg);

		if (0 == input.lineIndex) {
			subQueryInitialEndTestAndSave(prologSubQuery);
		} else {
			if ("".equals(input.predicate))
				throw new QueryConversorException("You must refine your search.");
			if (1 < input.lineIndex) {
				if ("".equals(input.aggregator))
					throw new QueryConversorException("You must choose an aggregator.");
			}

			if (((!"".equals(input.operator)) || (!"".equals(input.value)))
					&& ((!"".equals(input.quantifier0)) || (!"".equals(input.quantifier1)))) {
				throw new QueryConversorException("We cannot process a so complex query.");
			}

			if ((!"".equals(input.operator)) && (!"".equals(input.value))) {
				subqueryRfuzzyComputeOperatorEndTestAndSave(prologSubQuery);
			} else {
				PredicateInfo predicateInfo = ciaoPrologIntrospectionQuery.getProgramIntrospection()
						.getPredicateInfo(input.predicate);
				String[] type = { input.database, KConstants.PrologTypes.rfuzzy_truth_value_type };
				if (predicateInfo.hasType(type, false)) {
					subqueryFuzzyEndTestAndSave(prologSubQuery);
				} else {
					throw new QueryConversorException(
							"You need to fill all the fields for non-fuzzy queries.");
				}
			}
		}
	}

	private void subQueryInitialEndTestAndSave(PrologSubQuery prologSubQuery) throws QueryConversorException,
			RequestStoreHouseException, CiaoPrologConnectorException {
		ConversionInput input = prologSubQuery.input;
		if (input.database == null) {
			throw new QueryConversorException("No initial predicate for the query.");
		}
		if (showVariables != null) {
			throw new QueryConversorException("You cannot configure twice the initial subquery.");
		}

		PredicateInfo predicateInfo = ciaoPrologIntrospectionQuery.getProgramIntrospection()
				.getPredicateInfo(input.database);
		// SubGoal1: call the typing predicate.
		int predArityInt = predicateInfo.getPredicateArity();
		PLVariable[] plArgsSubGoal1 = new PLVariable[predArityInt];
		for (int i = 0; i < predArityInt; i++) {
			plArgsSubGoal1[i] = new PLVariable();
		}

		showVariables = new PLVariable[plArgsSubGoal1.length + 1];
		showVariables[0] = new PLVariable();
		for (int i = 0; i < plArgsSubGoal1.length; i++) {
			showVariables[i + 1] = plArgsSubGoal1[i];
		}

		PredMoreInfoInterface predMoreInfo = predicateInfo.getPredicateMoreInfoAs("database");
		showVariablesNames = predMoreInfo.generateVariablesNames(input.database);

		PLStructure subGoal1 = new PLStructure(input.database, plArgsSubGoal1);

		// SubGoal2: ensure the input variable always has values from the typing
		// predicate.
		PLTerm[] plArgsSubGoal2 = new PLTerm[2];
		plArgsSubGoal2[0] = showVariables[0];
		plArgsSubGoal2[1] = subGoal1;

		PLStructure subGoal2 = new PLStructure("=", plArgsSubGoal2);

		String localUserName = requestStoreHouse.getSession().getLocalUserInfo().getLocalUserName();
		PLTerm[] argsAssert = new PLTerm[] { new PLAtom(localUserName) };
		PLStructure subGoal3 = new PLStructure("assertLocalUserName", argsAssert);

		initialSubQuery = new PrologSubQuery();
		initialSubQuery.subQuery = new PLStructure(",", new PLTerm[] { subGoal3, subGoal1 });
		initialSubQuery.subQuery = new PLStructure(",", new PLTerm[] { initialSubQuery.subQuery, subGoal2 });
		initialSubQuery.SubQuerySimpleInfoString = "";
		CiaoPrologTermInJava tmpQuery = new CiaoPrologTermInJava(initialSubQuery.subQuery, null);
		initialSubQuery.SubQueryComplexInfoString = tmpQuery.toString();
		initialSubQuery.resultVariable = showVariables[0];
	}

	private void subqueryRfuzzyComputeOperatorEndTestAndSave(PrologSubQuery prologSubQuery)
			throws QueryConversorException, CiaoPrologConnectorException {
		ConversionInput input = prologSubQuery.input;
		PredicateInfo predicateInfo = ciaoPrologIntrospectionQuery.getProgramIntrospection()
				.getPredicateInfo(input.predicate);
		if (predicateInfo.getPredicateArity() != 2) {
			throw new QueryConversorException("Arity of predicate is not 2. Predicate " + input.predicate);
		}

		PLTerm database = new PLAtom(input.database);
		PLVariable resultVar = new PLVariable();
		PLStructure dbValue = new PLStructure(input.predicate, new PLTerm[] { showVariables[0] });
		PLAtom operator = new PLAtom(input.operator);

		boolean isInteger = true;
		int valueInt = 0;
		try {
			valueInt = Integer.parseInt(input.value);
		} catch (Exception e) {
			isInteger = false;
		}

		boolean isDouble = true;
		double valueDouble = 0;
		try {
			valueDouble = Double.parseDouble(input.value);
		} catch (Exception e) {
			isDouble = false;
		}

		PLTerm enteredValue = null;
		if ((!isInteger) && (!isDouble)) {
			enteredValue = new PLAtom(input.value);
		} else {
			if (isInteger) {
				enteredValue = new PLInteger(valueInt);
			} else {
				enteredValue = new PLFloat(valueDouble);
			}
		}

		/*
		 * if ("=~=".equals(tmpRfuzzyComputeOperator)) { value = new PLStructure(tmpPredicate, new
		 * PLTerm[]{value}); }
		 */

		prologSubQuery.subQuery = new PLStructure("rfuzzy_compute", new PLTerm[] { operator, enteredValue,
				dbValue, database, resultVar });
		prologSubQuery.SubQuerySimpleInfoString = " " + input.predicate + "(" + input.database + ")" + " "
				+ input.operator + " " + input.value;
		CiaoPrologTermInJava tmpQuery = new CiaoPrologTermInJava(prologSubQuery.subQuery, null);
		prologSubQuery.SubQueryComplexInfoString = tmpQuery.toString();
		prologSubQuery.resultVariable = resultVar;

	}

	private void subqueryFuzzyEndTestAndSave(PrologSubQuery prologSubQuery) throws QueryConversorException,
			CiaoPrologConnectorException {
		ConversionInput input = prologSubQuery.input;

		PredicateInfo predicateInfo = ciaoPrologIntrospectionQuery.getProgramIntrospection()
				.getPredicateInfo(input.predicate);
		if (predicateInfo.getPredicateArity() != 2) {
			throw new QueryConversorException("Arity of predicate is not 2. Predicate " + input.predicate);
		}

		PLVariable tmpVar1 = new PLVariable();
		PLVariable tmpVar2 = null;
		PLVariable tmpVar3 = null;

		PLStructure subGoal1 = new PLStructure(input.predicate, new PLTerm[] { showVariables[0], tmpVar1 });
		PLStructure subGoal2 = null;
		PLStructure subGoal3 = null;

		if (!"".equals(input.quantifier1)) {
			tmpVar2 = new PLVariable();
			subGoal2 = new PLStructure(input.quantifier1, new PLTerm[] { subGoal1, tmpVar2 });
		} else {
			subGoal2 = subGoal1;
			tmpVar2 = tmpVar1;
		}

		if (!"".equals(input.quantifier0)) {
			tmpVar3 = new PLVariable();
			subGoal3 = new PLStructure(input.quantifier0, new PLTerm[] { subGoal2, tmpVar3 });
		} else {
			subGoal3 = subGoal2;
			tmpVar3 = tmpVar2;
		}

		prologSubQuery.subQuery = subGoal3;
		prologSubQuery.SubQuerySimpleInfoString = " ";
		if (input.quantifier0 != null)
			prologSubQuery.SubQuerySimpleInfoString += input.quantifier0 + "(";
		if (input.quantifier1 != null)
			prologSubQuery.SubQuerySimpleInfoString += input.quantifier1 + "(";
		prologSubQuery.SubQuerySimpleInfoString += input.predicate + "(" + input.database + ")";

		if (input.quantifier0 != null)
			prologSubQuery.SubQuerySimpleInfoString += ")";
		if (input.quantifier0 != null)
			prologSubQuery.SubQuerySimpleInfoString += ")";

		CiaoPrologTermInJava tmpQuery = new CiaoPrologTermInJava(prologSubQuery.subQuery, null);
		prologSubQuery.SubQueryComplexInfoString = tmpQuery.toString();
		prologSubQuery.resultVariable = tmpVar3;

	}

	private void queryConvert() throws QueryConversorException, CiaoPrologConnectorException {

		if (initialSubQuery == null) {
			throw new QueryConversorException("No initial subQuery to convert.");
		}

		querySimpleInfoString = "";
		queryComplexInfoString = "";

		PLVariable currentVar = null;
		PLVariable tmpVar = null;
		PLStructure aggrSubQuery = null;
		CiaoPrologTermInJava tmpQuery = null;

		Iterator<PrologSubQuery> subQueriesIterator = null;
		PrologSubQuery currentSubQuery = null;

		if (subQueries != null) {
			subQueriesIterator = subQueries.iterator();
			while (subQueriesIterator.hasNext()) {
				currentSubQuery = subQueriesIterator.next();

				if (finalQuery == null) {
					finalQuery = currentSubQuery.subQuery;
					currentVar = currentSubQuery.resultVariable;
					querySimpleInfoString += currentSubQuery.SubQuerySimpleInfoString;
					queryComplexInfoString += currentSubQuery.SubQueryComplexInfoString;
				} else {
					if (currentSubQuery.input.aggregator == null) {
						throw new QueryConversorException("No aggregator to combine the truth values.");
					}

					finalQuery = new PLStructure(",", new PLTerm[] { finalQuery, currentSubQuery.subQuery });
					tmpVar = new PLVariable();
					aggrSubQuery = new PLStructure(currentSubQuery.input.aggregator, new PLTerm[] {
							currentVar, currentSubQuery.resultVariable, tmpVar });
					currentVar = tmpVar;
					tmpVar = null;
					finalQuery = new PLStructure(",", new PLTerm[] { finalQuery, aggrSubQuery });

					querySimpleInfoString += ", " + currentSubQuery.SubQuerySimpleInfoString;
					queryComplexInfoString += ", " + currentSubQuery.SubQueryComplexInfoString;
					tmpQuery = new CiaoPrologTermInJava(currentSubQuery.subQuery, null);
					queryComplexInfoString += ", " + tmpQuery.toString();

				}
			}

			// currentVar, after all subQueries, is our output variable !!!
			outputVariable = currentVar;

			if ((currentSubQuery != null) && (currentSubQuery.input.aggregator != null)) {
				querySimpleInfoString = currentSubQuery.input.aggregator + "(" + querySimpleInfoString + ")";
			}
		}

		if (finalQuery == null) {
			outputVariable = new PLVariable();
			PLStructure hackTruthValueQuery = new PLStructure("=", new PLTerm[] { outputVariable,
					new PLAtom("1") });
			finalQuery = new PLStructure(",", new PLTerm[] { initialSubQuery.subQuery, hackTruthValueQuery });
			querySimpleInfoString = initialSubQuery.SubQuerySimpleInfoString;
			queryComplexInfoString = initialSubQuery.SubQueryComplexInfoString;
		} else {
			finalQuery = new PLStructure(",", new PLTerm[] { initialSubQuery.subQuery, finalQuery });
			querySimpleInfoString = initialSubQuery.SubQuerySimpleInfoString + querySimpleInfoString;
			queryComplexInfoString = initialSubQuery.SubQueryComplexInfoString + queryComplexInfoString;

		}

		// rfuzzy_var_truth_value(Var, Condition, Value)
		finalQuery = fixVariables(finalQuery);

		LOG.debug("\n -- Query Conversor ended -- \n");
	}

	private PLStructure fixVariables(PLStructure finalQueryIn) {
		PLVariable tmpVar = null;
		PLVariable auxVar = null;
		PLStructure conversion = null;
		for (int i = 0; i < showVariables.length; i++) {
			tmpVar = new PLVariable();
			auxVar = new PLVariable();
			// rfuzzy_var_truth_value(Var, Condition, Value)
			conversion = new PLStructure("rfuzzy_var_truth_value", new PLTerm[] { showVariables[i], auxVar,
					tmpVar });
			finalQueryIn = new PLStructure(",", new PLTerm[] { finalQueryIn, conversion });
			showVariables[i] = tmpVar;
		}
		if (outputVariable != null) {
			tmpVar = new PLVariable();
			auxVar = new PLVariable();
			conversion = new PLStructure("rfuzzy_var_truth_value", new PLTerm[] { outputVariable, auxVar,
					tmpVar });
			finalQueryIn = new PLStructure(",", new PLTerm[] { finalQueryIn, conversion });
			outputVariable = tmpVar;
		}
		return finalQueryIn;
	}

	public PLVariable[] getListOfVariables() {
		int length = showVariables.length;
		if (outputVariable != null)
			length++;
		PLVariable[] listOfVariables = new PLVariable[length];
		for (int i = 0; i < showVariables.length; i++)
			listOfVariables[i] = showVariables[i];
		if (outputVariable != null)
			listOfVariables[length - 1] = outputVariable;
		return listOfVariables;
	}

	public String[] getListOfNamesForVariables() {
		if (showVariablesNames == null)
			return null;
		int length = showVariablesNames.length;
		if (outputVariable != null)
			length++;
		String[] listOfNamesForVariables = new String[length];
		for (int i = 0; i < showVariablesNames.length; i++)
			listOfNamesForVariables[i] = showVariablesNames[i];
		if (outputVariable != null)
			listOfNamesForVariables[length - 1] = outputVariableName;
		return listOfNamesForVariables;
	}

	public String getQuerySimpleInfoString() {
		return querySimpleInfoString;
	};

	public String getQueryComplexInfoString() {
		return queryComplexInfoString;
	};

	public PLStructure getConvertedQuery() {
		return finalQuery;
	};

}

// ////