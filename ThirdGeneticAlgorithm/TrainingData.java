/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThirdGeneticAlgorithm;

import java.io.BufferedReader;
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
public class TrainingData {

    private static ArrayList<double[]> data = new ArrayList<>();
    private static ArrayList<double[]> trainingData = new ArrayList<>();
    private static ArrayList<double[]> validationData = new ArrayList<>();

    public static void readFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("D:\\data3.txt"));
            String line = br.readLine();
            while (line != null) {
                double[] individualRule = Arrays.stream(line.split(" ")).mapToDouble(Double::parseDouble).toArray();
                data.add(individualRule);
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Individual.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Individual.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i = 0; i < data.size(); i++){
            if(i < data.size()/2){
                trainingData.add(data.get(i));
            }else{
                validationData.add(data.get(i));
            }
        }
    } 
    
    public static ArrayList<double[]> getTrainingData(){
        return trainingData;
    }
    
    public static ArrayList<double[]> getValidationData(){
        return validationData;
    }

}
