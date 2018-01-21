package com.fredrikw.fructose.game;

/**
 * A move evaluator that can rate moves given
 * a board position. Implementations should provide
 * domain-specific knowledge about the game.
 * 
 * @author Fredrik
 *
 */
@FunctionalInterface
public interface MoveEvaluator {
	/**
	 * Rates a move given a state of the game. The evaluation should
	 * always happen in favor of the given role.
	 * 
	 * @param gameBeforeMove - The game state before the move
	 * @param gameAfterMove - The game state after the move
	 * @param move - The move to be evaluated
	 * @return A rating of the given move on the board
	 */
	<M extends GameMove, R extends GameRole> double rate(
			R role,
			GameState<M, R> gameBeforeMove,
			GameState<M, R> gameAfterMove,
			M move,
			double incrementalDepth
	);
}
