package me.whiteoak.carapace;

import lombok.Value;

/**
 * A value-type containing data about a post.
 *
 * @author White Oak
 */
@Value public class Post {

    private String nickname;
    private String username;
    private String status;
    private boolean online;
    private String date;
    private String text;
}
