/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SecondGeneticAlgorithm;

import java.util.Arrays;

/**
 *
 * @author js2-keizer
 */
public class Rule {
    
    public static final int ruleSize = 8; 
    
    private int[] condition = new int[ruleSize  - 1];
    private int action;
    
    public Rule(int[] candidateSolution){
        System.arraycopy(candidateSolution, 0, this.condition, 0, ruleSize - 1);
        this.action = candidateSolution[ruleSize - 1];
    }
    
    public int[] getCondition(){
        return this.condition;
    }
    
    public int getAction(){
        return this.action;
    }
    
    @Override
    public String toString(){
        String rule = "Condtion:" + Arrays.toString(condition) + " Action:" + action;
        return rule;
    }
    
}
