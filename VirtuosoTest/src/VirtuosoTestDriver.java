import java.sql.Statement;
import java.util.ArrayList;

import org.eclipse.rdf4j.repository.sail.SailRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import virtuoso.rdf4j.driver.VirtuosoRepository;

public class VirtuosoTestDriver {
	
	public static  ArrayList<String> runVirtuosoJDBCTest(String host,String port,String username,String pwd,int queryIterations,String countFlag,String mode)
	{
		ArrayList<String> results = new ArrayList<String>();	
		Connection conn =  VirtuosoUtils.getJDBCconnection(host,port,username,pwd);
		
		results.add(" +++++ Virtuoso JDBC Tests +++++ "); 
		//SPARQLQueryGeographicaSet mySet = new SPARQLQueryGeographicaSet();
		//SPARQLQuerySet mySet = new SPARQLQuerySet();
		
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
					Pair p = VirtuosoUtils.runSPARQLJDBC(conn, queryItem.getQueryName(), queryItem.getQueryString(),true);
					runTimes[i][r-1] = p.getTime();
					runResultCount[i] = p.getCount();
					}
				else {
					Pair p = VirtuosoUtils.runSPARQLJDBC(conn, queryItem.getQueryName(), queryItem.getQueryString(),false);
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
	
	public static ArrayList<String>  runVirtuosoRDF4JTest(String host,String port,String username,String pwd,String RDFFileName, int queryIterations, int warmupIterations,String countFlag,String mode)
	{
		
		ArrayList<String> results = new ArrayList<String>();	
		
		VirtuosoRepository repo =  VirtuosoUtils.createRepository(host,port,username,pwd);
		results.add("Load Time (Milli Sec) : " + VirtuosoUtils.loadData(repo, RDFFileName));
		results.add(" +++++ Virtuoso RDF4J Tests +++++ "); 
		
		
		SPARQLQueryWarmUpSet warmupSet = new SPARQLQueryWarmUpSet();
		for (int r=1;r<=warmupIterations;r++) {
			System.out.println();			
			System.out.println(" ++ Warm Up Set iteration No " + r );
			for(int i=0;i<warmupSet.getQueryCount();i++)
			{
				SPARQLQuerySetMember queryItem = warmupSet.getItem(i);
				System.out.println("     " + queryItem.getQueryName());
				VirtuosoUtils.runSPARQL(repo, queryItem.getQueryName(), queryItem.getQueryString(),false);					
			}	
		}
	
		
		//SPARQLQueryGeographicaSet mySet = new SPARQLQueryGeographicaSet();
		//SPARQLQuerySet mySet = new SPARQLQuerySet();
		
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
				
				if(r==queryIterations && countFlag.equalsIgnoreCase("on")) {
					Pair p = VirtuosoUtils.runSPARQL(repo, queryItem.getQueryName(), queryItem.getQueryString(),true);	
					runTimes[i][r-1] = p.getTime();
					runResultCount[i] = p.getCount();
					}
				else {
					Pair p = VirtuosoUtils.runSPARQL(repo, queryItem.getQueryName(), queryItem.getQueryString(),false);	
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
		repo = null;
		return(results);
		
	
	}

}
