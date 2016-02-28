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
public class Register {

    private String r0;
    private String r1;
    private String r2;
    private String r3;
    private String r4;
    private String r5;
    private String r6;
    private String r7;
    private String r8;
    private String r9;
    private String r10;
    private String r11;
    private String r12;
    private String r13;
    private String r14;
    private String r15;

    /**
     * @return the r0
     */
    public String getR0() {
        return r0;
    }

    /**
     * @param r0 the r0 to set
     */
    public void setR0(String r0) {
        this.r0 = r0;
    }

    /**
     * @return the r1
     */
    public String getR1() {
        return r1;
    }

    /**
     * @param r1 the r1 to set
     */
    public void setR1(String r1) {
        this.r1 = r1;
    }

    /**
     * @return the r2
     */
    public String getR2() {
        return r2;
    }

    /**
     * @param r2 the r2 to set
     */
    public void setR2(String r2) {
        this.r2 = r2;
    }

    /**
     * @return the r3
     */
    public String getR3() {
        return r3;
    }

    /**
     * @param r3 the r3 to set
     */
    public void setR3(String r3) {
        this.r3 = r3;
    }

    /**
     * @return the r4
     */
    public String getR4() {
        return r4;
    }

    /**
     * @param r4 the r4 to set
     */
    public void setR4(String r4) {
        this.r4 = r4;
    }

    /**
     * @return the r5
     */
    public String getR5() {
        return r5;
    }

    /**
     * @param r5 the r5 to set
     */
    public void setR5(String r5) {
        this.r5 = r5;
    }

    /**
     * @return the r6
     */
    public String getR6() {
        return r6;
    }

    /**
     * @param r6 the r6 to set
     */
    public void setR6(String r6) {
        this.r6 = r6;
    }

    /**
     * @return the r7
     */
    public String getR7() {
        return r7;
    }

    /**
     * @param r7 the r7 to set
     */
    public void setR7(String r7) {
        this.r7 = r7;
    }

    /**
     * @return the r8
     */
    public String getR8() {
        return r8;
    }

    /**
     * @param r8 the r8 to set
     */
    public void setR8(String r8) {
        this.r8 = r8;
    }

    /**
     * @return the r9
     */
    public String getR9() {
        return r9;
    }

    /**
     * @param r9 the r9 to set
     */
    public void setR9(String r9) {
        this.r9 = r9;
    }

    /**
     * @return the r10
     */
    public String getR10() {
        return r10;
    }

    /**
     * @param r10 the r10 to set
     */
    public void setR10(String r10) {
        this.r10 = r10;
    }

    /**
     * @return the r11
     */
    public String getR11() {
        return r11;
    }

    /**
     * @param r11 the r11 to set
     */
    public void setR11(String r11) {
        this.r11 = r11;
    }

    /**
     * @return the r12
     */
    public String getR12() {
        return r12;
    }

    /**
     * @param r12 the r12 to set
     */
    public void setR12(String r12) {
        this.r12 = r12;
    }

    /**
     * @return the r13
     */
    public String getR13() {
        return r13;
    }

    /**
     * @param r13 the r13 to set
     */
    public void setR13(String r13) {
        this.r13 = r13;
    }

    /**
     * @return the r14
     */
    public String getR14() {
        return r14;
    }

    /**
     * @param r14 the r14 to set
     */
    public void setR14(String r14) {
        this.r14 = r14;
    }

    /**
     * @return the r15
     */
    public String getR15() {
        return r15;
    }

    /**
     * @param r15 the r15 to set
     */
    public void setR15(String r15) {
        this.r15 = r15;
    }

    public String findRegister(String register) {
        String aux = register.toLowerCase();
        String out = "";
        switch (aux) {
            case "r0":
                out=this.r0;
                break;
            case "r1":
                out=this.r1;
                break;
            case "r2":
                out=this.r2;
                break;
            case "r3":
                out=this.r3;
                break;
            case "r4":
                out=this.r4;
                break;
            case "r5":
                out=this.r5;
                break;
            case "r6":
                out=this.r6;
                break;
            case "r7":
                out=this.r7;
                break;
            case "r8":
                out=this.r8;
                break;
            case "r9":
                out=this.r9;
                break;
            case "r10":
                out=this.r10;
                break;
            case "r11":
                out=this.r11;
                break;
            case "r12":
                out=this.r12;
                break;
            case "r13":
                out=this.r13;
                break;
            case "r14":
                out=this.r14;
                break;
            case "r15":
                out=this.r15;
                break;                
            default:
                System.out.println("Error en el metodo find register");
                break;
        }
        return out;

    }
    
    public void setRegister(String register, String data) {        
        String aux = register.toLowerCase();        
        switch (aux) {
            case "r0":
                this.setR0(data);
                break;
            case "r1":
                this.setR1(data);
                break;
            case "r2":
                this.setR2(data);
                break;
            case "r3":
                this.setR3(data);
                break;
            case "r4":
                this.setR4(data);
                break;
            case "r5":               
                this.setR5(data);
                break;
            case "r6":
                this.setR6(data);
                break;
            case "r7":
                this.setR7(data);
                break;
            case "r8":
                this.setR8(data);
                break;
            case "r9":
                this.setR9(data);
                break;
            case "r10":
                this.setR10(data);
                break;
            case "r11":
                this.setR11(data);
                break;
            case "r12":
                this.setR12(data);
                break;
            case "r13":
                this.setR13(data);
                break;
            case "r14":
                this.setR14(data);
                break;
            case "r15":
                this.setR15(data);
                break;                
            default:
                System.out.println("Error en el metodo set register");
                break;
        }    
    }

}
