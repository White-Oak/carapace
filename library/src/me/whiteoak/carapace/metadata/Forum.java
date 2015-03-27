package me.whiteoak.carapace.metadata;

import java.util.List;
import lombok.Value;

/**
 *
 * @author White Oak
 */
@Value public class Forum {

    private String name;
    private List<Subforum> forumsList;
}
