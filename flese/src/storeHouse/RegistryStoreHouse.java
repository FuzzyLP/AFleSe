package storeHouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import auxiliar.RegistryEntry;

public class RegistryStoreHouse {
	
	private HashMap<String, RegistryEntry> registryEntries = null;

	public RegistryStoreHouse() {
		resetRegistryEntries();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void resetRegistryEntries() {
		this.registryEntries = null;
	}
	
	public void addRegistryEntry(RegistryEntry registryEntry) {
		if (this.registryEntries == null) {
			this.registryEntries = new HashMap<String, RegistryEntry>();
		}
		this.registryEntries.put(registryEntry.getStringOfDateOut(), registryEntry);
	}
	
	public String [] getRegistryEntries() {
		if (this.registryEntries == null) {
			return new String[0];
		}
		
		Set<String> keysSet = this.registryEntries.keySet();
		String [] keys = keysSet.toArray(new String[keysSet.size()]);
		Arrays.sort(keys, Collections.reverseOrder());
		ArrayList<String> results = new ArrayList<String>();
				
		for (int i=0; i<keys.length; i++) {
			RegistryEntry registryEntry = this.registryEntries.get(keys[i]);
			if (i==0) {
				results.add(registryEntry.getHtmlTableHead());
			}
			results.add(registryEntry.getHtmlTableRow());
		}
		return results.toArray(new String[results.size()]);
	}


}
