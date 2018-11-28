/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SecondGeneticAlgorithm;

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
    public static final double mutationProbability = 0.02;
    public static final double ruleOrderMutationProbability = 0.1;
    public static final double crossoverProbability = 0.2;
    public static Individual[] population = new Individual[100];
    public static final int numberOfGenerations = 500;

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

        Individual[] offSpring = new Individual[100];
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
                    if (i != 0 && (i + 1) % Rule.ruleSize == 0) {
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
                    } else {
                        int randomNumber = (int) Math.floor(Math.random() * 101);
                        switch (candidateSolution.getGene(i)) {
                            case 2:
                                if (randomNumber <= 50) {
                                    candidateSolution.setGene(i, 1);
                                } else {
                                    candidateSolution.setGene(i, 0);
                                }
                                break;
                            case 1:
                                if (randomNumber <= 50) {
                                    candidateSolution.setGene(i, 2);
                                } else {
                                    candidateSolution.setGene(i, 0);
                                }
                                break;
                            case 0:
                                if (randomNumber <= 50) {
                                    candidateSolution.setGene(i, 2);
                                } else {
                                    candidateSolution.setGene(i, 1);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            candidateSolution.calculateFitness();
        }

        //mutate order of rules
        for (Individual candidateSolution : offSpring) {
            if (random.nextDouble() <= ruleOrderMutationProbability) {
                int crossOverPoint = random.nextInt(Individual.numberOfRules - 1) + 1;
                int[] firstSubSet = new int[crossOverPoint * Rule.ruleSize];
                int[] secondSubSet = new int[(Individual.numberOfRules * Rule.ruleSize) - (crossOverPoint * Rule.ruleSize)];
                for(int i = 0; i < firstSubSet.length; i++){
                    firstSubSet[i] = candidateSolution.getGene(i);
                }
                for(int j = firstSubSet.length; j < secondSubSet.length; j++){
                    secondSubSet[j - firstSubSet.length] = candidateSolution.getGene(j);
                }
                int[] newCandidateSolution = new int[candidateSolution.getCandidateSolution().length];
                System.arraycopy(secondSubSet, 0, newCandidateSolution, 0, secondSubSet.length);
                System.arraycopy(firstSubSet, 0, newCandidateSolution, secondSubSet.length, firstSubSet.length);
                candidateSolution.setCandidateSolution(newCandidateSolution);
            }
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
