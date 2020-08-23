package org.stonlexx.gamelibrary.utility.query;

public interface ThrowableResponseHandler<R, O, T extends Throwable> {

    R handleResponse(O o) throws T;
}
