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
public class HashLabel {

    List<List<String>> hashTableLabels;
    ReserveInstructions reserveInstructions;

    public HashLabel(ReserveInstructions reserveInstructions) {
        this.hashTableLabels = new ArrayList<>();
        this.reserveInstructions = reserveInstructions;
    }

    public void addLabel(String instruction, int pcCounter) {
        List<String> aux = new ArrayList<>();
        aux.add(instruction);
        aux.add(Integer.toString(pcCounter));
        hashTableLabels.add(aux);
    }

    public void printHashLabel() {
        for (int i = 0; i < hashTableLabels.size(); i++) {
            System.out.println("Label: " + hashTableLabels.get(i).get(0) + ", " + hashTableLabels.get(i).get(1));

        }
    }

    public void cleanHashLabel() {
        this.hashTableLabels.clear();
    }

    public int findLabel(String label) {
        //System.out.println(label);

        int out = -1;

        for (int i = 0; i < hashTableLabels.size(); i++) {
            if (hashTableLabels.get(i).get(0).equals(label.toLowerCase().trim())) {
                out = Integer.parseInt(hashTableLabels.get(i).get(1));
            }
        }
        return out;

    }

    public void fillHashTable(String instruction, int position) {

        String fixInstruction = instruction.toLowerCase().trim();

        if (this.reserveInstructions.isDataProcessingInstructions(fixInstruction)
                || this.reserveInstructions.isMultiplyInstructions(fixInstruction)
                || this.reserveInstructions.isMemoryInstructions(fixInstruction)
                || this.reserveInstructions.isBranchInstructions(fixInstruction)) {
            //       
        } else if (!instruction.contains(" ")) {
            if (fixInstruction.length() != 0) {
                System.out.println("Label: " + fixInstruction + ", size: " + fixInstruction.length());
                this.addLabel(fixInstruction, position);
            }
        } else {
            System.out.println("Instruccion no reconocida, no aplica como label: " + instruction);
        }

    }

}
