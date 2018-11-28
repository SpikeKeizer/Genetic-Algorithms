/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThirdGeneticAlgorithm;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

/**
 *
 * @author Spike
 */
public class Gene {

    private double lowerBound;
    private double upperBound;
    private double value;

    public Gene() {
        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.DOWN);
        double firstRandomNumber = Double.parseDouble(df.format(Math.random()));
        double secondRandomNumber = Double.parseDouble(df.format(Math.random()));
        if (firstRandomNumber < secondRandomNumber) {
            this.lowerBound = firstRandomNumber;
            this.upperBound = secondRandomNumber;
        } else {
            this.lowerBound = secondRandomNumber;
            this.upperBound = firstRandomNumber;
        }
        this.value = -1.0;
    }

    public Gene(double lowerBound, double upperBound, double value) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.value = value;
    }

    public double getLowerBound() {
        return this.lowerBound;
    }

    public void setLowerBound(double value) {
        this.lowerBound = value;
    }

    public double getUpperBound() {
        return this.upperBound;
    }

    public void setUpperBound(double value) {
        this.upperBound = value;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void mutateGene() {
        DecimalFormat df = new DecimalFormat("#.######");
        df.setRoundingMode(RoundingMode.DOWN);
        Random random = new Random();
        double mutationChange = GeneticAlgorithm.mutationChange * random.nextDouble();
        if (random.nextDouble() <= 0.5) { //mutate lower bound
            if (random.nextDouble() <= 0.5) { //mutate down
                if (this.lowerBound - mutationChange < 0) {
                    this.lowerBound = 0.0;
                } else {
                    this.lowerBound = this.lowerBound - mutationChange;
                }
            } else { //mutate up
                if (this.lowerBound + mutationChange > 1) {
                    this.lowerBound = 1.0;
                } else {
                    this.lowerBound = this.lowerBound + mutationChange;
                }
            }
        } else { //mutate upper bound
            if (random.nextDouble() <= 0.5) { //mutate down
                if (this.upperBound - mutationChange < 0) {
                    this.upperBound = 0.0;
                } else {
                    this.upperBound = this.upperBound - mutationChange;
                }
            } else { //mutate up
                if (this.upperBound + mutationChange > 1) {
                    this.upperBound = 1.0;
                } else {
                    this.upperBound = this.upperBound + mutationChange;
                }
            }
        }

        if (this.upperBound < this.lowerBound) {
            double temp = this.lowerBound;
            this.lowerBound = this.upperBound;
            this.upperBound = temp;
        }
    }

}
