package com.fwcd.fructose.game.ai;

import com.fwcd.fructose.exception.Rethrow;
import com.fwcd.fructose.game.GameMove;
import com.fwcd.fructose.game.GameRole;
import com.fwcd.fructose.game.GameState;
import com.fwcd.fructose.genetic.core.ManualPopulation;
import com.fwcd.fructose.genetic.operators.Decoder;
import com.fwcd.fructose.genetic.operators.Encoder;
import com.fwcd.fructose.ml.neural.SimplePerceptron;
import com.fwcd.fructose.time.Timer;

public class GeneticNeuralGameAI<M extends GameMove, R extends GameRole> extends EvaluatingGameAI<M, R> {
	private final ManualPopulation population = new ManualPopulation();
	private final SimplePerceptron neuralNet;
	
	private final Encoder<float[], GameState<M, R>> neuralEncoder;
	private final Decoder<float[], Float> neuralDecoder;
	
	/**
	 * Create a new GeneticNeuralAI.
	 * 
	 * <p><b>The first value in the networkLayerSizes NEEDS to
	 * match the float array size produced by the encoder and
	 * the last value in the networkLayerSizes NEEDS to match
	 * the float array size taken by the decoder!!</b></p>
	 * 
	 * @param networkLayerSizes - Contains the neural net sizes
	 * @param neuralEncoder - The encoder (converts a game-state to a neural net input)
	 * @param neuralDecoder - The decoder (converts the neural net output to a game-state rating)
	 */
	public GeneticNeuralGameAI(
			int[] networkLayerSizes,
			Encoder<float[], GameState<M, R>> neuralEncoder,
			Decoder<float[], Float> neuralDecoder
	) {
		this.neuralEncoder = neuralEncoder;
		this.neuralDecoder = neuralDecoder;
		
		neuralNet = new SimplePerceptron(networkLayerSizes);
		population.spawn(20, () -> new SimplePerceptron(networkLayerSizes).getWeights());
		sampleNetwork();
	}
	
	private void sampleNetwork() {
		neuralNet.setWeights(population.selectBestGenes());
	}

	@Override
	public void onGameStart(GameState<M, R> initialState, R role) {
		sampleNetwork();
	}
	
	@Override
	public void onGameEnd(GameState<M, R> finalState, R role) {
		int fitness = finalState.getWinners().contains(role) ? (100 - finalState.getMoveCount()) : 0;
		population.setFitness(neuralNet.getWeights(), fitness);
		population.evolve();
	}

	@Override
	protected double rateMove(GameState<M, R> gameBeforeMove, M move, Timer timer) {
		try {
			GameState<M, R> gameAfterMove = gameBeforeMove.spawnChild(move);
			return neuralDecoder.decode(neuralNet.compute(neuralEncoder.encode(gameAfterMove)));
		} catch (Exception e) {
			throw new Rethrow("An error occurred while rating the move.", e);
		}
	}
}
