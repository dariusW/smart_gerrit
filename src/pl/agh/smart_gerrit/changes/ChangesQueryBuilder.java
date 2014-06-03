package pl.agh.smart_gerrit.changes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pl.agh.smart_gerrit.GerritClientQuery;

public class ChangesQueryBuilder {

	private static final Map<String, String> DEFAULT_PARAM_MAP;
	private static final List<String> DEFAULT_URL;

	static {
		DEFAULT_PARAM_MAP = new TreeMap<String, String>();
		DEFAULT_PARAM_MAP.put("n", "25"); // number of changes
		DEFAULT_PARAM_MAP.put("S", "0"); // offset
		

		DEFAULT_URL= new ArrayList<String>();
		DEFAULT_URL.add("changes");
	}

	public static Query getBuider() {
		return new Query();
	}

	public static class Query implements GerritClientQuery {
		private Map<String, String> params = new TreeMap<String, String>();
		private CommitStatus status;
		private String owner;
		private String project;

		private Query() {
			params.putAll(DEFAULT_PARAM_MAP);
		}

		@Override
		public Map<String, String> getParams() {
			StringBuilder queryBuilder = new StringBuilder();
			boolean firstArrtibute = true;
			if(status!=null){
				firstArrtibute=false;
				queryBuilder.append("status:");
				queryBuilder.append(status.name().toLowerCase());
			}
			if(project!=null){
				if(firstArrtibute==false){
					firstArrtibute = false;
					queryBuilder.append("+");
				}
				queryBuilder.append("project:");
				queryBuilder.append(project);
			}
			params.put("q",queryBuilder.toString());
			return params;
		}

		public Query setProject(String projectWithParent){
			project =projectWithParent;
			return this;
		}
		
		public Query setOffset(int i) {
			params.put("S", Integer.toString(i));
			return this;
		}

		public Query setStatus(CommitStatus status) {
			this.status = status;
			return this;
		}

		@Override
		public List<String> getUrl() {
			return DEFAULT_URL;
		}
	}

}
