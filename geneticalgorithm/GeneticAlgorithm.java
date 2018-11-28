/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author js2-keizer
 */
public class GeneticAlgorithm {

    /**
     * @param args the command line arguments
     */
    public static final double mutationProbability = 0.006;
    public static final double crossoverProbability = 0.6;
    public static Individual[] population = new Individual[200];
    public static final int numberOfGenerations = 100;

    public static void main(String[] args) {
        // TODO code application logic here

        for (int i = 0; i < population.length; i++) {
            population[i] = new Individual();
        }

        System.out.println("Best Fitness, Mean Fitness");
        System.out.println(getBestFitness(population) + "," + getMeanFitness(population));

        for (int i = 1; i <= numberOfGenerations; i++) {
            evolve();
            System.out.println(getBestFitness(population) + "," + getMeanFitness(population));
        }

        for (Individual candidateSolution : population) {
            if (candidateSolution.getFitness() == getBestFitness(population)) {
                System.out.println("Best Solution:");
                System.out.println(Arrays.toString(candidateSolution.getCandidateSolution()));
                break;
            }
        }

    }

    public static void evolve() {

        //save best candidate solution
        Individual best = new Individual();
        int bestFitness = getBestFitness(population);
        for (Individual candidateSolution : population) {
            if (candidateSolution.getFitness() == bestFitness) {
                best.setCandidateSolution(candidateSolution.getCandidateSolution());
                break;
            }
        }

        Individual[] offSpring = new Individual[200];
        Random random = new Random();

        //selection
        for (int i = 0; i < population.length; i++) {
            offSpring[i] = new Individual();
            int parent1 = random.nextInt(population.length);
            int parent2 = random.nextInt(population.length);
            if (population[parent1].getFitness() >= population[parent2].getFitness()) {
                offSpring[i].setCandidateSolution(population[parent1].getCandidateSolution());
            } else {
                offSpring[i].setCandidateSolution(population[parent2].getCandidateSolution());
            }
        }

        //crossover
        for (int i = 0; i < offSpring.length; i++) {
            if (random.nextDouble() <= crossoverProbability) {
                int crossoverPoint = random.nextInt(Individual.solutionSize);
                for (int j = 0; j < crossoverPoint; j++) {
                    int temp = offSpring[i].getGene(j);
                    offSpring[i].setGene(j, offSpring[i + 1].getGene(j));
                    offSpring[i + 1].setGene(j, temp);
                }
                offSpring[i].calculateFitness();
                offSpring[i + 1].calculateFitness();
            }
            i++;
        }

        //mutation
        for (Individual candidateSolution : offSpring) {
            for (int i = 0; i < Individual.solutionSize; i++) {
                if (random.nextDouble() <= mutationProbability) {
                    switch (candidateSolution.getGene(i)) {
                        case 1:
                            candidateSolution.setGene(i, 0);
                            break;
                        case 0:
                            candidateSolution.setGene(i, 1);
                            break;
                        default:
                            break;
                    }
                }
            }
            candidateSolution.calculateFitness();
        }

        //replace worst candidate solution with previous best
        int worstFitness = getWorstFitness(offSpring);
        for (Individual candidateSolution : offSpring) {
            if (candidateSolution.getFitness() == worstFitness) {
                candidateSolution.setCandidateSolution(best.getCandidateSolution());
                break;
            }
        }

        System.arraycopy(offSpring, 0, population, 0, population.length);
    }

    public static int getBestFitness(Individual[] population) {
        int best = 0;
        for (Individual candidateSolution : population) {
            if (candidateSolution.getFitness() > best) {
                best = candidateSolution.getFitness();
            }
        }
        return best;
    }

    public static int getWorstFitness(Individual[] population) {
        int worst = getBestFitness(population);
        for (Individual candidateSolution : population) {
            if (candidateSolution.getFitness() < worst) {
                worst = candidateSolution.getFitness();
            }
        }
        return worst;
    }

    public static int getMeanFitness(Individual[] population) {
        int meanFitness = 0;
        for (Individual candidateSolution : population) {
            meanFitness += candidateSolution.getFitness();
        }
        return meanFitness / population.length;
    }
}
