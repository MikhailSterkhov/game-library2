package org.stonlexx.gamelibrary.utility;

public interface ResponseHandler<R, O> {

    R handleResponse(O o);
}
