/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ARM;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author Daniel
 */
public class WriteTxtFile {

    FileWriter fichero ;
    PrintWriter pw;
    
    public WriteTxtFile()
    {
        this.fichero=null;
        this.pw = null;
    }

    public void writeFile(String[] lines) {
        try {
            fichero = new FileWriter("codeARMJTextPane.txt");
            pw = new PrintWriter(fichero);

            for (int i = 0; i < lines.length; i++) {
                if(lines[i]!="\\n")
                {
                    pw.println(lines[i]);
                    
                }
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

}
