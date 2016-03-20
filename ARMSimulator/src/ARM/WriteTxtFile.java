/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ARM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Daniel
 */
public class WriteTxtFile {

    FileWriter fichero;
    PrintWriter pw;

    public WriteTxtFile() {
        this.fichero = null;
        this.pw = null;
    }

    public String readFile(String root) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(root))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String read = sb.toString();
            return read;
        }
    }

    public void writeFile(String[] lines, String root) {
        try {
            fichero = new FileWriter(root);
            pw = new PrintWriter(fichero);

            for (int i = 0; i < lines.length; i++) {
                if (lines[i].length() != 1) {
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
