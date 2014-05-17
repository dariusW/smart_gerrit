package pl.agh.smart_gerrit.changes;

import java.util.Map;
import java.util.TreeMap;

public class ChangesParamBuilder {

	private static final Map<String, String> DEFAULT_PARAM_MAP;
	
	static {
		DEFAULT_PARAM_MAP = new TreeMap<String, String>();
		DEFAULT_PARAM_MAP.put("d", null);
		DEFAULT_PARAM_MAP.put("n", "25"); // number of projects
		DEFAULT_PARAM_MAP.put("S", "0"); // offset
	}
	
	public static ParamHolder getBuider(){
		return new ParamHolder();
	}
	
	public static class ParamHolder{
		private Map<String,String> params = new TreeMap<String, String>();
	
		private ParamHolder(){
			params.putAll(DEFAULT_PARAM_MAP);
		}
		
		public Map<String,String> getParams(){
			return params;
		}
		
		public ParamHolder setOffset(int i){
			params.put("S", Integer.toString(i));			
			return this;
		}

		public ParamHolder setPrefix(String pref){
			params.put("p", pref);			
			return this;
		}
	}
	
	
	
}
