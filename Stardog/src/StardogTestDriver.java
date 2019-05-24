import java.util.ArrayList;

import org.eclipse.rdf4j.repository.sail.SailRepository;

import com.complexible.stardog.Stardog;
import com.complexible.stardog.sesame.StardogRepository;


public class StardogTestDriver {
	
	public static ArrayList<String> runTest(String dbName, String sourceRDFFile, int queryIterations, String storagePath, int warmupIterations,String countFlag,String mode)
	{		
		
		ArrayList<String> results = new ArrayList<String>();
				
		results.add("++++Stardog SNARL Results++++");
						
		SPARQLQueryWarmUpSet warmupSet = new SPARQLQueryWarmUpSet();
		for (int r=1;r<=warmupIterations;r++) {
			System.out.println();			
			System.out.println(" ++ Warm Up Set iteration No " + r );
			for(int i=0;i<warmupSet.getQueryCount();i++)
			{
				SPARQLQuerySetMember queryItem = warmupSet.getItem(i);
				System.out.println("     " + queryItem.getQueryName());
				StardogUtils.runSPARQL(dbName, queryItem.getQueryName(), queryItem.getQueryString(),false,null);
			}	
		}
		
		//SPARQLQuerySet mySet = new SPARQLQuerySet();
		//SPARQLQueryGeographicaSet mySet = new SPARQLQueryGeographicaSet();
		
		ISPARQLQuerySet mySet;
		
		if(mode.toLowerCase().equals("geographica")) {
			mySet = new SPARQLQueryGeographicaSet();
		}
		else {			
			mySet = new SPARQLQuerySet();
		}
		
		long  runTimes[][] = new long[mySet.getQueryCount()][queryIterations];
		long  runResultCount[] = new long[mySet.getQueryCount()];
				
		for (int r=1;r<=queryIterations;r++) {
						
			System.out.println();			
			System.out.println(" ++ iteration No " + r );
			for(int i=0;i<mySet.getQueryCount();i++)
			{
				SPARQLQuerySetMember queryItem = mySet.getItem(i);
				System.out.println("     " + queryItem.getQueryName());
				
				if(r==1 && countFlag.equalsIgnoreCase("on")) {
					Pair p = StardogUtils.runSPARQL(dbName, queryItem.getQueryName(), queryItem.getQueryString(),true,storagePath);
					runTimes[i][r-1] = p.getTime();
					runResultCount[i] = p.getCount();
					}
				else {
					Pair p = StardogUtils.runSPARQL(dbName, queryItem.getQueryName(), queryItem.getQueryString(),false,null);
					runTimes[i][r-1] = p.getTime();					
				}
			}			
		}
		
		for(int i=0;i<mySet.getQueryCount();i++) {
			SPARQLQuerySetMember queryItem = mySet.getItem(i);
			
			String strResult = "";
			double sumMilliSecAfter20Iterations = 0;
			for(int j=0;j<queryIterations;j++) {
				strResult = strResult + runTimes[i][j] + ",";
				if(j>19) {
					//sumMilliSecAfter20Iterations += (runTimes[i][j]/1000000.0);
					sumMilliSecAfter20Iterations += (runTimes[i][j]);
				}
			}		
//			if(queryIterations>20) {
//				double avgMilliSecAfter20Iterations = sumMilliSecAfter20Iterations/(queryIterations-20);
//				strResult = queryItem.getQueryName() + " :: Count : " + runResultCount[i] + 
//						    ", Average excluding first 20 (MS) : " + avgMilliSecAfter20Iterations + ", Timings (NS) : " + "" + strResult;
//			}
//			else{
//				strResult = queryItem.getQueryName() + " :: Count = " + runResultCount[i] + " Timings (NS) : " + "" + strResult;	
//			}
//			results.add(strResult);
			if(queryIterations>20) {
				double avgMilliSecAfter20Iterations = sumMilliSecAfter20Iterations/(queryIterations-20);
				if(countFlag.equalsIgnoreCase("on")) {
					strResult = queryItem.getQueryName() + " :: Count : " + runResultCount[i] + 
						    ", Average excluding first 20 (Sec) : " + (avgMilliSecAfter20Iterations/1000) + ", Timings (Milli Sec) : " + "" + strResult;
				}
				else
				{
					strResult = queryItem.getQueryName() + " Average excluding first 20 (Sec) : " + (avgMilliSecAfter20Iterations/1000) + ", Timings (Milli Sec) : " + "" + strResult;
				}
			}
			else{
				if(countFlag.equalsIgnoreCase("on")) {
					strResult = queryItem.getQueryName() + " :: Count = " + runResultCount[i] + " Timings (Milli Sec) : " + "" + strResult;	
				}
				else
				{
					strResult = queryItem.getQueryName() + " Timings (Milli Sec) : " + "" + strResult;
				}
			}
			results.add(strResult);
		}
		
		return(results);
	}
	
	public static ArrayList<String> runRDF4JTest(String dbName, String sourceRDFFile, int queryIterations, String storagePath,String countFlag,String mode)
	{		
		ArrayList<String> results = new ArrayList<String>();
				
		results.add("++++Stardog Instance RDF4J Results++++");
		StardogRepository repo = StardogUtils.createRepository(dbName);
						
		//SPARQLQuerySet mySet = new SPARQLQuerySet();
		//SPARQLQueryGeographicaSet mySet = new SPARQLQueryGeographicaSet();
		
		ISPARQLQuerySet mySet;
		
		if(mode.toLowerCase().equals("geographica")) {
			mySet = new SPARQLQueryGeographicaSet();
		}
		else {			
			mySet = new SPARQLQuerySet();
		}
		
		long  runTimes[][] = new long[mySet.getQueryCount()][queryIterations];
		long  runResultCount[] = new long[mySet.getQueryCount()];
				
		for (int r=1;r<=queryIterations;r++) {
						
			System.out.println();			
			System.out.println(" ++ iteration No " + r );
			for(int i=0;i<mySet.getQueryCount();i++)
			{
				SPARQLQuerySetMember queryItem = mySet.getItem(i);
				System.out.println("     " + queryItem.getQueryName());
				
				if(r==1 && countFlag.equalsIgnoreCase("on")) {
					Pair p = StardogUtils.runSPARQLOnRepo(repo, queryItem.getQueryName(), queryItem.getQueryString(),true,storagePath);
					runTimes[i][r-1] = p.getTime();
					runResultCount[i] = p.getCount();
					}
				else {
					Pair p = StardogUtils.runSPARQLOnRepo(repo, queryItem.getQueryName(), queryItem.getQueryString(),false,null);
					runTimes[i][r-1] = p.getTime();					
				}
			}			
		}
		
		for(int i=0;i<mySet.getQueryCount();i++) {
			SPARQLQuerySetMember queryItem = mySet.getItem(i);
			
			String strResult = "";
			double sumMilliSecAfter20Iterations = 0;
			for(int j=0;j<queryIterations;j++) {
				strResult = strResult + runTimes[i][j] + ",";
				if(j>19) {
					//sumMilliSecAfter20Iterations += (runTimes[i][j]/1000000.0);
					sumMilliSecAfter20Iterations += (runTimes[i][j]);
				}
			}		
//			if(queryIterations>20) {
//				double avgMilliSecAfter20Iterations = sumMilliSecAfter20Iterations/(queryIterations-20);
//				strResult = queryItem.getQueryName() + " :: Count : " + runResultCount[i] + 
//						    ", Average excluding first 20 (MS) : " + avgMilliSecAfter20Iterations + ", Timings (NS) : " + "" + strResult;
//			}
//			else{
//				strResult = queryItem.getQueryName() + " :: Count = " + runResultCount[i] + " Timings (NS) : " + "" + strResult;	
//			}
//			results.add(strResult);
			if(queryIterations>20) {
				double avgMilliSecAfter20Iterations = sumMilliSecAfter20Iterations/(queryIterations-20);
				if(countFlag.equalsIgnoreCase("on")) {
					strResult = queryItem.getQueryName() + " :: Count : " + runResultCount[i] + 
						    ", Average excluding first 20 (Sec) : " + (avgMilliSecAfter20Iterations/1000) + ", Timings (Milli Sec) : " + "" + strResult;
				}
				else
				{
					strResult = queryItem.getQueryName() + " Average excluding first 20 (Sec) : " + (avgMilliSecAfter20Iterations/1000) + ", Timings (Milli Sec) : " + "" + strResult;
				}
			}
			else{
				if(countFlag.equalsIgnoreCase("on")) {
					strResult = queryItem.getQueryName() + " :: Count = " + runResultCount[i] + " Timings (Milli Sec) : " + "" + strResult;	
				}
				else
				{
					strResult = queryItem.getQueryName() + " Timings (Milli Sec) : " + "" + strResult;
				}
			}
			results.add(strResult);
		}
	
		repo.shutDown();
		return(results);
	}

}
