package com.fredrikw.fructose.genetic.core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.fredrikw.fructose.Distribution;
import com.fredrikw.fructose.function.ToFloatFunction;
import com.fredrikw.fructose.genetic.operators.Crossover;
import com.fredrikw.fructose.genetic.operators.Decoder;
import com.fredrikw.fructose.genetic.operators.Encoder;
import com.fredrikw.fructose.genetic.operators.FitnessFunction;
import com.fredrikw.fructose.genetic.operators.GaussianMutator;
import com.fredrikw.fructose.genetic.operators.Mutator;
import com.fredrikw.fructose.genetic.operators.UniformCrossover;

public class Population implements Serializable {
	private static final long serialVersionUID = -147928374982734L;
	private int[][] genotype;
	
	private Crossover crossover;
	private FitnessFunction fitnessFunc;
	private Mutator mutator;
	
	private float mutationChance; // Between 0 and 1
	private int generation = 0;
	
	public Population(
			Crossover crossover,
			FitnessFunction fitnessFunc,
			Mutator mutator,
			int[][] genotype,
			float mutationChance
	) {
		this.crossover = crossover;
		this.fitnessFunc = fitnessFunc;
		this.mutator = mutator;
		this.genotype = genotype;
	}
	
	public void setMutationChance(float chance) {
		mutationChance = chance;
	}
	
	/**
	 * @return The amount of individuals
	 */
	public int size() {
		return genotype.length;
	}
	
	public int getGeneration() {
		return generation;
	}
	
	public <T> T getIndividualPhenes(Decoder<T> decoder, int index) {
		return decoder.decode(genotype[index]);
	}
	
	public int[] getIndividualGenes(int index) {
		return genotype[index];
	}
	
	public void setGenotype(int[][] genotype) {
		this.genotype = genotype;
	}
	
	public int[][] getGenotype() {
		return genotype;
	}
	
	/**
	 * Selects an individual stochastically from the genotype using a
	 * weighted distribution based of the fitness values.
	 * 
	 * @return The index of the individual in the genotype
	 */
	private int select() {
		Distribution<int[]> dist = new Distribution<>();
		
		for (int[] individual : genotype) {
			dist.add(individual, fitnessFunc.getFitness(individual));
		}
		
		return dist.pickIndexStochastically();
	}
	
	/**
	 * Mutates every individual with the given chance (mutationChance).
	 */
	private void mutate() {
		Random random = ThreadLocalRandom.current();
		
		for (int i=0; i<genotype.length; i++) {
			if (random.nextFloat() < mutationChance) {
				int[] mutation = mutator.mutate(genotype[i]);
				genotype[i] = mutation;
			}
		}
	}
	
	public void evolve(int generations) {
		for (int i=0; i<generations; i++) {
			evolve();
		}
	}
	
	/**
	 * Evolves this population to the next generation. This
	 * includes selection, crossover and mutation.
	 */
	public void evolve() {
		int parentA = select();
		int parentB;
		
		do {
			parentB = select();
		} while (parentA == parentB); // Require two distinct parents
		
		int[] childA = crossover.crossover(genotype[parentA], genotype[parentB]);
		int[] childB = crossover.crossover(genotype[parentA], genotype[parentB]);
		
		genotype[parentA] = childA;
		genotype[parentB] = childB;
		
		mutate();
		generation++;
	}
	
	public int[] getFittestGenes() {
		int[] bestGenes = null;
		float maxFitness = Float.NEGATIVE_INFINITY;
		
		for (int[] genes : genotype) {
			float fitness = fitnessFunc.getFitness(genes);
			
			if (fitness > maxFitness) {
				maxFitness = fitness;
				bestGenes = genes;
			}
		}
		
		if (bestGenes != null) {
			return bestGenes;
		} else {
			throw new NoSuchElementException("Can't fetch the fittest genes when the genotype is empty!");
		}
	}
	
	public <T> T getFittestPhenes(Decoder<T> decoder) {
		return decoder.decode(getFittestGenes());
	}
	
	public Stream<int[]> streamGenes() {
		return Arrays.stream(genotype);
	}
	
	public <T> Stream<T> streamPhenes(Decoder<T> decoder) {
		return streamGenes().map(decoder::decode);
	}
	
	@Override
	public String toString() {
		String s = "Population:\n";
		
		for (int[] genes : genotype) {
			s += Arrays.toString(genes) + "\n";
		}
		
		return s;
	}
	
	public static class Builder {
		private Crossover crossoverFunc = new UniformCrossover();
		private Mutator mutator = new GaussianMutator();
		private FitnessFunction fitnessFunc;
		private float mutationChance = 0.1F;
		private Set<int[]> genotype = new HashSet<>();
		
		public Builder crossoverFunc(Crossover crossoverFunc) {
			this.crossoverFunc = crossoverFunc;
			return this;
		}
		
		public Builder mutator(Mutator mutator) {
			this.mutator = mutator;
			return this;
		}
		
		public <T> Builder fitnessFunc(Decoder<T> decoder, ToFloatFunction<T> decodedFitnessFunc) {
			fitnessFunc = genes -> decodedFitnessFunc.apply(decoder.decode(genes));
			return this;
		}
		
		public Builder fitnessFunc(FitnessFunction fitnessFunc) {
			this.fitnessFunc = fitnessFunc;
			return this;
		}
		
		public Builder mutationChance(float chance) {
			this.mutationChance = chance;
			return this;
		}
		
		public <T> Builder spawnIndividuals(Encoder<T> encoder, Supplier<T> supplier, int count) {
			for (int i=0; i<count; i++) {
				int[] genes = encoder.encode(supplier.get());
				
				if (genes.length == 0) {
					throw new UnsupportedOperationException("Individual can't have a gene sequence length of 0!");
				}
				
				genotype.add(genes);
			}
			return this;
		}
		
		public Population build() {
			if (crossoverFunc == null) {
				throw new IllegalStateException("Missing crossover function.");
			} else if (fitnessFunc == null) {
				throw new IllegalStateException("Missing fitness function.");
			} else if (mutator == null) {
				throw new IllegalStateException("Missing mutator function.");
			}
			
			return new Population(crossoverFunc, fitnessFunc, mutator, genotype.toArray(new int[0][0]), mutationChance);
		}
	}
}
