/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import DailyProphet.EventLogger;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import library.BidTradeArgs;
import library.ExecutorRemote;
import library.History;
import library.UserStats;


/**
 * @author Asish
 */
public class Executor extends Thread implements ExecutorRemote{
    class Request implements Serializable
    {
        int userID;
        int op;
        Object args;
        volatile Object result;
        Request(int userID, int op, Object args)
        {
            this.userID=userID;
            this.op=op;
            this.args=args;
            result = null;
        }
    }
    private final int maxRequestSize=1000;
    private volatile boolean closeFlag=false; 
    private Request buffer[][];
    private int size[];
    private int in[];
    private int out[];
    private Lock lock[];
    Condition condition[][]; 
    
    public Executor()
    {
        buffer=new Request[MAX_COMMODITY][maxRequestSize];
        size=new int[MAX_COMMODITY];
        in=new int[MAX_COMMODITY];
        out=new int[MAX_COMMODITY];
        lock=new ReentrantLock[MAX_COMMODITY];
        condition=new Condition[MAX_COMMODITY][4];
        for(int i=0; i<MAX_COMMODITY; i++)
        {
            lock[i]=new ReentrantLock(true);
            condition[i][0]=lock[i].newCondition();
            condition[i][1]=lock[i].newCondition();
            condition[i][2]=lock[i].newCondition();
        }
    }
    
    public Object invoke(int userID, int itemNumber, int op, Object obj) throws Exception
    {
        Request r=new Request(userID, op, obj);
        lock[itemNumber].lock();
        try{
        while(closeFlag==false && size[itemNumber]>=maxRequestSize)
            try{ condition[itemNumber][0].await(); }catch(InterruptedException ie) {ie.printStackTrace();}
        if(closeFlag) //assert(!closeFlag)
            throw new Exception();
        buffer[itemNumber][in[itemNumber]]=r;
        in[itemNumber]=(in[itemNumber]+1)%maxRequestSize;
        size[itemNumber]++;
        condition[itemNumber][2].signal(); //Wake up the executor thread
        while(r.result == null && closeFlag == false)
            try{ condition[itemNumber][1].await(); }catch(InterruptedException ie) {ie.printStackTrace();}
        if(closeFlag) //assert(!closeFlag)
            throw new Exception();
        }finally{
            lock[itemNumber].unlock();
        }
        return r.result;
    }
    
    private boolean execute(int itemNumber)
    {
        Request r=null;
        //await((closeFlag = false) v (size > 0))
        lock[itemNumber].lock();
        try{
            while(closeFlag == false && size[itemNumber] <= 0) 
                try{ condition[itemNumber][2].await(); } catch(InterruptedException ie){ie.printStackTrace();}
            if(size[itemNumber]>0)
            {
               r=buffer[itemNumber][out[itemNumber]]; //Take request out of buffer
               out[itemNumber]=(out[itemNumber]+1)%maxRequestSize;
               size[itemNumber]--;
               condition[itemNumber][0].signal();
            }
            else
            {
                return false;
            }
            BidTradeArgs bidtrade;
            Everyone e;
            switch(r.op)
            {
                case 0: //Trade
                    EventLogger.debug("Executor: Operation = TRADE!");
                    bidtrade=(BidTradeArgs)r.args;
                    e=Main.getUser(r.userID);
                    boolean res=e.trade(bidtrade.getPrice(), bidtrade.getQuantity(), itemNumber, bidtrade.getTime());
                    Boolean result=new Boolean(res);
                    r.result=result;
                    break;
                case 1: //Modify Trade
                    EventLogger.debug("Executor: Operation = MODIFY TRADE!");
                    bidtrade=(BidTradeArgs)r.args;
                    e=Main.getUser(r.userID);
                    res=e.modifyTrade(bidtrade.getID(), bidtrade.getPrice(), bidtrade.getQuantity(), itemNumber, bidtrade.getTime());
                    result=new Boolean(res);
                    r.result=result;
                    break;
                case 2: //Get All Info
                    EventLogger.debug("Executor: Operation = GET ALL INFO!");
                    e=Main.getUser(r.userID);
                    if(e.isWizard())
                    {
                        WizardSeller ws=(WizardSeller)e;
                        UserStats us=new UserStats(e.getScore(), ws.getTargetQuantity(), ws.getTargetCost(), ws.getTargetQuantityLocked(), ws.getTargetCommodityInfo());
                        r.result=us;
                    }
                    else
                    {
                        ApprenticeBuyer ab=(ApprenticeBuyer)e;
                        UserStats us=new UserStats(e.getScore(), ab.getTargetQuantity(), ab.getTargetCost(), ab.getTargetQuantityLocked(), ab.getTargetCommodityInfo());
                        r.result=us;
                    }
                    break;
                case 3: //Bid
                    EventLogger.debug("Executor: Operation = BID!");
                    bidtrade=(BidTradeArgs)r.args;
                    e=Main.getUser(r.userID);
                    res=e.bid(bidtrade.getPrice(), bidtrade.getQuantity(), itemNumber, bidtrade.getTime());
                    result=new Boolean(res);
                    r.result=result;
                    break;
                case 4: //Modify Bid
                    EventLogger.debug("Executor: Operation = MODIFY BID!");
                    bidtrade=(BidTradeArgs)r.args;
                    e=Main.getUser(r.userID);
                    res=e.modifyBid(bidtrade.getID(), bidtrade.getPrice(), bidtrade.getQuantity(), itemNumber, bidtrade.getTime());
                    result=new Boolean(res);
                    r.result=result;
                    break;
                case 5:
                    EventLogger.debug("Executor: Operation = GET COMMODITY QUANTITY!");
                    e=Main.getUser(r.userID);                    
                    Integer resInt=new Integer(e.getQuantity(itemNumber));
                    r.result=resInt;
                    break;
                case 6:
                    EventLogger.debug("Executor: Operation = GET AVERAGE SELLING PRICE");
                    resInt=new Integer(Main.getAverageSellingPrice(itemNumber));                    
                    r.result=resInt;
                    break;
                case 7:
                    EventLogger.debug("Executor: Operation = GET BID HISTORY");
                    e=Main.getUser(r.userID);
                    ArrayList<History> h=e.getBidHistory(itemNumber);
                    r.result=h;
                    break;
                case 8:
                    EventLogger.debug("Executor: Operation = GET TRADE HISTORY");
                    e=Main.getUser(r.userID);
                    h=e.getTradeHistory(itemNumber);
                    r.result=h;
                    break;
            }
            condition[itemNumber][1].signal();
        }finally{
            lock[itemNumber].unlock();
        }
        return true;
    }
    
    public void close()
    {        
         closeFlag=true;
         for (int i=0; i<MAX_COMMODITY; i++)
         {
             lock[i].lock();
             condition[i][0].signalAll();
             condition[i][1].signalAll();
             condition[i][2].signalAll();
             lock[i].unlock();
         }
    }
    
    class LazyExecutor extends Thread implements Serializable
    {
        int itemNumber;
        LazyExecutor(int itemNumber)
        {
            this.itemNumber=itemNumber;
        }
        
        public void run()
        {
            boolean cont;
            do{
                cont=execute(itemNumber); //Execute the next request
            }while(cont); 
        }               
    }
    
    public void run()
    {
        for(int i=0; i<MAX_COMMODITY; i++)
        {
            LazyExecutor le=new LazyExecutor(i);
            le.start();
        }
    }
}
