import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.eclipse.rdf4j.RDF4JException;
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
import org.eclipse.rdf4j.repository.config.RepositoryImplConfig;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.manager.LocalRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;

import com.ontotext.trree.OwlimSchemaRepository;
import com.ontotext.trree.monitorRepository.MonitorRepository;

public class GraphDBUtils {

	public static RepositoryManager createLocalRepositoryManager(String storageFolder)
	{
		RepositoryManager repositoryManager = new LocalRepositoryManager(new File(storageFolder));
		repositoryManager.initialize();	
		return(repositoryManager);		
	}
	public static Repository createRepository(RepositoryManager repoManager,String configFile )
	{
		Repository repo= null;
		TreeModel graph = new TreeModel();				
		
		try {
			InputStream config = new FileInputStream(configFile);
			RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
			rdfParser.setRDFHandler(new StatementCollector(graph));
			rdfParser.parse(config, RepositoryConfigSchema.NAMESPACE);
			config.close();			
			
			Resource repositoryNode = GraphUtil.getUniqueSubject(graph, RDF.TYPE, RepositoryConfigSchema.REPOSITORY);
			RepositoryConfig repositoryConfig = RepositoryConfig.create(graph, repositoryNode);
			repoManager.addRepositoryConfig(repositoryConfig);
			repo = repoManager.getRepository("graphdb-repo");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return(null);
		} catch (RDFParseException e) {
			e.printStackTrace();
			return(null);
		} catch (RDFHandlerException e) {
			e.printStackTrace();
			return(null);
		} catch (IOException e) {
			e.printStackTrace();
			return(null);
		}
		
		
		return repo;
	}
	public static Repository connectRepository(String url)
	{
		
		HTTPRepository repo = new HTTPRepository(url);
			
		return repo;
	}
	
	
	public static long loadData(Repository repo, String RDFFileName) {
		
		long startTime=0;
		long stopTime=0;
		long elapsedTime=0;
		startTime = System.currentTimeMillis();
		
		RepositoryConnection repoConnection = repo.getConnection();		
		File file1 = new File(RDFFileName);		
		
		String baseURI = "http://meta.icos-cp.eu/resources/socat/";
		try {
			repoConnection.add(file1, baseURI, RDFFormat.RDFXML);
			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime-startTime;			
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
	
public static long enableGeoSPARQL(Repository repo) {
		
		long startTime=0;
		long stopTime=0;
		long elapsedTime=0;
		startTime = System.currentTimeMillis();
		
		RepositoryConnection repoConnection = repo.getConnection();		
		
		String pluginString =  " PREFIX : <http://www.ontotext.com/plugins/geosparql#> " +
				   " INSERT DATA { "  +
				   " _:s :enabled \"true\" . " +
				   " } ";			
		org.eclipse.rdf4j.query.Update updQuery = repoConnection.prepareUpdate(pluginString);
		updQuery.execute();
		
		pluginString =  " PREFIX : <http://www.ontotext.com/plugins/geosparql#> " +
				   " INSERT DATA { "  +
				   " _:s :ignoreErrors \"true\" . " +
				   " } ";			
		updQuery = repoConnection.prepareUpdate(pluginString);
		updQuery.execute();
		
		pluginString =  " PREFIX : <http://www.ontotext.com/plugins/geosparql#> " +
				   " INSERT DATA { "  +
				   " _:s :forceReindex  \"\" . " +
				   " } ";			
		updQuery = repoConnection.prepareUpdate(pluginString);
		updQuery.execute();
		
		
		repoConnection.close();
		return(elapsedTime);
		
	}

	public static Pair runSPARQL(Repository repo, String qryName,String qryString,boolean queryCount,String dirPath) {
		long startTime=0;
		long stopTime=0;
		long elapsedTime=0;
		long cnt=-1;
		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = qryString;
			startTime = System.currentTimeMillis();
			//startTime = System.nanoTime();
			TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);		
			TupleQueryResult result=null;
			try {				
				result = tupleQuery.evaluate();
				result.hasNext();
				stopTime = System.currentTimeMillis();
				//stopTime = System.nanoTime();
				elapsedTime = stopTime-startTime;
				if(queryCount) {
					cnt = getResultCount(result,qryName,qryString,dirPath);
				}
				
			}catch (Exception e) {
				elapsedTime = -1;
				cnt = -1;
				System.out.println("   Exception in execution ");
				System.out.println(e.getMessage());
			}
			finally { 
				result.close();
				conn.close();			
			}
		}catch (Exception e) {
			elapsedTime = -1;
			cnt = -1;
			System.out.println("   Exception in execution ");
			System.out.println(e.getMessage());
		}
		
		return(new Pair(elapsedTime,cnt));
	}
	
	private static long getResultCount(TupleQueryResult tupleQueryResult,String qryName,String qryString,String dirPath)
	{
		long cnt =0;	
		try { 
				while (tupleQueryResult.hasNext()) {
					cnt++;
					tupleQueryResult.next();
				}
			 
		} 
        catch (Exception e) { 
            System.out.println("exception occoured" + e); 
        } 		
		return(cnt);				
	}
	
}
