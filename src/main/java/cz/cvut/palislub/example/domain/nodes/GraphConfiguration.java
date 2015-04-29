package cz.cvut.palislub.example.domain.nodes;

import cz.cvut.palislub.annotations.Node;
import cz.cvut.palislub.annotations.NodeProperty;
import cz.cvut.palislub.annotations.Unique;

import java.util.Date;

/**
 * Created by lubos on 23.3.2015.
 */
@Node(name = "Configuration")
public class GraphConfiguration {

	@Unique
	@NodeProperty
	private long configurationId = 1;

	@NodeProperty
	private Date lastSynchronization;

	public GraphConfiguration() {
	}

	public void setLastSynchronization(Date lastSynchronization) {
		this.lastSynchronization = lastSynchronization;
	}

	public Date getLastSynchronization() {
		return lastSynchronization;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("GraphConfiguration{");
		sb.append("configurationId=").append(configurationId);
		sb.append(", lastSynchronization=").append(lastSynchronization);
		sb.append('}');
		return sb.toString();
	}
}
