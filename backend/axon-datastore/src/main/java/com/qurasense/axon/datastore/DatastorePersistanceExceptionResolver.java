package com.qurasense.axon.datastore;

import com.google.cloud.datastore.DatastoreException;
import com.google.rpc.Code;
import org.axonframework.common.jdbc.PersistenceExceptionResolver;

public class DatastorePersistanceExceptionResolver implements PersistenceExceptionResolver {

    @Override
    public boolean isDuplicateKeyViolation(Exception exception) {
        Throwable exceptionCause = exception.getCause();
        if (exception instanceof DatastoreException && exceptionCause instanceof DatastoreException ) {
            if (((DatastoreException) exceptionCause).getCode() == Code.ALREADY_EXISTS.getNumber()) {
                return true;
            }
        }
        return false;
    }

}
