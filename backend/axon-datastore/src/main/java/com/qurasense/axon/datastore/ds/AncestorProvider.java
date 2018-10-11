package com.qurasense.axon.datastore.ds;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.PathElement;

public class AncestorProvider {

    public static final String AXON_PARENT = "AxonParent";
    public static final String DEFAULT = "default";
    public static final PathElement PATH_ELEMENT = PathElement.of(AXON_PARENT, DEFAULT);
    private final Key ancestorKey;

    public AncestorProvider(Datastore datastore) {
        ancestorKey = datastore.newKeyFactory().setKind(AXON_PARENT).newKey(DEFAULT);
    }

    public PathElement getAncestorPathElement() {
        return PATH_ELEMENT;
    }

    public Key getAncestorKey() {
        return ancestorKey;
    }

}
