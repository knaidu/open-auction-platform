/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.util.Calendar;
import library.History;

/**
 *
 * @author Asish
 */
public class DiagonAlleySellerAccount  extends DiagonAlleyAccount{
    Everyone e;
    DiagonAlleySellerAccount(Everyone e)
    {
        this.e=e;
    }
    
    public void add(int price, int quantity, Calendar time)
    {
        super.add(price, quantity, time);
        getMinimum();
    }
    
    public void modify()
    {
        if(index != -1)
        {
            History h=history.remove(index);
            add(this.price, this.quantity, this.time);
        }
    }

    public void modify(int i)
    {
        if(i != -1&&i<history.size())
        {
            History h=history.remove(i);
            add(this.price, this.quantity, this.time);
        }
    }
}
