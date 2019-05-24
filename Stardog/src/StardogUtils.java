
import java.io.BufferedWriter;
import java.io.File;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResultHandlerException;
//import org.eclipse.rdf4j.query.resultio.QueryResultIO;
import org.eclipse.rdf4j.query.resultio.TupleQueryResultFormat;
import org.eclipse.rdf4j.query.resultio.UnsupportedQueryResultFormatException;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryResult;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.resultio.QueryResultFormat;
import org.openrdf.query.resultio.QueryResultIO;
import org.openrdf.query.resultio.TupleQueryResultWriterFactory;
import org.openrdf.rio.RDFFormat;
import org.eclipse.rdf4j.repository.Repository;

/*import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;*/

import com.complexible.common.openrdf.repository.RepositoryConnections;
import com.complexible.common.rdf.query.TupleQueryResultIterator;
import com.complexible.common.rdf.query.resultio.TextTableQueryResultWriter;
import com.complexible.stardog.Stardog;
import com.complexible.stardog.StardogException;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.SelectQuery;
import com.complexible.stardog.api.admin.AdminConnection;
import com.complexible.stardog.api.admin.AdminConnectionConfiguration;
import com.complexible.stardog.db.DatabaseOptions;
import com.complexible.stardog.sesame.StardogRepository;
import com.complexible.stardog.sesame.StardogRepositoryConnection;
import com.complexible.stardog.spatial.GeospatialOptions;
import com.stardog.stark.io.RDFFormats;
import com.stardog.stark.query.SelectQueryResult;
import com.stardog.stark.query.io.QueryResultWriters;
import com.stardog.stark.query.io.ResultWritingFailed;


public class StardogUtils {

	private static boolean dontLoad = false;
	
	public static Stardog startServerInstance() {
		Stardog stardogInstance = Stardog.builder().set(GeospatialOptions.USE_JTS,true).create();
		return(stardogInstance);
	}
	public static void createDatabase(String dbName) {
		AdminConnection stardogAdminConnection = AdminConnectionConfiguration.toEmbeddedServer().credentials("admin", "admin").connect();
		if (stardogAdminConnection.list().contains(dbName)) {
			System.out.println("***Database already available***");
			dontLoad = true;
			//stardogAdminConnection.drop(dbName);
		}
		else{		
			com.complexible.common.base.Duration duration = com.complexible.common.base.Duration.valueOf("1h");
			stardogAdminConnection.newDatabase(dbName).set(GeospatialOptions.SPATIAL_ENABLED, true).set(DatabaseOptions.QUERY_TIMEOUT, duration).create();
		}
		stardogAdminConnection.close();
		
	}
	
	public static long loadData(String dbName, String fileName) {
		
		if(dontLoad) {
			System.out.println("***Reusing existing data***");	
			return(0);
		}
		
		long startTime=0;
		long stopTime=0;
		long elapsedTime=0;
		startTime = System.currentTimeMillis();
		Connection stardogConnection = ConnectionConfiguration.to(dbName).credentials("admin", "admin").connect();
		stardogConnection.begin();

		try {
			stardogConnection.add().io().format(RDFFormats.RDFXML).stream(new FileInputStream(fileName));			
		} catch (StardogException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		stardogConnection.commit();			
		stardogConnection.close();
		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime-startTime;
		
		return(elapsedTime);
	}
	
	public static long loadDataToRepo(String fileName,StardogRepository repo) {
		
		long startTime,stopTime,elapsedTime=0;
		startTime = System.currentTimeMillis();
		
//		File file1 = new File(fileName);				
//		String baseURI = "http://meta.icos-cp.eu/resources/socat/";
		StardogRepositoryConnection conn = repo.getConnection();
		try{
			RepositoryConnections.add(conn, new FileInputStream(fileName), RDFFormat.RDFXML);			
			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime-startTime;			
		}
		catch (RDF4JException e) {
			System.out.println("RDF4 Exception in Data Load");
		}
		catch (IOException e) {
			System.out.println("I/O Exception in Data Load");
		}
		
		conn.close();
		return(elapsedTime);
	}
	public static Pair runSPARQL(String dbName, String qryName,String qryString, boolean queryCount,String dirPath) {
		long startTime=0;
		long stopTime=0;
		long elapsedTime=0;
		long cnt=0;
		Connection stardogConnection = ConnectionConfiguration.to(dbName).credentials("admin", "admin").connect();
		try {
			String queryString = qryString;
			startTime = System.currentTimeMillis();
			//startTime = System.nanoTime();
			SelectQuery SPARQLQuery = stardogConnection.select(qryString);	
			SelectQueryResult ResultSet = SPARQLQuery.execute();
			ResultSet.hasNext();
			stopTime = System.currentTimeMillis();
			//stopTime = System.nanoTime();
			elapsedTime = stopTime-startTime;	
			if(queryCount) {
				cnt = getResultCount(ResultSet,qryName,qryString,dirPath);
			}
			//QueryResultWriters.write(ResultSet, System.out, TextTableQueryResultWriter.FORMAT);			
		} catch (Exception e) {
			elapsedTime = -1;
			cnt = -1;
			System.out.println("   Exception in execution ");
			System.out.println(e.getMessage());
		}	
		finally { 
			stardogConnection.close();		
		}
		return(new Pair(elapsedTime,cnt));
	}
	
	private static long getResultCount(SelectQueryResult tupleQueryResult,String qryName,String qryString, String dirPath)
	{
		long cnt =0;
		try {
			while (tupleQueryResult.hasNext()) {
				cnt++;
				tupleQueryResult.next();				 				
			}			
		}catch (Exception e) { 
            System.out.println("exception occoured" + e); 
        }
		return(cnt);				
	}
	
	public static StardogRepository createRepository(String dbName) {
		
		StardogRepository repo =  new StardogRepository(ConnectionConfiguration.to(dbName).credentials("admin", "admin")) ;
		repo.initialize();		
		return(repo);		
	}
	public static Pair runSPARQLOnRepo(StardogRepository repo, String qryName,String qryString, boolean queryCount,String dirPath) {
		long startTime=0;
		long stopTime=0;
		long elapsedTime=0;
		long cnt=0;
		//StardogRepositoryConnection conn =  StardogRepo.getConnection();
		StardogRepositoryConnection conn = repo.getConnection();
		try{
			startTime = System.currentTimeMillis();
			//startTime = System.nanoTime();			
			org.openrdf.query.TupleQuery SPARQLQuery = conn.prepareTupleQuery(qryString);
			QueryResult<BindingSet> ResultSet = SPARQLQuery.evaluate();
			ResultSet.hasNext();
			stopTime = System.currentTimeMillis();
			//stopTime = System.nanoTime();
			elapsedTime = stopTime-startTime;	
			if(queryCount) {
				cnt = getResultCount(ResultSet,qryName,qryString,dirPath);
			}	
			}catch (Exception e) {
			elapsedTime = -1;
			cnt = -1;
			System.out.println("   Exception in execution ");
			System.out.println(e.getMessage());
		}  finally { 
				conn.close();
			
		}
			
		return(new Pair(elapsedTime,cnt));
	}
	private static long getResultCount(QueryResult<BindingSet> tupleQueryResult,String qryName,String qryString, String dirPath)
	{
		long cnt =0;
		try {
			while (tupleQueryResult.hasNext()) {
				cnt++;
				tupleQueryResult.next();
				 				
			}			
		}catch (Exception e) { 
            System.out.println("exception occoured" + e); 
        }
		return(cnt);
	}
	
}
