import java.io.BufferedWriter;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.spatial.EntityDefinition;
import org.apache.jena.query.spatial.SpatialDatasetFactory;
import org.apache.jena.query.spatial.SpatialIndex;
import org.apache.jena.query.spatial.SpatialIndexLucene;
import org.apache.jena.query.spatial.SpatialQuery;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.util.QueryExecUtils;
import org.apache.jena.tdb.TDBFactory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLOperations;
import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig;
import io.github.galbiston.geosparql_jena.implementation.DatasetHandler;
import io.github.galbiston.geosparql_jena.implementation.datatype.GeometryDatatype;
import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import io.github.galbiston.rdf_tables.datatypes.DatatypeController;

public class JenaUtils {
	
	public static Dataset createMemoryDataset() throws IOException
    {
		return(DatasetFactory.create());
    }
    
    public static Dataset createTDBDataset(File TDBDir) throws IOException{
    	return(TDBFactory.createDataset(TDBDir.getAbsolutePath()));
    }
    
    public static void createSpatialIndex(Dataset dataset)
    {
    	GeoSPARQLConfig.setupMemoryIndex();
		GeoSPARQLOperations.applyDefaultGeometry(dataset);
    }
    public static long loadData(Dataset dataset, String file) {
        
        long startTime,stopTime,elapsedTime = 0;
        startTime = System.currentTimeMillis();
       
        dataset.begin(ReadWrite.WRITE);
        try {
            Model targetModel = dataset.getDefaultModel();
            Model model = RDFDataMgr.loadModel(file);   
            targetModel.add(model);
            dataset.commit();
        } catch (Exception e) {
			e.printStackTrace();
			dataset.abort();
		} finally {
			dataset.end();
        }
        stopTime = System.currentTimeMillis();
        elapsedTime = (stopTime - startTime);;
        return(elapsedTime);
    }
    
    public static Pair runSPARQL(Dataset dataset,String qryName,String qryString,boolean queryCount,String dirPath) {
        long startTime=0;
        long stopTime=0;
        long elapsedTime=0;
        long countResult=0;	
        startTime = System.currentTimeMillis();
        //startTime = System.nanoTime();
        dataset.begin(ReadWrite.READ);
	    try {
	           // org.apache.jena.query.Query q = QueryFactory.create(qryString);	            
	            QueryExecution qexec = QueryExecutionFactory.create(qryString, dataset);	            
	            ResultSet results = qexec.execSelect() ;
	            results.hasNext();
	            stopTime = System.currentTimeMillis();
	            //stopTime = System.nanoTime();
	            elapsedTime = stopTime - startTime ;
	            if(queryCount == true) {
					countResult = getResultCount(results,qryName,qryString,dirPath);
				}
				qexec.close();
	        } catch (Exception e) {
	        	elapsedTime = -1;
	        	countResult = -1;
	        	System.out.println("   Exception in execution ");
	        	System.out.println(e.getMessage());
	        } finally {
	        	dataset.end();
	        }	        
    	
	        return(new Pair(elapsedTime,countResult));		
     }
    private static long getResultCount(ResultSet QueryResult,String qryName,String qryString,String dirPath)
	{
		long cnt =0;	
		try { 
			while (QueryResult.hasNext()) {
					cnt++;
					QueryResult.next();
				}
			 
		} 
        catch (Exception e) { 
            System.out.println("exception occoured" + e); 
        } 		
		return(cnt);				
	}
    

}
