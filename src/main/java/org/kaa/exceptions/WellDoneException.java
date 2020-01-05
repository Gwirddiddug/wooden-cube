package org.kaa.exceptions;

import lombok.Getter;
import org.kaa.model.RealSpace;

@Getter
public class WellDoneException extends RuntimeException {
	private RealSpace solution;

	public WellDoneException(RealSpace solution) {
		this.solution = solution;
	}
}
