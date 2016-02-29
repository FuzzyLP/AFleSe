package managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;



public class FuzzificationsAlgorithms {
	public static String[][] algo(ArrayList<HashMap<String,String>> a)
	{
		//return averageAlgo();
		return machineLearning(a);
	}
	
	//make an average of the value of the fuzzifications
	public static String[][] averageAlgo(ArrayList<HashMap<String,String>> a)
    {
        //problem cases
        if ((a == null)||(a.size() == 0))
        {
            return new String[0][0];
        }
       
        Set<String> strSet = a.get(0).keySet();
        String[][] resul = new String[strSet.size()][2];
         double avg = 0;
         int i = 0;
        for (String key: strSet)
        {
             avg = 0;
             double v;
            for (HashMap<String, String> entry: a) {
                v = Double.parseDouble(entry.get(key));
                avg += v;
            }
            resul[i][0] = key;
            Double value = new Double(Math.round(avg*1000/a.size())/1000.);
              if (((value*10) % 10) == 0)
              {
              	resul[i][1] = String.valueOf(value.intValue());
              	} else {
              	resul[i][1] = String.valueOf(value);
              	}
            i++;
        }
            return resul;
        }
	
	//make an average of the value of the fuzzifications
	public static String[][] medianAlgo(ArrayList<HashMap<String,String>> a)
    {
        //problem cases
        if ((a == null)||(a.size() == 0))
        {
            return new String[0][0];
        }
        
        Set<String> strSet = a.get(0).keySet();
        String[][] resul = new String[strSet.size()][2];
        
        int i = 0;
        for (String key: strSet)
        {
            List<Double> contents = new ArrayList<Double>();
            double v;
            for (HashMap<String, String> entry: a) 
            {
                v = Double.parseDouble(entry.get(key));
                contents.add(v);
            }
            resul[i][0] = key;
        	Collections.sort(contents, new Comparator<Double>() {
        	    @Override
        	    public int compare(Double c1, Double c2) {
        	        return Double.compare(c1, c2);
        	    }
        	});
        	
        	if(contents.size()%2==1)
        	{
        		int pos = (int) (contents.size()-1)/2;
        		resul[i][1]=contents.get(pos).toString();
        	}
        	else
        	{
        		int pos = (int) (contents.size())/2;
        		Double result = (contents.get(pos) + contents.get(pos-1))/2;
        		resul[i][1]= result.toString();
        	}
        	
            //resul[i][1]=contents.get(Math.round((contents.size()+1)/2)-1).toString();
            i++;
        }
        
		//LOG.info("Default rule updated with: " + resul);
            return resul;
        }
	
	private static int obtainNumberofDivisions( int numberOfPersonalizations, int numberOfDimensions )
	{
		double doubleValue = numberOfPersonalizations / numberOfDimensions;
		int numberOfDivisions = ( ( Double ) doubleValue ).intValue();
		if( numberOfDivisions < 2 )
			return 2;
		else if( numberOfDimensions > 10 )
			return 10;
		else
			return numberOfDivisions;
	}
	
	private static int getSubspaceCoordinate( double value, int numberOfDivisions )
	{
		int i = 1;
		boolean done = false;
		while( !done && i <= numberOfDivisions )
		{
			if( value <= ( double ) ( i / numberOfDivisions ) )
				done = true;
			else
				i++;
		}
		return i;
	}
	
	private static boolean sameValue( int[] element1, int[] element2 )
	{
		if( element1.length != element2.length )
			return false;
		int i = 0;
		while( i < element1.length )
		{
			if( element1[i] != element2[i] )
				return false;
			i++;
		}
		return true;
	}
	
	private static int frequency( List<int[]> lista, int[] element )
	{
		int i = 0;
		int frec = 0;
		while( i < lista.size() )
		{
			if( sameValue( element, lista.get( i ) ) )
				frec++;
			i++;
		}
		return frec;
	}
	
	private static ArrayList<Integer> findtheAreaWithMorePersonalizations( int[][] subspacesSet )
	{
		List<int[]> subspacesList = new ArrayList<int[]>();
		for( int[] subspace : subspacesSet )
		{
			subspacesList.add( subspace );
		}
		int maxZoneFrecuency = 0;
		ArrayList<int[]> selectedZones = new ArrayList<int[]>();
		while( subspacesList.size() != 0 )
		{
			int[] zone = subspacesList.get( 0 );
			int zoneFrecuency = frequency( subspacesList, zone );
			if( zoneFrecuency > maxZoneFrecuency )
			{
				maxZoneFrecuency = zoneFrecuency;
				selectedZones.clear();
				selectedZones.add( subspacesList.get( 0 ) );
			}
			else if( zoneFrecuency == maxZoneFrecuency )
				selectedZones.add( subspacesList.get( 0 ) );
			int i = 0;
			while( i < subspacesList.size() )
			{
				if( sameValue( subspacesList.get( i ), zone ) )
					subspacesList.remove( i );
				else
					i++;
			}
		}
		ArrayList<Integer> positionsOfZonesSelected = new ArrayList<Integer>();
		for( int[] zone: selectedZones )
		{
			int i = 0;
			while( i < subspacesSet.length )
			{
				if( sameValue( subspacesSet[i], zone ) )
					positionsOfZonesSelected.add( i );
				i++;
			}
		}
		return positionsOfZonesSelected;
	}
	
	public static String[][] machineLearning(ArrayList<HashMap<String,String>> a)
    {
		if ( ( a == null ) || ( a.size() == 0 ) )
        {
            return new String[0][0];
        }
		
		int numberOfPersonalizations = a.size();
        int numberOfDimensions = a.get( 0 ).size();
        int numberOfDivisionsPerDimension = obtainNumberofDivisions( numberOfPersonalizations, numberOfDimensions );
        
        int[][] subspacesSet = new int[ numberOfPersonalizations ][ numberOfDimensions ];
        Set<String> domainValuesSet = a.get( 0 ).keySet();
        int dimension = 0;
        for ( String domainValue: domainValuesSet )
        {
            int personalizationNumber = 0;
            for ( HashMap<String, String> personalization: a ) 
            {
                double value = Double.parseDouble( personalization.get( domainValue ) );
                subspacesSet[ personalizationNumber ][ dimension ] = getSubspaceCoordinate( value, numberOfDivisionsPerDimension );
                personalizationNumber++;
            }
            dimension++;
        }
        ArrayList<Integer> positionsOfZonesSelected = findtheAreaWithMorePersonalizations( subspacesSet );
        ArrayList<HashMap<String,String>> selectedPersonalizations = new ArrayList<HashMap<String,String>>();
        for( Integer position : positionsOfZonesSelected )
        {
        	selectedPersonalizations.add( a.get( position ) );
        }
		return medianAlgo( selectedPersonalizations );
    }

	//use this method to test the fuzzification algorithm
	/*
	public static void main(String [ ] args)
	{
	ArrayList<HashMap<String,String>> a = new ArrayList<HashMap<String,String>>();
	HashMap<String,String> h1 = new HashMap<String,String>();
	h1.put("50","0.1");
	h1.put("100","0.4");
	h1.put("150","0.7");
	a.add(h1);
	HashMap<String,String> h2 = new HashMap<String,String>();
	h2.put("50","0.2");
	h2.put("100","0.4");
	h2.put("150","0.6");
	a.add(h2);
	HashMap<String,String> h3 = new HashMap<String,String>();
	h3.put("50","0.3");
	h3.put("100","0.4");
	h3.put("150","0.5");
	a.add(h3);
	HashMap<String,String> h4 = new HashMap<String,String>();
	h4.put("50","0.4");
	h4.put("100","0.4");
	h4.put("150","0.4");
	a.add(h4);
	HashMap<String,String> h5 = new HashMap<String,String>();
	h5.put("50","0.5");
	h5.put("100","0.4");
	h5.put("150","0.3");
	a.add(h5);
	String[][] tab = medianAlgo(a);
	System.out.println(tab[0][0]);
	System.out.println(tab[0][1]);
	System.out.println(tab[1][0]);
	System.out.println(tab[1][1]);
	System.out.println(tab[2][0]);
	System.out.println(tab[2][1]);
	}*/
	
	
}
