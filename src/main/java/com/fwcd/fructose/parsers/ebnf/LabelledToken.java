package com.fwcd.fructose.parsers.ebnf;

import java.util.OptionalInt;

/**
 * A labelled token. Mainly useful for parsing.
 * 
 * @author Fredrik
 *
 */
public class LabelledToken implements Token {
	private final Token token;
	private final String name;
	
	public LabelledToken(String name, Token token) {
		this.token = token;
		this.name = name;
	}
	
	@Override
	public boolean matches(Terminal... sequence) {
		return token.matches(sequence);
	}
	
	@Override
	public OptionalInt matchCount(Terminal... sequence) {
		return token.matchCount(sequence);
	}
	
	@Override
	public String toString() {
		return "<" + name + ">";
	}
}