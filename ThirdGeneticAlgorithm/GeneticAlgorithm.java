/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThirdGeneticAlgorithm;

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
    public static double mutationProbability = 0.02;
    public static double mutationChange = 0.3;
    public static double ruleOrderMutationProbability = 0.18;
    public static double crossoverProbability = 0.4;
    public static Individual[] population = new Individual[100];
    public static final int numberOfGenerations = 10000;
    public static int generationCounter = 0;

    public static void main(String[] args) {
        // TODO code application logic here

        TrainingData.readFile();

        for (int i = 0; i < population.length; i++) {
            population[i] = new Individual();
        }

        System.out.println("Training best, Val. best, Training mean, Val. mean");
        System.out.println(getBestFitness(population) + "," + getBestValFitness(population) + "," + getMeanFitness(population) + "," + getMeanValFitness(population));

        //fix mutation so upper cant be lower than lower
        for (int i = 1; i <= numberOfGenerations; i++) {
            evolve();
            generationCounter++;
            if ((numberOfGenerations - generationCounter) % (numberOfGenerations * 0.1) == 0) {
                mutationChange = mutationChange * 0.95;
                mutationProbability = mutationProbability * 0.95;
                ruleOrderMutationProbability = ruleOrderMutationProbability * 0.95;
                crossoverProbability = crossoverProbability * 1.1;
            }
            System.out.println(getBestFitness(population) + "," + getBestValFitness(population) + "," + getMeanFitness(population) + "," + getMeanValFitness(population));
        }

        for (Individual candidateSolution : population) {
            if (candidateSolution.getFitness() == getBestFitness(population)) {
                System.out.println("Best Solution:");
                for (Gene gene : candidateSolution.getCandidateSolution()) {
                    if (gene.getValue() == -1) {
                        System.out.print("(" + gene.getLowerBound() + ", " + gene.getUpperBound() + "), ");
                    } else {
                        System.out.print(gene.getValue() + ", ");
                    }
                }
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
                    Gene temp = offSpring[i].getGene(j);
                    offSpring[i].setGene(j, offSpring[i + 1].getGene(j));
                    offSpring[i + 1].setGene(j, temp);
                }
                offSpring[i].calculateFitness();
                offSpring[i + 1].calculateFitness();
            }
            i++;
        }

        //mutation
        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.DOWN);
        for (Individual candidateSolution : offSpring) {
            for (int i = 0; i < Individual.solutionSize; i++) {
                if (random.nextDouble() <= mutationProbability) {
                    if (i != 0 && (i + 1) % Rule.ruleSize == 0) {
                        if (candidateSolution.getGene(i).getValue() == 1.0) {
                            candidateSolution.getGene(i).setValue(0.0);
                        } else {
                            candidateSolution.getGene(i).setValue(1.0);
                        }
                    } else {
                        candidateSolution.getGene(i).mutateGene();
                    }
                }
            }
            candidateSolution.calculateFitness();
        }

//        mutate order of rules
        for (Individual candidateSolution : offSpring) {
            if (random.nextDouble() <= ruleOrderMutationProbability) {
                int crossOverPoint = random.nextInt(Individual.numberOfRules - 1) + 1;
                Gene[] firstSubSet = new Gene[crossOverPoint * Rule.ruleSize];
                Gene[] secondSubSet = new Gene[(Individual.numberOfRules * Rule.ruleSize) - (crossOverPoint * Rule.ruleSize)];
                for (int i = 0; i < firstSubSet.length; i++) {
                    firstSubSet[i] = new Gene(candidateSolution.getGene(i).getLowerBound(), candidateSolution.getGene(i).getUpperBound(), candidateSolution.getGene(i).getValue());
                }
                for (int j = firstSubSet.length; j < candidateSolution.getCandidateSolution().length; j++) {
                    secondSubSet[j - firstSubSet.length] = new Gene(candidateSolution.getGene(j).getLowerBound(), candidateSolution.getGene(j).getUpperBound(), candidateSolution.getGene(j).getValue());
                }
                Gene[] newCandidateSolution = new Gene[candidateSolution.getCandidateSolution().length];
                for (int i = 0; i < secondSubSet.length; i++) {
                    newCandidateSolution[i] = new Gene(secondSubSet[i].getLowerBound(), secondSubSet[i].getUpperBound(), secondSubSet[i].getValue());
                }
                for (int i = 0; i < firstSubSet.length; i++) {
                    newCandidateSolution[secondSubSet.length + i] = new Gene(firstSubSet[i].getLowerBound(), firstSubSet[i].getUpperBound(), firstSubSet[i].getValue());
                }
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

    public static int getBestValFitness(Individual[] population) {
        int best = 0;
        for (Individual candidateSolution : population) {
            if (candidateSolution.getValidationFitness() > best) {
                best = candidateSolution.getValidationFitness();
            }
        }
        return best;
    }

    public static int getMeanValFitness(Individual[] population) {
        int meanFitness = 0;
        for (Individual candidateSolution : population) {
            meanFitness += candidateSolution.getValidationFitness();
        }
        return meanFitness / population.length;
    }
}
