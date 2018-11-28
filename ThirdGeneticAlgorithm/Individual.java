/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThirdGeneticAlgorithm;


import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author js2-keizer
 */
public class Individual {

    public final static int numberOfRules = 5;
    public final static int solutionSize = Rule.ruleSize * numberOfRules;
    public final static double probHash = 0.1;

    private Gene[] candidateSolution = new Gene[solutionSize];
    private int fitness;
    private int valFitness;

    public Individual() {
        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.DOWN);
        Random random = new Random();
        for (int i = 0; i < Individual.solutionSize; i++) {
            this.candidateSolution[i] = new Gene();
            if (i != 0 && (i + 1) % Rule.ruleSize == 0) {
                if (random.nextDouble() <= 0.5) {
                    this.candidateSolution[i].setValue(1.0);
                } else {
                    this.candidateSolution[i].setValue(0.0);
                }
            }
        }
        this.calculateFitness();
    }

    public void setGene(int index, Gene value) {
        this.candidateSolution[index] = new Gene(value.getLowerBound(), value.getUpperBound(), value.getValue());
    }

    public Gene getGene(int index) {
        return candidateSolution[index];
    }

    public void setCandidateSolution(Gene[] candidateSolution) {
        for (int i = 0; i < candidateSolution.length; i++) {
            this.candidateSolution[i] = new Gene(candidateSolution[i].getLowerBound(), candidateSolution[i].getUpperBound(), candidateSolution[i].getValue());
        }
        this.calculateFitness();
    }

    public Gene[] getCandidateSolution() {
        return this.candidateSolution;
    }

    public int getFitness() {
        return this.fitness;
    }

    public int getValidationFitness() {
        return this.valFitness;
    }

    public void calculateFitness() {
        this.fitness = 0;
        this.valFitness = 0;
        Rule[] rules = new Rule[Individual.solutionSize / Rule.ruleSize];
        Gene[] ruleData = new Gene[Rule.ruleSize];
        int ruleNumber = 0;
        for (int i = 0; i < Individual.solutionSize;) {
            for (int j = 0; j < Rule.ruleSize; j++) {
                ruleData[j] = new Gene(this.candidateSolution[i].getLowerBound(), this.candidateSolution[i].getUpperBound(), this.candidateSolution[i].getValue());
                i++;
            }
            Rule rule = new Rule(ruleData);
            rules[ruleNumber] = rule;
            ruleNumber++;
        }

        ArrayList<double[]> trainingData = TrainingData.getTrainingData();
        ArrayList<double[]> validationData = TrainingData.getValidationData();

        for (double[] testCase : trainingData) {
            for (Rule rule : rules) {
                boolean matchingConditions = true;
                for (int i = 0; i < testCase.length - 1; i++) {
                    if ((rule.getCondition()[i].getLowerBound() > testCase[i] || rule.getCondition()[i].getUpperBound() < testCase[i])) {
                        matchingConditions = false;
                        break;
                    }
                }
                if (matchingConditions) {
                    if (rule.getAction().getValue() == testCase[testCase.length - 1]) {
                        this.fitness++;
                    }
                    break;
                }
            }
        }

        for (double[] testCase : validationData) {
            for (Rule rule : rules) {
                boolean matchingConditions = true;
                for (int i = 0; i < testCase.length - 1; i++) {
                    if ((rule.getCondition()[i].getLowerBound() > testCase[i] || rule.getCondition()[i].getUpperBound() < testCase[i])) {
                        matchingConditions = false;
                        break;
                    }
                }
                if (matchingConditions) {
                    if (rule.getAction().getValue() == testCase[testCase.length - 1]) {
                        this.valFitness++;
                    }
                    break;
                }
            }
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(candidateSolution);
    }

}
