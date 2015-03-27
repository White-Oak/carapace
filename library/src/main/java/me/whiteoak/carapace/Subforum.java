package me.whiteoak.carapace;

import lombok.Value;

/**
 *
 * @author White Oak
 */
@Value public class Subforum {

    private String name;
    private int topicsCount;
    private Topic lastUpdatedTopic;
}
