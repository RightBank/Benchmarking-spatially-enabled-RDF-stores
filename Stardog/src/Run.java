import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.complexible.common.rdf.query.resultio.TextTableQueryResultWriter;
import com.complexible.stardog.Stardog;
import com.complexible.stardog.StardogException;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.SelectQuery;
import com.complexible.stardog.api.admin.AdminConnection;
import com.complexible.stardog.api.admin.AdminConnectionConfiguration;
import com.complexible.stardog.spatial.GeospatialOptions;
import com.stardog.stark.io.RDFFormats;
import com.stardog.stark.query.SelectQueryResult;
import com.stardog.stark.query.io.QueryResultWriters;
import com.stardog.stark.query.io.ResultWritingFailed;


public class Run {
	public static void main(String[] args) {
		
		//String dbName = System.getProperty("user.dir") + "/StardogTemp";
		String dbName = "StardogTemp";
		String RDFFilename = "";
		int queryIterations = 1; 
		int warmUPIterations = 20;
		String count = "off";
		String mode = "";
		
		for(String cmdParam: args) {
			
			String keyValue[] = cmdParam.split("=");
			if(keyValue[0].toLowerCase().equals("data")) {
				RDFFilename = keyValue[1];
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

		if(RDFFilename=="" || mode=="") {
			System.out.println("\n Data File not provided. Usage is given below");
			System.out.println("\n java -cp Stardogtest.jar Run data=<data_file> repeat=<test_iterations> warmup=<warmup_iterations> count=<on/off> mode=<geographica/general> ");
			System.out.println("\n java -cp Stardogtest.jar Run data=/home/CPMeta.rdf repeat=20 warmup=20 count=on mode=geographica  ");
			return;
		}
		
		Stardog server = StardogUtils.startServerInstance();
		StardogUtils.createDatabase(dbName);
		String loadResult = "Load Time (Milli Sec) : " + StardogUtils.loadData(dbName, RDFFilename);
		
		ArrayList<String> resultSet1 = StardogTestDriver.runTest(dbName,RDFFilename , queryIterations, dbName,warmUPIterations,count,mode);
		
		ArrayList<String> resultSet2 = StardogTestDriver.runRDF4JTest(dbName,RDFFilename , queryIterations, dbName,count,mode);
				
		server.shutdown();
		
		
		
		System.out.println("*********************  Test Peformance *********************");			
		try { 
			BufferedWriter out = new BufferedWriter(new FileWriter(dbName + "/StardogPerfs.txt",false)); 			
			//write the result on console and file. Change here to write in a file or in a file
			System.out.println(loadResult);	
			out.write(loadResult); 
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

/*Stardog stardogInstance = Stardog.builder().set(GeospatialOptions.USE_JTS,true).create();
//AdminConnection stardogAdminConnection = AdminConnectionConfiguration.toEmbeddedServer().credentials("admin", "admin").set(GeospatialOptions.USE_JTS, true).connect();
AdminConnection stardogAdminConnection = AdminConnectionConfiguration.toEmbeddedServer().credentials("admin", "admin").connect();
if (stardogAdminConnection.list().contains("ICOSMetaDB")) {
	stardogAdminConnection.drop("ICOSMetaDB");
}

//stardogAdminConnection.newDatabase("ICOSMetaDB").set(GeospatialOptions.SPATIAL_ENABLED, true).create();
stardogAdminConnection.newDatabase("ICOSMetaDB").set(GeospatialOptions.SPATIAL_ENABLED, true).create();

//	stardogAdminConnection.newDatabase("ICOSMetaDB").create();
stardogAdminConnection.close();
//Connection stardogConnection = ConnectionConfiguration.to("ICOSMetaDB").credentials("admin", "admin").set(GeospatialOptions.USE_JTS,true).connect();
Connection stardogConnection = ConnectionConfiguration.to("ICOSMetaDB").credentials("admin", "admin").connect();
stardogConnection.begin();

try {
	stardogConnection.add().io().format(RDFFormats.RDFXML).stream(new FileInputStream("CPMeta.RDF"));
} catch (StardogException | FileNotFoundException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}


stardogConnection.commit();


SelectQuery SPARQLQuery = stardogConnection.select("select * where { ?s ?p ?o }");

try(SelectQueryResult aResult = SPARQLQuery.execute()) {
	System.out.println("The first ten results...");

	QueryResultWriters.write(aResult, System.out, TextTableQueryResultWriter.FORMAT);
} catch (ResultWritingFailed e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

stardogConnection.close();
//GeospatialOptions.


//Stardog aServer = Stardog.buildServer().bind(SNARLProtocolConstants.EMBEDDED_ADDRESS).start();

//AdminConnection stardogConnection = AdminConnectionConfiguration.toEmbeddedServer().credentials("admin", "admin").connect();
//Connection stardogConnection = ConnectionConfiguration.to("System").credentials("admin", "admin").connect();
		
System.out.println("ok");

		
stardogInstance.shutdown();

//Connection stardogConnection = ConnectionConfiguration.to("TestDB").server("http://localhost:5820").credentials("admin", "admin").connect();	

//AdminConnection stardogConnection = AdminConnectionConfiguration.toServer("http://localhost:5820").credentials("admin", "admin").connect();


//stardogConnection.close();*/