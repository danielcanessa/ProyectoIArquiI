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

    /**
     * Método que interpreta el contenido del archivo que se encuentra en el path
     * que recibe como parámentro
     * @param path ruta del archivo a interpretar
     */
    public void generateCode() throws IOException {
        createOutputFile();
        lexicalError = 0;
        syntacticError = 0;
        semanticError = 0;
        calculatingAddressesCompleted = 0;
        errorList.clear();
        labelList.clear();
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
            pars=new SyntacticAnalizer(new LexicalAnalyzer(buffer));
            pars.parse();
            String path = "out.txt";
            File outputFile = new File(path);
            BufferedWriter writeInstruction=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile,true), "utf-8"));  
            writeInstruction.write("-----------------------------------------------------------" + "\r\n");  
    //Cierra el flujo de escritura  
            writeInstruction.close();
        } catch (Exception ex) {
            System.out.println("Error fatal en compilación de entrada.");
            System.out.println("Causa: "+ex.getCause());
        }
    }
    
    //Check if the out.txt have been created, if not its created
    private void createOutputFile() throws IOException{
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
}