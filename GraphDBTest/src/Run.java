
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.GraphUtil;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.Binding;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryConfigSchema;
import org.eclipse.rdf4j.repository.manager.LocalRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;

public class Run {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		String config = "";
		String dataFile   = "" ; 
		String storageLocation = "GraphDBTemp" ; 
		int iterations = 1;
		int warmUPIterations = 20;
		String count = "off";
		String mode = "";
		
		for(String cmdParam: args) {
			
			String keyValue[] = cmdParam.split("=");
			if(keyValue[0].toLowerCase().equals("data")) {
				dataFile = keyValue[1];
			}
			else if (keyValue[0].toLowerCase().equals("repeat")) {
				iterations = Integer.parseInt(keyValue[1]) ;
			}
			else if (keyValue[0].toLowerCase().equals("config")) {
				config = keyValue[1];
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
		
		
		if(dataFile=="" || config=="" || mode=="") {
			System.out.println("\n Data File not provided. Usage is given below");
			System.out.println("\n java -cp GraphDBtest.jar Run data=<data_file> repeat=<test_iterations> warmup=<warmup_iterations> count=<on/off> config=<config_file> mode=<geographica/general> ");
			System.out.println("\n java -cp GraphDBtest.jar Run data=/home/CPMeta.rdf repeat=20 warmup=20 count=on config=/home/config.ttl mode=geographica ");
			return;
		}
		
		String resultLoad = "Load Time (Milli Sec) : " + GraphDBTestDriver.initializeTestPlatform(config,dataFile,storageLocation);
		
		
		ArrayList<String> resultSet1 = GraphDBTestDriver.runTest(iterations,false,storageLocation,warmUPIterations,count,mode);		
		ArrayList<String> resultSet2 = GraphDBTestDriver.runTest(iterations,true,storageLocation,warmUPIterations,count,mode);		
		GraphDBTestDriver.shutDown();
		
		System.out.println("*********************  Test Peformance *********************");			
		try { 
			BufferedWriter out = new BufferedWriter(new FileWriter(storageLocation + "/GraphDBPerfs.txt",false)); 			
			//write the result on console and file. Change here to write in a file or in a file
			System.out.println(resultLoad);	
			out.write(resultLoad); 
			out.write("\n");
			
			for (String res:resultSet1) {
				System.out.println(res);	
				out.write(res); 
				out.write("\n"); 
			}
			System.out.println();
			out.write("\n");
			for (String res:resultSet2) {
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
