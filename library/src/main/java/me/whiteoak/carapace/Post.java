package me.whiteoak.carapace;

import lombok.Value;

/**
 * A value-type containing data about a post.
 *
 * @author White Oak
 */
@Value public class Post {

    private String name;
    private String text;
}
