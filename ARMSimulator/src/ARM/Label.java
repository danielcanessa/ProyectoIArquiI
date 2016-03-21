/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ARM;

/**
 *
 * @author Felipe
 */
public class Label {
    String labelName;
    int labelDirection;
    
    public Label(String name, int direction){
        labelName = name;
        labelDirection = direction;
    }
    
    public String getName(){
        return labelName;
    }
    
    public int getDirection(){
        return labelDirection;
    }
    
}
