import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;

import virtuoso.rdf4j.driver.VirtuosoRepository;
import virtuoso.rdf4j.driver.VirtuosoRepositoryConnection;
import virtuoso.rdf4j.driver.config.VirtuosoRepositoryFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.query.BindingSet;


public class VirtuosoUtils {

	public static VirtuosoRepository createRepository(String host,String port,String username,String pwd) {
		
		VirtuosoRepository repo =  new VirtuosoRepository(host + ":" + port, username, pwd) ;
		repo.initialize();		
		return(repo);		
	}
	
public static long loadData(VirtuosoRepository repo, String RDFFileName) {
		
		long startTime,stopTime,elapsedTime=0;
		startTime = System.currentTimeMillis();
		
		VirtuosoRepositoryConnection repoConnection = (VirtuosoRepositoryConnection) repo.getConnection();		
		File file1 = new File(RDFFileName);		
		
		String baseURI = "http://meta.icos-cp.eu/resources/socat/";
		try (RepositoryConnection con = repo.getConnection()) {
			con.add(file1, baseURI, RDFFormat.RDFXML);
			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime-startTime;	
			con.commit();
		}
		catch (RDF4JException e) {
			System.out.println("RDF4 Exception in Data Load");
		}
		catch (IOException e) {
			System.out.println("I/O Exception in Data Load");
		}
		
		repoConnection.close();
		return(elapsedTime);
	}
	
	public static Pair runSPARQL(VirtuosoRepository repo, String qryName,String qryString, boolean queryCount) {
		long startTime=0;
		long stopTime=0;
		long elapsedTime=0;
		long cnt=0;
		try (VirtuosoRepositoryConnection conn = (VirtuosoRepositoryConnection) repo.getConnection()) {
			String queryString = qryString;
			startTime = System.currentTimeMillis();
			//startTime = System.nanoTime();
			TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);			
			try {
					TupleQueryResult result = tupleQuery.evaluate();
					result.hasNext();
					stopTime = System.currentTimeMillis();
					//stopTime = System.nanoTime();
					elapsedTime = stopTime-startTime;
					if(queryCount == true) {
						cnt = getResultCount(result,qryName,qryString);
					} 
					result.close();
				}catch (Exception e) {
					elapsedTime = -1;
					cnt = -1;
					System.out.println("   Exception in execution ");
					System.out.println(e.getMessage());
				}						
			conn.close();
			}catch (Exception e) {
				elapsedTime = -1;
				cnt = -1;
				System.out.println("   Exception in execution ");
				System.out.println(e.getMessage());			
			}
		return(new Pair(elapsedTime,cnt));
	}
	
	private static long getResultCount(TupleQueryResult tupleQueryResult,String qryName,String qryString)
	{
		long cnt =0;
		while (tupleQueryResult.hasNext()) {
			cnt++;
			tupleQueryResult.next();
		}
		return(cnt);				
	}
	
	private static long getResultCount(java.sql.ResultSet tupleQueryResult,String qryName,String qryString)
	{
		long cnt =1;
		try {
			while (tupleQueryResult.next()) {
				cnt++;				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return(cnt);				
	}
	
	
	public static Connection getJDBCconnection(String host,String port,String username,String pwd) {
		Connection conn=null;
		try {
			conn = DriverManager.getConnection("jdbc:virtuoso://" + host + ":" + port,username,pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return(conn);
		
	}
	
	public static Pair runSPARQLJDBC(Connection conn, String qryName,String qryString, boolean queryCount) {
		long startTime=0;
		long stopTime=0;
		long elapsedTime=0;
		long cnt=0;
		String queryString = "SPARQL " + qryString;
		startTime = System.currentTimeMillis();
		//startTime = System.nanoTime();
		try {
			Statement  stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(queryString);	
			rset.next();
			stopTime = System.currentTimeMillis();
			//stopTime = System.nanoTime();
			elapsedTime = stopTime-startTime;			
			if(queryCount == true) {
				cnt = getResultCount(rset,qryName,qryString);
			}
				
		} catch (Exception e) {
			elapsedTime = -1;
			cnt = -1;
			System.out.println("   Exception in execution ");
			System.out.println(e.getMessage());			
		}
		return(new Pair(elapsedTime,cnt));
	}
}
