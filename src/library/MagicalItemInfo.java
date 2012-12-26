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
public class MagicalItemInfo implements Serializable, Remote{
    String name;
    String symbol;
    String picture;
    int index;
    public MagicalItemInfo(String name, String symbol, String picture, int index)
    {
        this.name=name;
        this.symbol=symbol;
        this.picture=picture;
        this.index=index;
    }
    
    public int getIndex()
    {
        return this.index;
    }
    
    public String getName() {
        return this.name;
    }
    public String getSymbol() {
        return this.symbol;
    }
    public String getPicture() {
        return this.picture;
    }
}
