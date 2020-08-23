package org.stonlexx.gamelibrary.utility.query;

public interface ResponseHandler<R, O> {

    R handleResponse(O o);
}
