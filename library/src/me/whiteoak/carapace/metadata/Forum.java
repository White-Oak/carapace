package me.whiteoak.carapace.metadata;

import java.util.List;

/**
 *
 * @author White Oak
 */
public class Forum {

    private final String name;
    private final List<Subforum> forumsList;

    public Forum(String name, List<Subforum> forumsList) {
	this.name = name;
	this.forumsList = forumsList;
    }

    public String getName() {
	return name;
    }

    public List<Subforum> getForumsList() {
	return forumsList;
    }

}
