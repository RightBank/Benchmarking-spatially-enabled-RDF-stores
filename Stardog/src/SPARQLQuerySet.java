

import java.util.ArrayList;


public class SPARQLQuerySet implements ISPARQLQuerySet {
	
	private static String queryPrefix;
	static {
		StringBuilder strPrefix = new StringBuilder();
		strPrefix.append("PREFIX cpmeta1: <http://meta.icos-cp.eu/ontologies/cpmeta/>");
		strPrefix.append("PREFIX geo: <http://www.opengis.net/ont/geosparql#>");
		strPrefix.append("PREFIX sf: <http://www.opengis.net/ont/sf#>");
		strPrefix.append("PREFIX geof: <http://www.opengis.net/def/function/geosparql/>");
		strPrefix.append("prefix unit: <http://qudt.org/vocab/unit#>");
		queryPrefix = strPrefix.toString();
	}

	private ArrayList<SPARQLQuerySetMember> querySet = new ArrayList<SPARQLQuerySetMember>();
	
	public int getQueryCount() {
		return(querySet.size());
	}
	
	public SPARQLQuerySetMember getItem(int index) {
		
		return(querySet.get(index));
	}
	public SPARQLQuerySet() {
		
		SPARQLQuerySetMember Q7 = new SPARQLQuerySetMember("Q7:Equal=>Lines-aLine",queryPrefix, 
				"SELECT ?result								 " + 
				"WHERE {									 " + 
				"  ?geom1 a sf:LineString;					 " + 
				"         geo:asWKT ?result.				 " + 
				"  FILTER(geof:relate(?geom1, \"LINESTRING (-42.983 59.343,-41.927 59.179,-41.168 59.104,-40.03101 59.137,-37.384 59.382,-36.268 59.538,-34.50699 59.756,-32.87201 59.939,-31.17999 60.088,-30.06799 60.195,-28.18701 60.33,-24.71201 60.532,-21.578 60.606,-18.43399 60.614,-12.10901 60.418,-8.091 60.138,-4.05801 59.719,-2.396 59.51,7.444 57.683,10.013 57.775)\"^^geo:wktLiteral, geo:equals)) .  " +	
				"}" );
		
		SPARQLQuerySetMember Q8 = new SPARQLQuerySetMember("Q8:Equals=>Polygons-aPolygon",queryPrefix,
				"SELECT ?result								 " + 
				"WHERE {									 " + 
				"  ?geom1 a sf:Polygon;						 " + 
				"         geo:asWKT ?result.				 " + 
				"  FILTER(geof:relate(?geom1,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral,geo:equals)). " + 
				"}" );
		
		SPARQLQuerySetMember Q9 = new SPARQLQuerySetMember("Q9:Intersect=>Lines-aPolygon",queryPrefix,
				"SELECT ?result								 " + 
						"WHERE {									 " + 
						"  ?geom1 a sf:LineString;						 " + 
						"         geo:asWKT ?result.				 " + 
						"  FILTER(geof:relate(?geom1,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral,geo:intersects)). " + 
					    "}" );
		SPARQLQuerySetMember Q10 = new SPARQLQuerySetMember("Q10:Intersect=>Polygons-aPolygon",queryPrefix, 
				"SELECT ?result						 " + 
						"WHERE {									 " + 
						"  ?geom1 a sf:Polygon;						 " + 
						"         geo:asWKT ?result.				 " + 
						"  FILTER(geof:relate(?geom1,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral,geo:intersects)). " + 
						"}" );
		
		 SPARQLQuerySetMember Q13 = new SPARQLQuerySetMember("Q13:Within=>Points-aPoly",queryPrefix,
					"SELECT ?result 							" + 
					"WHERE {									" + 
					"  ?geom1 a sf:Point;					" + 
					"         geo:asWKT ?result.				" + 
					"  FILTER(geof:relate(?geom1,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral,geo:within)) ." +
					"}" );
		 
		SPARQLQuerySetMember Q13b = new SPARQLQuerySetMember("Q13b:Within=>Points-aPoly",queryPrefix,
				"SELECT ?result 							" + 
				"WHERE {									" + 
				"  ?geom1 a sf:Point;					" + 
				"         geo:asWKT ?result.				" + 
				"  ?geom1 geof:within \"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral ." +
				"}" );
		
		SPARQLQuerySetMember Q13c = new SPARQLQuerySetMember("Q13c:Within=>Points-aPoly",queryPrefix,
				"SELECT ?result 							" + 
				"WHERE {									" + 
				"  ?geom1 a sf:Point;					" + 
				"         geo:asWKT ?result.				" + 
				"  ?geom1 geo:within \"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral ." +
				"}" );
		
		SPARQLQuerySetMember Q15 = new SPARQLQuerySetMember("Q15:Nearby=>Points-aPoint",queryPrefix,
				"SELECT ?result 							" + 
				"WHERE {									" + 
				"  ?geom1 a sf:Point;					" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(geof:distance(?geom1, \"POINT(18.984339 57.430061)\"^^geo:wktLiteral, unit:Kilometer) < 500 ) . " +
				"}" );
		
		SPARQLQuerySetMember Q16 = new SPARQLQuerySetMember("Q16:Disjoint=>Points-aPolygon",queryPrefix,
				"SELECT ?result								" + 
				"WHERE {									" + 
				"  ?geom1 a sf:Point;					" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(geof:relate(?geom1,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral,geo:disjoint))." +
				"}" );
		
		SPARQLQuerySetMember Q17 = new SPARQLQuerySetMember("Q17:Disjoint=>Lines-aPolygon",queryPrefix,
				"SELECT ?result								" + 
				"WHERE {									" + 
				"  ?geom1 a sf:LineString;					" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(geof:relate(?geom1,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral,geo:disjoint))." +
				"}" );
		
		SPARQLQuerySetMember Q18 = new SPARQLQuerySetMember("Q18:Equals=>Points-anyPoints",queryPrefix,
				"SELECT distinct ?result 					   " + 
				"WHERE {									   " + 
				"  ?geom1 a sf:Point;					       " + 
				"         geo:asWKT ?result.				   " + 
				"  ?geom2 a sf:Point;						   " + 
				"         geo:asWKT ?coord.				       " + 
				"  FILTER(?geom1 != ?geom2). 	   			   " +
				"  FILTER(geof:relate(?geom1,?geom2,geo:equals)). 	  " +
				"}" );
		
		SPARQLQuerySetMember Q19 = new SPARQLQuerySetMember("Q19:Intersect=>Points-anyLines",queryPrefix,
				"SELECT distinct ?result 					   " + 
				"WHERE {									   " + 
				"  ?geom1 a sf:Point;					       " + 
				"         geo:asWKT ?result.				   " + 
				"  ?geom2 a sf:LineString;					   " + 
				"         geo:asWKT ?coord.				   	   " + 
				"  FILTER(geof:relate(?geom1,?geom2,geo:intersects)). 	       " +
				"}" );
		
			
		SPARQLQuerySetMember Q20 = new SPARQLQuerySetMember("Q20:Intersect=>Points-anyPolygons",queryPrefix,
				"SELECT distinct ?result 					   " + 
				"WHERE {									   " + 
				"  ?geom1 a sf:Point;					       " + 
				"         geo:asWKT ?result.				   " + 
				"  ?geom2 a sf:Polygon;					        " + 
				"         geo:asWKT ?coord.				   	   " + 
				"  FILTER(geof:relate(?geom1,?geom2,geo:intersects)). 	       " +
				"}" );
		
		
		SPARQLQuerySetMember Q21 = new SPARQLQuerySetMember("Q21:Intersect=>Lines-anyPolygons",queryPrefix,
				"SELECT distinct ?result 					   " + 
				"WHERE {									   " + 
				"  ?geom1 a sf:LineString;					       " + 
				"         geo:asWKT ?result.				   " + 
				"  ?geom2 a sf:Polygon;					   " + 
				"         geo:asWKT ?coord.				   	   " + 
				"  FILTER(geof:relate(?geom1,?geom2,geo:intersects)). 	       " +
				"}" );
			
		SPARQLQuerySetMember Q22 = new SPARQLQuerySetMember("Q22:Within=>Points-anyPolygons",queryPrefix,
				"SELECT distinct ?result			 		  	" + 
				"WHERE {										" + 
				"  ?geom1 a sf:Point;						    " + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.					    " + 
				"  FILTER(geof:relate(?geom1,?geom2,geo:within)) .		" +
				"}" );
		/*
		SPARQLQuerySetMember Q22b = new SPARQLQuerySetMember("Q22b:Within=>Points-anyPolygons",queryPrefix,
				"SELECT distinct ?geom1			 		  	" + 
				"WHERE {										" + 
				"  ?geom1 a sf:Point;						    " + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.					    " + 
				"  FILTER(geof:relate(?geom1,?geom2,geo:within)).		" +
				"}" );*/
		
		SPARQLQuerySetMember Q23 = new SPARQLQuerySetMember("Q23:Within=>Lines-anyPolygons",queryPrefix,
				"SELECT distinct ?result 			 		  	" + 
				"WHERE {										" + 
				"  ?geom1 a sf:LineString;						" + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.						" + 
				"  FILTER(geof:relate(?geom1,?geom2,geo:within)).		" +
				"}" );
		
		/*SPARQLQuerySetMember Q23b = new SPARQLQuerySetMember("Q23b:Within=>Lines-anyPolygons",queryPrefix,
				"SELECT distinct ?geom1 			 		  	" + 
				"WHERE {										" + 
				"  ?geom1 a sf:LineString;						" + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.						" + 
				"  ?geom1 geof:within ?geom2 .		" +
				"}" );*/
		
		SPARQLQuerySetMember Q24 = new SPARQLQuerySetMember("Q24:Within=>Polygons-anyPolygons",queryPrefix,
				"SELECT distinct ?result			 		  		" + 
				"WHERE {										" + 
				"  ?geom1 a sf:Polygon;							" + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.					    " + 
				"  FILTER(?geom1 != ?geom2).					" +
				"  FILTER(geof:relate(?geom1,?geom2,geo:within)).		" +
				"}" );
		
	/*	SPARQLQuerySetMember Q24b = new SPARQLQuerySetMember("Q24b:Within=>Polygons-anyPolygons",queryPrefix,
				"SELECT distinct ?geom1			 		  		" + 
				"WHERE {										" + 
				"  ?geom1 a sf:Polygon;							" + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.					    " + 
				"  FILTER(?geom1 != ?geom2).					" +
				"  ?geom1 geof:within ?geom2 .					" +
				"}" );*/
		
				
		/*SPARQLQuerySetMember Qx = new SPARQLQuerySetMember("Qx:Polys-within-direct",queryPrefix,
				"SELECT distinct ?geom1 ?geom2			 " + 
						"WHERE {	" +
						" ?geom1 a sf:Polygon; " +
						"       geo:asWKT ?coord. " +
						" ?geom2 a sf:Polygon; " +
						"       geo:asWKT ?res. " +
						" ?geom1 geof:within ?geom2 . " +
						" FILTER(?geom1!=?geom2)." +
						"}" );
		
		SPARQLQuerySetMember Qy = new SPARQLQuerySetMember("Qy:Polys-within-relate",queryPrefix,
				"SELECT distinct ?geom1 ?geom2			 " + 
						"WHERE {	" +
						" ?geom1 a sf:Polygon; " +
						"       geo:asWKT ?coord. " +
						" ?geom2 a sf:Polygon; " +
						"       geo:asWKT ?res. " +
						" FILTER(?geom1!=?geom2)." +
						" FILTER(geof:relate(?geom1,?geom2,geo:within))."+
						"}" );
		
		querySet.add(Qx);
		querySet.add(Qy);*/
		
		querySet.add(Q7);
		querySet.add(Q8);
		querySet.add(Q9);
		querySet.add(Q10);
		querySet.add(Q13);
		//querySet.add(Q13b);
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
				
	}
}
