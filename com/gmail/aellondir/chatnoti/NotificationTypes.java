package com.gmail.aellondir.chatnoti;

/**
 *
 * @author James Hull
 * @serial McMod JPGH.0001 v1
 * @version 0.01
 */
public enum NotificationTypes {
    /**
     * Added to the Notifications queue when a General notification is needed.
     */
    GENERAL,
    /**
     * Added to the Notifications queue when an Admin notification is needed.
     */
    ADMIN,
    /**
     * Ditto for Watch user names.
     */
    W_UN,
    /**
     * NEVER ADDED TO THE NOTIFICATIONS QUEUE ONLY EVER SENT TO THE THREAD TO MAKE ABSOLUTELY SURE THAT IT DOES NOT TRY
     * TO MAKE A NOTE WHEN NONE IS NEEDED.
     */
    NIL;
}
