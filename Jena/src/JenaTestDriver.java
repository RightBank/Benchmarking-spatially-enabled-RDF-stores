
import org.slf4j.Logger ;



import org.slf4j.LoggerFactory ;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.management.Query;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.atlas.logging.LogCtl ;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.spatial.EntityDefinition;
import org.apache.jena.query.spatial.SpatialDatasetFactory;
import org.apache.jena.query.spatial.SpatialIndex;
import org.apache.jena.query.spatial.SpatialIndexLucene;
import org.apache.jena.query.spatial.SpatialQuery;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.util.QueryExecUtils;
import org.apache.jena.tdb.TDBFactory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class JenaTestDriver {
	   
    public static ArrayList<String> runTest(DATASET_TYPES setType, String sourceRDFFile, String dataDirPath, int queryIterations, int warmupIterations,String countFlag,String mode) throws IOException
    {
    	ArrayList<String> results = new ArrayList<String>();
    	Dataset dataset=null;
    
    	File TDBDataDir = new File(dataDirPath);
    	if(setType == DATASET_TYPES.MEMORY) {
    		dataset = JenaUtils.createMemoryDataset();
    	}else if(setType == DATASET_TYPES.TDB) {
    		dataset = JenaUtils.createTDBDataset(TDBDataDir);
    	}
    	
    	JenaUtils.createSpatialIndex(dataset);
    	
    	results.add("Data Set Type = " + setType.toString());
    	results.add("Load Time (Milli Sec) : " + JenaUtils.loadData(dataset, sourceRDFFile));
    	
    	SPARQLQueryWarmUpSet warmupSet = new SPARQLQueryWarmUpSet();
		for (int r=1;r<=warmupIterations;r++) {
			System.out.println();			
			System.out.println(" ++ Warm Up Set iteration No " + r );
			for(int i=0;i<warmupSet.getQueryCount();i++)
			{
				SPARQLQuerySetMember queryItem = warmupSet.getItem(i);
				System.out.println("     " + queryItem.getQueryName());
				//RDF4JUtils.runSPARQL(repo, queryItem.getQueryName(), queryItem.getQueryString(),false,null);	
				JenaUtils.runSPARQL(dataset, queryItem.getQueryName(), queryItem.getQueryString(),false,null);	
			}	
		}
		ISPARQLQuerySet mySet ;
		//SPARQLQuerySet mySet = new SPARQLQuerySet();
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
					Pair p = JenaUtils.runSPARQL(dataset, queryItem.getQueryName(), queryItem.getQueryString(),true,dataDirPath);	
					runTimes[i][r-1] = p.getTime();
					runResultCount[i] = p.getCount();
					}
				else {
					Pair p = JenaUtils.runSPARQL(dataset, queryItem.getQueryName(), queryItem.getQueryString(),false,null);	
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
		
		
		//JenaUtils.destroy(spatialDataset);
    	dataset.close();
    	dataset = null;    	
    	return(results);
    }
  
    
    

}
