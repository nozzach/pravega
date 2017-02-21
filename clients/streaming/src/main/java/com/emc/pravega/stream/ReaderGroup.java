/**
 *
 *  Copyright (c) 2017 Dell Inc., or its subsidiaries.
 *
 */
package com.emc.pravega.stream;

import com.emc.pravega.ClientFactory;

import java.util.List;
import java.util.Set;

/**
 * A reader group is a collection of readers that collectively read all the events in the
 * stream. The events are distributed among the readers in the group such that each event goes
 * to only one reader.
 *
 * The readers in the group may change over time. Readers are added to the group by calling
 * {@link ClientFactory#createReader(String, String, Serializer, ReaderConfig)} and are removed by
 * calling {@link #readerOffline(String, Position)}
 */
public interface ReaderGroup {

    /**
     * Returns the scope of the stream which the group is associated with.
     *
     * @return A scope string
     */
    String getScope();

    /**
     * Returns the names of the streams the group is associated with.
     *
     * @return List of stream names
     */
    List<String> getStreamNames();

    /**
     * Returns the name of the group.
     *
     * @return Reader group name
     */
    String getGroupName();

    /**
     * Returns the configuration of the reader group.
     *
     * @return Reader group configuration
     */
    ReaderGroupConfig getConfig();

    /**
     * Invoked when a reader that was added to the group is no longer consuming events. This will
     * cause the events that were going to that reader to be redistributed among the other
     * readers. Events after the lastPosition provided will be (re)read by other readers in the
     * {@link ReaderGroup}.
     *
     * Note that this method is automatically invoked by {@link EventStreamReader#close()}
     *
     * @param readerId The id of the reader that is offline.
     * @param lastPosition The position of the last event that was successfully processed by the
     *        reader.
     */
    void readerOffline(String readerId, Position lastPosition);

    /**
     * Returns a set of readerIds for the readers that are considered to be online by the group.
     * i.e. {@link ClientFactory#createReader(String, String, Serializer, ReaderConfig)} was called but
     * {@link #readerOffline(String, Position)} was not called subsequently.
     *
     * @return Set of active reader IDs of the group
     */
    Set<String> getOnlineReaders();
}