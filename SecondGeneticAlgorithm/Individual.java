/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SecondGeneticAlgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author js2-keizer
 */
public class Individual {
    
    public final static int numberOfRules = 5;
    public final static int solutionSize = Rule.ruleSize * numberOfRules;
    
    private int[] candidateSolution = new int[solutionSize];
    private int fitness;
    
    public Individual(){
        for (int i = 0; i< Individual.solutionSize; i++) {
            int randomNumber = (int) Math.floor(Math.random() * 101);
            if(i != 0 && (i + 1) % Rule.ruleSize == 0){
                if(randomNumber <= 50){
                    this.candidateSolution[i] = 1;
                }else{
                    this.candidateSolution[i] = 0;
                }
            }else{
                if(randomNumber <= 33){
                    this.candidateSolution[i] = 1;
                }else if(randomNumber <= 66){
                    this.candidateSolution[i] = 0;
                }else{
                    this.candidateSolution[i] = 2;
                }    
            }
        }
        this.calculateFitness();
    }
    
    public void setGene(int index, int value){
        this.candidateSolution[index] = value;
    }
    
    public int getGene(int index){
        return candidateSolution[index];
    }
    
    public void setCandidateSolution(int[] candidateSolution){
        System.arraycopy(candidateSolution, 0, this.candidateSolution, 0, Individual.solutionSize);
        this.calculateFitness();
    }
    
    public int[] getCandidateSolution(){
        return this.candidateSolution;
    }
    
    public int getFitness(){
        return this.fitness;
    }
    
    public void calculateFitness(){
        this.fitness = 0;
        Rule[] rules = new Rule[Individual.solutionSize/Rule.ruleSize];
        int[] ruleData = new int[Rule.ruleSize];
        int ruleNumber = 0;
        for(int i = 0; i < Individual.solutionSize; i+=Rule.ruleSize){
            System.arraycopy(this.candidateSolution, i, ruleData, 0, Rule.ruleSize);
            Rule rule = new Rule(ruleData);
            rules[ruleNumber] = rule;
            ruleNumber++;
        }
        
        ArrayList<String> data = new ArrayList<>();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader("D:\\data2.txt"));
            String line = br.readLine();
            while (line != null) {
                data.add(line);
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Individual.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Individual.class.getName()).log(Level.SEVERE, null, ex);
        }     
        
        for(String conditionAndAction : data){
            for(Rule rule : rules){
                String ruleCondition = Arrays.toString(rule.getCondition()).replaceAll("[, \\[\\]]", "");
                String dataCondition = conditionAndAction.substring(0, Rule.ruleSize - 1);
                for(int c = 0; c < ruleCondition.length(); c++){
                    if(ruleCondition.charAt(c) == '2'){
                        StringBuilder sb = new StringBuilder(ruleCondition);
                        StringBuilder sb2 = new StringBuilder(dataCondition);
                        sb.deleteCharAt(c);
                        sb2.deleteCharAt(c);
                        ruleCondition = sb.toString();
                        dataCondition = sb2.toString();
                        c--;
                    }
                }
                if(ruleCondition.equals(dataCondition)){
                    if(rule.getAction() == Character.getNumericValue(conditionAndAction.charAt(conditionAndAction.length() - 1))){
                        this.fitness++;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public String toString(){
        return Arrays.toString(candidateSolution);
    }

}
