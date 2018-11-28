/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThirdGeneticAlgorithm;

import java.util.Arrays;

/**
 *
 * @author js2-keizer
 */
public class Rule {
    
    public static final int ruleSize = 8; 
    
    private Gene[] condition = new Gene[ruleSize  - 1];
    private Gene action;
    
    public Rule(Gene[] candidateSolution){
        for(int i = 0; i < ruleSize - 1; i++){
            condition[i] = new Gene(candidateSolution[i].getLowerBound(), candidateSolution[i].getUpperBound(), candidateSolution[i].getValue());
        }
        action = new Gene(candidateSolution[ruleSize - 1].getLowerBound(), candidateSolution[ruleSize - 1].getUpperBound(), candidateSolution[ruleSize - 1].getValue());
    }
    
    public Gene[] getCondition(){
        return this.condition;
    }
    
    public Gene getAction(){
        return this.action;
    }
    
    @Override
    public String toString(){
        String rule = "Condtion:" + Arrays.toString(condition) + " Action:" + action;
        return rule;
    }
    
}
