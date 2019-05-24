import java.io.BufferedWriter;

import java.io.File;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.tdb.TDBFactory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.jena.query.spatial.EntityDefinition;
import org.apache.jena.query.spatial.SpatialDatasetFactory;

import com.github.jsonldjava.core.RDFDataset.Literal;
//import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;

import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig;
import io.github.galbiston.geosparql_jena.configuration.GeoSPARQLOperations;
import io.github.galbiston.geosparql_jena.implementation.datatype.GeometryDatatype;
import io.github.galbiston.geosparql_jena.implementation.datatype.WKTDatatype;
import io.github.galbiston.rdf_tables.datatypes.DatatypeController;

import org.apache.jena.query.* ;

public class Run {
	
	public static void main(String[] args) {
		
		DATASET_TYPES setType = DATASET_TYPES.TDB;
		//DATASET_TYPES setType = DATASET_TYPES.MEMORY
		
		String dirPath = System.getProperty("user.dir") + "/JenaTemp"; //"/home/amir/RDFTemps/JenaTemp";
		String RDFFile = "" ; //  "/home/amir/CPMeta.RDF";
		int queryIterations = 1;
		int warmUPIterations = 20;
		String count = "off";
		String mode = "";
		
		ArrayList<String> resultSet  = null;
		
		for(String cmdParam: args) {
			
			String keyValue[] = cmdParam.split("=");
			if(keyValue[0].toLowerCase().equals("data")) {
				RDFFile = keyValue[1];
			}
			else if (keyValue[0].toLowerCase().equals("repeat")) {
				queryIterations = Integer.parseInt(keyValue[1]) ;
			}
			else if (keyValue[0].toLowerCase().equals("warmup")) {
				warmUPIterations = Integer.parseInt(keyValue[1]) ;
			}
			else if (keyValue[0].toLowerCase().equals("count")) {
				count = keyValue[1];
			}
			else if (keyValue[0].toLowerCase().equals("mode")) {
				if(keyValue[1].toLowerCase().equals("general") || keyValue[1].toLowerCase().equals("geographica")) {
					mode = keyValue[1];
				}
			}
		}
		
		if(RDFFile==""  || mode=="") {
			System.out.println("\n Data File not provided. Usage is given below");
			System.out.println("\n java -cp Jenatest.jar Run data=<data_file> repeat=<test_iterations> warmup=<warmup_iterations> count=<on/off> mode=<geographica/general> ");
			System.out.println("\n java -cp JenaJtest.jar Run data=/home/CPMeta.rdf repeat=20 warmup=25 count=<on> mode=geographica ");
			return;
		}
		
		
		try {
			resultSet = JenaTestDriver.runTest(setType, RDFFile, dirPath, queryIterations,warmUPIterations,count,mode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//write the result on console. Change here to write in a file or in a file
		/*for (String res:resultSet) {
			System.out.println(res);			
		}*/
		System.out.println("*********************  Test Peformance *********************");		
		try { 
			BufferedWriter out = new BufferedWriter(new FileWriter(dirPath + "/JenaPerfs.txt",false)); 			
			//write the result on console and file. Change here to write in a file or in a file
			for (String res:resultSet) {
				System.out.println(res);	
				out.write(res); 
				out.write("\n"); 
			}
			out.close();	
		} 
		
        catch (IOException e) { 
            System.out.println("exception occoured" + e); 
        } 
		

	}
	
	
}
