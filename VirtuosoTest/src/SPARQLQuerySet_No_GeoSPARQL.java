

import java.util.ArrayList;


public class SPARQLQuerySet_No_GeoSPARQL {
	
	private static String queryPrefix;
	static {
		StringBuilder strPrefix = new StringBuilder();
		strPrefix.append("PREFIX geo: <http://www.opengis.net/ont/geosparql#> ");
		strPrefix.append("PREFIX sf: <http://www.opengis.net/ont/sf#>");
		strPrefix.append("PREFIX geof: <http://www.opengis.net/def/function/geosparql/>");
		queryPrefix = strPrefix.toString();
	}

	private ArrayList<SPARQLQuerySetMember> querySet = new ArrayList<SPARQLQuerySetMember>();
	
	public int getQueryCount() {
		return(querySet.size());
	}
	
	public SPARQLQuerySetMember getItem(int index) {
		
		return(querySet.get(index));
	}
	public SPARQLQuerySet_No_GeoSPARQL() {
		
		SPARQLQuerySetMember Q1 = new SPARQLQuerySetMember("Q1:Boundary[Ext. Ring]=>Polygons",queryPrefix,			
				"SELECT ?result                                " + 
				"WHERE {                                       " + 
				"  ?geom1 a sf:Polygon;                        " + 
				"         geo:asWKT ?coord1.                   " + 
				"  BIND(bif:ST_ExteriorRing(?coord1) as ?result) .   " + 
				"}" );
		
		SPARQLQuerySetMember Q2 = new SPARQLQuerySetMember("Q2:Envelope[Bound Box]=>Polygons",queryPrefix,
				"SELECT ?result								" + 
				"WHERE {  									" + 
				"  ?geom1 a sf:Polygon;						" + 
				"         geo:asWKT ?coord1.				" + 
				"  BIND(bif:st_get_bounding_box(?coord1) as ?result). " + 
				"}" );
		
		SPARQLQuerySetMember Q3 = new SPARQLQuerySetMember("Q3:ConvexHull[Int. Ring]=>Polygons",queryPrefix, 
				"SELECT ?result 							  " + 
				"WHERE {									  " + 
				"  ?geom1 a sf:Polygon;						  " + 
				"         geo:asWKT ?coord1.				  " + 
				"  BIND(bif:ST_InteriorRing(?coord1,1) as ?result). " + 
				"}" );
		
		SPARQLQuerySetMember Q9 = new SPARQLQuerySetMember("Q9:Intersect=>Lines-aPolygon",queryPrefix,
				"SELECT ?result 							" + 
				"WHERE {									" + 
				"  ?geom1 a sf:LineString;					" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(bif:st_intersects(?result,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral))." +
				"}" );
		
		SPARQLQuerySetMember Q10 = new SPARQLQuerySetMember("Q10:Intesect=>Polygons-aPolygon",queryPrefix, 
				"SELECT ?result 							" + 
				"WHERE {									" + 
				"  ?geom1 a sf:Polygon;						" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(bif:st_intersects(?result,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral))." + 
				"}" );
				
		SPARQLQuerySetMember Q13 = new SPARQLQuerySetMember("Q13:Within=>Points-aPoly",queryPrefix,
				"SELECT ?result 							" + 
				"WHERE {									" + 
				"  ?geom1 a sf:Point;					" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(bif:st_within(?result,\"POLYGON ((11.269861 54.034543,15.168792 55.336793,18.585725 56.828099,22.425709 59.301356,29.805112 59.96095,29.165639 60.247796,23.763893 60.0552,17.899336 57.815479,16.68 56.142,12.515636 54.859636,12.533385 54.717942,12.576021 54.718478,12.874306 54.780347,12.875032 54.776845,12.864769 54.775192,12.574 54.718,12.556 54.716,12.530651 54.706441,12.52323 54.762456,11.248194 54.168128,11.269861 54.034543))\"^^geo:wktLiteral))." +
				"}" );
		
		SPARQLQuerySetMember Q15 = new SPARQLQuerySetMember("Q15:Nearby=>Points-aPoint",queryPrefix,
				"SELECT ?result 							" + 
				"WHERE {									" + 
				"  ?geom1 a sf:Point;					" + 
				"         geo:asWKT ?result.				" + 
				"  FILTER(bif:st_distance(?result,\"POINT(18.984339 57.430061)\"^^geo:wktLiteral)<500000)." +
				"}" );
		
		SPARQLQuerySetMember Q19 = new SPARQLQuerySetMember("Q19:Intersect=>Points-anyLines",queryPrefix,
				"SELECT distinct ?result 					   " + 
				"WHERE {									   " + 
				"  ?geom1 a sf:Point;					       " + 
				"         geo:asWKT ?result.				   " + 
				"  ?geom2 a sf:LineString;					   " + 
				"         geo:asWKT ?coord.				   	   " + 
				"  FILTER(bif:st_intersects(?result,?coord)).  " +
				"}" );
		
		SPARQLQuerySetMember Q20 = new SPARQLQuerySetMember("Q20:Intersect=>Points-anyPolygons",queryPrefix,
				"SELECT distinct ?result 					   " + 
				"WHERE {									   " + 
				"  ?geom1 a sf:Point;					       " + 
				"         geo:asWKT ?result.				   " + 
				"  ?geom2 a sf:Polygon;					 	   " + 
				"         geo:asWKT ?coord.				  	   " + 
				"  FILTER(bif:st_intersects(?result,?coord)).  " +
				"}" );
		
		SPARQLQuerySetMember Q21 = new SPARQLQuerySetMember("Q21:Intersect=>Lines-anyPolygons",queryPrefix,
				"SELECT distinct ?result 					   " + 
				"WHERE {									   " + 
				"  ?geom1 a sf:LineString;					   " + 
				"         geo:asWKT ?result.				   " + 
				"  ?geom2 a sf:Polygon;						   " + 
				"         geo:asWKT ?coord.				  	   " + 
				"  FILTER(bif:st_intersects(?result,?coord)).  " +
				"}" );
		
		SPARQLQuerySetMember Q22 = new SPARQLQuerySetMember("Q22:Within=>Points-anyPolygons",queryPrefix,
				"SELECT distinct ?result			 		  	" + 
				"WHERE {										" + 
				"  ?geom1 a sf:Point;						    " + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.					    " + 
				"  FILTER(bif:st_within(?result,?coord)).		" +
				"}" );
		
	
		SPARQLQuerySetMember Q23 = new SPARQLQuerySetMember("Q23:Within=>Lines-anyPolygons",queryPrefix,
				"SELECT distinct ?result			 		  	" + 
				"WHERE {										" + 
				"  ?geom1 a sf:LineString;						" + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.						" + 
				"  FILTER(bif:st_within(?result,?coord)).		" +
				"}" );
		
		SPARQLQuerySetMember Q24 = new SPARQLQuerySetMember("Q24:Within=>Polygons-anyPolygons",queryPrefix,
				"SELECT distinct ?result			 		  	" + 
				"WHERE {										" + 
				"  ?geom1 a sf:Polygon;							" + 
				"         geo:asWKT ?result.					" + 
				"  ?geom2 a sf:Polygon;							" + 
				"         geo:asWKT ?coord.					    " + 
				"  FILTER(?geom1 != ?geom2).					" +
				"  FILTER(bif:st_within(?result,?coord)).		" +
				"}" );
	
			
		querySet.add(Q1);
		querySet.add(Q2);
		//querySet.add(Q3);
		querySet.add(Q9);
		querySet.add(Q10);
		
		//querySet.add(Q13);
		querySet.add(Q15);
		
		querySet.add(Q19);
		querySet.add(Q20);
		querySet.add(Q21);
		querySet.add(Q22);
		querySet.add(Q23);
		querySet.add(Q24);
		
	
		
	}
}
