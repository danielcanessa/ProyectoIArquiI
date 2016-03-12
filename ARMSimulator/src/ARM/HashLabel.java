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

    public HashLabel() {
        this.hashTableLabels = new ArrayList<>();
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
    
    public void cleanHashLabel()
    {
        this.hashTableLabels.clear();
    }

    public int findLabel(String label) {
        String aux = label.toLowerCase().trim();
        int out = -1;
         for (int i = 0; i < hashTableLabels.size(); i++) {
            if(hashTableLabels.get(i).get(0)==label.toLowerCase().trim())
            {
                out = Integer.parseInt(hashTableLabels.get(i).get(1));            
            }
        }        
        return out;

    }

}
