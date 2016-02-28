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

            this.movDesIns(instruction, bankRegister);

        }

        if (instruction.toLowerCase().contains("add")) {

            this.addDesIns(instruction, bankRegister);

        }

    }

    public void addDesIns(String instruction, Register bankRegister) {
        //Se descompone la instruccion
        instruction = instruction.toLowerCase().replace("add", "").replace(" ", "");
        String rd = instruction.substring(0, instruction.indexOf(','));
        String rn = instruction.substring(instruction.indexOf(',') + 1, instruction.lastIndexOf(','));
        String src = instruction.substring(instruction.lastIndexOf(',') + 1);
        //Se obtiene los valores de los registros
        String rnValue = bankRegister.findRegister(rn);

        Long srcValueLong;
        Long rnValueLong;
        boolean hex = false;

        //Casting rnValue to long         
        if (rnValue.contains("x")) {
            rnValue = rnValue.substring(rnValue.indexOf('x') + 1);
            rnValueLong = Long.parseLong(rnValue, 16);
            hex = true;
        } 
        else 
        {
            rnValueLong = Long.valueOf(rnValue);
        }

        //Casting src to long   
        if (src.contains("#")) 
        {
            String srcValue;
            //inmediate
            if(src.contains("x"))
            {
                srcValue = src.substring(src.indexOf('x') + 1);
                srcValueLong = Long.parseLong(srcValue, 16);
                hex = true;
            
            }
            else
            {
                srcValue = src.substring(src.indexOf('#') + 1);
                srcValueLong = Long.valueOf(srcValue);
            }

        } 
        else 
        {
            //register
            String srcValue = bankRegister.findRegister(src);
            if (srcValue.contains("x")) {
                srcValue = srcValue.substring(srcValue.indexOf('x') + 1);
                srcValueLong = Long.parseLong(srcValue, 16);
                hex = true;
            } else {
                srcValueLong = Long.valueOf(srcValue);
            }
        }

        this.add(rd, rnValueLong, srcValueLong, bankRegister, hex);

    }

    public void add(String rd, Long rn, Long src, Register bankRegister, boolean hex) {
        Long result = rn + src;
        if (hex) {

            this.mov(rd, Long.toHexString(result), bankRegister);
        } else {
            this.mov(rd, Long.toString(result), bankRegister);

        }
    }

    public void movDesIns(String instruction, Register bankRegister) {
        instruction = instruction.toLowerCase().replace("mov", "").replace(" ", "");
        String rd = instruction.substring(0, instruction.indexOf(','));
        String src = instruction.substring(instruction.indexOf('#') + 1);
        this.mov(rd, src, bankRegister);

    }

    //rd<-src, src=inmediate (hex o dec)
    public void mov(String rd, String src, Register bankRegister) {

        bankRegister.setRegister(rd, src);
    }

}
