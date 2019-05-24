import java.util.ArrayList;

public class SPARQLQueryWarmUpSet {


private ArrayList<SPARQLQuerySetMember> querySet = new ArrayList<SPARQLQuerySetMember>();
	
	public int getQueryCount() {
		return(querySet.size());
	}
	
	public SPARQLQuerySetMember getItem(int index) {
		
		return(querySet.get(index));
	}
	public SPARQLQueryWarmUpSet() {
		
		String queryPrefix = " PREFIX cpmeta: <http://meta.icos-cp.eu/ontologies/cpmeta/> " + 
				       		 " PREFIX cpst: <http://meta.icos-cp.eu/ontologies/stationentry/> " +
				       		 " PREFIX dcterms: <http://purl.org/dc/terms/> " +
		                     " PREFIX st: <http://meta.icos-cp.eu/ontologies/stationentry/> " +
		                     " PREFIX prov: <http://www.w3.org/ns/prov#> ";
				
		SPARQLQuerySetMember W1 = new SPARQLQuerySetMember("W1:WarmUp Q1",queryPrefix,			
						     " SELECT *  WHERE { " +
			                 " ?coll a cpmeta:Collection . " +
			                 " OPTIONAL{?coll cpmeta:hasDoi ?doi} " +
			                 " ?coll dcterms:title ?title . } " );
		
		SPARQLQuerySetMember W2 = new SPARQLQuerySetMember("W2:WarmUp Q2",queryPrefix,			
							 " SELECT * WHERE { " +
			                 " ?doc a cpmeta:DocumentObject . " +
			                 " ?doc cpmeta:hasName ?fileName . } " );
		
		SPARQLQuerySetMember W3 = new SPARQLQuerySetMember("W3:WarmUp Q3",queryPrefix,	
				             " SELECT * WHERE { " +
				             " ?s cpst:hasCountry ?country . " +
				             " ?s cpst:hasShortName ?sName . " +
				             " ?s cpst:hasLongName ?lName . " +
				             " ?s cpst:hasSiteType ?siteType . } " ); 
		
		SPARQLQuerySetMember W4 = new SPARQLQuerySetMember("W4:WarmUp Q4",queryPrefix,	
							" SELECT distinct ?stationId ?stationClass ?firstName ?lastName ?email " + 
						    "  WHERE { " +
						    "  ?s a st:ES . " + 
						    "  ?s st:hasShortName ?stationId . " +
						    "  ?s st:hasStationClass ?stationClass . " +
						    "  ?s st:hasPi ?pi . " +
						    "  ?pi st:hasFirstName ?firstName . " +
						    "  ?pi st:hasLastName ?lastName . " +
						    "  ?pi st:hasEmail ?email . } ");

		SPARQLQuerySetMember W5 = new SPARQLQuerySetMember("W5:WarmUp Q5",queryPrefix,	
							" select (str(?submTime) as ?time) ?dobj ?spec ?dataLevel ?fileName where { " + 
							"	?dobj cpmeta:wasSubmittedBy/prov:endedAtTime ?submTime . " + 
							"	?dobj cpmeta:hasName ?fileName . " + 
							"	?dobj cpmeta:hasObjectSpec [rdfs:label ?spec ; cpmeta:hasDataLevel ?dataLevel]. " + 
							" } " + 
							" order by desc(?submTime) " + 
							" limit 1000 " ); 
							
		querySet.add(W1);
		querySet.add(W2);
		querySet.add(W3);
		querySet.add(W4);
		querySet.add(W5);
	}
}
