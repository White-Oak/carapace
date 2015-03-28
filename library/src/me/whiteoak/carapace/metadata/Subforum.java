package me.whiteoak.carapace.metadata;

/**
 *
 * @author White Oak
 */
public class Subforum {

    private final String name;
    private final int id;
    private final int topicsCount;
    private final Topic lastUpdatedTopic;

    public Subforum(String name, int id, int topicsCount, Topic lastUpdatedTopic) {
	this.name = name;
	this.id = id;
	this.topicsCount = topicsCount;
	this.lastUpdatedTopic = lastUpdatedTopic;
    }

    public String getName() {
	return name;
    }

    public int getId() {
	return id;
    }

    public int getTopicsCount() {
	return topicsCount;
    }

    public Topic getLastUpdatedTopic() {
	return lastUpdatedTopic;
    }

}
