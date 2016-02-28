package ARM;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Daniel
 */
public class Memory {
    ArrayList<String> memory; 
    
    public Memory ()
    {
        this.memory = new ArrayList<>();
        for (int i = 0; i < 1024; i++) {
            memory.add("");            
        }        
    }
    
    public void storeByte(int slot, String data)
    {
        this.memory.set(slot, data);    
    }
    
    public void storeWord(int slotMSB, String word)
    {
       
        this.storeByte(slotMSB, word.substring(0, 2));
        this.storeByte(slotMSB+1, word.substring(2, 4));
        this.storeByte(slotMSB+2, word.substring(4, 6)); 
        this.storeByte(slotMSB+3, word.substring(6, 8)); 
    }
    
    public String loadByte(int slot)
    {
        return this.memory.get(slot);
    }
    
    public String loadWord(int slotMSB)
    {
        String x = "";
        x += this.loadByte(slotMSB);
        x += this.loadByte(slotMSB+1);
        x += this.loadByte(slotMSB+2);
        x += this.loadByte(slotMSB+3);
        return x;        
    }
    
    public ArrayList<String> getMemory()
    {
        return this.memory;
    }
    
    public int getSize()
    {
        return this.memory.size();
    }
    
    public String intToHex(int number)
    {
        return Integer.toHexString(number);
    }
    
    
}
