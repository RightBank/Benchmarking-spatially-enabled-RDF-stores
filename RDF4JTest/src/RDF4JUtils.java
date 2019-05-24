

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.elasticsearch.ElasticsearchIndex;
import org.eclipse.rdf4j.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.eclipse.rdf4j.sail.lucene.LuceneIndex;
import org.eclipse.rdf4j.sail.lucene.LuceneSail;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.eclipse.rdf4j.sail.solr.SolrIndex;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;


public class RDF4JUtils {
	
	private static final byte RAM=1;
	private static final byte FILE=2;
	
	
	public static SailRepository createRepository(REPO_TYPES repoType, String path) {
		SailRepository repo = null;
		switch(repoType){
			case MEMORY_SIMPLE:
				repo = new SailRepository(createMemoryStore(path));
				break;
			case MEMORY_WITH_LUCENE_INDEX_IN_RAM:
				repo = new SailRepository(createLuceneWrapper(createMemoryStore(path),RAM));
				break;
			case MEMORY_WITH_LUCENE_INDEX_IN_FILE:
				repo = new SailRepository(createLuceneWrapper(createMemoryStore(path),FILE));
				break;				
			case NATIVE_SIMPLE:
				repo = new SailRepository(createNativeStore(path));
				break;
			case NATIVE_WITH_LUCENE_INDEX_IN_RAM:
				repo = new SailRepository(createLuceneWrapper(createNativeStore(path),RAM));
				break;			
			case NATIVE_WITH_LUCENE_INDEX_IN_FILE:
				repo = new SailRepository(createLuceneWrapper(createNativeStore(path),FILE));
				break;
		}
		if(repo != null) {
			repo.initialize();
		}
		return(repo);
	}
	
	private static Sail createNativeStore(String dirLocation)
	{
		Sail sail = null;
		File fileLoc = new File(dirLocation);
		sail = new NativeStore(fileLoc,"spoc,sopc,psoc,posc,opsc,ospc");		
		return(sail);		
	}
	private static Sail createMemoryStore(String dirLocation)
	{
		Sail sail = null;
		File fileLoc = new File(dirLocation);
		sail = new MemoryStore(fileLoc);		
		return(sail);		
	}
	private static Sail createLuceneWrapper(Sail sail, Byte indexType)
	{
		LuceneSail luceneSail = new LuceneSail();
		luceneSail.setBaseSail(sail);
		luceneSail.setParameter(LuceneSail.WKT_FIELDS,"geo:asWKT");
		luceneSail.setParameter(LuceneSail.INDEX_CLASS_KEY, LuceneIndex.class.getName());
		if(indexType==FILE) {
			luceneSail.setParameter(LuceneSail.LUCENE_DIR_KEY, sail.getDataDir().getPath() + "\\Indexes" );
		}
		else if(indexType==RAM) {
			luceneSail.setParameter(LuceneSail.LUCENE_RAMDIR_KEY, "true");
		}	
		return(luceneSail);
	}
	
	
	public static long loadData(SailRepository repo, String RDFFileName) {
		
		long startTime,stopTime,elapsedTime=0;
		startTime = System.currentTimeMillis();
		
		RepositoryConnection repoConnection = repo.getConnection();		
		File file1 = new File(RDFFileName);		
		
		String baseURI = "http://meta.icos-cp.eu/resources/socat/";
		try (RepositoryConnection con = repo.getConnection()) {
			con.add(file1, baseURI, RDFFormat.RDFXML);
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
	public static Pair runSPARQL(SailRepository repo, String qryName,String qryString, boolean queryCount,String dirPath) {
		long startTime=0;
		long stopTime=0;
		long elapsedTime=0;
		RepositoryConnection conn=null;
		long countResult=0;	
		try {
			conn = repo.getConnection();
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
					countResult = getResultCount(result,qryName,qryString,dirPath);
				}
			}catch (Exception e) {
				elapsedTime = -1;
				countResult = -1;
				System.out.println("   Exception in execution ");
				System.out.println(e.getMessage());
			}	finally { 
			conn.close();
			}
		} catch (Exception e) {
			elapsedTime = -1;
			countResult = -1;
			System.out.println("   Exception in execution ");
			System.out.println(e.getMessage());
		}
		return(new Pair(elapsedTime,countResult));
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
