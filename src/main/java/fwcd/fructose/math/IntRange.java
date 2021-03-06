package fwcd.fructose.math;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An immutable, iterable range of integers
 * including the start and excluding the end.
 */
public class IntRange implements Iterable<Integer>, Serializable {
	private static final long serialVersionUID = 7827392109032747976L;
	private final int start;
	private final int end;
	private final int step;
	private final int value;
	
	public IntRange(int start, int end) {
		this(start, end, 1);
	}
	
	public IntRange(int start, int end, int step) {
		this(start, end, step, start);
	}
	
	public IntRange(int start, int end, int step, int value) {
		this.start = start;
		this.end = end;
		this.step = step;
		this.value = value;
	}
	
	public int length() {
		return end - start;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public int getStep() {
		return step;
	}
	
	public int getValue() {
		return value;
	}
	
	public List<Integer> asList() {
		List<Integer> list = new ArrayList<>();
		
		for (int n : this) {
			list.add(n);
		}
		
		return list;
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {
			int i = start - step;
			
			@Override
			public boolean hasNext() {
				return i < (end - step);
			}

			@Override
			public Integer next() {
				i += step;
				
				return i;
			}
		};
	}
}
