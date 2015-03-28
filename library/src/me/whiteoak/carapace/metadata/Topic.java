package me.whiteoak.carapace.metadata;

/**
 * Describes a metadata for a topic.
 *
 * @author White Oak
 */
public class Topic {

    private final int id;
    private final String name;
    private final int currentPost;
    /**
     * A number of posts to be skipped when reading topic through
     * {@link me.whiteoak.carapace.Carapace#getTopicPosts(me.whiteoak.carapace.metadata.Topic)}.
     */
    private final int lastPagePost;
    private final String updated;

    public int getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public int getCurrentPost() {
	return currentPost;
    }

    public int getLastPagePost() {
	return lastPagePost;
    }

    public String getUpdated() {
	return updated;
    }

    public Topic(int id, String name, int currentPost, int lastPagePost, String updated) {
	this.id = id;
	this.name = name;
	this.currentPost = currentPost;
	this.lastPagePost = lastPagePost;
	this.updated = updated;
    }

    public Topic(int id) {
	this(id, null, 0, -1, null);
    }

}
