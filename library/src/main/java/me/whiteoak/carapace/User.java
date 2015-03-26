package me.whiteoak.carapace;

import lombok.Value;

/**
 * A value-type for authorization data.
 *
 * @author White Oak
 */
@Value public class User {

    private int id;
    private String password;
}
