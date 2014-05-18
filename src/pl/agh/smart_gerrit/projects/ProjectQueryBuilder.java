package pl.agh.smart_gerrit.projects;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pl.agh.smart_gerrit.GerritClientQuery;

public class ProjectQueryBuilder {

	public static enum Type{
		LIST, ITEM, BRANCHES
	}
	
	private static final Map<String, String> DEFAULT_LIST_PARAM_MAP;
	private static final List<String> DEFAULT_URL;
	
	static {
		DEFAULT_LIST_PARAM_MAP = new TreeMap<String, String>();
		DEFAULT_LIST_PARAM_MAP.put("d", null);
		DEFAULT_LIST_PARAM_MAP.put("n", "25"); // number of projects
		DEFAULT_LIST_PARAM_MAP.put("S", "0"); // offset
		
		DEFAULT_URL= new ArrayList<String>();
		DEFAULT_URL.add("projects");
	}
	
	public static Query getBuider(Type type){
		return new Query(type);
	}
	
	public static class Query implements GerritClientQuery{
		private Map<String,String> params = new TreeMap<String, String>();
		private List<String> url = new ArrayList<String>();
	
		private Query(Type type){
			switch (type) {
			case LIST:
				params.putAll(DEFAULT_LIST_PARAM_MAP);
				break;

			default:
				break;
			}
			url.addAll(DEFAULT_URL);
			
		}
		
		public Map<String,String> getParams(){
			return params;
		}
		
		public Query setOffset(int i){
			params.put("S", Integer.toString(i));			
			return this;
		}

		public Query setPrefix(String pref){
			params.put("p", pref);			
			return this;
		}
		
		public Query setId(String part){
			url.add(part);			
			return this;
		}
		private boolean branchEnabled = false;
		public Query setBranchQuery(){
			branchEnabled = true;		
			return this;
		}

		@Override
		public List<String> getUrl() {
			if(branchEnabled){
				url.add("branches");
			}
			return url;
		}
	}
	
	
	
}
