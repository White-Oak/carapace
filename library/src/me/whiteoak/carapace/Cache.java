package me.whiteoak.carapace;

import lombok.*;
import me.whiteoak.carapace.metadata.User;

/**
 *
 * @author White Oak
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE) @Getter(AccessLevel.PACKAGE) public class Cache {

    private final Settings settings;
    private final User user;
    private final Cookies cookies;
}
