package cz.cvut.palislub.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: Lubos Palisek
 * Date: 17. 2. 2015
 */
public class CustomNode {

	private String label;

	private String uniqueKey;

	private Set<String> indexes = new HashSet();

	private Map<String, Object> properties = new HashMap();

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public Set<String> getIndexes() {
		return indexes;
	}

	public void setIndexes(Set<String> indexes) {
		this.indexes = indexes;
	}

	public void addIndex(String key) {
		indexes.add(key);
	}

	public void addProperty(String key, Object obj) {
		this.properties.put(key, obj);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Object getProperty(String key) {
		return properties.get(key);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("CustomNode{");
		sb.append("label='").append(label).append('\'');
		sb.append("uniqueKey='").append(uniqueKey).append('\'');
		sb.append(", properties='{");
		for (String key : properties.keySet()) {
			sb.append(key + " - " + properties.get(key));
			sb.append(", ");
		}
		sb.append("}, indexes='{");
		for (String key : indexes) {
			sb.append(key);
			sb.append(", ");
		}
		sb.append('}');
		return sb.toString();
	}
}
