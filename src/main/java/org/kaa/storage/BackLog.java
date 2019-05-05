package org.kaa.storage;

/**
 * @author sbt-kopilov-aa
 * @version 5.0
 * @since 18/12/2014
 * класс для хранения вариантов решения
 */
//public class BackLog extends LinkedList<RealSpace> {
public class BackLog extends Storage {

    public BackLog(long backlogLimit, long serializationPackSize) {
        this.backlogLimit = backlogLimit;
        this.serializationPackSize = serializationPackSize;
    }

}
