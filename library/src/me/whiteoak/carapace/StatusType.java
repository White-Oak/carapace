package me.whiteoak.carapace;

/**
 * Is used to describe type of {@link Status} message.
 *
 * @author White Oak
 */
public enum StatusType {

    /**
     * Means the state in which nothing is happening — user is not authorized and no data is available.
     */
    IDLE,
    /**
     * Generated right after the successfull authorization.
     */
    AUTHORIZED,
    /**
     * Any non-critical error (such as 404 or 302 code responses) is reported with this state.
     */
    ERROR,
    /**
     * Generated after actions that prolong your Online status.
     */
    ONLINE,
    /**
     * There is not much to be said.
     */
    SHITPOSTING
}
