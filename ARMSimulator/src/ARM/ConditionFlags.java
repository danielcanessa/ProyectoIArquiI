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
public class ConditionFlags {
    private boolean negative;
    private boolean zero;
    private boolean carry;
    private boolean oVerflow;
    
    public ConditionFlags()
    {
        this.setCarry(false);
        this.setNegative(false);
        this.setZero(false);
        this.setoVerflow(false);
    }
    
    public void cleanFlags()
    {
        this.setCarry(false);
        this.setNegative(false);
        this.setZero(false);
        this.setoVerflow(false);
    }

    /**
     * @return the negative
     */
    public boolean isNegative() {
        return negative;
    }

    /**
     * @param negative the negative to set
     */
    public void setNegative(boolean negative) {
        this.negative = negative;
    }

    /**
     * @return the zero
     */
    public boolean isZero() {
        return zero;
    }

    /**
     * @param zero the zero to set
     */
    public void setZero(boolean zero) {
        this.zero = zero;
    }

    /**
     * @return the carry
     */
    public boolean isCarry() {
        return carry;
    }

    /**
     * @param carry the carry to set
     */
    public void setCarry(boolean carry) {
        this.carry = carry;
    }

    /**
     * @return the oVerflow
     */
    public boolean isoVerflow() {
        return oVerflow;
    }

    /**
     * @param oVerflow the oVerflow to set
     */
    public void setoVerflow(boolean oVerflow) {
        this.oVerflow = oVerflow;
    }
    
}
