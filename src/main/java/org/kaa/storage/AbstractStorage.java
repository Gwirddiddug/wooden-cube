package org.kaa.storage;

import org.kaa.exceptions.OutOfUnitsException;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Gwirggiddug on 08.02.2015.
 */
public abstract class AbstractStorage<T> {
	private LinkedList<T> units = new LinkedList<>();

	/**
	 * забирает первый элемент из хранилища
	 *
	 * @return первй элемен в хранилище
	 */
	public T get() throws OutOfUnitsException {
		return get(0);
	}


	public T getLast() {
		return get(size() - 1);
	}

	private T get(int index) throws OutOfUnitsException {
		if (units.size() == 0) {
			throw new OutOfUnitsException();
		}
		T t = units.get(index);
		units.remove(index);
		checkSize();
		return t;
	}

	/**
	 * проверяет не выходит ли хранилище за рамки минимального или максимального размера
	 *
	 * @return true, если выходит
	 */
	abstract protected boolean checkSize();

	public boolean add(T unit) {
		boolean add = this.units.add(unit);
		checkSize();
		return add;
	}

	public boolean addAll(Collection<T> units) {
		boolean addAll = this.units.addAll(units);
		checkSize();
		return addAll;
	}

	public int size() {
		return units.size();
	}
}
