package org.kaa.storage;

import org.kaa.model.RealSpace;

/**
 * @author sbt-kopilov-aa
 * @version 5.0
 * @since 18/12/2014
 * класс для хранения вариантов решения
 */
//public class BackLog extends LinkedList<RealSpace> {
public class BackLog extends Storage {

	public BackLog(long backlogLimit, long serializationPackSize, RealSpace space) {
		this.space = space;
		this.backlogLimit = backlogLimit;
		this.serializationPackSize = serializationPackSize;
	}

}
