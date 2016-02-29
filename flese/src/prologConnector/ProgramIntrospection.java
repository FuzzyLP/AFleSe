package prologConnector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import filesAndPaths.ProgramFileInfo;

public class ProgramIntrospection {

	private ArrayList<PredicateInfo> predicatesInfos = null;
	private ProgramFileInfo programFileInfo = null;

	public ProgramIntrospection(ProgramFileInfo programFileInfo) {
		this.programFileInfo = programFileInfo;
		predicatesInfos = new ArrayList<PredicateInfo>();
		resetComputedInformation();
	}

	/**
	 * This method resets all computed information.
	 */
	private void resetComputedInformation() {

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void addAnswerInfo(CiaoPrologQueryAnswer answer) {
		resetComputedInformation();

		PredicateInfo predicateInfo;
		try {
			predicateInfo = new PredicateInfo(answer);
			predicatesInfos.add(predicateInfo);
		} catch (PredicateInfoException e) {
			e.printStackTrace();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public ProgramFileInfo getProgramFileInfo() {
		return this.programFileInfo;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public PredicateInfo getPredicateInfo(String predicateName) throws CiaoPrologConnectorException {
		if (predicateName == null) {
			throw new CiaoPrologConnectorException("predicateName cannot be null.");
		}
		if ("".equals(predicateName)) {
			throw new CiaoPrologConnectorException("predicateName cannot be empty string.");
		}

		PredicateInfo predicateInfo = null;
		int i = 0;
		boolean found = false;
		while (i < predicatesInfos.size() && (!found)) {
			predicateInfo = predicatesInfos.get(i);
			if ((predicateInfo != null) && (predicateName.equals(predicateInfo.getPredicateName())))
				found = true;
			else
				i++;
		}
		if (predicateInfo == null) {
			throw new CiaoPrologConnectorException("returned answer cannot be null.");
		}
		return predicateInfo;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public PredicateInfo[] getPredicatesInfosByMoreInfoKey(String key) throws CiaoPrologConnectorException {
		if (key == null) {
			throw new CiaoPrologConnectorException("key cannot be null.");
		}
		if ("".equals(key)) {
			throw new CiaoPrologConnectorException("key cannot be empty string.");
		}
		ArrayList<PredicateInfo> predicatesArrayList = new ArrayList<PredicateInfo>();
		PredicateInfo predicateInfo = null;
		Set<String> keys = null;
		int i = 0;
		boolean valid = false;

		while (i < predicatesInfos.size()) {
			predicateInfo = predicatesInfos.get(i);
			keys = predicateInfo.getPredicateMoreInfoKeys();
			valid = keys.contains(key);
			if (valid) {
				predicatesArrayList.add(predicateInfo);
			}
			i++;
		}

		PredicateInfo[] predicatesArray = predicatesArrayList.toArray(new PredicateInfo[predicatesArrayList.size()]);
		Arrays.sort(predicatesArray);
		
		return predicatesArray;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public PredicateInfo[] getPredicatesInfosByType(String[] type) throws CiaoPrologConnectorException {
		if (type == null) {
			throw new CiaoPrologConnectorException("type cannot be null.");
		}
		if (type.length == 0) {
			throw new CiaoPrologConnectorException("type cannot be of length 0.");
		}
		HashMap<String, PredicateInfo> predicatesList = new HashMap<String, PredicateInfo>();
		PredicateInfo predicateInfo = null;
		int i = 0;

		while (i < predicatesInfos.size()) {
			predicateInfo = predicatesInfos.get(i);
			if (predicateInfo != null) {
				String [][] predicateFullyDefinedTypes = predicateInfo.getFullyDefinedTypes(type);
				if ((predicateFullyDefinedTypes != null) && (predicateFullyDefinedTypes.length > 0)) {
					predicatesList.put(predicateInfo.getPredicateName() + predicateInfo.getPredicateArity(), predicateInfo);
				}
			}
			i++;
		}

		PredicateInfo [] predicatesArray = predicatesList.values().toArray(new PredicateInfo[predicatesList.size()]); 
		Arrays.sort(predicatesArray);
		return predicatesArray;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
