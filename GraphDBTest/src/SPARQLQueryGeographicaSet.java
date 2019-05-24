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
		queryPrefix = strPrefix.toString();
	}

	private String fixedPolygon = "POLYGON ((21.38804095801072 40.61958360724939,22.69541400488572 40.97720444492195,23.88193744238572 41.07666159023133,24.73887103613572 40.98549827750862,24.83774798926072 40.42750768684824,23.69516986426072 40.2013253647531,23.37656634863572 39.95754551979838,22.66245502051072 39.90699937962775,21.66269916113572 40.251653567380075,21.30015033301072 40.26003796541901,21.38804095801072 40.61958360724939))";
	private String fixedLine    = "LINESTRING (21.38804095801072 40.61958360724939,22.66245502051072 39.90699937962775,21.66269916113572 40.251653567380075,21.30015033301072 40.26003796541901,21.38804095801072 40.61958360724939)";
	private String fixedPoint   = "POINT (21.66269916113572 40.251653567380075)";
	
	
	public SPARQLQueryGeographicaSet(Boolean indexedQuery) {
	
		SPARQLQuerySetMember Q1,Q2,Q3,Q4,Q5,Q7,Q8,Q9,Q10,Q11,Q12,Q13,Q15,Q16,Q17,Q18,Q19,Q20,Q21,Q22,Q23,Q24,Q25,Q26,Q27;
		
		Q1 = new SPARQLQuerySetMember("Q1:Boundary_CLC",queryPrefix,			
				" SELECT (geof:boundary(?o1) AS ?result) " + 
				"  WHERE { " + 
				"  		?s1 clc:asWKT ?o1. " + 
				" } "); 
				
		Q2 = new SPARQLQuerySetMember("Q2:Envelope_CLC",queryPrefix,	
				" SELECT (geof:envelope(?o1) AS ?result) " +
		        " WHERE { " + 
			    " 		?s1 clc:asWKT ?o1. " +
		        " } "); 

		Q3 = new SPARQLQuerySetMember("Q3:ConvexHull_CLC",queryPrefix,	
				" SELECT (geof:convexHull(?o1) AS ?result) " +
		        " WHERE { " +
			    " 		?s1 clc:asWKT ?o1. " + 
		        " } "); 

		 Q4 = new SPARQLQuerySetMember("Q4:Buffer_GeoNames_2",queryPrefix,	
		        " SELECT (geof:buffer(?o1, 4, opengis:metre) AS ?result) " +
		        " WHERE { " +                                                                           
		        " 		?s1 geonames:asWKT ?o1. " +
		        " } ");

		 Q5 = new SPARQLQuerySetMember("Q5:Buffer_LGD_2",queryPrefix,	
				" SELECT (geof:buffer(?o1, 4, opengis:metre) AS ?result) " + 
		        "  WHERE { " +
			    "  		?s1 lgd:asWKT ?o1. " +
		        " } "); 
		
		
		 Q15 = new SPARQLQuerySetMember("Q15:GeoNames_Point_Distance",queryPrefix,
					" SELECT ?result " + 
				    "  WHERE { " +  
					"  				?s1 geonames:asWKT ?result " + 
					"  FILTER(geof:distance(?result, \"" + fixedPoint + "\"^^geo:wktLiteral, opengis:metre) <= 3000). "  +
				    " }"); 
			
			
		
		if(indexedQuery) {
			Q7 = new SPARQLQuerySetMember("Q7:Equals_LGD_GivenLine",queryPrefix,
				  	" SELECT ?result " +
				    "  WHERE { " +
					"  			?s1 lgd:asWKT ?result. " + 
					"  ?s1 geo:sfEquals \"" + fixedLine + "\"^^geo:wktLiteral. " +
				    " }");  
	
			Q8 = new SPARQLQuerySetMember("Q8:Equals_GAG_GivenPolygon",queryPrefix,
					" SELECT ?result " +
				    " WHERE { " + 
					" 			?s1 gag:asWKT ?result. " + 
					"  ?s1 geo:sfEquals \"" + fixedPolygon + "\"^^geo:wktLiteral. " +
				    " }");  
			
			Q9 = new SPARQLQuerySetMember("Q9:Intersects_LGD_GivenPolygon",queryPrefix,
				    " SELECT ?result " + 
				    " WHERE { " + 
					" 			?s1 lgd:asWKT ?result. " +
					" ?s1 geo:sfIntersects \"" + fixedPolygon + "\"^^geo:wktLiteral. " +
			 	    " }");
			
			Q10 = new SPARQLQuerySetMember("Q10:Intersects_CLC_GivenLine",queryPrefix,
					" SELECT ?result " + 
				    " WHERE { " +  
					" 			?s1 clc:asWKT ?result. " +
					" ?s1 geo:sfIntersects \"" + fixedLine + "\"^^geo:wktLiteral. " +
				    " } " );
	
			Q11 = new SPARQLQuerySetMember("Q11:Overlaps_CLC_GivenPolygon",queryPrefix,
				    " SELECT ?result " + 
				    "  WHERE { " +
					"  			?s1 clc:asWKT ?result. " +
					"  ?s1 geo:sfOverlaps \"" + fixedPolygon + "\"^^geo:wktLiteral. " +
				    " } ");
	
			Q12 = new SPARQLQuerySetMember("Q12:Crosses_LGD_GivenLine",queryPrefix,
				    " SELECT ?result "  +
				    "  WHERE { " + 
					"  			?s1 lgd:asWKT ?result. " +
					"  ?s1 geo:sfCrosses \"" + fixedLine + "\"^^geo:wktLiteral. " +
				    " }");
	
			Q13 = new SPARQLQuerySetMember("Q13:Within_GeoNames_GivenPolygon",queryPrefix,
					" SELECT ?result " +
				    "  WHERE { " + 
					"  			?s1 geonames:asWKT ?result. " + 
					"  ?s1 geo:sfWithin \"" + fixedPolygon + "\"^^geo:wktLiteral. " +
				    "  }" );  
	
		/*	Q14 = new SPARQLQuerySetMember("Q14:Within_GeoNames_Point_Buffer",queryPrefix,
					" SELECT ?result " + 
				    "  WHERE { " + 
					"  		 		?s1 geonames:asWKT ?result. " + 
					"  ?result geo:sfWithin geof:buffer(\"" + fixedPoint + "\"^^geo:wktLiteral , 3000, opengis:metre). " +  
				    " } "); */
	
			Q16 = new SPARQLQuerySetMember("Q16:Disjoint_GeoNames_GivenPolygon",queryPrefix,
					"  SELECT ?result " +
				    "   WHERE { " +
					"   			?s1 geonames:asWKT ?result. " + 
					"   ?s1 geo:sfDisjoint \"" + fixedPolygon + "\"^^geo:wktLiteral. " +
				    "   }" );  
			
			Q17 = new SPARQLQuerySetMember("Q17:Disjoint_LGD_GivenPolygon",queryPrefix,
					"	SELECT ?result " + 
				    "    WHERE { " +
					"    			?s1 lgd:asWKT ?result. " + 
					"    ?s1 geo:sfDisjoint \"" + fixedPolygon + "\"^^geo:wktLiteral. " +
				    "    }" );
			
			Q18 = new SPARQLQuerySetMember("Q18:Equals_GeoNames_DBPedia",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result geonames:asWKT ?o1. " + 
					"           ?s2 dbpedia:asWKT ?o2. " +  
					"			?result geo:sfEquals ?s2." +  
					"		}" ); 
					
			Q19 = new SPARQLQuerySetMember("Q19:Intersects_GeoNames_LGD",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result geonames:asWKT ?o1. " + 
					"           ?s2 lgd:asWKT ?o2. " +  
					"			?result geo:sfIntersects ?s2." +  
					"		}" ); 
			
			Q20 = new SPARQLQuerySetMember("Q20:Intersects_GeoNames_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result geonames:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			?result geo:sfIntersects ?s2." +  
					"		}" ); 
			
			Q21 = new SPARQLQuerySetMember("Q21:Intersects_LGD_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result lgd:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			?result geo:sfIntersects ?s2." +  
					"		}" ); 
			
			Q22 = new SPARQLQuerySetMember("Q22:Within_GeoNames_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result geonames:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			?result geo:sfWithin ?s2." +  
					"		}" ); 
			
			Q23 = new SPARQLQuerySetMember("Q23:Within_LGD_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result lgd:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			?result geo:sfWithin ?s2." +  
					"		}" ); 
			
			Q24 = new SPARQLQuerySetMember("Q24:Within_CLC_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result clc:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			?result geo:sfWithin ?s2." +  
					"		}" ); 
			
			Q25 = new SPARQLQuerySetMember("Q25:Crosses_LGD_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result lgd:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			?result geo:sfCrosses ?s2." +  
					"		}" ); 
			
			Q26 = new SPARQLQuerySetMember("Q26:Touches_GAG_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result gag:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			?result geo:sfTouches ?s2." +  
					"		}" );
			
			Q27 = new SPARQLQuerySetMember("Q27:Overlaps_GAG_CLC",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result gag:asWKT ?o1. " + 
					"           ?s2 clc:asWKT ?o2. " +  
					"			?result geo:sfOverlaps ?s2." +  
					"		}" ); 	
		}
		else {
			Q7 = new SPARQLQuerySetMember("Q7:Equals_LGD_GivenLine",queryPrefix,
				  	" SELECT ?result " +
				    "  WHERE { " +
					"  			?s1 lgd:asWKT ?result. " + 
					"  FILTER(geof:sfEquals(?result, \"" + fixedLine + "\"^^geo:wktLiteral)). " +
				    " }");  
	
			Q8 = new SPARQLQuerySetMember("Q8:Equals_GAG_GivenPolygon",queryPrefix,
					" SELECT ?result " +
				    " WHERE { " + 
					" 			?s1 gag:asWKT ?result. " + 
					"  FILTER(geof:sfEquals(?result, \"" + fixedPolygon + "\"^^geo:wktLiteral)). " +
				    " }");  
			
			Q9 = new SPARQLQuerySetMember("Q9:Intersects_LGD_GivenPolygon",queryPrefix,
				    " SELECT ?result " + 
				    " WHERE { " + 
					" 			?s1 lgd:asWKT ?result. " +
					" FILTER(geof:sfIntersects(?result,\"" + fixedPolygon + "\"^^geo:wktLiteral)). " +
			 	    " }");
			
			Q10 = new SPARQLQuerySetMember("Q10:Intersects_CLC_GivenLine",queryPrefix,
					" SELECT ?result " + 
				    " WHERE { " +  
					" 			?s1 clc:asWKT ?result. " +
					" FILTER(geof:sfIntersects(?result,\"" + fixedLine + "\"^^geo:wktLiteral)). " +
				    " } " );
	
			Q11 = new SPARQLQuerySetMember("Q11:Overlaps_CLC_GivenPolygon",queryPrefix,
				    " SELECT ?result " + 
				    "  WHERE { " +
					"  			?s1 clc:asWKT ?result. " +
					"  FILTER(geof:sfOverlaps(?result,\"" + fixedPolygon + "\"^^geo:wktLiteral)). " +
				    " } ");
	
			Q12 = new SPARQLQuerySetMember("Q12:Crosses_LGD_GivenLine",queryPrefix,
				    " SELECT ?result "  +
				    "  WHERE { " + 
					"  			?s1 lgd:asWKT ?result. " +
					"  FILTER(geof:sfCrosses(?result,\"" + fixedLine + "\"^^geo:wktLiteral)). " +
				    " }");
	
			Q13 = new SPARQLQuerySetMember("Q13:Within_GeoNames_GivenPolygon",queryPrefix,
					" SELECT ?result " +
				    "  WHERE { " + 
					"  			?s1 geonames:asWKT ?result. " + 
					"  FILTER(geof:sfWithin(?result,\"" + fixedPolygon + "\"^^geo:wktLiteral)). " +
				    "  }" );  
	
			Q16 = new SPARQLQuerySetMember("Q16:Disjoint_GeoNames_GivenPolygon",queryPrefix,
					"  SELECT ?result " +
				    "   WHERE { " +
					"   			?s1 geonames:asWKT ?result. " + 
					"   FILTER(geof:sfDisjoint(?result,\"" + fixedPolygon + "\"^^geo:wktLiteral)). " +
				    "   }" );  
			
			Q17 = new SPARQLQuerySetMember("Q17:Disjoint_LGD_GivenPolygon",queryPrefix,
					"	SELECT ?result " + 
				    "    WHERE { " +
					"    			?s1 lgd:asWKT ?result. " + 
					"    FILTER(geof:sfDisjoint(?result,\"" + fixedPolygon + "\"^^geo:wktLiteral)). " +
				    "    }" );
			
			Q18 = new SPARQLQuerySetMember("Q18:Equals_GeoNames_DBPedia",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result geonames:asWKT ?o1. " + 
					"           ?s2 dbpedia:asWKT ?o2. " +  
					"			FILTER(geof:sfEquals(?o1, ?o2))." +  
					"		}" ); 
					
			Q19 = new SPARQLQuerySetMember("Q19:Intersects_GeoNames_LGD",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result geonames:asWKT ?o1. " + 
					"           ?s2 lgd:asWKT ?o2. " +  
					"			FILTER(geof:sfIntersects(?o1, ?o2))." +  
					"		}" ); 
			
			Q20 = new SPARQLQuerySetMember("Q20:Intersects_GeoNames_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result geonames:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			FILTER(geof:sfIntersects(?o1, ?o2))." +  
					"		}" ); 
			
			Q21 = new SPARQLQuerySetMember("Q21:Intersects_LGD_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result lgd:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			FILTER(geof:sfIntersects(?o1, ?o2))." +  
					"		}" ); 
			
			Q22 = new SPARQLQuerySetMember("Q22:Within_GeoNames_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result geonames:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			FILTER(geof:sfWithin(?o1, ?o2))." +  
					"		}" ); 
			
			Q23 = new SPARQLQuerySetMember("Q23:Within_LGD_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result lgd:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			FILTER(geof:sfWithin(?o1, ?o2))." +  
					"		}" ); 
			
			Q24 = new SPARQLQuerySetMember("Q24:Within_CLC_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result clc:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			FILTER(geof:sfWithin(?o1, ?o2))." +  
					"		}" ); 
			
			Q25 = new SPARQLQuerySetMember("Q25:Crosses_LGD_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result lgd:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			FILTER(geof:sfCrosses(?o1, ?o2))." +  
					"		}" ); 
			
			Q26 = new SPARQLQuerySetMember("Q26:Touches_GAG_GAG",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result gag:asWKT ?o1. " + 
					"           ?s2 gag:asWKT ?o2. " +  
					"			FILTER(geof:sfTouches(?o1, ?o2))." +  
					"		}" );
			
			Q27 = new SPARQLQuerySetMember("Q27:Overlaps_GAG_CLC",queryPrefix,
					"	SELECT ?result " +
					"	 WHERE { " + 
					"			?result gag:asWKT ?o1. " + 
					"           ?s2 clc:asWKT ?o2. " +  
					"			FILTER(geof:sfOverlaps(?o1, ?o2))." +  
					"		}" ); 
		}
		querySet.add(Q1);
		querySet.add(Q2);
		querySet.add(Q3);
		querySet.add(Q4);
		querySet.add(Q5);
		querySet.add(Q7);
		querySet.add(Q8);
		querySet.add(Q9);
		querySet.add(Q10);
		querySet.add(Q11);
		querySet.add(Q12);
		querySet.add(Q13);
		querySet.add(Q15);
		querySet.add(Q16);
		querySet.add(Q17);
		querySet.add(Q18);
		querySet.add(Q19);
		querySet.add(Q20);
		querySet.add(Q21);
		querySet.add(Q22);
		querySet.add(Q23);
		querySet.add(Q24);
		querySet.add(Q25);
		querySet.add(Q26);
		querySet.add(Q27);
		

	}
}
