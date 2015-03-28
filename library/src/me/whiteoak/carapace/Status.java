package me.whiteoak.carapace;

/**
 * A common class that is used to inform about successes, errors.
 *
 * @see StatusType
 * @author White Oak
 */
public class Status {

    private final StatusType type;
    private final Object message;

    public StatusType getType() {
	return type;
    }

    public Object getMessage() {
	return message;
    }

    public Status(StatusType type) {
	this(type, null);
    }

    public Status(StatusType type, Object message) {
	this.type = type;
	this.message = message;
    }

}
