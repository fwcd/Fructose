package com.fwcd.fructose.ml.function;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A very simple {@link LearningFunction} that learns through
 * memorization.
 * 
 * @author Fredrik
 *
 * @param <I> - The input type
 * @param <O> - The output type
 */
public class TabledFunction<I, O> implements LearningFunction<I, O> {
	private final Map<I, O> results = new HashMap<>();
	private Optional<O> defaultOutput;
	
	public TabledFunction() {
		defaultOutput = Optional.empty();
	}
	
	public TabledFunction(O defaultOutput) {
		this.defaultOutput = Optional.of(defaultOutput);
	}
	
	@Override
	public O compute(I input) {
		O result = results.get(input);
		
		if (result == null) {
			result = defaultOutput.orElseThrow(() -> new IllegalStateException("No output found for " + input.toString()));
		}
		
		return result;
	}

	@Override
	public void teach(Map<I, O> examples) {
		results.putAll(examples);
	}
}
