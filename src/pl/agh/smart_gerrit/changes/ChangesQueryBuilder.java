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
		DEFAULT_PARAM_MAP.put("n", "15"); // number of changes
		//DEFAULT_PARAM_MAP.put("S", "0"); // offset
		

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
		private String reviewer;
		private String project;

		private Query() {
			params.putAll(DEFAULT_PARAM_MAP);
		}

		@Override
		public Map<String, String> getParams() {
			StringBuilder queryBuilder = new StringBuilder();
			boolean firstArrtibute = true;
			if(status!=null){
				firstArrtibute = appendAttribute(queryBuilder, firstArrtibute, "status", status.name().toLowerCase());
			}
			if(project!=null){
				firstArrtibute = appendAttribute(queryBuilder, firstArrtibute, "project", project);
			}
			if(owner!=null){
				firstArrtibute = appendAttribute(queryBuilder, firstArrtibute, "owner", owner);
			}
			if(reviewer!=null){
				firstArrtibute = appendAttribute(queryBuilder, firstArrtibute, "reviewer", reviewer);
			}
			params.put("q",queryBuilder.toString());
			return params;
		}

		private boolean appendAttribute(StringBuilder queryBuilder,
				boolean firstArrtibute, String type, String value) {
			if(firstArrtibute==false){
				queryBuilder.append("+");
			}
			firstArrtibute = false;
			queryBuilder.append(type);
			queryBuilder.append(":");
			queryBuilder.append(value);
			return firstArrtibute;
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
		
		public Query setOwner(String owner) {
			this.owner = owner;
			return this;
		}

		public Query setMy() {
			this.owner = "self";
			return this;
		}

		public Query setReviewer(String reviewer) {
			this.reviewer = reviewer;
			return this;
		}

		public Query setAssignedToMe() {
			this.reviewer = "self";
			return this;
		}
		
		
		@Override
		public List<String> getUrl() {
			return DEFAULT_URL;
		}
	}

}
