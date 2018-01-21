package com.fredrikw.fructose.game;

import java.util.List;
import java.util.Random;

public class RandomMoveChooser implements MoveChooser {
	private final Random random = new Random();
	
	@Override
	public <M extends GameMove, R extends GameRole> M chooseMove(GameState<M, R> game) {
		List<? extends M> moves = game.getLegalMoves();
		return moves.get(random.nextInt(moves.size()));
	}
}
