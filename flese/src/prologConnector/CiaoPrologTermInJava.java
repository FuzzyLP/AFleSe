package prologConnector;

import java.text.DecimalFormat;
import java.util.HashMap;

import CiaoJava.PLAtom;
import CiaoJava.PLFloat;
import CiaoJava.PLInteger;
import CiaoJava.PLList;
import CiaoJava.PLString;
import CiaoJava.PLStructure;
import CiaoJava.PLTerm;
import CiaoJava.PLVariable;

public class CiaoPrologTermInJava {

	private String singleAnswerTerm = null; // It can be . (list) or ,
											// (structure).
	private CiaoPrologTermInJava[] compositeAnswerTerm = null;

	private PLTerm prologQueryAnswer = null;
	private String creationMsgs = "";

	public CiaoPrologTermInJava(PLTerm term, PLTerm prologQueryAnswer) throws CiaoPrologConnectorException {
		creationMsgs += " ";
		conversion(term, prologQueryAnswer);
	}

	private void conversion(PLTerm term, PLTerm prologQueryAnswer) throws CiaoPrologConnectorException {
		if (term == null)
			throw new CiaoPrologConnectorException("term is null.");
		// Log ...
		creationMsgs += "\nTerm: " + term.toString() + " ";
		// For variables.
		if (term.isVariable()) {
			PLVariable prologVariable = (PLVariable) term;
			creationMsgs += "is a variable ";
			PLTerm binding = prologVariable.getBinding();
			if (binding == null) {
				creationMsgs += "without binding. ";
				singleAnswerTerm = "-variable-";
			} else {
				creationMsgs += "with binding: " + binding.toString();
				conversion(binding, prologQueryAnswer);
			}
		}
		// For lists.
		if (term.isList()) {
			creationMsgs += "is a list. ";
			singleAnswerTerm = ".";
			PLList prologList = (PLList) term;
			int listLength = prologList.length();
			if (listLength > 0) {
				compositeAnswerTerm = new CiaoPrologTermInJava[listLength];
				for (int i = 0; i < listLength; i++) {
					compositeAnswerTerm[i] = new CiaoPrologTermInJava(prologList.getHead(), prologQueryAnswer);
					creationMsgs += compositeAnswerTerm[i].getCreationMsgs();
					if ((prologList.getTail() != null) && (prologList.getTail().isList())) {
						prologList = (PLList) prologList.getTail();
					} else
						creationMsgs += "\nRemaining list: " + prologList.getTail().toString() + " ";
				}
			}
		}
		// For integer
		if (term.isInteger()) {
			creationMsgs += " is an integer. ";
			PLInteger prologInteger = (PLInteger) term;
			singleAnswerTerm = prologInteger.toString();
		}
		// For float
		if (term.isFloat()) {
			creationMsgs += " is a float. ";
			PLFloat prologFloat = (PLFloat) term;
			double doubleValue = prologFloat.getValue();
			DecimalFormat df = new DecimalFormat("#.##");
			singleAnswerTerm = df.format(doubleValue);
		}
		// For atom ???
		if (term.isAtom()) {
			creationMsgs += " is an atom. ";
			PLAtom prologAtom = (PLAtom) term;
			singleAnswerTerm = prologAtom.toString();
		}

		// For structure
		if (term.isStructure()) {
			creationMsgs += " is an structure. ";
			PLStructure prologStructure = (PLStructure) term;
			String functor = prologStructure.getFunctor();
			singleAnswerTerm = functor; // It will be , when no explicit
										// functor.

			int listLength = prologStructure.getArity();
			if (listLength > 0) {
				compositeAnswerTerm = new CiaoPrologTermInJava[listLength];

				for (int i = 0; i < listLength; i++) {
					compositeAnswerTerm[i] = new CiaoPrologTermInJava(prologStructure.getArg(i), prologQueryAnswer);
					creationMsgs += compositeAnswerTerm[i].getCreationMsgs();
				}
			}
			// singleAnswerTerm = prologStructure.toString();
		}
		// For string
		if (term.isString()) {
			creationMsgs += " is an string. ";
			PLString prologString = (PLString) term;
			singleAnswerTerm = prologString.toString();
		}

		if (!(term.isVariable() || term.isList() || term.isInteger() || term.isStructure() || term.isString() || term.isAtom() || term
				.isFloat())) {
			creationMsgs += " -- what the hell is this??? ";
			singleAnswerTerm = term.toString();
		}
	}

	public String toString() {
		String retVal = "";
		if (singleAnswerTerm == null)
			return null;
		if ((!",".equals(singleAnswerTerm)) && (!".".equals(singleAnswerTerm)))
			retVal += singleAnswerTerm;
		if (compositeAnswerTerm != null) {
			if (singleAnswerTerm.equals("."))
				retVal += "[";
			else
				retVal += "(";

			for (int i = 0; i < compositeAnswerTerm.length; i++) {
				retVal += compositeAnswerTerm[i].toString();
				if ((i + 1) < compositeAnswerTerm.length)
					retVal += ", ";
			}

			if (singleAnswerTerm.equals("."))
				retVal += "]";
			else
				retVal += ")";
		}
		retVal += "";
		return retVal;
	}

	public PLTerm getprologQueryAnswer() {
		return prologQueryAnswer;
	}

	public String getCreationMsgs() {
		return creationMsgs;
	}

	/**
	 * Tests if the answerTerm is a list. This happens only if (1) the functor
	 * is '.' and has arguments or if (2) the functor is '[]' and has no
	 * arguments.
	 * 
	 * @return = ((singleAnswerTerm == null) && (compositeAnswerTerm != null))
	 */
	public boolean isList() {
		return (((singleAnswerTerm != null) && (compositeAnswerTerm != null) && (".".equals(singleAnswerTerm))) || ((singleAnswerTerm != null)
				&& (compositeAnswerTerm == null) && ("[]".equals(singleAnswerTerm))));
	}

	/**
	 * Tests if the answerTerm is a structure. This happens only if the functor
	 * is ',' and has arguments.
	 * 
	 * @return = ((singleAnswerTerm == null) && (compositeAnswerTerm != null))
	 */
	public boolean isArray() {
		return ((singleAnswerTerm != null) && (compositeAnswerTerm != null) && (",".equals(singleAnswerTerm)));
	}

	public int length() {
		if ((!isList()) && (!isArray()))
			return 0;
		else {
			if (compositeAnswerTerm != null) {
				return compositeAnswerTerm.length;
			} else {
				return 0;
			}
		}
	}

	public CiaoPrologTermInJava atPosition(int i) {
		if (((isList()) || (isArray())) && (i >= 0) && (i < length())) {
			return compositeAnswerTerm[i];
		} else
			return null;
	}

	public String toJavaScript() {
		String retVal = "";

		if (singleAnswerTerm == null)
			return null;
		if (compositeAnswerTerm == null) {
			if (singleAnswerTerm.equals("[]")) {
				retVal += "new Array()";
			} else
				retVal += "\'" + singleAnswerTerm + "\'";
		} else {
			if ((singleAnswerTerm.equals(".")) || (singleAnswerTerm.equals(","))) {
				retVal += "new Array("; // A list
			} else
				retVal += "("; // A structure

			for (int i = 0; i < compositeAnswerTerm.length; i++) {
				retVal += compositeAnswerTerm[i].toJavaScript();
				if (i + 1 < compositeAnswerTerm.length)
					retVal += ",";
			}
			retVal += ")";
		}
		return retVal;
	}

	public HashMap<String, String> toHashMap() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		if (!isList())
			return hashMap;
		for (int i = 0; i < length(); i++) {
			CiaoPrologTermInJava term = atPosition(i);
			if ((term != null) && (term.isList()) && (term.length() >= 2)) {
				String key = term.atPosition(0).toString();
				String value = term.atPosition(1).toString();
				hashMap.put(key, value);
			}
		}
		return hashMap;
	}
}

// //////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////
