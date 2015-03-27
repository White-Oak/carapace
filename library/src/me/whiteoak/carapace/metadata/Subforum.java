package me.whiteoak.carapace.metadata;

import lombok.Value;

/**
 *
 * @author White Oak
 */
@Value public class Subforum {

    private String name;
    private int id;
    private int topicsCount;
    private Topic lastUpdatedTopic;
}
