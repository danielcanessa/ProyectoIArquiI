/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author Daniel
 */
public class TEST {

    /**
     * @param args the command line arguments
     */
   
    public static boolean isDataProcessingInstructions(String instruction,List<String> DataProcessingInstructions)
    {       
        for (int i = 0; i < DataProcessingInstructions.size(); i++) {
            if (instruction.contains(DataProcessingInstructions.get(i))) {
                return true;               
            }
            
        }
        return false;
         
    }
    public static void main(String[] args) {

      
        List<String> DataProcessingInstructions;
        DataProcessingInstructions = new ArrayList<>();
        DataProcessingInstructions.add("mov ");
        DataProcessingInstructions.add("add ");
        DataProcessingInstructions.add("sub ");
        DataProcessingInstructions.add("eor ");
        DataProcessingInstructions.add("and ");
        DataProcessingInstructions.add("rsb ");
        DataProcessingInstructions.add("sbc ");
        DataProcessingInstructions.add("rsc ");
        DataProcessingInstructions.add("orr ");
        DataProcessingInstructions.add("lsl ");
        DataProcessingInstructions.add("asr ");
        DataProcessingInstructions.add("ror ");
        DataProcessingInstructions.add("bic ");
        DataProcessingInstructions.add("mvn ");
        DataProcessingInstructions.add("rrx ");
        DataProcessingInstructions.add("cmp ");
        DataProcessingInstructions.add("cmn ");
        System.out.println(isDataProcessingInstructions("cmp r0,455",DataProcessingInstructions));
        
    
        

    }

}
