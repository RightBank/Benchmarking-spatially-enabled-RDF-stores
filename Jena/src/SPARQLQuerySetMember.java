

public class SPARQLQuerySetMember{
	
	private String queryName;
	private String queryString;
	
	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	public SPARQLQuerySetMember(String queryName, String queryPrefix, String queryString) {
		this.queryName = queryName;
		this.queryString = queryPrefix + queryString;
		
	}

	
}


