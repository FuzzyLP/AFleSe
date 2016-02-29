package managers;

import programAnalysis.ProgramAnalysis;
import programAnalysis.ProgramPartAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

import auxiliar.LocalUserInfo;
import auxiliar.NextStep;
import constants.KConstants;
import constants.KUrls;
import filesAndPaths.ProgramFileInfo;

public class FuzzificationsManager extends AbstractManager {


	public FuzzificationsManager() {
		super();
	}
	

	   
	@Override
	public String methodToInvokeIfMethodRequestedIsNotAvailable() {
		return "list";
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void list() throws Exception {
		ProgramFileInfo programFileInfo = requestStoreHouse.getProgramFileInfo();
		LocalUserInfo localUserInfo = requestStoreHouse.getSession().getLocalUserInfo();
		ProgramAnalysis programAnalized = ProgramAnalysis.getProgramAnalysisClass(programFileInfo);

		String mode = requestStoreHouse.getRequestParameter(KConstants.Request.mode);
		ProgramPartAnalysis[][] programPartAnalysis = programAnalized.getProgramFuzzifications(localUserInfo, "", "", mode);
		resultsStoreHouse.setProgramFileInfo(programFileInfo);
		resultsStoreHouse.setProgramPartAnalysis(programPartAnalysis);

		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Fuzzifications.ListPage, ""));

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void edit() throws Exception {
		ProgramFileInfo programFileInfo = requestStoreHouse.getProgramFileInfo();
		LocalUserInfo localUserInfo = requestStoreHouse.getSession().getLocalUserInfo();
		String predDefined = requestStoreHouse.getRequestParameter(KConstants.Fuzzifications.predDefined);
		String predNecessary = requestStoreHouse.getRequestParameter(KConstants.Fuzzifications.predNecessary);
		String mode = requestStoreHouse.getRequestParameter(KConstants.Request.mode);

		ProgramAnalysis programAnalized = ProgramAnalysis.getProgramAnalysisClass(programFileInfo);
		ProgramPartAnalysis[][] programPartAnalysis = programAnalized.getProgramFuzzifications(localUserInfo, predDefined, predNecessary,
				mode);
		resultsStoreHouse.setProgramFileInfo(programFileInfo);
		resultsStoreHouse.setProgramPartAnalysis(programPartAnalysis);

		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Fuzzifications.EditPage, ""));

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void save() throws Exception {
		ProgramFileInfo programFileInfo = requestStoreHouse.getProgramFileInfo();
		LocalUserInfo localUserInfo = requestStoreHouse.getSession().getLocalUserInfo();
		ProgramAnalysis programAnalized = ProgramAnalysis.getProgramAnalysisClass(programFileInfo);
        //preDefined = expensive(house) - predNecessary price(house) mode advanced - functionDefinition table
		
		
		String predDefined = requestStoreHouse.getRequestParameter(KConstants.Fuzzifications.predDefined);
		String predNecessary = requestStoreHouse.getRequestParameter(KConstants.Fuzzifications.predNecessary);
		String mode = requestStoreHouse.getRequestParameter(KConstants.Request.mode);
		
		String[][] functionDefinition = programAnalized.getFunctionDefinition(requestStoreHouse);
		
		
		int result = programAnalized.updateProgramFile(localUserInfo, predDefined, predNecessary, mode, functionDefinition);

		String msg = "Program file " + programFileInfo.getFileName() + " owned by " + programFileInfo.getFileOwner()
				+ " has NOT been updated. ";
		if (result == 0) {
			msg = "Program file " + programFileInfo.getFileName() + " owned by " + programFileInfo.getFileOwner() + " has been updated. ";
		}

		resultsStoreHouse.addResultMessage(msg);
		
		//getting the data
		
		updateDefaults();
		//programAnalized = ProgramAnalysis.getProgramAnalysisClass(programFileInfo);
		//
		////analyzing the data
		//String[][] resul = FuzzificationsAlgorithms.algo(programAnalized.getAllDefinedFunctionDefinition(predDefined));
		////setting the data
		//mode = KConstants.Request.modeEditingDefault;
		//programAnalized.updateProgramFile(localUserInfo, predDefined, predNecessary, mode, resul);
		setNextStep(new NextStep(KConstants.NextStep.forward_to, KUrls.Fuzzifications.SavePage, ""));
		
	}
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void updateDefaults() throws Exception
	{
		boolean finished=false;
		int [] actualUsercaracs = getUserCaracFromUserName(requestStoreHouse.getSession().getLocalUserInfo().getLocalUserName(), null);
		ProgramFileInfo programFileInfo = requestStoreHouse.getProgramFileInfo();
		LocalUserInfo localUserInfo = requestStoreHouse.getSession().getLocalUserInfo();
		ProgramAnalysis programAnalized = ProgramAnalysis.getProgramAnalysisClass(programFileInfo);
		String mode = KConstants.Request.modeAdvanced;
		ProgramPartAnalysis[][] programPartAnalysis = programAnalized.getAllProgramFuzzifications(localUserInfo, "", "", mode);
		
		String conceptToUpdate = requestStoreHouse.getRequestParameter(KConstants.Fuzzifications.predDefined);
		
		ArrayList<String> PredAnalyzed = new ArrayList<String>();
		while( !finished )
		{
			finished=true;
			for(ProgramPartAnalysis[] concept: programPartAnalysis)
			{
				if( conceptToUpdate.length() == 0 || conceptToUpdate.equals( concept[ 0 ].getPredDefined() ) )
				{
					if( actualUsercaracs == null || isAllZeros( actualUsercaracs ) )
					{
						String conceptName = concept[0].getPredDefined();
						if( !PredAnalyzed.contains( conceptName ) && concept.length != 1 )
						{
							String relatedConceptName = concept[ 0 ].getPredNecessary();
							String[][] newDefaultDefinition = FuzzificationsAlgorithms.algo( programAnalized.getAllDefinedFunctionDefinition( conceptName ) );
							mode = KConstants.Request.modeEditingDefault;
							programAnalized.updateProgramFile( localUserInfo, conceptName, relatedConceptName, mode, newDefaultDefinition );
							//update data with the new definition
							programAnalized = ProgramAnalysis.getProgramAnalysisClass( programFileInfo );
							mode = KConstants.Request.modeAdvanced;
							programPartAnalysis = programAnalized.getAllProgramFuzzifications( localUserInfo, "", "", mode );
							if( conceptToUpdate.length() == 0 )
							{
								PredAnalyzed.add( conceptName );
								finished = false;
							}
							break;
						}
					}
					else if( concept.length != 1 )
					{
						String conceptName = concept[0].getPredDefined();
						if( !PredAnalyzed.contains( conceptName ) )
						{
							ArrayList<int[]> caracsFromAllUsers = new ArrayList<int[]>();
							for( ProgramPartAnalysis personalization : concept )
							{
								String ownerName = personalization.getPredOwner();
								if( ownerName != "default definition" )
								{
									int[] caracteristics = getUserCaracFromUserName( ownerName, actualUsercaracs );
									if( caracteristics != null )
										caracsFromAllUsers.add( caracteristics );	
								}
							}
							ArrayList<int[]> usersCaracsOfInterest = kMeansAlgorithm( caracsFromAllUsers, actualUsercaracs );
							
							String relatedConceptName = concept[0].getPredNecessary();
							ArrayList<HashMap<String,String>> PersonalizationsToUse = getAllDefinedFunctionDefinition( concept, usersCaracsOfInterest, actualUsercaracs );
							String[][] newDefaultDefinition = null;
							if( PersonalizationsToUse.size() != 0)
								newDefaultDefinition = FuzzificationsAlgorithms.algo( PersonalizationsToUse );
							else
								newDefaultDefinition = FuzzificationsAlgorithms.algo( programAnalized.getAllDefinedFunctionDefinition( conceptName ) );
							//update data with the new definition
							mode = KConstants.Request.modeEditingDefault;
							programAnalized.updateProgramFile(localUserInfo, conceptName, relatedConceptName, mode, newDefaultDefinition);
							programAnalized = ProgramAnalysis.getProgramAnalysisClass(programFileInfo);
							mode = KConstants.Request.modeAdvanced;;
							programPartAnalysis = programAnalized.getAllProgramFuzzifications(localUserInfo, "", "", mode);
							if( conceptToUpdate.length() == 0)
							{
								PredAnalyzed.add( conceptName );
								finished = false;
							}
							break;
						}
					}
					else
					{
						if( conceptToUpdate.length() == 0 )
						{
							PredAnalyzed.add( concept[0].getPredDefined() );
						}
						
					}
				}
			}
		}
		
	}
	
	public ArrayList<HashMap<String,String>> getAllDefinedFunctionDefinition( ProgramPartAnalysis[] concept, ArrayList<int[]> caracsOfInterest, int[] caracsActualUser )
	{
		ArrayList<HashMap<String,String>> personalizationSelected = new ArrayList<HashMap<String,String>>();
		for (int j = 0 ; j < concept.length ; j++)
		{
			//Taking out the default rule
			int[] caracs = getUserCaracFromUserName( concept[ j ].getPredOwner(), caracsActualUser );
			if( concept[ j ].getOnly_for_user() != null && caracs != null && ArrayListContainArray( caracsOfInterest, caracs ) )
			{
				HashMap<String,String> personalization = concept[ j ].getFunctionPoints();
				personalizationSelected.add( personalization );
			}
		}
		return personalizationSelected;
	}
	
	private boolean ArrayListContainArray( ArrayList<int[]> list, int[] array)
	{
		for( int[] arrayI : list )
		{
			if( arrayI.length != array.length )
				continue;
			boolean found = true;
			for( int i = 0; i < arrayI.length; i++ )
			{
				if( arrayI[ i ] != array[ i ] )
				{
					found = false;
					break;
				}
			}
			if( found )
				return found;
		}
		return false;
	}
	
	private boolean isAllZeros( int[] list )
	{
		for( int value : list )
		{
			if( value != 0 )
				return false;
		}
		return true;
	}
	
	public ArrayList<int[]> kMeansAlgorithm(ArrayList<int[]> caracsFromAllUsers, int[] caracsActualUser)
	{
		boolean finished = false;
		int MaxNumberOfIterations = 10;
		ArrayList<double[]> representativesCaracs = new ArrayList<double[]>();
		double[] representative1 = { 1, 2 };
		double[] representative2 = { 0, 6 };
		representativesCaracs.add( representative1 );
		representativesCaracs.add( representative2 );
		
		for( int i = 0; !finished && i < MaxNumberOfIterations; i++ )
		{
			int[] indexesOfUsersGroups = new int[ caracsFromAllUsers.size() ];
			int j = 0;
			for( int[] personalization : caracsFromAllUsers)
			{
				indexesOfUsersGroups[ j ] = getGroupIndex( representativesCaracs, personalization );
				j++;
			}
			double[][] newRepresentants = new double[ representativesCaracs.size() ][ representativesCaracs.get( 0 ).length ];
			int [] AmountOfValuesForMeans = new int[ representativesCaracs.size() ];
			for( int k = 0; k < indexesOfUsersGroups.length; k++ )
			{
				newRepresentants[ indexesOfUsersGroups[ k ] ] = addPersonalizaionToCalculateNewRepresentation(newRepresentants[indexesOfUsersGroups[k]] ,caracsFromAllUsers.get(k), AmountOfValuesForMeans[indexesOfUsersGroups[k]] );
				AmountOfValuesForMeans[ indexesOfUsersGroups[ k ] ]++;
			}
			finished = true;
			for( int m = 0; m < representativesCaracs.size(); m++ )
			{
				if( !DoubleArrayEqual( representativesCaracs.get( m ), newRepresentants[ m ] ) && AmountOfValuesForMeans[m] != 0 )
				{
					representativesCaracs.set( m, newRepresentants[ m ] );
					if( finished )
						finished = false;
				}
			}
				
		}
		int[] indexesOfUsersGroups = new int[ caracsFromAllUsers.size() ];
		int j = 0;
		for( int[] userCaracs : caracsFromAllUsers )
		{
			indexesOfUsersGroups[j] = getGroupIndex( representativesCaracs, userCaracs );
			j++;
		}
		int actualUserGroupIndex = getGroupIndex( representativesCaracs, caracsActualUser );
		ArrayList<int[]> result = new ArrayList<int[]>();
		for( int k =0; k < indexesOfUsersGroups.length; k++ )
		{
			if( indexesOfUsersGroups[ k ] == actualUserGroupIndex )
				result.add( caracsFromAllUsers.get( k ) );		
		}
		return result;
	}
	
	private boolean DoubleArrayEqual( double[] caracs1, double[] caracs2 )
	{
		if( caracs1.length != caracs2.length )
			return false;
		for( int i = 0; i < caracs1.length; i++)
		{
			if( caracs1[ i ] != caracs2[ i ] )
				return false;
		}
		return true;
	}
	
	private double[] addPersonalizaionToCalculateNewRepresentation( double[] mean, int[] caracs, int amountOfValuesForMean )
	{
		for( int i = 0; i < mean.length; i++)
		{
			mean[ i ] = ( ( mean[ i ] * ( double ) amountOfValuesForMean ) + ( double ) caracs[ i ] ) / ( double )( amountOfValuesForMean + 1 );
		}
		return mean;
	}
	
	private int getGroupIndex( ArrayList<double[]> representants, int[] personalization )
	{
		ArrayList<Double> distances = new ArrayList<Double>();
		for( double[] represent : representants )
		{
			distances.add( distanceBetween( represent, personalization ) );
		}
		int result = -1;
		Double Value = ( double ) -1;
		for( Double distance: distances )
		{
			if( Value == ( double )-1 || distance < Value )
			{
				Value = distance;
				result = distances.indexOf( distance );
			}
		}
		return result;
	}
	
	private Double distanceBetween( double[] representant, int[] personalization )
	{
		Double result = ( double ) 0;
		for( int i = 0; i < personalization.length; i++ )
		{
			result = result + ( ( ( double ) personalization[ i ] ) - representant[ i ] ) * ( ( ( double ) personalization[ i ] ) - representant[ i ] );
		}
		result = Math.sqrt( result );
		return result;
	}
	
	public int[] getUserCaracFromUserName( String UserName, int[] comparableCaracs )
	{
		ArrayList<Integer> caracsInt = new ArrayList<Integer>();
		int i = UserName.length() - 1;
		while( i >= 0)
		{
			if( UserName.charAt( i ) != '_' && !Character.isDigit( UserName.charAt( i ) ) )
				break;
			i--;
		}
		i = i + 2;
		String carac = "";
		while(i <= UserName.length())
		{
			if( i == UserName.length() || UserName.charAt( i ) == '_' )
			{
				caracsInt.add( Integer.parseInt( carac ) );
				carac = "";
			}
			else if(Character.isDigit(UserName.charAt(i)))
			{
				carac = carac+UserName.charAt(i);
			}
			i++;
		}
		int[] results = new int[caracsInt.size()];
		int j=0;
		for(Integer value:caracsInt)
		{
			results[j] = value;
			j++;
		}
		if( comparableCaracs == null )
			return results;
		else
		{
			for( int k = 0; k < results.length; k++ )
			{
				if( comparableCaracs[k] == 0 )
					results[k] = 0;
				else if( results[k] == 0 )
					return null;
			}
			return results;
		}
			
	}

}
