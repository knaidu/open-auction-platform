/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;

import java.io.Serializable;
import java.rmi.Remote;

/**
 *
 * @author Asish
 */
public class EveryoneRef implements Serializable, Remote{
    private boolean isWizard;
    private int id;
    private int itemNumber;
    public EveryoneRef(boolean isWizard, int id, int itemNumber)
    {
        this.isWizard=isWizard;
        this.id=id;
    }
    public boolean isWizard()
    {
        return isWizard;
    }
    public int getID()
    {
        return id;
    }
    public int getItemNumber()
    {
        return itemNumber;
    }
}
