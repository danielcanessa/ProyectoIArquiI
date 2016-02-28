/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ARM;

/**
 *
 * @author Daniel
 */
public class Operation {

    public void selectOperation(String instruction, Register bankRegister, Memory memory) {

        if (instruction.toLowerCase().contains("mov")) {

            this.mov(instruction, bankRegister);

        }

        if (instruction.toLowerCase().contains("add")) {

            this.add(instruction, bankRegister);

        }

    }

    
    public void add(String instruction, Register bankRegister) {
        instruction = instruction.toLowerCase().replace("add", "").replace(" ", "");
        String rd = instruction.substring(0, instruction.indexOf(','));
        String rn = instruction.substring(instruction.indexOf(',') + 1, instruction.lastIndexOf(','));
        String src = instruction.substring(instruction.lastIndexOf(',') + 1);
        
        String rnValue = bankRegister.findRegister(rn);
        
        //Casting rnValue to long         
        if(rnValue.contains("x"))
        {
            //substring(d.indexOf('x')+1);
        
        }
        else
        {
            
        }
        
        

    }

    //rd<-src, src=inmediate (hex o dec)
    public void mov(String instruction, Register bankRegister) {
        instruction = instruction.toLowerCase().replace("mov", "").replace(" ", "");
        String rd = instruction.substring(0, instruction.indexOf(','));
        String src = instruction.substring(instruction.indexOf('#') + 1);
        bankRegister.setRegister(rd, src);

    }

}
