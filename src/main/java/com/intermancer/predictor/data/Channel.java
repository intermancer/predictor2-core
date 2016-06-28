package com.intermancer.predictor.data;

/**
 * A Channel is a single data element. Source Channels, or Channels that are
 * initially fed to an organism, won't have a source.
 * 
 * @author johnfryar
 * 
 */
public class Channel {

	private QuantumConsumer source;
	private Double value;

	public Channel() {
		// Nothing.
	}

	public Channel(QuantumConsumer source, Double value) {
		this.source = source;
		this.value = value;
	}

	public Channel(Double value) {
		this.value = value;
	}

	public QuantumConsumer getSource() {
		return source;
	}

	public void setSource(QuantumConsumer source) {
		this.source = source;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Channel clone() {
		Channel channel = new Channel(this.source, this.value);
		return channel;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Channel) {
			return ((Channel) obj).getValue().equals(getValue());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getValue().intValue();
	}

	@Override
	public String toString() {
		return value.toString();
	}

}
