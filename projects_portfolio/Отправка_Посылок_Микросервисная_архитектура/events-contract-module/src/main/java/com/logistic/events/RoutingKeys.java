package com.logistic.events;

public final class RoutingKeys {
    public static final String EXCHANGE = "logistic.events";

    public static final String SENDING_CREATED = "sending.created";
    public static final String SENDING_STATUS_UPDATED = "sending.status.updated";
    public static final String SENDING_DELETED = "sending.deleted";
    public static final String ENTRANT_CREATED = "entrant.created";
    public static final String ENTRANT_DELETED = "entrant.deleted";
    public static final String SENDING_ENRICHED = "sending.enriched";

    public static final String ALL_EVENTS = "#";
    public static final String ALL_SENDING_EVENTS = "sending.*";

    private RoutingKeys() {}
}

