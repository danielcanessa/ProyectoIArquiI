/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Daniel
 */
public class TEST {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String a = "add R0,R1,#0x10";
        
        a=a.toLowerCase().replace("add", "");
        a=a.toLowerCase().replace(" ", "");
        
        String b = a.substring(0, a.indexOf(','));
        String c = a.substring(a.indexOf(',')+1,a.lastIndexOf(','));
        String d = a.substring(a.lastIndexOf(',')+1);
        d=d.substring(d.indexOf('x')+1);
        
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
        
        System.out.println(Long.valueOf("10203040").longValue());
       
    }

}
