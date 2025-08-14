package org.kaa.storage;

import org.kaa.exceptions.OutOfUnitsException;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Gwirggiddug on 08.02.2015.
 */
public abstract class AbstractStorage<T> implements CommonStorage<T> {
	private final LinkedList<T> units = new LinkedList<>();

	/**
	 * забирает первый элемент из хранилища
	 *
	 * @return первй элемен в хранилище
	 */
	@Override
	public T getFirst() throws OutOfUnitsException {
		return get(0);
	}

	@Override
	public T getLast() {
		if (size() == 0) return null;
		return get(size() - 1);
	}

	private T get(int index) throws OutOfUnitsException {
		if (units.isEmpty()) {
			throw new OutOfUnitsException();
		}
		T t = units.get(index);
		units.remove(index);
		checkSize();
		return t;
	}

	/**
	 * проверяет не выходит ли хранилище за рамки минимального или максимального размера
	 * при необходимости сериализует или десериализует варианты решений
	 */
	abstract protected void checkSize();

	@Override
	public boolean add(T unit) {
		boolean add = this.units.add(unit);
		checkSize();
		return add;
	}

	@Override
	public boolean addAll(Collection<T> units) {
		boolean addAll = this.units.addAll(units);
		checkSize();
		return addAll;
	}

	@Override
	public int size() {
		return units.size();
	}

	@Override
	public void saveSolution(T solution) {

	}
}
