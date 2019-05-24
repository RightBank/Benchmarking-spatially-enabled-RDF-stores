import java.util.ArrayList;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;

public class GraphDBTestDriver {
	private static RepositoryManager repoManager = null;
	private static Repository repo = null;
	
	public static long initializeTestPlatform(String configFile, String sourceRDFFile, String dataDirPath)
	{
		repoManager = GraphDBUtils.createLocalRepositoryManager(dataDirPath);
		repo = GraphDBUtils.createRepository(repoManager,configFile);
		//repo = GraphDBUtils.connectRepository(url);		
		return(GraphDBUtils.loadData(repo, sourceRDFFile));
	}
	public static ArrayList<String> runTest(int queryIterations, Boolean indexedQuery,String storagePath,int warmupIterations,String countFlag,String mode)
	{
		ArrayList<String> results = new ArrayList<String>();
		
		if(indexedQuery){
			GraphDBUtils.enableGeoSPARQL(repo);
		}
		else
		{
			SPARQLQueryWarmUpSet warmupSet = new SPARQLQueryWarmUpSet();
			for (int r=1;r<=warmupIterations;r++) {
				System.out.println();			
				System.out.println(" ++ Warm Up Set iteration No " + r );
				for(int i=0;i<warmupSet.getQueryCount();i++)
				{
					SPARQLQuerySetMember queryItem = warmupSet.getItem(i);
					System.out.println("     " + queryItem.getQueryName());
					GraphDBUtils.runSPARQL(repo, queryItem.getQueryName(), queryItem.getQueryString(),false,null);	
				}	
			}
		}
		
		//SPARQLQuerySet mySet = new SPARQLQuerySet(indexedQuery);	
		//SPARQLQueryGeographicaSet mySet = new SPARQLQueryGeographicaSet(indexedQuery);
		
		ISPARQLQuerySet mySet;
		
		if(mode.toLowerCase().equals("geographica")) {
			mySet = new SPARQLQueryGeographicaSet(indexedQuery);
		}
		else {			
			mySet = new SPARQLQuerySet(indexedQuery);
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
				
				/*Pair p = GraphDBUtils.runSPARQL(repo, queryItem.getQueryName(), queryItem.getQueryString());
				runTimes[i][r-1] = p.getTime();
				runResultCount[i][r-1] = p.getCount();*/
				
				if(r==1 && countFlag.equalsIgnoreCase("on")) {
					Pair p = GraphDBUtils.runSPARQL(repo, queryItem.getQueryName(), queryItem.getQueryString(),true,null);	
					runTimes[i][r-1] = p.getTime();	
					runResultCount[i] = p.getCount();
				}
				else {
					Pair p = GraphDBUtils.runSPARQL(repo, queryItem.getQueryName(), queryItem.getQueryString(),false,null);	
					runTimes[i][r-1] = p.getTime();	
				}
				
			}			
		}
		
		results.add(" +++ GraphDB Indexing (" + indexedQuery.toString() + ") +++ ");
		
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
	
	public static void shutDown()
	{
		if(repo != null)
			repo.shutDown();
		if(repoManager != null)
			repoManager.shutDown();
		
	}
}
