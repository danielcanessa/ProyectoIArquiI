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
public class Operation {

    Register bankRegister;
    Memory memory;
    ConditionFlags conditionFlag;
    HashLabel hashLabel;
    int pcCounter;
    List<List<String>> hashTableLabels ;

    public Operation(Register bankRegister, Memory memory, ConditionFlags conditionFlag,HashLabel hashLabel) {       
        this.hashTableLabels= new ArrayList<>();
        this.bankRegister = bankRegister;
        this.memory = memory;
        this.conditionFlag = conditionFlag;
        this.pcCounter=0;
        this.hashLabel=hashLabel;

    }
    
    

    public int getPCCounter()
    {
        return this.pcCounter;
    }
    
    public void selectOperation(String instruction) {
        
        this.mov("r13", Long.parseLong(Integer.toString(this.pcCounter)));
        
        String fixInstruction = instruction.toLowerCase().trim();
       
        if (fixInstruction.contains("mov") || fixInstruction.contains("add") || fixInstruction.contains("sub") || fixInstruction.contains("eor")
                || fixInstruction.contains("and") || fixInstruction.contains("rsb") || fixInstruction.contains("sbc") || fixInstruction.contains("rsc")
                || fixInstruction.contains("orr") || fixInstruction.contains("lsl") || fixInstruction.contains("asr") || fixInstruction.contains("ror")
                || fixInstruction.contains("bic") || fixInstruction.contains("mvn") || fixInstruction.contains("rrx") || fixInstruction.contains("cmp")
                || fixInstruction.contains("cmn")) {
            this.dataProcessingDecodeInstruction(fixInstruction);
        }
        else if (fixInstruction.contains("mul") || fixInstruction.contains("mla")) {
            this.multiplyDecodeInstruction(fixInstruction);

        }

        else if (fixInstruction.contains("str") || fixInstruction.contains("ldr") || fixInstruction.contains("strb") || fixInstruction.contains("ldrb")) {
            this.memoryDecodeInstruction(fixInstruction);

        }
        else
        {
            if(!instruction.contains(" ")){
                this.hashLabel.addLabel(instruction.toLowerCase().trim(), pcCounter);
               
            }
            else{
                System.out.println("Instruccion no reconocida: "+instruction);
            }
            
        }
         
        this.pcCounter+=1;
    }

    public int countComma(String decodeInstruction) {
        int countComma = 0;
        for (int i = 0; i < decodeInstruction.length(); i++) {
            if (decodeInstruction.charAt(i) == ',') {
                countComma += 1;
            }
        }
        return countComma;
    }

    public void memoryDecodeInstruction(String instruction) {
        String decodeInstruction = "";
        if (instruction.contains("strb") || instruction.contains("ldrb")) {
            decodeInstruction = instruction.replace(" ", "").substring(4).trim();
        } else {
            decodeInstruction = instruction.replace(" ", "").substring(3).trim();
        }

        decodeInstruction = decodeInstruction.replace("[", "").replace("]", "");

        int countComma = countComma(decodeInstruction);

        String rd = decodeInstruction.substring(0, decodeInstruction.indexOf(','));
        //System.out.println("rd: " + rd);
        String src2 = decodeInstruction.substring(decodeInstruction.lastIndexOf(',') + 1);
        //System.out.println("src2: " + src2);
        Long rdValue;
        Long src2Value;
        Long rnValue;

        //inmediato
        if (src2.contains("#")) {
            if (src2.contains("x")) {
                src2Value = castHexRegStringToLong(src2);
            } else {
                src2Value = castDecRegStringToLong(src2);
            }
        } //registro
        else {
            src2Value = getRegisterValue(src2);
        }

        if (countComma == 2) {
            String rn = decodeInstruction.substring(decodeInstruction.indexOf(',') + 1, decodeInstruction.lastIndexOf(','));
            //System.out.println("rn: " + rn);
            rnValue = getRegisterValue(rn);
            if (instruction.contains("strb")) {
                 rdValue= getRegisterValue(rd);
                 this.strb(rdValue, rnValue, src2Value);    

            } else if (instruction.contains("ldrb")) {
                 this.ldrb(rd, rnValue, src2Value);

            } else {
                 if (instruction.contains("str")) {
                     rdValue= getRegisterValue(rd);
                     this.str(rdValue, rnValue, src2Value);                 
                 }
                 else 
                 {
                      this.ldr(rd, rnValue, src2Value);
                 }
            }
        } else {
            if (instruction.contains("strb")) {
                 rdValue= getRegisterValue(rd);
                 this.strb(rdValue, 0L, src2Value);    

            } else if (instruction.contains("ldrb")) {
                 this.ldrb(rd, 0L , src2Value);

            } else {
                 if (instruction.contains("str")) {
                     rdValue= getRegisterValue(rd);
                     this.str(rdValue, 0L , src2Value);                 
                 }
                 else 
                 {
                      this.ldr(rd, 0L , src2Value);
                 }
            }

        }

    }

    public void ldr(String rd, Long rn, Long src2) {       
        Long memorySlot = rn + src2;
        if (memorySlot % 4 == 0) {
            
            this.mov(rd, Long.parseLong(this.memory.loadWord(memorySlot.intValue()),16));

        } else {
            System.out.println("Memoria desalineada ldr dirección: " + memorySlot);
        }
    }
    
    public void ldrb(String rd, Long rn, Long src2) {       
        Long memorySlot = rn + src2;
        this.mov(rd, Long.parseLong(this.memory.loadByte(memorySlot.intValue()),16));

       
    }
    
    public void str(Long rd, Long rn, Long src2) {       
        Long memorySlot = rn + src2;
        if (memorySlot % 4 == 0) {
            String hexRd = Long.toHexString(rd);           
            int length = hexRd.length();
            if (length < 8) {
                String aux = "";
                for (int i = 0; i < 8 - length; i++) {
                    aux = aux + "0";
                }
                hexRd = aux + hexRd;
            }           
            if (length > 8) {
                hexRd = hexRd.substring(length - 8);
            }
            this.memory.storeWord(memorySlot.intValue(), hexRd);

        } else {
            System.out.println("Memoria desalineada str dirección: " + memorySlot);
        }
    }
    
    public void strb(Long rd, Long rn, Long src2) {       
        Long memorySlot = rn + src2;
        String hexRd = Long.toHexString(rd); 
        int length = hexRd.length();
        if (length < 2) {
            String aux = "";
            for (int i = 0; i < 2 - length; i++) {
                aux = aux + "0";
            }
            hexRd = aux + hexRd;
            }           
            if (length > 2) {
                hexRd = hexRd.substring(length - 2);
            }
            this.memory.storeByte(memorySlot.intValue(), hexRd);       
    }
    

    public void dataProcessingDecodeInstruction(String instruction) {
        String decodeInstruction = instruction.replace(" ", "").substring(3).trim();
        //System.out.println(decodeInstruction);
        String rd = decodeInstruction.substring(0, decodeInstruction.indexOf(','));
        //System.out.println(rd);
        String src2 = decodeInstruction.substring(decodeInstruction.lastIndexOf(',') + 1);
        //System.out.println(src2);
        //Long rdValue = getRegisterValue(rd);
        Long src2Value;

        //inmediato
        if (src2.contains("#")) {
            if (src2.contains("x")) {
                src2Value = castHexRegStringToLong(src2);
            } else {
                src2Value = castDecRegStringToLong(src2);
            }
        } //registro
        else {
            src2Value = getRegisterValue(src2);
        }

        //instructions 
        if (instruction.contains("cmp") || instruction.contains("cmn") || instruction.contains("mov") || instruction.contains("mvn")) {
            if (instruction.contains("mov")) {
                this.mov(rd, src2Value);
            }
            if (instruction.contains("mvn")) {
                this.mvn(rd, src2Value);
            }
        } else {
            String rn = decodeInstruction.substring(decodeInstruction.indexOf(',') + 1, decodeInstruction.lastIndexOf(','));
            Long rnValue = getRegisterValue(rn);

            if (instruction.contains("add")) {
                this.add(rd, rnValue, src2Value);
            }
            if (instruction.contains("sub")) {
                this.sub(rd, rnValue, src2Value);
            }
            if (instruction.contains("rsb")) {
                this.sub(rd, src2Value, rnValue);
            }
            if (instruction.contains("eor")) {
                this.eor(rd, rnValue, src2Value);
            }
            if (instruction.contains("and")) {
                this.and(rd, rnValue, src2Value);
            }
            if (instruction.contains("sbc")) {
                this.sbc(rd, rnValue, src2Value);
            }
            if (instruction.contains("rsc")) {
                this.sbc(rd, src2Value, rnValue);
            }
            if (instruction.contains("orr")) {
                this.orr(rd, src2Value, rnValue);
            }
            if (instruction.contains("lsl")) {
                this.lsl(rd, rnValue, src2Value);
            }
            if (instruction.contains("asr")) {
                this.asr(rd, rnValue, src2Value);
            }
            if (instruction.contains("ror")) {
                this.ror(rd, rnValue, src2Value);
            }
            if (instruction.contains("bic")) {
                this.bic(rd, rnValue, src2Value);
            }

        }

    }

    public void multiplyDecodeInstruction(String instruction) {
        String decodeInstruction = instruction.replace(" ", "").substring(3).trim();
        String rd = decodeInstruction.substring(0, decodeInstruction.indexOf(','));

        //instructions 
        if (instruction.contains("mul")) {
            String rn = decodeInstruction.substring(decodeInstruction.indexOf(',') + 1, decodeInstruction.lastIndexOf(','));
            String rm = decodeInstruction.substring(decodeInstruction.lastIndexOf(',') + 1);
            Long rmValue = getRegisterValue(rm);
            Long rnValue = getRegisterValue(rn);
            this.mul(rd, rnValue, rmValue);
        } else {
            String middle = decodeInstruction.substring(decodeInstruction.indexOf(',') + 1, decodeInstruction.lastIndexOf(','));
            String rn = middle.substring(0, middle.indexOf(','));
            String rm = middle.substring(middle.lastIndexOf(',') + 1);
            String ra = decodeInstruction.substring(decodeInstruction.lastIndexOf(',') + 1);
            Long rmValue = getRegisterValue(rm);
            Long rnValue = getRegisterValue(rn);
            Long raValue = getRegisterValue(ra);
            this.mla(rd, rnValue, rmValue, raValue);

        }

    }

    public Long getRegisterValue(String reg) {
        String regValue = bankRegister.findRegister(reg);

        if (!"".equals(regValue)) {
            return castHexRegStringToLong(regValue);
        } else {
            System.out.println("Error se intentó acceder a un registro que no tenía nada escrito: " + reg);
            return null;
        }

    }

    public Long castHexRegStringToLong(String regValue) {
        Long regValueLong;
        regValue = regValue.substring(regValue.indexOf('x') + 1);
        regValueLong = Long.parseLong(regValue, 16);
        return regValueLong;

    }

    public Long castDecRegStringToLong(String regValue) {
        Long regValueLong;
        regValue = regValue.substring(regValue.indexOf('#') + 1);
        regValueLong = Long.valueOf(regValue);
        return regValueLong;

    }

    public void mul(String rd, Long rn, Long rm) {

        Long result = rn * rm;
        this.mov(rd, result);
    }

    public void mla(String rd, Long rn, Long rm, Long ra) {

        Long result = (rn * rm) + ra;
        this.mov(rd, result);
    }

    public void add(String rd, Long rn, Long src2) {

        Long result = rn + src2;

        this.mov(rd, result);
    }

    public void eor(String rd, Long rn, Long src2) {
        Long result = rn ^ src2;
        this.mov(rd, result);
    }

    public void orr(String rd, Long rn, Long src2) {
        Long result = rn | src2;
        this.mov(rd, result);
    }

    public void lsl(String rd, Long rn, Long src2) {
        Long result = Long.parseLong(Long.toBinaryString(rn <<= src2), 2);
        this.mov(rd, result);
    }

    public void asr(String rd, Long rn, Long src2) {

        Long result = Long.parseLong(Long.toBinaryString(rn >>= src2), 2);
        System.out.println(Long.toHexString(result));
        this.mov(rd, result);
    }

    public void and(String rd, Long rn, Long src2) {
        Long result = rn & src2;
        this.mov(rd, result);
    }

    public void bic(String rd, Long rn, Long src2) {
        Long result = rn & ~src2;
        this.mov(rd, result);
    }

    public void mvn(String rd, Long src2) {
        Long result = ~src2;
        this.mov(rd, result);
    }

    public void sub(String rd, Long rn, Long src2) {
        Long result = rn - src2;
        this.mov(rd, result);
    }

    public void sbc(String rd, Long rn, Long src2) {
        Long result = rn - src2;
        if (!this.conditionFlag.isCarry()) {
            result -= 1;
        }
        this.mov(rd, result);
    }

    public void ror(String rd, Long rn, Long src2) {
        String concat = "";
        String aux = Long.toBinaryString(rn);
        int length = aux.length();
        if (length > src2) {
            String rot = aux.substring(length - src2.intValue());
            String old = aux.substring(0, length - src2.intValue());
            concat = rot;
            if (length < 32) {
                for (int i = 0; i < 32 - length; i++) {
                    concat = concat + "0";
                }
            }
            concat = concat + old;
        }
        Long result = Long.parseLong(concat, 2);
        this.mov(rd, result);
    }

    public void mov(String rd, Long src2) {
        String hexSrc2 = Long.toHexString(src2);
        int length = hexSrc2.length();
        if (length > 8) {
            hexSrc2 = hexSrc2.substring(length - 8);
        }
        bankRegister.setRegister(rd, "0x" + hexSrc2);
    }

    public void cmp(Long rn, Long src2) {
        Long result = rn - src2;
        //Negative
        if (result < 0) {
            this.conditionFlag.setNegative(true);
        } else {
            this.conditionFlag.setNegative(false);
        }
        //Zero                
        if (result == 0) {
            this.conditionFlag.setZero(true);
        } else {
            this.conditionFlag.setZero(false);
        }
    }

    public void cmn(Long rn, Long src2) {
        Long result = rn + src2;
        //Negative
        if (result < 0) {
            this.conditionFlag.setNegative(true);
        } else {
            this.conditionFlag.setNegative(false);
        }
        //Zero                
        if (result == 0) {
            this.conditionFlag.setZero(true);
        } else {
            this.conditionFlag.setZero(false);
        }
        //Carry
        if (result > 4294967295L) {
            this.conditionFlag.setCarry(true);
        } else {
            this.conditionFlag.setCarry(true);
        }

        //overflow 
        if (result > Integer.MAX_VALUE) {
            this.conditionFlag.setoVerflow(true);
        } else {
            this.conditionFlag.setoVerflow(true);
        }
    }

}
