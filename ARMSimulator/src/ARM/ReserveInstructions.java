/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ARM;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class ReserveInstructions {

    List<String> DataProcessingInstructions;
    List<String> MultiplyInstructions;
    List<String> MemoryInstructions;
    List<String> BranchInstructions;

    public ReserveInstructions() {
        this.DataProcessingInstructions = new ArrayList<>();
        this.MultiplyInstructions = new ArrayList<>();
        this.MemoryInstructions = new ArrayList<>();
        this.BranchInstructions = new ArrayList<>();
        this.fillDataProcessingInstructions();
        this.fillMultiplyInstructions();
        this.fillMemoryInstructions();
        this.fillBranchInstructions();
    }

    private void fillDataProcessingInstructions() {
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
        DataProcessingInstructions.add("adc ");

    }

    private void fillMultiplyInstructions() {
        MultiplyInstructions.add("mul ");
        MultiplyInstructions.add("mla ");
    }

    private void fillMemoryInstructions() {
        MemoryInstructions.add("str ");
        MemoryInstructions.add("ldr ");
        MemoryInstructions.add("strb ");
        MemoryInstructions.add("ldrb ");
    }

    private void fillBranchInstructions() {
        BranchInstructions.add("b ");
        BranchInstructions.add("bge ");
        BranchInstructions.add("ble ");
        BranchInstructions.add("blt ");
        BranchInstructions.add("bgt ");
        BranchInstructions.add("beq ");
        BranchInstructions.add("bne ");
        BranchInstructions.add("bl ");
        BranchInstructions.add("bleq ");
        BranchInstructions.add("blne ");
        BranchInstructions.add("blge ");
        BranchInstructions.add("bllt ");
        BranchInstructions.add("blgt ");
        BranchInstructions.add("blle ");
    }
    
    public boolean isDataProcessingInstructions(String instruction)
    {
        for (int i = 0; i < DataProcessingInstructions.size(); i++) {
            if (instruction.contains(DataProcessingInstructions.get(i))) {
                return true;               
            }
            
        }
        return false;         
    }
    
    public boolean isMultiplyInstructions(String instruction)
    {
        for (int i = 0; i < MultiplyInstructions.size(); i++) {
            if (instruction.contains(MultiplyInstructions.get(i))) {
                return true;               
            }            
        }
        return false;         
    }
    
    public boolean isMemoryInstructions(String instruction)
    {
        for (int i = 0; i < MemoryInstructions.size(); i++) {
            if (instruction.contains(MemoryInstructions.get(i))) {
                return true;               
            }            
        }
        return false;         
    }
    
    public boolean isBranchInstructions(String instruction)
    {
        for (int i = 0; i < BranchInstructions.size(); i++) {
            if (instruction.contains(BranchInstructions.get(i))) {
                return true;               
            }            
        }
        return false;         
    }
}
