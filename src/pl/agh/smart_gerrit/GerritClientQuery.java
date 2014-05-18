package pl.agh.smart_gerrit;

import java.util.List;
import java.util.Map;

public interface GerritClientQuery {
	public Map<String, String> getParams();

	public List<String> getUrl();
}
