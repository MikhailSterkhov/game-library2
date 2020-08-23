package org.stonlexx.gamelibrary.core.event.exception;

public class EventException extends RuntimeException {

    public EventException(String errorMessage, Object... elements) {
        super(String.format(errorMessage, elements));
    }

}
