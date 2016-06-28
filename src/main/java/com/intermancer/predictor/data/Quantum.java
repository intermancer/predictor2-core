package com.intermancer.predictor.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Quantum represents a "bundle" of information which is processed by an
 * Organism. Every Gene adds one or more Channels to the Quantum.
 * 
 * @author johnfryar
 * 
 */
public class Quantum {

	private List<Channel> channels;
	private Date timestamp;

	private int compressionBoundary = 0;

	public Quantum() {
		this.channels = new ArrayList<Channel>();
	}

	/**
	 * Getter for channel property.
	 * 
	 * @param index
	 *            Offset in List. The actual index used is a mod of the given
	 *            index.
	 * @return The Channel at the given index. null iff the List is empty.
	 */
	public Channel getChannel(int index) {
		int indexActual = index % channels.size();
		if (indexActual < 0) {
			indexActual = channels.size() + indexActual;
		}
		return channels.get(indexActual);
	}

	public void addChannel(Channel channel) {
		channels.add(channel);
	}

	/**
	 * A Quantum has a single timestamp for all of its channels.
	 * 
	 * @return The timestamp property.
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Setter for timestamp property.
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void removeChannel(int index) {
		channels.remove(index);
	}

	public void clearAllChannels() {
		channels.clear();
	}

	public Channel getLastChannel() {
		return channels.get(channels.size() - 1);
	}

	public int getCompressionBoundary() {
		return compressionBoundary;
	}

	public void setCompressionBoundary(int compressionBoundary) {
		this.compressionBoundary = compressionBoundary;
	}

	public void setCompressionBoundary() {
		compressionBoundary = channels.size();
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	@Override
	public boolean equals(Object obj) {
		boolean returnValue = true;
		if (obj instanceof Quantum) {
			Quantum other = (Quantum) obj;
			if (getChannels().size() == other.getChannels().size()) {
				for (int i = 0; i < getChannels().size(); i++) {
					Channel channel1 = getChannel(i);
					Channel channel2 = other.getChannel(i);
					if (!(channel1.equals(channel2))) {
						returnValue = false;
						break;
					}
				}
				return returnValue;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashValue = 0;
		for (Channel channel : getChannels()) {
			hashValue += channel.hashCode();
		}
		return hashValue;
	}

	public void compress() {
		if (compressionBoundary < channels.size() - 1) {
			List<Channel> previouslyCompressed = channels.subList(0,
					compressionBoundary);
			Channel lastChannel = getLastChannel();
			setChannels(previouslyCompressed);
			addChannel(lastChannel);
		}
		setCompressionBoundary();
	}

	public Quantum clone() {
		Quantum quantum = new Quantum();
		quantum.setTimestamp(new Date(timestamp.getTime()));
		List<Channel> newChannels = new ArrayList<Channel>(channels.size());
		for (Channel channel : channels) {
			newChannels.add(channel.clone());
		}
		quantum.setChannels(newChannels);
		quantum.setCompressionBoundary();
		return quantum;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(Channel channel : getChannels()) {
			builder.append(channel.toString());
			builder.append("||");
		}
		return builder.toString();
	}

}
