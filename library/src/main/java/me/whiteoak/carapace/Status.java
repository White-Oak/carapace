package me.whiteoak.carapace;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 *
 * @author White Oak
 */
@Value @AllArgsConstructor public class Status {

    private StatusType type;
    private Object message;

    public Status(StatusType type) {
	this(type, null);
    }

}
