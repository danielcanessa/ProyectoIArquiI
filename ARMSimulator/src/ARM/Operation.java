/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ARM;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

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
    List<List<String>> hashTableLabels;
    ReserveInstructions reserveInstructions;
    JTextArea OutputText;
    boolean error;
    int clockCycles;
    int branchesExecute;

    public Operation(Register bankRegister, Memory memory, ConditionFlags conditionFlag, HashLabel hashLabel, ReserveInstructions reserveInstructions, JTextArea OutputText) {
        this.hashTableLabels = new ArrayList<>();
        this.bankRegister = bankRegister;
        this.memory = memory;
        this.conditionFlag = conditionFlag;
        this.pcCounter = 0;
        this.hashLabel = hashLabel;
        this.reserveInstructions = reserveInstructions;
        this.OutputText = OutputText;
        this.error=false;
        this.clockCycles = 0;
        this.branchesExecute=0;

    }

    public int getPCCounter() {
        return this.pcCounter;
    }

    public void selectOperation(String instruction) {
        //OutputText.setText(OutputText.getText()+"Instrucción ejecutada: "+instruction+"\r\n");

        // System.out.println(instruction);
       // 
        this.pcCounter += 1;
        this.clockCycles += 1;

        String fixInstruction = instruction.toLowerCase().trim();
        if (this.reserveInstructions.isDataProcessingInstructions(fixInstruction)) {
            this.dataProcessingDecodeInstruction(fixInstruction);
        } else if (this.reserveInstructions.isMultiplyInstructions(fixInstruction)) {
            this.multiplyDecodeInstruction(fixInstruction);

        } else if (this.reserveInstructions.isMemoryInstructions(fixInstruction)) {
            this.memoryDecodeInstruction(fixInstruction);

        } else if (this.reserveInstructions.isBranchInstructions(fixInstruction)) {
            this.branchesExecute+=1;
            this.BranchDecodeInstruction(fixInstruction);

        } else if (this.hashLabel.findLabel(fixInstruction) == -1) {
            System.out.println("Instruccion no reconocida: " + instruction);
            this.error=true;
            OutputText.setText(OutputText.getText() + "Instrucción no reconocida: " + instruction + ", línea: " + this.pcCounter + "\r\n");
        }
       
       this.mov("r15_update", Long.parseLong(Integer.toString((this.pcCounter + 2) * 4)));

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
        if (instruction.contains("cmp ") || instruction.contains("cmn ") || instruction.contains("mov ") || instruction.contains("mvn ")) {
            if (instruction.contains("mov ")) {
                this.mov(rd, src2Value);
            }
            if (instruction.contains("mvn ")) {
                this.mvn(rd, src2Value);
            }
            if (instruction.contains("cmn ")) {
                Long rdValue = getRegisterValue(rd);
                this.cmn(rdValue, src2Value);
            }
            if (instruction.contains("cmp ")) {
                Long rdValue = getRegisterValue(rd);
                this.cmp(rdValue, src2);
            }
        } else {
            String rn = decodeInstruction.substring(decodeInstruction.indexOf(',') + 1, decodeInstruction.lastIndexOf(','));
            Long rnValue = getRegisterValue(rn);

            if (instruction.contains("add ")) {
                this.add(rd, rnValue, src2Value);
            }
            if (instruction.contains("sub ")) {
                this.sub(rd, rnValue, src2Value);
            }
            if (instruction.contains("rsb ")) {
                this.sub(rd, src2Value, rnValue);
            }
            if (instruction.contains("eor ")) {
                this.eor(rd, rnValue, src2Value);
            }
            if (instruction.contains("and ")) {
                this.and(rd, rnValue, src2Value);
            }
            if (instruction.contains("sbc ")) {
                this.sbc(rd, rnValue, src2Value);
            }
            if (instruction.contains("rsc ")) {
                this.sbc(rd, src2Value, rnValue);
            }
            if (instruction.contains("orr ")) {
                this.orr(rd, src2Value, rnValue);
            }
            if (instruction.contains("lsl ")) {
                this.lsl(rd, rnValue, src2Value);
            }
            if (instruction.contains("asr ")) {
                this.asr(rd, rnValue, src2Value);
            }
            if (instruction.contains("ror ")) {
                this.ror(rd, rnValue, src2Value);
            }
            if (instruction.contains("bic ")) {
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

    public void memoryDecodeInstruction(String instruction) {

        String decodeInstruction;
        if (instruction.contains("strb ") || instruction.contains("ldrb ")) {
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
        else if (src2.contains("-")) {
            src2Value = getRegisterValue(src2.substring(src2.indexOf("-") + 1));
            src2Value = src2Value * -1;

        } else {
            src2Value = getRegisterValue(src2);
        }

        if (countComma == 2) {
            String rn = decodeInstruction.substring(decodeInstruction.indexOf(',') + 1, decodeInstruction.lastIndexOf(','));
            //System.out.println("rn: " + rn);
            rnValue = getRegisterValue(rn);
            if (instruction.contains("strb ")) {
                rdValue = getRegisterValue(rd);
                this.strb(rdValue, rnValue, src2Value);

            } else if (instruction.contains("ldrb ")) {
                this.ldrb(rd, rnValue, src2Value);

            } else if (instruction.contains("str ")) {
                rdValue = getRegisterValue(rd);
                this.str(rdValue, rnValue, src2Value);
            } else {
                this.ldr(rd, rnValue, src2Value);
            }
        } else if (instruction.contains("strb ")) {
            rdValue = getRegisterValue(rd);
            this.strb(rdValue, 0L, src2Value);

        } else if (instruction.contains("ldrb ")) {
            this.ldrb(rd, 0L, src2Value);

        } else if (instruction.contains("str ")) {
            rdValue = getRegisterValue(rd);
            this.str(rdValue, 0L, src2Value);
        } else {
            this.ldr(rd, 0L, src2Value);
        }

    }

    private void BranchDecodeInstruction(String fixInstruction) {
        if (fixInstruction.contains("b ") || fixInstruction.contains("bl ")) {
            boolean link = fixInstruction.contains("bl ");
            String label = fixInstruction.substring(2).replace(" ", "").trim();
            this.b(label, link);
        }

        if (fixInstruction.contains("bge ") || fixInstruction.contains("blge ")) {
            boolean link = fixInstruction.contains("blge ");
            String label = fixInstruction.substring(4).replace(" ", "").trim();
            this.bge(label, link);
        }
        if (fixInstruction.contains("ble ") || fixInstruction.contains("blle ")) {
            boolean link = fixInstruction.contains("blle ");
            String label = fixInstruction.substring(4).replace(" ", "").trim();
            this.ble(label, link);
        }
        if (fixInstruction.contains("blt ") || fixInstruction.contains("bllt ")) {
            boolean link = fixInstruction.contains("bllt ");
            String label = fixInstruction.substring(4).replace(" ", "").trim();
            this.blt(label, link);
        }
        if (fixInstruction.contains("bgt ") || fixInstruction.contains("blgt ")) {
            boolean link = fixInstruction.contains("blgt ");
            String label = fixInstruction.substring(4).replace(" ", "").trim();
            this.bgt(label, link);
        }
        if (fixInstruction.contains("beq ") || fixInstruction.contains("bleq ")) {
            boolean link = fixInstruction.contains("bleq ");
            String label = fixInstruction.substring(4).replace(" ", "").trim();
            this.beq(label, link);
        }
        if (fixInstruction.contains("bne ") || fixInstruction.contains("blne ")) {
            boolean link = fixInstruction.contains("blne ");
            String label = fixInstruction.substring(4).replace(" ", "").trim();
            this.bne(label, link);
        }
    }

    public void b(String label, boolean link) {
        int position = this.hashLabel.findLabel(label);
        if (position != -1) {
            if (link) {
                this.mov("r14", Long.parseLong(Integer.toString((this.pcCounter) * 4)));
            }
            this.pcCounter = position;
        }

    }

    public void bge(String label, boolean link) {
        int position = this.hashLabel.findLabel(label);
        if (position != -1 && (this.conditionFlag.isZero() || !this.conditionFlag.isNegative())) {
            if (link) {
                this.mov("r14", Long.parseLong(Integer.toString((this.pcCounter) * 4)));
            }
            this.pcCounter = position;
        }

    }

    public void ble(String label, boolean link) {
        int position = this.hashLabel.findLabel(label);
        if (position != -1 && (this.conditionFlag.isZero() || this.conditionFlag.isNegative())) {
            if (link) {
                this.mov("r14", Long.parseLong(Integer.toString((this.pcCounter) * 4)));
            }
            this.pcCounter = position;
        }

    }

    public void blt(String label, boolean link) {
        int position = this.hashLabel.findLabel(label);
        if (position != -1 && (this.conditionFlag.isNegative())) {
            if (link) {
                this.mov("r14", Long.parseLong(Integer.toString((this.pcCounter) * 4)));
            }
            this.pcCounter = position;
        }

    }

    public void bgt(String label, boolean link) {
        int position = this.hashLabel.findLabel(label);
        if (position != -1 && (!this.conditionFlag.isNegative() && !this.conditionFlag.isZero())) {
            if (link) {
                this.mov("r14", Long.parseLong(Integer.toString((this.pcCounter) * 4)));
            }
            this.pcCounter = position;
        }

    }

    public void beq(String label, boolean link) {
        int position = this.hashLabel.findLabel(label);
        if (position != -1 && (this.conditionFlag.isZero())) {
            if (link) {
                this.mov("r14", Long.parseLong(Integer.toString((this.pcCounter) * 4)));
            }
            this.pcCounter = position;
        }

    }

    public void bne(String label, boolean link) {
        int position = this.hashLabel.findLabel(label);
        if (position != -1 && (!this.conditionFlag.isZero())) {
            if (link) {
                this.mov("r14", Long.parseLong(Integer.toString((this.pcCounter) * 4)));
            }
            this.pcCounter = position;
        }

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

    public void ldr(String rd, Long rn, Long src2) {
        Long memorySlot = rn + src2;
        if (memorySlot > 1023 && memorySlot < 2048) {
            if (memorySlot % 4 == 0) {
                String word = this.memory.loadWord(memorySlot.intValue());
                if ("".equals(word)) {
                    word = "00000000";
                }
                this.mov(rd, Long.parseLong(word, 16));

            } else {
                this.error=true;
                OutputText.setText(OutputText.getText() + "Memoria desalineada, se intentó hacer un ldr del slot de memoria: " + memorySlot + ", en la línea: " + this.pcCounter + "\r\n");
                //System.out.println("Memoria desalineada ldr dirección: " + memorySlot);
            }
        } else if (memorySlot >= 0 & memorySlot < 1024) {
            this.error=true;
            OutputText.setText(OutputText.getText() + "Posición de memoria reservada para memoria del programa, se intentó hacer un ldr del slot de memoria: " + memorySlot + ", en la línea: " + this.pcCounter + "\r\n");
            //System.out.println("Posición no existente en la memoria");
        } else {
            this.error=true;
            
            OutputText.setText(OutputText.getText() + "Posición de memoria no existente, se intentó hacer un ldr del slot de memoria: " + memorySlot + ", en la línea: " + this.pcCounter + "\r\n");
            //System.out.println("Posición no existente en la memoria");
        }

    }

    public void ldrb(String rd, Long rn, Long src2) {
        Long memorySlot = rn + src2;
        if (memorySlot > 1023 && memorySlot < 2048) {
            //System.out.println("Slot: " + Long.parseLong(this.memory.loadByte(memorySlot.intValue()), 16));
            String resultByte = this.memory.loadByte(memorySlot.intValue());
            if ("".equals(resultByte)) {
                resultByte = "00";

            }
            this.mov(rd, Long.parseLong(resultByte, 16));

        } else if (memorySlot >= 0 & memorySlot < 1024) {
            this.error=true;
            OutputText.setText(OutputText.getText() + "Posición de memoria reservada para memoria del programa, se intentó hacer un ldrb del slot de memoria: " + memorySlot + ", en la línea: " + this.pcCounter + "\r\n");
            //System.out.println("Posición no existente en la memoria");
        } else {
            this.error=true;
            OutputText.setText(OutputText.getText() + "Posición de memoria no existente, se intentó hacer un ldrb del slot de memoria: " + memorySlot + ", en la línea: " + this.pcCounter + "\r\n");
            //System.out.println("Posición no existente en la memoria");
        }

    }

    public void str(Long rd, Long rn, Long src2) {
        Long memorySlot = rn + src2;
        if (memorySlot > 1023 && memorySlot < 2048) {
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
                this.error=true;
                OutputText.setText(OutputText.getText() + "Memoria desalineada, se intentó hacer un str del slot de memoria: " + memorySlot + ", en la línea: " + this.pcCounter + "\r\n");
                //System.out.println("Memoria desalineada str dirección: " + memorySlot);
            }
        } else if (memorySlot >= 0 & memorySlot < 1024) {
            this.error=true;
            OutputText.setText(OutputText.getText() + "Posición de memoria reservada para memoria del programa, se intentó hacer un str del slot de memoria: " + memorySlot + ", en la línea: " + this.pcCounter + "\r\n");
            //System.out.println("Posición no existente en la memoria");
        } else {
            this.error=true;
            OutputText.setText(OutputText.getText() + "Posición de memoria no existente, se intentó hacer un str del slot de memoria: " + memorySlot + ", en la línea: " + this.pcCounter + "\r\n");
            //System.out.println("Posición no existente en la memoria");
        }

    }

    public void strb(Long rd, Long rn, Long src2) {
        Long memorySlot = rn + src2;
        if (memorySlot > 1023 && memorySlot < 2048) {
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
        } else if (memorySlot >= 0 & memorySlot < 1024) {
            this.error=true;
            OutputText.setText(OutputText.getText() + "Posición de memoria reservada para memoria del programa, se intentó hacer un strb del slot de memoria: " + memorySlot + ", en la línea: " + this.pcCounter + "\r\n");
            //System.out.println("Posición no existente en la memoria");
        } else {
            this.error=true;
            OutputText.setText(OutputText.getText() + "Posición de memoria no existente, se intentó hacer un strb del slot de memoria: " + memorySlot + ", en la línea: " + this.pcCounter + "\r\n");
            //System.out.println("Posición no existente en la memoria");
        }
    }

    public Long getRegisterValue(String reg) {
        if ("pc".equals(reg)) {
            reg = "r15";
        }
        if ("lr".equals(reg)) {
            reg = "r14";
        }
        String regValue = bankRegister.findRegister(reg);

        if (!"".equals(regValue)) {
            return castHexRegStringToLong(regValue);
        } else {
            this.error=true;
            OutputText.setText(OutputText.getText() + "Se intentó acceder a un registro de una manera no soportada por la instrucción: " + reg + ", en la línea: " + this.pcCounter + "\r\n");
            //System.out.println("Error se intentó acceder a un registro que no tenía nada escrito: " + reg);
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
        // System.out.println("rn: "+Long.toBinaryString(rn)+" , src2: "+Long.toBinaryString(src2));
        Long result = rn + src2;
        //  System.out.println("reuslt: "+Long.toBinaryString(result) +", cantidad: "+Long.toBinaryString(result).length());
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
        //System.out.println(Long.toHexString(result));
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
        //System.out.println("rn: "+Long.toBinaryString(rn)+" , src2: "+Long.toBinaryString(src2));
        Long result = rn + (-src2);
        // System.out.println("reuslt: "+Long.toBinaryString(result) +", cantidad: "+Long.toBinaryString(result).length());
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
        
        
        if("pc".equals(rd) || "r15".equals(rd))
        {           
            rd="r15";
            this.branchesExecute+=1;
            this.pcCounter=(int) (src2/4);
        }
        if("r15_update".equals(rd))
        {
            rd="r15";
        }
        bankRegister.setRegister(rd, "0x" + hexSrc2);
    }

    public boolean verifyNegative(int length, String binaryResult) {

        boolean result = false;
        if (length == 32) {
            if (binaryResult.charAt(0) == '1') {
                result = true;
            }
        }

        if (length >= 32) {
            binaryResult = binaryResult.substring(binaryResult.length() - 32);
            if (binaryResult.charAt(0) == '1') {
                result = true;
            }
        }

        return result;
    }

    public boolean verifyZero(int length, String binaryResult) {
        boolean result = false;
        if (length > 32) {
            String binaryResultAux = binaryResult.substring(binaryResult.length() - 32);
            if (Long.parseLong(binaryResultAux, 2) == 0) {
                result = true;
            }

        } else if (Long.parseLong(binaryResult, 2) == 0) {
            result = true;
        }
        return result;
    }

    public boolean verifyCarry(int length) {
        boolean result = false;
        if (length > 32) {
            result = true;
        }
        return result;
    }

    public boolean verifyOverflow(String binaryRn, String binarySrc2, String binaryResult, int lenght, String op) {
        boolean result = false;
        if (binaryRn.length() > 32) {
            binaryRn = binaryRn.substring(binaryRn.length() - 32);

        }
        if (binaryRn.length() < 32) {
            String aux = "";
            for (int i = 0; i < 32 - binaryRn.length(); i++) {
                aux = aux + "0";
            }
            binaryRn = aux + binaryRn;

        }
        if (binarySrc2.length() > 32) {
            binarySrc2 = binarySrc2.substring(binarySrc2.length() - 32);

        }
        if (binarySrc2.length() < 32) {
            String aux = "";
            for (int i = 0; i < 32 - binarySrc2.length(); i++) {
                aux = aux + "0";
            }
            binarySrc2 = aux + binarySrc2;
        }

        if (lenght > 32) {
            binaryResult = binaryResult.substring(lenght - 32);

        }
        if (lenght < 32) {
            String aux = "";
            for (int i = 0; i < 32 - lenght; i++) {
                aux = aux + "0";
            }
            binaryResult = aux + binaryResult;
        }

        if (binaryRn.charAt(0) != binaryResult.charAt(0)) {
            if (op == "add") {
                if (binaryRn.charAt(0) == binarySrc2.charAt(0)) {
                    result = true;
                }
            }
            if (op == "sub") {
                if (binaryRn.charAt(0) != binarySrc2.charAt(0)) {
                    result = true;
                }

            }
        }
        return result;
    }

    public void cmn(Long rn, Long src2) {
        Long result = rn + src2;
        String binaryResult = Long.toBinaryString(result);
        String binaryRn = Long.toBinaryString(rn);
        String binarySrc2 = Long.toBinaryString(src2);
        int lenghtResult = binaryResult.length();
        //Negative
        this.conditionFlag.setNegative(this.verifyNegative(lenghtResult, binaryResult));
        //Zero   
        this.conditionFlag.setZero(this.verifyZero(lenghtResult, binaryResult));
        //Carry
        this.conditionFlag.setCarry(this.verifyCarry(lenghtResult));
        //overflow 
        this.conditionFlag.setoVerflow(this.verifyOverflow(binaryRn, binarySrc2, binaryResult, lenghtResult, "add"));

    }

    public String addNegative(String src2) {
        String aux = "FFFFFFFF";
        Long auxValue = Long.parseLong(aux, 16);
        Long src2Value = Long.parseLong(src2, 16);
        String result = Long.toString(auxValue - src2Value + 1, 16);
        if (result.length() > 8) {
            result = result.substring(result.length() - 8);

        }
        return result;

    }

    public void cmp(Long rn, String src2) {
        Long src2Value;
        if (src2.contains("#")) {
            if (src2.contains("x")) {

                src2 = src2.substring(src2.indexOf('x') + 1);
                src2 = addNegative(src2);
                src2Value = Long.parseLong(src2, 16);

            } else {
                src2 = "-" + src2.substring(src2.indexOf('#') + 1);
                src2Value = Long.valueOf(src2);
            }
        } //registro
        else {
            src2 = bankRegister.findRegister(src2);
            src2 = src2.substring(src2.indexOf('x') + 1);

            src2 = addNegative(src2);

            src2Value = Long.parseLong(src2, 16);
        }

        Long result = rn + src2Value;

        String binaryResult = Long.toBinaryString(result);
        String binaryRn = Long.toBinaryString(rn);
        String binarySrc2 = Long.toBinaryString(src2Value);

        //  System.out.println("rn: "+binaryRn+", src: "+binarySrc2+" ,result: "+binaryResult);
        int lenghtResult = binaryResult.length();
        //Negative
        this.conditionFlag.setNegative(this.verifyNegative(lenghtResult, binaryResult));
        //Zero   
        this.conditionFlag.setZero(this.verifyZero(lenghtResult, binaryResult));
        //Carry
        this.conditionFlag.setCarry(this.verifyCarry(lenghtResult));
        //overflow 
        this.conditionFlag.setoVerflow(this.verifyOverflow(binaryRn, binarySrc2, binaryResult, lenghtResult, "sub"));
    }

}
