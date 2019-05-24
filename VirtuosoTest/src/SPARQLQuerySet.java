

import java.util.ArrayList;


public class SPARQLQuerySet implements ISPARQLQuerySet {
	
	private static String queryPrefix;
	static {
		StringBuilder strPrefix = new StringBuilder();
		strPrefix.append("PREFIX cpmeta1: <http://meta.icos-cp.eu/ontologies/cpmeta/>");
		strPrefix.append("PREFIX geo: <http://www.opengis.net/ont/geosparql#>");
		strPrefix.append("PREFIX sf: <http://www.opengis.net/ont/sf#>");
		strPrefix.append("PREFIX geof: <http://www.opengis.net/def/function/geosparql/>");
		strPrefix.append("PREFIX uom: <http://www.opengis.net/def/uom/OGC/1.0/>");
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
		
		SPARQLQuerySetMember Q1 = new SPARQLQuerySetMember("Q1:Boundary=>Polygons",queryPrefix,			
				"SELECT ?result                                " + 
				"WHERE {                                       " + 
				"  ?geom1 a sf:Polygon;                        " + 
				"         geo:asWKT ?coord1.                   " + 
				"  BIND(geof:boundary(?coord1) as ?result) .   " + 
				"}" );
		
		SPARQLQuerySetMember Q2 = new SPARQLQuerySetMember("Q2:Envelope=>Polygons",queryPrefix,
				"SELECT ?result								" + 
				"WHERE {  									" + 
				"  ?geom1 a sf:Polygon;						" + 
				"         geo:asWKT ?coord1.				" + 
				"  BIND(geof:envelope(?coord1) as ?result). " + 
				"}" );
		
		SPARQLQuerySetMember Q3 = new SPARQLQuerySetMember("Q3:ConvexHull=>Polygons",queryPrefix, 
				"SELECT ?result 							  " + 
				"WHERE {									  " + 
				"  ?geom1 a sf:Polygon;						  " + 
				"         geo:asWKT ?coord1.				  " + 
				"  BIND(geof:convexHull(?coord1) as ?result). " + 
				"}" );
		
		SPARQLQuerySetMember Q4 = new SPARQLQuerySetMember("Q4:Buffer=>Lines",queryPrefix,
				"SELECT ?result											  " + 
				"WHERE {									 			  " + 
				"  ?geom1 a sf:LineString;					 			  " + 
				"         geo:asWKT ?coord1.				 			  " + 
				"  BIND(geof:buffer(?coord1,100.0,uom:metre) as ?result). " + 
				"}" );
		
		SPARQLQuerySetMember Q5 = new SPARQLQuerySetMember("Q5:Buffer=>Polygons",queryPrefix, 
				"SELECT ?result											  " + 
				"WHERE {												  " + 
				"  ?geom1 a sf:Polygon;									  " + 
				"         geo:asWKT ?coord1.				 			  " + 
				"  BIND(geof:buffer(?coord1,100.0,uom:metre) as ?result). " + 
				"}" );
		
		SPARQLQuerySetMember Q7 = new SPARQLQuerySetMember("Q7:Equal=>Lines-aLine",queryPrefix, 
				"SELECT ?result								 " + 
				"WHERE {									 " + 
				"  ?geom1 a sf:LineString;					 " + 
				"         geo:asWKT ?result.				 " + 
				"  FILTER(geof:sfEquals(?result,\"LINESTRING (-42.983 59.343,-41.927 59.179,-41.168 59.104,-40.03101 59.137,-37.384 59.382,-36.268 59.538,-34.50699 59.756,-32.87201 59.939,-31.17999 60.088,-30.06799 60.195,-28.18701 60.33,-24.71201 60.532,-21.578 60.606,-18.43399 60.614,-12.10901 60.418,-8.091 60.138,-4.05801 59.719,-2.396 59.51,7.444 57.683,10.013 57.775)\"^^geo:wktLiteral)). " +
				"}" );
		
		SPARQLQuerySetMember Q8 = new SPARQLQuerySetMember("Q8:Equals=>Polygons-aPolygon",queryPrefix,
				"SELECT ?result								 " + 
				"WHERE {									 " + 
				"  ?geom1 a sf:Polygon;						 " + 
				"         geo:asWKT ?result.				 " + 
				"  FILTER(geof:sfEquals(?result,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral)). " + 
				"}" );
		
		SPARQLQuerySetMember Q9 = new SPARQLQuerySetMember("Q9:Intersect=>Lines-aPolygon",queryPrefix,
				"SELECT ?result 							" + 
				"WHERE {									" + 
				"  ?geom1 a sf:LineString;					" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(geof:sfIntersects(?result,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral))." +
				"}" );
		
		SPARQLQuerySetMember Q10 = new SPARQLQuerySetMember("Q10:Intesect=>Polygons-aPolygon",queryPrefix, 
				"SELECT ?result 							" + 
				"WHERE {									" + 
				"  ?geom1 a sf:Polygon;						" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(geof:sfIntersects(?result,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral))." + 
				"}" );
		
		SPARQLQuerySetMember Q11 = new SPARQLQuerySetMember("Q11:Overlap=>Polygons-aPolygon",queryPrefix,
				"SELECT ?result								" + 
				"WHERE {									" + 
				"  ?geom1 a sf:Polygon;						" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(geof:sfOverlaps(?result,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral))." +
				"}" );
		
		SPARQLQuerySetMember Q12 = new SPARQLQuerySetMember("Q12:Crosses=>Lines-aLine",queryPrefix,
				"SELECT ?result 							" + 
				"WHERE {									" + 
				"  ?geom1 a sf:LineString;					" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(geof:sfCrosses(?result,\"LINESTRING (-42.983 59.343,-41.927 59.179,-41.168 59.104,-40.03101 59.137,-37.384 59.382,-36.268 59.538,-34.50699 59.756,-32.87201 59.939,-31.17999 60.088,-30.06799 60.195,-28.18701 60.33,-24.71201 60.532,-21.578 60.606,-18.43399 60.614,-12.10901 60.418,-8.091 60.138,-4.05801 59.719,-2.396 59.51,7.444 57.683,10.013 57.775)\"^^geo:wktLiteral))." +
				"}" );
		
		SPARQLQuerySetMember Q13 = new SPARQLQuerySetMember("Q13:Within=>Points-aPoly",queryPrefix,
				"SELECT ?result 							" + 
				"WHERE {									" + 
				"  ?geom1 a sf:Point;					" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(geof:sfWithin(?result,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral))." +
				"}" );
		
		SPARQLQuerySetMember Q15 = new SPARQLQuerySetMember("Q15:Nearby=>Points-aPoint",queryPrefix,
				"SELECT ?result 							" + 
				"WHERE {									" + 
				"  ?geom1 a sf:Point;					" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(geof:distance(?result,\"POINT(18.984339 57.430061)\"^^geo:wktLiteral,uom:metre)<500000)." +
				"}" );
		
		SPARQLQuerySetMember Q16 = new SPARQLQuerySetMember("Q16:Disjoint=>Points-aPolygon",queryPrefix,
				"SELECT ?result								" + 
				"WHERE {									" + 
				"  ?geom1 a sf:Point;					" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(geof:sfDisjoint(?result,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral))." +
				"}" );
		
		SPARQLQuerySetMember Q17 = new SPARQLQuerySetMember("Q17:Disjoint=>Lines-aPolygon",queryPrefix,
				"SELECT ?result								" + 
				"WHERE {									" + 
				"  ?geom1 a sf:LineString;					" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(geof:sfDisjoint(?result,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral))." +
				"}" );
		
		SPARQLQuerySetMember Q18 = new SPARQLQuerySetMember("Q18:Equals=>Points-anyPoints",queryPrefix,
				"SELECT distinct ?result 					   " + 
				"WHERE {									   " + 
				"  ?geom1 a sf:Point;					       " + 
				"         geo:asWKT ?result.				   " + 
				"  ?geom2 a sf:Point;						   " + 
				"         geo:asWKT ?coord.				   " + 
				"  FILTER(?geom1 != ?geom2). 	   			   " +
				"  FILTER(geof:sfEquals(?result,?coord)). 	   " +
				"}" );
		
		SPARQLQuerySetMember Q18a = new SPARQLQuerySetMember("Q18a:Equals=>Points-anyPoints",queryPrefix,
				"SELECT distinct ?result 					   " + 
				"WHERE {									   " + 
				"  ?geom1 a sf:Point;					       " + 
				"         geo:asWKT ?result.				   " + 
				"  ?geom2 a sf:Point;						   " + 
				"         geo:asWKT ?coord.				   " + 
				"  ?result geo:sfEquals ?coord. 	   " +
				"}" );
	
		
		SPARQLQuerySetMember Q19 = new SPARQLQuerySetMember("Q19:Intersect=>Points-anyLines",queryPrefix,
				"SELECT distinct ?result 					   " + 
				"WHERE {									   " + 
				"  ?geom1 a sf:Point;					       " + 
				"         geo:asWKT ?result.				   " + 
				"  ?geom2 a sf:LineString;					   " + 
				"         geo:asWKT ?coord.				   	   " + 
				"  FILTER(geof:sfIntersects(?result,?coord)).  " +
				"}" );
		
		SPARQLQuerySetMember Q20 = new SPARQLQuerySetMember("Q20:Intersect=>Points-anyPolygons",queryPrefix,
				"SELECT distinct ?result 					   " + 
				"WHERE {									   " + 
				"  ?geom1 a sf:Point;					       " + 
				"         geo:asWKT ?result.				   " + 
				"  ?geom2 a sf:Polygon;					 	   " + 
				"         geo:asWKT ?coord.				  	   " + 
				"  FILTER(geof:sfIntersects(?result,?coord)).  " +
				"}" );
		
		SPARQLQuerySetMember Q20a = new SPARQLQuerySetMember("Q20a:Intersect=>Points-anyPolygons",queryPrefix,
				"SELECT distinct ?result 					   " + 
				"WHERE {									   " + 
				"  ?geom1 a sf:Point;					       " + 
				"         geo:asWKT ?result.				   " + 
				"  ?geom2 a sf:Polygon;					 	   " + 
				"         geo:asWKT ?coord.				  	   " + 
				"  ?result geo:sfIntersects ?coord.  " +
				"}" );
		
		
		SPARQLQuerySetMember Q21 = new SPARQLQuerySetMember("Q21:Intersect=>Lines-anyPolygons",queryPrefix,
				"SELECT distinct ?result 					   " + 
				"WHERE {									   " + 
				"  ?geom1 a sf:LineString;					   " + 
				"         geo:asWKT ?result.				   " + 
				"  ?geom2 a sf:Polygon;						   " + 
				"         geo:asWKT ?coord.				  	   " + 
				"  FILTER(geof:sfIntersects(?result,?coord)).  " +
				"}" );
		
		SPARQLQuerySetMember Q22 = new SPARQLQuerySetMember("Q22:Within=>Points-anyPolygons",queryPrefix,
				"SELECT distinct ?result			 		  	" + 
				"WHERE {										" + 
				"  ?geom1 a sf:Point;						    " + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.					    " + 
				"  FILTER(geof:sfWithin(?result,?coord)).		" +
				"}" );
		
		SPARQLQuerySetMember Q23 = new SPARQLQuerySetMember("Q23:Within=>Lines-anyPolygons",queryPrefix,
				"SELECT distinct ?result			 		  	" + 
				"WHERE {										" + 
				"  ?geom1 a sf:LineString;						" + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.						" + 
				"  FILTER(geof:sfWithin(?result,?coord)).		" +
				"}" );
		
		SPARQLQuerySetMember Q23a = new SPARQLQuerySetMember("Q23a:Within=>Lines-anyPolygons",queryPrefix,
				"SELECT distinct ?result			 		  	" + 
				"WHERE {										" + 
				"  ?geom1 a sf:LineString;						" + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.						" + 
				"  ?result geo:sfWithin ?coord.		" +
				"}" );
	
		SPARQLQuerySetMember Q24 = new SPARQLQuerySetMember("Q24:Within=>Polygons-anyPolygons",queryPrefix,
				"SELECT distinct ?result			 		  	" + 
				"WHERE {										" + 
				"  ?geom1 a sf:Polygon;							" + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.					    " + 
				"  FILTER(?geom1 != ?geom2).					" +
				"  FILTER(geof:sfWithin(?result,?coord)).		" +
				"}" );
		
		SPARQLQuerySetMember Q25 = new SPARQLQuerySetMember("Q25:Cross=>Lines-anyPolygons",queryPrefix,
				"SELECT distinct ?result						" + 
				"WHERE {										" + 
				"  ?geom1 a sf:LineString;						" + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.					    " + 
				"  FILTER(geof:sfCrosses(?result,?coord)).		" +
				"}" );
		
		SPARQLQuerySetMember Q26 = new SPARQLQuerySetMember("Q26:Touch=>Polygons-anyPolygons",queryPrefix,
				"SELECT distinct ?result 						" + 
				"WHERE {										" + 
				"  ?geom1 a sf:Polygon;							" + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.				     	" + 
				"  FILTER(?geom1!=?geom2).						" + 
				"  FILTER(geof:sfTouches(?result,?coord))		" +
				"}" );
		
		SPARQLQuerySetMember Q27 = new SPARQLQuerySetMember("Q27:Overlap=>Polygons-anyPolygons",queryPrefix,
				"SELECT distinct ?result 				" + 
				"WHERE {										" + 
				"  ?geom1 a sf:Polygon;							" + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.					    " + 
				"  FILTER(?geom1!=?geom2).						" + 
				"  FILTER(geof:sfOverlaps(?result,?coord))		" +
				"}" );
		
		
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
		querySet.add(Q18a);
		querySet.add(Q19);
		querySet.add(Q20);
		querySet.add(Q20a);
		querySet.add(Q21);
		querySet.add(Q22);
		querySet.add(Q23);
		querySet.add(Q23a);
		querySet.add(Q24);
		querySet.add(Q25);
		querySet.add(Q26);
		querySet.add(Q27);
	}
}
