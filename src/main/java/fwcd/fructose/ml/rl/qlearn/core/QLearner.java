package fwcd.fructose.ml.rl.qlearn.core;

import fwcd.fructose.ml.rl.qlearn.env.QAction;
import fwcd.fructose.ml.rl.qlearn.env.QState;

public interface QLearner<S extends QState<S, A>, A extends QAction> extends QAgent<S, A> {
	QFunction<S, A> getQFunction();

	double maxQ(S state);
}
