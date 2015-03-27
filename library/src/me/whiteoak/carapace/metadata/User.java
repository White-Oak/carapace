package me.whiteoak.carapace.metadata;

import lombok.Value;

/**
 * A value-type describing authorization data for a user.
 *
 * @author White Oak
 */
@Value public class User {

    private int id;
    private String password;
}
