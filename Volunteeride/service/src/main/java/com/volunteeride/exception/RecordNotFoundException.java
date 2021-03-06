package com.volunteeride.exception;

import com.volunteeride.exception.BaseVolunteerideRuntimeException;

/**
 * This class represents exceptions in scenarios where in an expected record is not found.
 *
 * Created by ayazlakdawala on 9/3/15.
 */
public class RecordNotFoundException extends BaseVolunteerideRuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public RecordNotFoundException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param messageKey the key to retrieve detailed message for exception from.
     */
    public RecordNotFoundException(String messageKey) {
        super(messageKey);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message     the detail message (which is saved for later retrieval
     *                    by the {@link #getMessage()} method).
     * @param cause       the cause (which is saved for later retrieval by the
     *                    {@link #getCause()} method).  (A <tt>null</tt> value is
     *                    permitted, and indicates that the cause is nonexistent or
     *                    unknown.)
     * @param customCause represents custom configured cause of the exception.
     * @param resolution  represents possible solution for the exception.
     * @param errorCode   represents errorcode related to exception
     * @since 1.4
     */
    public RecordNotFoundException(String message, Throwable cause, String customCause, String resolution, String errorCode) {
        super(message, cause, customCause, resolution, errorCode);
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message     the detail message. The detail message is saved for
     *                    later retrieval by the {@link #getMessage()} method.
     * @param customCause represents custom configured cause of the exception.
     * @param resolution  represents possible solution for the exception.
     * @param errorCode   represents errorcode related to exception
     */
    public RecordNotFoundException(String message, String customCause, String resolution, String errorCode) {
        super(message, customCause, resolution, errorCode);
    }

    /**
     * @param messageKey
     * @param args
     */
    public RecordNotFoundException(String messageKey, Object[] args){
        super(messageKey, args);
    }
}
