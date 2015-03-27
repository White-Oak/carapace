package me.whiteoak.carapace;

import lombok.*;

/**
 * Describes a metadata for a topic.
 *
 * @author White Oak
 */
@AllArgsConstructor @RequiredArgsConstructor @Getter public class Topic {

    private final int id;
    private String name;

    private int postsToSkip;

    /**
     * Sets a number of posts to be skipped when reading topic through {@link Carapace#getTopic(me.whiteoak.carapace.Topic)}.
     *
     * @param postsToSkip number of posts to be skipped.
     */
    public void setPostsToSkip(int postsToSkip) {
	this.postsToSkip = postsToSkip;
    }

}
