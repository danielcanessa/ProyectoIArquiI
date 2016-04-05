/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ARM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe
 */
public class CodeGeneration {

    //List that contains all of the labels and their directions
    static List<Label> labelList = new ArrayList<Label>();

    //List that contains all of the labels and their directions
    static List<String> instructionList = new ArrayList<String>();

    //List that contains all of the errors of the program
    static List<String> errorList = new ArrayList<String>();

    //Flag that indicates to stop the calculation of the labels direction
    static int calculatingAddressesCompleted = 0;

    //Flag that indicates if a lexical error occurred
    static int lexicalError = 0;

    //Flag that indicates if a syntactic error occurred
    static int syntacticError = 0;

    //Flag that indicates if a semantic error occurred
    static int semanticError = 0;

    //Flag that indicates if a label dont exist
    static int labelExist = 0;
    
    //Declare variable containing the start of the memory address for instructions
   public static int instruction_direction = 0;

    /**
     * Método que interpreta el contenido del archivo que se encuentra en el
     * path que recibe como parámentro
     *
     * @param path ruta del archivo a interpretar
     */
    public void generateCode() throws IOException {
        createOutputFile();
        lexicalError = 0;
        syntacticError = 0;
        semanticError = 0;
        calculatingAddressesCompleted = 0;
        instruction_direction = 0;
        errorList.clear();
        labelList.clear();
//        CUP$SyntacticAnalizer$actions.instruction_direction = 0;
        try {

            // Asignación del nombre de archivo por defecto que usará la aplicación
            String archivo = "codeARMJTextPane.txt";

            // Se trata de leer el archivo y analizarlo en la clase que se ha creado con JFlex
            BufferedReader buffer = new BufferedReader(new FileReader(archivo));
            LexicalAnalyzer analizadorJFlex = new LexicalAnalyzer(buffer);
            //int i = 0;
            while (true) {

                // Obtener el token analizado y mostrar su información
                //TokenPersonalizado token = analizadorJFlex.yylex();
                if (!analizadorJFlex.thereTokens()) {
                    calculatingAddressesCompleted = 1;
                    break;
                }

                analizadorJFlex.next_token();
            }
        } catch (Exception e) {
            //System.out.println(e.toString());
        }

        //Make syntactic analyzer
        SyntacticAnalizer pars;
        try {
            String inputFile = "codeARMJTextPane.txt";
            BufferedReader buffer = new BufferedReader(new FileReader(inputFile));
            pars = new SyntacticAnalizer(new LexicalAnalyzer(buffer));
            pars.parse();
            String path = "out.txt";
            File outputFile = new File(path);
            BufferedWriter writeInstruction = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile, true), "utf-8"));
            writeInstruction.write("-----------------------------------------------------------" + "\r\n");
            //Cierra el flujo de escritura  
            writeInstruction.close();
        } catch (Exception ex) {
            System.out.println("Error fatal en compilación de entrada.");
            System.out.println("Causa: " + ex.getCause());
        }
    }

    //Check if the out.txt have been created, if not its created
    private void createOutputFile() throws IOException {
        String path = "out.txt";
        File outputFile = new File(path);
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(outputFile));
        bw.write("-----------------------------------------------------------" + "\r\n");
        bw.write("\n\n");
        bw.close();
        // El fichero no existe y hay que crearlo
        //   }

    }
    
    //Write in the output file
public static void writeOutputFile(String hexCode) throws IOException{
    String path = "out.txt";
    File outputFile = new File(path);
    BufferedWriter writeInstruction=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile,true), "utf-8"));  
    writeInstruction.write(hexCode);
    //Cierra el flujo de escritura  
    writeInstruction.close(); 
            
    }

//Function that writes that converts the instruction into hexadecimal and writes in the file
public static void generateHexInstruction(String binaryCode) throws IOException{
    if(CodeGeneration.lexicalError == 0 && CodeGeneration.syntacticError == 0 &&
        CodeGeneration.semanticError == 0){
        CodeGeneration.instructionList.add(Long.toHexString(Long.parseLong(binaryCode,2)));
        String hexCode = "0x" + Long.toHexString(Long.parseLong(binaryCode,2)) + "\r\n";
        String hexInstruction = Integer.toBinaryString(instruction_direction + 0b10000000000000000000000000000000).substring(1);
        hexInstruction = "0x" + Long.toHexString(Long.parseLong(hexInstruction,2)) + "\t\t";
        writeOutputFile(hexInstruction);
        writeOutputFile(hexCode);
        instruction_direction = instruction_direction + 4;
    }
}
  
 //Esta instruccion genera el codigo para las instrucciones B y BL
 public static String generateCodeBranchInstructions(String cond, String funct, Object label) throws IOException{
    String labelName = label.toString();
    int labelDirection = findDirection(labelName);
    int pc_plus_8 = instruction_direction + 8;
    int branch_target_address = labelDirection - pc_plus_8;
    int immediate = branch_target_address/4;

    if(CodeGeneration.labelExist == 0){
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Label Error: The label " + labelName + " don't exist.");
    }

  //Convierte los registros a su valor en binario(24 bits)
    String imm24 = (Integer.toBinaryString(immediate).length() == 32) ? Integer.toBinaryString(immediate).substring(8,32) : Integer.toBinaryString(immediate + 0b1000000000000000000000000).substring(1);

  //Convierte la instrucción a hexadecimal
    String hexCode = cond + "10" + funct + imm24;
    generateHexInstruction(hexCode);
    return hexCode;
 }

//Esta funcion se encarga de ir a buscar la direccion de la etiqueta
public static int findDirection(String labelName){
    int large = CodeGeneration.labelList.size();
    int direction = 0;
    CodeGeneration.labelExist = 0;
    for(int i = 0; i < large; i++){
        String findLabel = CodeGeneration.labelList.get(i).getName();
        if(findLabel.equals(labelName)){
            direction = CodeGeneration.labelList.get(i).getDirection();
            CodeGeneration.labelExist = 1;
            break;
        }
    }
    return direction;
}

 /**
  * This function is responsible for generating the code for functions with 
  * register addressing(lsl R1,R2,R3), its for data-processing instrcutions
 **/
 public static String generateCodeAddressingRegisterOnly(String cond, String op, String i,
 String cmd, String s, Object Rn, Object Rd, String shamt5, String sh, Object Rm) throws IOException{
    
    String rd = Rd.toString();
    rd = rd.substring(1,rd.length()); //extracts the number of rd
    String rn = Rn.toString(); 
    rn = rn.substring(1,rn.length()); //extracts the number of rn
    String rm = Rm.toString();
    rm = rm.substring(1,rm.length()); //extracts the number of rm

    //Convert to integer each register to convert it to his corresponding binary
    int destination_register = Integer.parseInt(rd);
    int sourcern_register = Integer.parseInt(rn);
    int sourcerm_register = Integer.parseInt(rm);
    
    if(((destination_register > 15) || (0 > destination_register)) || ((sourcerm_register > 15) || (0 > sourcerm_register))
            || ((sourcern_register > 15) || (0 > sourcern_register))){
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Register Number Error: The register dont exist");
    }

    //Converts each register number to his binary value(4 bits)
    String register_rd = Integer.toBinaryString(destination_register + 0b10000).substring(1);
    String register_rn = Integer.toBinaryString(sourcern_register + 0b10000).substring(1);
    String register_rm = Integer.toBinaryString(sourcerm_register + 0b10000).substring(1);


    //Converts the instruction to hexadecimal
    String hexCode = cond + op + i + cmd + s + register_rn + register_rd + shamt5 + sh + 0 + register_rm;
    generateHexInstruction(hexCode);
    return "The generated code is: " + cmd + i + cond + Rd + Rn + Rm;
 }


/**
  * This function is responsible for generating the code for functions with 
  * register-shifted register addressing(lsl R1,R2,R3), its for shifts
  * instructions
 **/
public static String generateCodeAddressingRegisterShiftedRegister(String cond, String op, String i,
 String cmd, String s, String Rn, Object Rd, Object Rs, String sh, Object Rm) throws IOException{
    
    String rd = Rd.toString();
    rd = rd.substring(1,rd.length()); //extract the number of rd
    String rs = Rs.toString(); 
    rs = rs.substring(1,rs.length()); //extract the number of rs
    String rm = Rm.toString();
    rm = rm.substring(1,rm.length()); //extract the number of rm

    //Convert to integer each register to convert it to his corresponding binary
    int destination_register = Integer.parseInt(rd);
    int sourcers_register = Integer.parseInt(rs);
    int sourcerm_register = Integer.parseInt(rm);
    
    if(((destination_register > 15) || (0 > destination_register)) || ((sourcerm_register > 15) || (0 > sourcerm_register))
            || ((sourcers_register > 15) || (0 > sourcers_register))){
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Register Number Error: The register dont exist");
    }

    //Convert each register to his binary value(4 bits)
    String register_rd = Integer.toBinaryString(destination_register + 0b10000).substring(1);
    String register_rs = Integer.toBinaryString(sourcers_register + 0b10000).substring(1);
    String register_rm = Integer.toBinaryString(sourcerm_register + 0b10000).substring(1);

    //Converts the instruction to hexadecimal
    String hexCode = cond + op + i + cmd + s + Rn + register_rd + register_rs + "0" + sh + "1" + register_rm;
    generateHexInstruction(hexCode);
    return "The generated code is: " + cmd + i + cond + Rd + Rn + Rm;
 }

/**
  * This function is responsible for generating the code for
  * multiplication instructions
 **/
public static String generateCodeMultiplicationInstr(String cond, String cmd, 
String s, Object Rd, Object Ra, Object Rm,  Object Rn) throws IOException{

    String rd = Rd.toString();
    rd = rd.substring(1,rd.length()); //extract the number of rd
    String rm = Rm.toString(); 
    rm = rm.substring(1,rm.length()); //extract the number of rm
    String rn = Rn.toString();
    rn = rn.substring(1,rn.length()); //extract the number of rn
    String ra = Ra.toString();
    ra = ra.substring(1,ra.length()); //extract the number of ra

    //Convert to integer each register to his corresponding binary
    int destination_register = Integer.parseInt(rd);
    int sourcern_register = Integer.parseInt(rn);
    int sourcerm_register = Integer.parseInt(rm);
    int sourcera_register = Integer.parseInt(ra);
    
    if(((destination_register > 15) || (0 > destination_register)) || ((sourcerm_register > 15) || (0 > sourcerm_register))
            || ((sourcern_register > 15) || (0 > sourcern_register)) || ((sourcera_register > 15) || (0 > sourcera_register))){
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Register Number Error: The register dont exist");
    }

    //Convert each register to his binary value(4 bits)
    String register_rd = Integer.toBinaryString(destination_register + 0b10000).substring(1);
    String register_rn = Integer.toBinaryString(sourcern_register + 0b10000).substring(1);
    String register_rm = Integer.toBinaryString(sourcerm_register + 0b10000).substring(1);
    String register_ra = Integer.toBinaryString(sourcera_register + 0b10000).substring(1);

    //Converts the instruction to hexadecimal
    String hexCode = cond + "00" + "00" + cmd + s + register_rd + register_ra + register_rm + "1001" + register_rn;
    generateHexInstruction(hexCode);
    return "The generated code is: " + cmd  + cond + Rd + Rn + Rm;
 }

/**
  * This function is responsible for generating the code for
  * memory access instructions that use register offset LDR R1,[R2,R4]
 **/
public static String generateCodeMemoryRegisterOffset(String cond, 
String p, String u, String b ,String w, String l,
Object Rn, Object Rd, Object Rm) throws IOException{

    String rd = Rd.toString();
    rd = rd.substring(1,rd.length()); //extract the number off rd
    String rn = Rn.toString();
    rn = rn.substring(1,rn.length()); //extract the number off rn
    String rm = Rm.toString();
    rm = rm.substring(1,rm.length()); //contains the number off rm

    //Variables that containst the number of the register(Check if it is correct)
   
    
    //Convert to integer each register to his corresponding binary
    int destination_register = Integer.parseInt(rd);
    int sourcern_register = Integer.parseInt(rn);
    int sourcerm_register = Integer.parseInt(rm);
    
    if(((destination_register > 15) || (0 > destination_register)) || ((sourcerm_register > 15) || (0 > sourcerm_register))
            || ((sourcern_register > 15) || (0 > sourcern_register))){
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Register Number Error: The register dont exist");
    }

    //Convert each register to his binary value(4 bits)
    String register_rd = Integer.toBinaryString(destination_register + 0b10000).substring(1);
    String register_rn = Integer.toBinaryString(sourcern_register + 0b10000).substring(1);
    String register_rm = Integer.toBinaryString(sourcerm_register + 0b10000).substring(1);
    

    //Decodes the instruction to hexadecimal
    String hexCode = cond + "01" + "1" + p + u + b + w + l + register_rn + register_rd + "00000" + "00" + "0" + register_rm;
    generateHexInstruction(hexCode);
    return "The generated code is: "  + cond + Rd + Rn ;
 }

/**
  * This function is responsible for generating the code for
  * memory access instructions that use hexadecimal immediate
 **/
public static String generateCodeMemoryInstrHexImmediate(String cond, String p, String w,
String b, String l, Object Rn, Object Rd, Object hex_immediate) throws IOException{

    String rd = Rd.toString();
    rd = rd.substring(1,rd.length()); //extract the number off rd
    String rn = Rn.toString();
    rn = rn.substring(1,rn.length()); //extract the number off rn
    String immediate = hex_immediate.toString();
    immediate = immediate.substring(1,immediate.length()); //contains the imm
    
    //Variables that containst the large of the imm and the imm to binary
    int immediate_large= immediate.length(); //contains the large of the imm
    String immediate_register = ""; //contains the imm to binary equivalent

    if(immediate_large > 5){ //here is going to be an error
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Immediate Size Error: The size of the immediate is too large");
    }
    else if(immediate_large <= 5 ){
        int register_imm = Integer.decode(immediate);
        immediate_register = Integer.toBinaryString(register_imm+ 0b1000000000000).substring(1);
    }
    
    //Convert to integer each register to his corresponding binary
    int destination_register = Integer.parseInt(rd);
    int sourcern_register = Integer.parseInt(rn);
    
    if(((destination_register > 15) || (0 > destination_register)) || ((sourcern_register > 15) || (0 > sourcern_register))){
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Register Number Error: The register dont exist");
    }

    //Convert each register to his binary value(4 bits)
    String register_rd = Integer.toBinaryString(destination_register + 0b10000).substring(1);
    String register_rn = Integer.toBinaryString(sourcern_register + 0b10000).substring(1);
    

    //Decodes the instruction to hexadecimal
    String hexCode = cond + "01" + "0" + p + "1" + b + w + l + register_rn + register_rd + immediate_register;
    generateHexInstruction(hexCode);
    return "The generated code is: "  + cond + Rd + Rn ;
 }


/**
  * This function is responsible for generating the code for
  * memory access instructions that use decimal immediate
 **/
public static String generateCodeMemoryInstrDecImmediate(String cond, String p ,String w,
String b, String l, Object Rn, Object Rd, Object decimal_immediate) throws IOException{
    
    String rd = Rd.toString();
    rd = rd.substring(1,rd.length()); //extract the number off rd
    String rn = Rn.toString();
    rn = rn.substring(1,rn.length()); //extract the number off rn
    String immediate = decimal_immediate.toString();
    
    immediate = immediate.substring(1,immediate.length()); //have the imm
    String u = (immediate.charAt(0) == '-') ? "0" : "1"; 
    immediate = (immediate.charAt(0) == '-') ? immediate.substring(1,immediate.length()) : immediate; //Check the sub
    
    //Variables that containst the value of the imm and the imm to binary
    int immediate_value= Integer.parseInt(immediate); //have the imm value
    
    String immediate_register = ""; //contains the imm to binary

    if(immediate_value > 4095){ //If this happens occurs an error
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Immediate Size Error: The size of the immediate is too large");
    }
    
    else if((4096 > immediate_value) && (immediate_value >= 0) ){
        immediate_register = Integer.toBinaryString(immediate_value + 0b1000000000000).substring(1);
    }
    
    //Convert to integer each register to his corresponding binary
    int destination_register = Integer.parseInt(rd);
    int sourcern_register = Integer.parseInt(rn);
    
    if(((destination_register > 15) || (0 > destination_register)) || ((sourcern_register > 15) || (0 > sourcern_register))){
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Register Number Error: The register dont exist");
    }

    //Convert each register to his binary value(4 bits)
    String register_rd = Integer.toBinaryString(destination_register + 0b10000).substring(1);
    String register_rn = Integer.toBinaryString(sourcern_register + 0b10000).substring(1);
    
    //Decodes the instruction to hexadecimal
    String hexCode = cond + "01" + "0" + p + u + b + w + l + register_rn + register_rd + immediate_register;
    generateHexInstruction(hexCode);
    return "The generated code is: "  + cond + Rd + Rn ;
 }

/**
  * This function is responsible for generating the code for
  * data-processing instructions that use decimal immediate
 **/
public static String generateCodeDataProcessingInstrDecImmediate(String cond, 
String cmd, Object Rn, Object Rd, Object decimal_immediate) throws IOException{

    String rd = Rd.toString();
    rd = rd.substring(1,rd.length()); //extract the number of rd
    String rn = Rn.toString();
    rn = rn.substring(1,rn.length()); //extract the number of rn
    String immediate = decimal_immediate.toString();
    immediate = immediate.substring(1,immediate.length()); //have the imm
    
    //Change the cmd if the immediate is negative
    String final_cmd = (immediate.charAt(0) == '-' && cmd == "0000") ? "1110" : cmd; //pasa de and a bic
    final_cmd = (immediate.charAt(0) == '-' && cmd == "1110") ? "0000" : final_cmd; //pasa de bic a and
    final_cmd = (immediate.charAt(0) == '-' && cmd == "0100") ? "0010" : final_cmd; //pasa de add a sub
    final_cmd = (immediate.charAt(0) == '-' && cmd == "0010") ? "0100" : final_cmd; //pasa de sub a add
    final_cmd = (immediate.charAt(0) == '-' && cmd == "1010") ? "1011" : final_cmd; //pasa de cmp a cmn
    final_cmd = (immediate.charAt(0) == '-' && cmd == "1011") ? "1010" : final_cmd; //pasa de cmn a cmp
    final_cmd = (immediate.charAt(0) == '-' && cmd == "1101") ? "1111" : final_cmd; //pasa de mov a mvn

    //Assign the S value
    String s = (final_cmd == "1010" || final_cmd == "1011") ? "1" : "0";

    //Apply the complement to the negative immediate
    int complement_value = ~Integer.parseInt(immediate);
    

    /**
      * This variables contains the rot and imm parameters.
      * The variables that are need it to the rotation revision are initialized
     **/
    int immediate_value= (immediate.charAt(0) == '-' && ((cmd == "0000") || (cmd == "1101") || (cmd=="1110")))? 
                            complement_value: (immediate.charAt(0) == '-' && ((cmd == "0100") || (cmd == "0010") || (cmd=="1010")|| (cmd=="1011")))?
                            Integer.parseInt(immediate.substring(1,immediate.length())): Integer.parseInt(immediate); //large of imm
    String rotation=""; //contains the rotation value
    String immediate_8bits = immediate.substring(0,immediate.length()-1); //extract the imm
    String[] codification = new String[2];
    String immediate_register = "";

    if((immediate_value > 255) ){
        codification = getRotationImmediateValues(immediate_value); //calculate rotation and imm values
        if(codification[0] == "Error"){
           CodeGeneration.semanticError = 1;
           CodeGeneration.errorList.add("Immediate Codification Error: Rotated constant is too wide");
        }
        else{
           rotation = codification[1];
           immediate_register = codification[0];
        }
    }

    else if((256 > immediate_value) && (immediate_value >= 0)){
        rotation = "0000";
        int register_imm = immediate_value;
        immediate_register = Integer.toBinaryString(register_imm + 0b100000000).substring(1);
    }
    
    //Convert to integer each register to his corresponding binary
    int destination_register = Integer.parseInt(rd);
    int sourcern_register = Integer.parseInt(rn);
    
    if(((destination_register > 15) || (0 > destination_register)) || ((sourcern_register > 15) || (0 > sourcern_register))){
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Register Number Error: The register dont exist");
    }

    //Convert each register to his binary value(4 bits)
    String register_rd = Integer.toBinaryString(destination_register + 0b10000).substring(1);
    String register_rn = Integer.toBinaryString(sourcern_register + 0b10000).substring(1);
    
    //Decodes the instruction to hexadecimal
    String hexCode = cond + "00" + "1" + final_cmd + s + register_rn + register_rd + rotation + immediate_register;
    generateHexInstruction(hexCode);
    return "The generated code is: " + cmd  + cond + Rd + Rn ;
 }


/**
  * This function is responsible for generating the code for
  * data-processing shifts instructions that use decimal immediate
 **/
public static String generateCodeShiftInstrDecImmediate(String cond, 
String cmd, Object Rd, Object shamt5, String sh, Object Rm) throws IOException{

    String rd = Rd.toString();
    rd = rd.substring(1,rd.length()); //extract the number of rd
    String rm = Rm.toString();
    rm = rm.substring(1,rm.length()); //extract the number of rm
    String shamt = shamt5.toString();
    shamt = shamt.substring(1,shamt.length()); //contains the shamt
    
    //Initialize the variables that are going to be used in the code generation
    int shamt_value= Integer.parseInt(shamt); //contains the value of the shamt
    String shamt_register = "";
    
    if((shamt_value > 31) && (shamt_value >= 0)){
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Immediate Size Error: The size of the immediate is too large");

    }

    shamt_register = Integer.toBinaryString(shamt_value + 0b100000).substring(1); //shamt of 5 bits
    
    //Convert to integer each register to his corresponding binary
    int destination_register = Integer.parseInt(rd);
    int sourcerm_register = Integer.parseInt(rm);
    
    if(((destination_register > 15) || (0 > destination_register)) || ((sourcerm_register > 15) || (0 > sourcerm_register))){
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Register Number Error: The register dont exist");
    }

    //Convert each register to his binary value(4 bits)
    String register_rd = Integer.toBinaryString(destination_register + 0b10000).substring(1);
    String register_rm = Integer.toBinaryString(sourcerm_register+ 0b10000).substring(1);
    
    //Decodes the instruction to hexadecimal
    String hexCode = cond + "00" + "0" + cmd + "0" + "0000" + register_rd + shamt_register + sh + "0" + register_rm;
    generateHexInstruction(hexCode);
    return "The generated code is: " + cmd  + cond + Rd + Rm ;
 }

/**
  * This function is responsible for generating the code for
  * data-processing shifts instructions that use hex immediate
 **/
public static String generateCodeShiftInstrHexImmediate(String cond, 
String cmd, Object Rd, Object shamt5, String sh, Object Rm) throws IOException{

    String rd = Rd.toString();
    rd = rd.substring(1,rd.length()); //extract the number of rd
    String rm = Rm.toString();
    rm = rm.substring(1,rm.length()); //extract the number of rm
    String shamt = shamt5.toString();
    shamt = shamt.substring(1,shamt.length()); //contains the shamt
   
    //Initialize the variables that are going to be used in the code generation
    int shamt_value= Integer.decode(shamt); //contains the value of the shamt
    String shamt_register = "";
    
    if((shamt_value > 31) && (shamt_value >= 0)){
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Immediate Size Error: The size of the immediate is too large");

    }

    shamt_register = Integer.toBinaryString(shamt_value + 0b100000).substring(1); //shamt of 5 bits
    
    //Convert to integer each register to his corresponding binary
    int destination_register = Integer.parseInt(rd);
    int sourcerm_register = Integer.parseInt(rm);
    
    if(((destination_register > 15) || (0 > destination_register)) || ((sourcerm_register > 15) || (0 > sourcerm_register))){
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Register Number Error: The register dont exist");
    }

    //Convert each register to his binary value(4 bits)
    String register_rd = Integer.toBinaryString(destination_register + 0b10000).substring(1);
    String register_rm = Integer.toBinaryString(sourcerm_register+ 0b10000).substring(1);
    
    //Decodes the instruction to hexadecimal
    String hexCode = cond + "00" + "0" + cmd + "0" + "0000" + register_rd + shamt_register + sh + "0" + register_rm;
    generateHexInstruction(hexCode);
    return "The generated code is: " + cmd  + cond + Rd + Rm ;
 }


/**
  * This function is responsible for generating the code for
  * data-processing instructions that use hexadecimal immediate
 **/
public static String generateCodeDataProcessingInstrHexImmediate(String cond, 
String cmd, Object Rn, Object Rd, Object hex_immediate) throws IOException{

    String rd = Rd.toString();
    rd = rd.substring(1,rd.length()); //extract the number of rd
    String rn = Rn.toString();
    rn = rn.substring(1,rn.length()); //extract the number of rn
    String immediate = hex_immediate.toString();
    immediate = immediate.substring(1,immediate.length()); //have the imm

    //Assign the S value
    String s = (cmd == "1010" || cmd == "1011") ? "1" : "0";
    
    /**
      * This variables contains the rot and imm parameters.
      * The variables that are need it to the rotation revision are initialized
     **/
    int immediate_large= immediate.length(); //have the large immediate
    String rotation=""; //have the rotation value
    String immediate_8bits = immediate.substring(0,immediate.length()-1); //extract the immediate
    String[] codification = new String[2];
    String immediate_register = "";

  //  if(immediate_large > 5){
  //      CodeGeneration.semanticError = 1;
  //      CodeGeneration.errorList.add("Immediate Size Error: The size of the immediate is too large");

   // }
    if(immediate_large >= 5 ){
        codification = getRotationImmediateValues(Integer.decode(immediate)); //calculate rotation and immediate values
        if(codification[0] == "Error"){
           CodeGeneration.semanticError = 1;
           CodeGeneration.errorList.add("Immediate Codification Error: Rotated constant is too wide");
        }
        else{
           rotation = codification[1];
           immediate_register = codification[0];
        }
    }

    else if(immediate_large <= 4){
        rotation = "0000";
        int register_imm = Integer.decode(immediate);
        immediate_register = Integer.toBinaryString(register_imm+ 0b100000000).substring(1);
    }
    
    //Convert to integer each register to his corresponding binary
    int destination_register = Integer.parseInt(rd);
    int sourcern_register = Integer.parseInt(rn);
    
    if(((destination_register > 15) || (0 > destination_register)) || ((sourcern_register > 15) || (0 > sourcern_register))){
        CodeGeneration.semanticError = 1;
        CodeGeneration.errorList.add("Register Number Error: The register dont exist");
    }

    //Convert each register to his binary value(4 bits)
    String register_rd = Integer.toBinaryString(destination_register + 0b10000).substring(1);
    String register_rn = Integer.toBinaryString(sourcern_register + 0b10000).substring(1);
    
    //Decodes the instruction to hexadecimal
    String hexCode = cond + "00" + "1" + cmd + s + register_rn + register_rd + rotation + immediate_register;
    generateHexInstruction(hexCode);
    return "The generated code is: " + cmd  + cond + Rd + Rn ;
 }


//Function that performs the encoding of the immediate value and rotation value
public static String[] getRotationImmediateValues(int hex_value){
        //The array that returns the immediate and rotation is declared
        String[] codification = new String[2];
        codification[0] = "Error"; //if an imm dont match return an error

        //This variable contains the immediate extended with zeros to complete the 32-bit
        String immediate = String.format("%32s", Integer.toBinaryString(hex_value)).replace(" ", "0");

        /**
          * This variables contains the rot and imm parameters.
          * Also contains the variables for the loop 
          * The variables that are need it to the rotation revision are initialized
          **/
        String final_immediate = ""; //Contains the immediate encoded
        int i = 24; 
        int j = 0; 
        int k = 2;
        int rote_value = 0; //Contains the 4 bits rote_value
        int integer_immediate = 0;
        int immediate_rotated = 0; //Contains the immediate_rotated
        
        for(int n = 0; k <= 22; n++){
            if(i < 33){
                if(i == 24){
                    final_immediate = immediate.substring(i,32);
                    integer_immediate = Integer.parseInt(final_immediate,2);
                    immediate_rotated = Integer.rotateRight(integer_immediate, 2*rote_value);
                    if(immediate_rotated == hex_value){
                        codification[0] = final_immediate; //assigns the encoded immediate 
                        codification[1] = Integer.toBinaryString(rote_value); //assigns the encoded rotation    
                        break;
                    }
                    i = i+2;
                    rote_value = rote_value + 1;
                    
                }
                else{
                    final_immediate = immediate.substring(i,32) + immediate.substring(j,6-(31-i) + 1);
                    integer_immediate = Integer.parseInt(final_immediate,2);
                    immediate_rotated = Integer.rotateRight(integer_immediate, 2*rote_value);
                    if(immediate_rotated == hex_value){
                        codification[0] = final_immediate; //assigns the encoded immediate 
                        codification[1] = Integer.toBinaryString(rote_value); //assigns the encoded rotation 
                        break;
                    }
                    i = i+2;   
                    rote_value = rote_value + 1;
                }
            }
            else if(k < 24){
                final_immediate = immediate.substring(k,(k+8));
                integer_immediate = Integer.parseInt(final_immediate,2);
                immediate_rotated = Integer.rotateRight(integer_immediate, 2*rote_value);
                if(immediate_rotated == hex_value){
                    codification[0] = final_immediate; //assigns the encoded immediate
                    codification[1] = Integer.toBinaryString(rote_value); //assigns the encoded rotation 
                    break;
                }
                k = k+2;
                rote_value = rote_value + 1;
            }
        }
        return codification;
    }
}