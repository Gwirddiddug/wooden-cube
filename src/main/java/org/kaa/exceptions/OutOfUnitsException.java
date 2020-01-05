package org.kaa.exceptions;

/**
 * Created by Gwirggiddug on 10.02.2015.
 */
public class OutOfUnitsException extends RuntimeException {
	@Override
	public String getMessage() {
		return "Out of units";
	}
}
