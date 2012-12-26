/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import library.History;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
/**
 *
 * @author Asish
 */

public class DiagonAlleyAccount{
    ArrayList<History> history;
    int price;
    int quantity;
    Calendar time;
    int index;
    
    DiagonAlleyAccount()
    {
        index=-1;
        history=new ArrayList();
    }
    
    public History[] getAllHistory()
    {
        History h[]=new History[history.size()];
        for(int i=0; i<history.size(); i++)
        {
            h[i]=history.get(i);
        }
        return h;
    }
    
    public void add(int price, int quantity, Calendar time)
    {
        History h=new History(price, quantity, time);
        history.add(h);
    }
    
    public void getHistory(int id)
    {
        if(id<history.size())
        {
            History h=history.get(id);
            price=h.price;
            quantity=h.quantity;
            time=h.time;
        }
    }
    
    public void getMinimum()
    {
        History h;
        price=32767;
        quantity=0;
        time=null;
        Calendar d=new GregorianCalendar();
        for(int i=0; i<history.size(); i++)
        {
            h=history.get(i);
            if(h.time==null || h.time.before(d) || h.quantity==0)
            {
                history.remove(i);
            }
            else
            {
                if(h.price<price)
                {
                    price=h.price;
                    quantity=h.quantity;
                    time=h.time;
                    index=i;
                }                    
            }
        }
        if(price == 32767)
            price=0;
    }
    
    public void getMaximum()
    {
        History h;
        price=-32767;
        quantity=0;
        time=null;
        Calendar d=new GregorianCalendar();
        for(int i=0; i<history.size(); i++)
        {
            h=history.get(i);
            if(h.time==null || h.time.before(d) || h.quantity==0)
            {
                history.remove(i);
            }
            else
            {
                if(h.price>price)
                {
                    price=h.price;
                    quantity=h.quantity;
                    time=h.time;
                    index=i;
                }                    
            }
        }        
        if(price == -32767)
            price=0;
    }
}
