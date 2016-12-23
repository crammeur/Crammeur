/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.exceptions.repository;

/**
 * Created by Marc-Antoine on 2016-04-05.
 */
public final class NextAvailableIdException extends Exception {
    public NextAvailableIdException() {
        super();
    }

    public NextAvailableIdException(String message) {
        super(message);
    }

    public NextAvailableIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public NextAvailableIdException(Throwable cause) {
        super(cause);
    }
}
