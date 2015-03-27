package me.whiteoak.carapace;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * A common class that is used to inform about successes, errors.
 *
 * @see StatusType
 * @author White Oak
 */
@Value @AllArgsConstructor public class Status {

    private StatusType type;
    private Object message;

    public Status(StatusType type) {
	this(type, null);
    }

}
