/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ARM;
import java.io.File;
/**
 *
 * @author Felipe
 */
public class CreateLexicalSyntacticAnalyzers {
    public static void main(String[] args) {
        // TODO code application logic here
        String path = "src\\ARM\\Lexical.flex";
        generateLexer(path);
        generateCUP();
    }
    /*Esto se hace para crear el archivo Analizador.java el cual se llama asi porque se le cambio el nombre */
    public static void generateLexer(String path){
        File file=new File(path);
        jflex.Main.generate(file);
      //  java_cup.Main.main(path);
    }
    
    public static void generateCUP() {
        String opciones[] = new String[5];
        //Seleccionamos la opción de dirección de destino
        opciones[0] = "-destdir";
        //Le damos la dirección
        opciones[1] = "src\\ARM\\";
        //Seleccionamos la opción de nombre de archivo
        opciones[2] = "-parser";
        //Le damos el nombre que queremos que tenga
        opciones[3] = "SyntacticAnalizer";
        //Le decimos donde se encuentra el archivo .cup
        opciones[4] = "src\\ARM\\Syntactic.cup";
        try {
            java_cup.Main.main(opciones);
        } catch (Exception e) {
            System.out.print(e);
        }
    }
    
}
