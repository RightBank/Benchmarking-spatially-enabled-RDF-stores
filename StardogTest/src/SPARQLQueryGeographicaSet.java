import java.util.ArrayList;

public class SPARQLQueryGeographicaSet implements ISPARQLQuerySet {
	
private ArrayList<SPARQLQuerySetMember> querySet = new ArrayList<SPARQLQuerySetMember>();
	
	public int getQueryCount() {
		return(querySet.size());
	}
	
	public SPARQLQuerySetMember getItem(int index) {
		
		return(querySet.get(index));
	}
	
	private static String queryPrefix;
	static {
		StringBuilder strPrefix = new StringBuilder();
		strPrefix.append("PREFIX cpmeta1: <http://meta.icos-cp.eu/ontologies/cpmeta/> ");
		strPrefix.append("PREFIX geo: <http://www.opengis.net/ont/geosparql#> ");
		strPrefix.append("PREFIX sf: <http://www.opengis.net/ont/sf#> ");
		strPrefix.append("PREFIX geof: <http://www.opengis.net/def/function/geosparql/> ");
		strPrefix.append("PREFIX datasets: <http://geographica.di.uoa.gr/dataset/> ");
		strPrefix.append("PREFIX clc: <http://geo.linkedopendata.gr/corine/ontology#> ");
		strPrefix.append("PREFIX geonames: <http://www.geonames.org/ontology#> ");                             
		strPrefix.append("PREFIX opengis: <http://www.opengis.net/def/uom/OGC/1.0/> ");
		strPrefix.append("PREFIX lgd: <http://linkedgeodata.org/ontology/> ");
		strPrefix.append("PREFIX strdf: <http://strdf.di.uoa.gr/ontology#> ");
		strPrefix.append("PREFIX gag: <http://geo.linkedopendata.gr/gag/ontology/> ");			
		strPrefix.append("PREFIX uom: <http://www.opengis.net/def/uom/OGC/1.0/>");
		strPrefix.append("PREFIX dbpedia: <http://dbpedia.org/property/> ");
		strPrefix.append("prefix unit: <http://qudt.org/vocab/unit#> ");
		queryPrefix = strPrefix.toString();
	}

	private String fixedPolygon = "POLYGON ((21.38804095801072 40.61958360724939,22.69541400488572 40.97720444492195,23.88193744238572 41.07666159023133,24.73887103613572 40.98549827750862,24.83774798926072 40.42750768684824,23.69516986426072 40.2013253647531,23.37656634863572 39.95754551979838,22.66245502051072 39.90699937962775,21.66269916113572 40.251653567380075,21.30015033301072 40.26003796541901,21.38804095801072 40.61958360724939))";
	private String fixedLine    = "LINESTRING (21.38804095801072 40.61958360724939,22.66245502051072 39.90699937962775,21.66269916113572 40.251653567380075,21.30015033301072 40.26003796541901,21.38804095801072 40.61958360724939)";
	private String fixedPoint   = "POINT (21.66269916113572 40.251653567380075)";
	
	
	public SPARQLQueryGeographicaSet() {
	
		
		
		SPARQLQuerySetMember Q7 = new SPARQLQuerySetMember("Q7:Equals_LGD_GivenLine",queryPrefix,
			  	" SELECT ?result " +
			    "  WHERE { " +
				"  			?s1 lgd:asWKT ?result. " + 
				"  			?s1 geo:asWKT ?wkt. " +
				"  FILTER(geof:relate(?s1, \"" + fixedLine + "\"^^geo:wktLiteral,geo:equals)). " +
			    " }");  

		SPARQLQuerySetMember Q8 = new SPARQLQuerySetMember("Q8:Equals_GAG_GivenPolygon",queryPrefix,
				" SELECT ?result " +
			    " WHERE { " + 
				" 			?s1 gag:asWKT ?result. " + 
				"  			?s1 geo:asWKT ?wkt. " +
				"  FILTER(geof:relate(?s1, \"" + fixedPolygon + "\"^^geo:wktLiteral,geo:equals)). " +
			    " }");  
		
		SPARQLQuerySetMember Q9 = new SPARQLQuerySetMember("Q9:Intersects_LGD_GivenPolygon",queryPrefix,
			    " SELECT ?result " + 
			    " WHERE { " + 
				" 			?s1 lgd:asWKT ?result. " +
				"  			?s1 geo:asWKT ?wkt. " +
				" FILTER(geof:relate(?s1,\"" + fixedPolygon + "\"^^geo:wktLiteral,geo:intersects)). " +
		 	    " }");
		
		SPARQLQuerySetMember Q10 = new SPARQLQuerySetMember("Q10:Intersects_CLC_GivenLine",queryPrefix,
				" SELECT ?result " + 
			    " WHERE { " +  
				" 			?s1 clc:asWKT ?result. " +
				"  			?s1 geo:asWKT ?wkt. " +
				" FILTER(geof:relate(?s1,\"" + fixedLine + "\"^^geo:wktLiteral,geo:intersects)). " +
			    " } " );

		
		SPARQLQuerySetMember Q13 = new SPARQLQuerySetMember("Q13:Within_GeoNames_GivenPolygon-FILTER",queryPrefix,
				" SELECT ?result " +
			    "  WHERE { " + 
				"  			?s1 geonames:asWKT ?result. " + 
				"  			?s1 geo:asWKT ?wkt. " +
				" FILTER(geof:relate(?s1,\"" + fixedLine + "\"^^geo:wktLiteral,geo:within)). " +
			    "  }" ); 

		SPARQLQuerySetMember Q13a = new SPARQLQuerySetMember("Q13a:Within_GeoNames_GivenPolygon-PROPERTY",queryPrefix,
				" SELECT ?result " +
			    "  WHERE { " + 
				"  			?s1 geonames:asWKT ?result. " + 
				"  			?s1 geo:asWKT ?wkt. " +
				"  ?s1 geof:within \"" + fixedPolygon + "\"^^geo:wktLiteral. " +
			    "  }" );
		
		SPARQLQuerySetMember Q15 = new SPARQLQuerySetMember("Q15:GeoNames_Point_Distance",queryPrefix,
				" SELECT ?result " + 
			    "  WHERE { " +  
				"  			?s1 geonames:asWKT ?result. " + 
				"  			?s1 geo:asWKT ?wkt. " +
				"  FILTER(geof:distance(?s1, \"" + fixedPoint + "\"^^geo:wktLiteral, unit:Kilometer) < 3 ) . " +
			    " }"); 
		
		SPARQLQuerySetMember Q16 = new SPARQLQuerySetMember("Q16:Disjoint_GeoNames_GivenPolygon",queryPrefix,
				"  SELECT ?result " +
			    "   WHERE { " +
				"   			?s1 geonames:asWKT ?result. " +
				"   			?s1 geo:asWKT ?wkt. " + 
				"   FILTER(geof:relate(?s1,\"" + fixedPolygon + "\"^^geo:wktLiteral,geo:disjoint)). " +
			    "   }" );  
		
		SPARQLQuerySetMember Q17 = new SPARQLQuerySetMember("Q17:Disjoint_LGD_GivenPolygon",queryPrefix,
				"	SELECT ?result " + 
			    "    WHERE { " +
				"    			?s1 lgd:asWKT ?result. " + 
				"    			?s1 geo:asWKT ?wkt. " +
				"    FILTER(geof:relate(?s1,\"" + fixedPolygon + "\"^^geo:wktLiteral,geo:disjoint)). " +
			    "    }" );
		
		SPARQLQuerySetMember Q18 = new SPARQLQuerySetMember("Q18:Equals_GeoNames_DBPedia",queryPrefix,
				"	SELECT ?result " +
				"	 WHERE { " + 
				"			?result geonames:asWKT ?o1. " +
				"			?result geo:asWKT ?wkt1. " + 
				"           ?s2 dbpedia:asWKT ?o2. " +  
				"           ?s2 geo:asWKT ?wkt2. " +
				"			FILTER(geof:relate(?result,?s2, geo:equals))." +  
				"		}" ); 
				
		SPARQLQuerySetMember Q19 = new SPARQLQuerySetMember("Q19:Intersects_GeoNames_LGD",queryPrefix,
				"	SELECT ?result " +
				"	 WHERE { " + 
				"			?result geonames:asWKT ?o1. " + 
				"			?result geo:asWKT ?wkt1. " + 
				"           ?s2 lgd:asWKT ?o2. " +  
				"           ?s2 geo:asWKT ?wkt2. " +
				"			FILTER(geof:relate(?result,?s2, geo:intersects))." +  
				"		}" ); 
		
		SPARQLQuerySetMember Q20 = new SPARQLQuerySetMember("Q20:Intersects_GeoNames_GAG",queryPrefix,
				"	SELECT ?result " +
				"	 WHERE { " + 
				"			?result geonames:asWKT ?o1. " + 
				"			?result geo:asWKT ?wkt1. " + 
				"           ?s2 gag:asWKT ?o2. " +  
				"           ?s2 geo:asWKT ?wkt2. " +
				"			FILTER(geof:relate(?result,?s2, geo:intersects))." +  
				"		}" ); 
		
		SPARQLQuerySetMember Q21 = new SPARQLQuerySetMember("Q21:Intersects_LGD_GAG",queryPrefix,
				"	SELECT ?result " +
				"	 WHERE { " + 
				"			?result lgd:asWKT ?o1. " + 
				"			?result geo:asWKT ?wkt1. " + 
				"           ?s2 gag:asWKT ?o2. " +  
				"           ?s2 geo:asWKT ?wkt2. " +
				"			FILTER(geof:relate(?result,?s2,geo:intersects))." +  
				"		}" ); 
		
		SPARQLQuerySetMember Q22 = new SPARQLQuerySetMember("Q22:Within_GeoNames_GAG-FILTER",queryPrefix,
				"	SELECT ?result " +
				"	 WHERE { " + 
				"			?result geonames:asWKT ?o1. " + 
				"			?result geo:asWKT ?wkt1. " + 
				"           ?s2 gag:asWKT ?o2. " +  
				"           ?s2 geo:asWKT ?wkt2. " +				
				"  			FILTER(geof:relate(?result,?s2,geo:within)) .		" +
				"		}" ); 
		
		SPARQLQuerySetMember Q22a = new SPARQLQuerySetMember("Q22a:Within_GeoNames_GAG-PROPERTY",queryPrefix,
				"	SELECT ?result " +
				"	 WHERE { " + 
				"			?result geonames:asWKT ?o1. " + 
				"			?result geo:asWKT ?wkt1. " + 
				"           ?s2 gag:asWKT ?o2. " +  
				"           ?s2 geo:asWKT ?wkt2. " +
				"			?result geof:within ?s2." +  
				"		}" ); 
		
		
		SPARQLQuerySetMember Q23 = new SPARQLQuerySetMember("Q23:Within_LGD_GAG-FILTER",queryPrefix,
				"	SELECT ?result " +
				"	 WHERE { " + 
				"			?result lgd:asWKT ?o1. " + 
				"			?result geo:asWKT ?wkt1. " + 
				"           ?s2 gag:asWKT ?o2. " +  
				"           ?s2 geo:asWKT ?wkt2. " +
				"  			FILTER(geof:relate(?result,?s2,geo:within)) .		" +
				"		}" ); 
		
		SPARQLQuerySetMember Q23a = new SPARQLQuerySetMember("Q23a:Within_LGD_GAG-PRPOERTY",queryPrefix,
				"	SELECT ?result " +
				"	 WHERE { " + 
				"			?result lgd:asWKT ?o1. " + 
				"			?result geo:asWKT ?wkt1. " + 
				"           ?s2 gag:asWKT ?o2. " +  
				"           ?s2 geo:asWKT ?wkt2. " +
				"			?result geof:within ?s2." +
				"		}" ); 
		
		
		SPARQLQuerySetMember Q24 = new SPARQLQuerySetMember("Q24:Within_CLC_GAG-FILTER",queryPrefix,
				"	SELECT ?result " +
				"	 WHERE { " + 
				"			?result clc:asWKT ?o1. " + 
				"			?result geo:asWKT ?wkt1. " + 
				"           ?s2 gag:asWKT ?o2. " +  
				"           ?s2 geo:asWKT ?wkt2. " +
				"  			FILTER(geof:relate(?result,?s2,geo:within)) .		" +
				"		}" ); 
		
		
		SPARQLQuerySetMember Q24a = new SPARQLQuerySetMember("Q24a:Within_CLC_GAG-PROPERTY",queryPrefix,
				"	SELECT ?result " +
				"	 WHERE { " + 
				"			?result clc:asWKT ?o1. " + 
				"			?result geo:asWKT ?wkt1. " + 
				"           ?s2 gag:asWKT ?o2. " +  
				"           ?s2 geo:asWKT ?wkt2. " +
				"			?result geof:within ?s2." +
				"		}" ); 
		
		
		querySet.add(Q7);
		querySet.add(Q8);
		querySet.add(Q9);
		querySet.add(Q10);
		querySet.add(Q13);
		//querySet.add(Q13a);
		querySet.add(Q15);
		querySet.add(Q16);
		querySet.add(Q17);
		querySet.add(Q18);
		querySet.add(Q19);
		querySet.add(Q20);
		querySet.add(Q21);
		querySet.add(Q22);
		//querySet.add(Q22a);
		querySet.add(Q23);
		//querySet.add(Q23a);
		querySet.add(Q24);
		//querySet.add(Q24a);
		

	}
}
