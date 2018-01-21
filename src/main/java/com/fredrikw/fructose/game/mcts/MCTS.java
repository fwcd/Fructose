package com.fredrikw.fructose.game.mcts;

import java.util.Optional;

import com.fredrikw.fructose.game.GameMove;
import com.fredrikw.fructose.game.GameRole;
import com.fredrikw.fructose.game.GameState;
import com.fredrikw.fructose.game.TemplateGameAI;
import com.fredrikw.fructose.swing.TreePlotter;
import com.fredrikw.fructose.time.Timer;

/**
 * A monte-carlo-tree-search. It doesn't need
 * a domain-specific heuristic and thus is very generally applicable.
 * 
 * @author Fredrik
 *
 */
public class MCTS extends TemplateGameAI {
	private Optional<TreePlotter> plotter = Optional.empty();

	public void setPlotter(TreePlotter plotter) {
		this.plotter = Optional.of(plotter);
	}
	
	@Override
	protected <M extends GameMove, R extends GameRole> M selectMove(
			GameState<M, R> game,
			long softMaxTime
	) {
		Timer timer = new Timer();
		timer.start(softMaxTime);
		
		MCTSNode<M> node = new MCTSNode<>(game.getCurrentRole(), game);
		
		while (timer.isRunning()) {
			node.performIteration();
		}
		
		plotter.ifPresent(plotter -> plotter.setTree(node));
		
		return node.mostExploredChild().getMove();
	}
}
