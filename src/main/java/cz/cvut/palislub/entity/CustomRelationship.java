package cz.cvut.palislub.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Lubos Palisek
 * Date: 18. 2. 2015
 */
public class CustomRelationship {

	private String label;

	private Object nodeValueFrom;

	private String nodeLabelFrom;

	private Object nodeValueTo;

	private String nodeLabelTo;

	private boolean unique;

	private Map<String, Object> properties = new HashMap();

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Object getNodeValueFrom() {
		return nodeValueFrom;
	}

	public void setNodeValueFrom(Object nodeValueFrom) {
		this.nodeValueFrom = nodeValueFrom;
	}

	public String getNodeLabelTo() {
		return nodeLabelTo;
	}

	public void setNodeLabelTo(String nodeLabelTo) {
		this.nodeLabelTo = nodeLabelTo;
	}

	public String getNodeLabelFrom() {
		return nodeLabelFrom;
	}

	public void setNodeLabelFrom(String nodeLabelFrom) {
		this.nodeLabelFrom = nodeLabelFrom;
	}

	public Object getNodeValueTo() {
		return nodeValueTo;
	}

	public void setNodeValueTo(Object nodeValueTo) {
		this.nodeValueTo = nodeValueTo;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void addProperty(String key, Object value) {
		properties.put(key, value);
	}

	public Object getProperty(String key) {
		return properties.get(key);
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}
}
