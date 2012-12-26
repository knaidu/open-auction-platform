/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import library.MagicalItemInfo;
import DailyProphet.EventLogger;
import java.io.BufferedReader;
import java.util.Date;
import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import library.EveryoneRef;
import library.ExecutorRemote;
import library.MainRemote;


/**
 *
 * @author Asish
 */
public class Main extends Thread implements MainRemote{
     Date startTime;
     public static MagicalItem[] magicalItems;
     public static final int MAX_COMMODITY = ExecutorRemote.MAX_COMMODITY;
     public static ArrayList<Everyone> everyone;
     public static ArrayList<WizardSeller> wizards;
     public static ArrayList<ApprenticeBuyer> apprentices;
     public static ArrayList<MagicalItemInfo> magicalItemInfo;
     volatile int numberOfUsers=0;
     static Executor ex;
     static Main obj;
     static Registry registry;
     static int timeout=10*60;
     
     MagicalItem getMagicalItem(int magicalItemNumber)
     {
         return magicalItems[magicalItemNumber];
     }
     
     public MagicalItemInfo[] getAllMagicalItems()
     {
         MagicalItemInfo[] m=new MagicalItemInfo[magicalItemInfo.size()];
         for(int i=0; i<magicalItemInfo.size(); i++)
         {
            m[i]=magicalItemInfo.get(i);
         }
         return m;
     }
     
     public static Everyone getUser(int id)
     {
         if(id<everyone.size())
            return everyone.get(id);
         return null;
     }
     
    public static int getAverageSellingPrice(int itemNumber)
    {
            MagicalItem m=magicalItems[itemNumber];
            return m.minimumSellingPrice;
    }

     /**
     * Creates the Diagon Alley Wizards Sellers.
     */    
    int createWizards(String name)
    {
       WizardSeller ws=new WizardSeller(name, numberOfUsers-1);
       Random random = new Random();
       int commodity=random.nextInt(20), quantity, cost;
  
       int apprenticeNo=findApprentice(magicalItems[commodity].magicalItemInfo);
       if(apprenticeNo == -1)
       {
           quantity=random.nextInt(1000)+100;
           cost=random.nextInt(1000)+100;
       }
       else
       {
           ApprenticeBuyer ab=apprentices.get(apprenticeNo);
           cost=ab.getTargetCost()-random.nextInt(10);
           quantity=ab.getTargetQuantity();           
       }
       
       for(int i=0; i<MAX_COMMODITY; i++)
       {
           DiagonAlleyBuyerAccount daba=new DiagonAlleyBuyerAccount(ws);
           magicalItems[i].buyerAccount.add(daba);
           DiagonAlleySellerAccount dasa=new DiagonAlleySellerAccount(ws);
           magicalItems[i].sellerAccount.add(dasa);
       }
       
       for(int i=0; i<MAX_COMMODITY; i++)
       {
           CurrentInventoryList ci;
           if(i==commodity)
               ci=new CurrentInventoryList(cost, quantity, ws.index, magicalItems[i]);
           else
               ci=new CurrentInventoryList(0, 0, ws.index, magicalItems[i]);
           ws.currentInventoryList.add(ci);
       }
       for(int i=0; i<MAX_COMMODITY; i++)
       {
           FutureInventoryList fi=new FutureInventoryList(0, 0, ws.index, magicalItems[i]);
           ws.futureInventoryList.add(fi);
       }
       wizards.add(ws);
       everyone.add(ws);
       EventLogger.debug("Wizard Seller: "+name+" created!");
       return commodity;
    }
    
     /**
     * Finds the wizard selling a particular magical item.
     */    
    int findWizard(MagicalItemInfo m)
    {
        for(int i=0; i<wizards.size(); i++)
        {
            WizardSeller ws=wizards.get(i);
            if(ws.getTargetCommodityInfo() == m)
                return i;
        }
        EventLogger.debug("No previous wizard for the commodity "+m.getName()+" exists!");
        return -1;
    }

    /**
     * Finds the buyerAccount buying a particular magical item.
     */    
    int findApprentice(MagicalItemInfo m)
    {
        for(int i=0; i<apprentices.size(); i++)
        {
            ApprenticeBuyer ab=apprentices.get(i);
            if(ab.getTargetCommodityInfo() == m)
                return i;
        }
        EventLogger.debug("No previous apprentice for the commodity "+m.getName()+" exists!");
        return -1;
    }

     /**
     * Register a user in Diagon Alley.
     */    
    public synchronized EveryoneRef register(String name)
    {
        numberOfUsers++; //Handle Concurrency
        if(numberOfUsers%2 == 0) {
            EventLogger.write(name+" joined the game!\r\n"+name+" is a Wizard (Seller)\r\n");
            int commodity=createWizards(name);
            return new EveryoneRef(true,numberOfUsers-1, commodity);
        }
        else {
            EventLogger.write(name+" joined the game!\r\n"+name+" is a Apprentice (Buyer)\r\n");
            int commodity=createApprentices(name);
            return new EveryoneRef(false,numberOfUsers-1, commodity);
        }        
    }
    
     /**
     * Creates the Diagon Alley Apprentice Buyers.
     */    
    int createApprentices(String name)
    {
       ApprenticeBuyer ab=new ApprenticeBuyer(name, numberOfUsers-1);
       Random random = new Random();
       int commodity=random.nextInt(20), quantity, cost;
       int wizardNo=findWizard(magicalItems[commodity].magicalItemInfo);
       if(wizardNo == -1)
       {
           quantity=random.nextInt(1000)+100;
           cost=random.nextInt(1000)+100;
       }
       else
       {
           WizardSeller ws=wizards.get(wizardNo);
           cost=ws.getTargetCost()+random.nextInt(10);
           quantity=ws.getTargetQuantity();
       }
       
       for(int i=0; i<MAX_COMMODITY; i++)
       {
           DiagonAlleyBuyerAccount daba=new DiagonAlleyBuyerAccount(ab);
           magicalItems[i].buyerAccount.add(daba);
           DiagonAlleySellerAccount dasa=new DiagonAlleySellerAccount(ab);
           magicalItems[i].sellerAccount.add(dasa);
       }

       for(int i=0; i<MAX_COMMODITY; i++)
       {
           CurrentInventoryList ci=new CurrentInventoryList(0, 0, ab.index, magicalItems[i]);
           ab.currentInventoryList.add(ci);
       }
       for(int i=0; i<MAX_COMMODITY; i++)
       {
           FutureInventoryList fi;
           if(i==commodity)
               fi=new FutureInventoryList(cost, quantity, ab.index, magicalItems[i]);
           else
               fi=new FutureInventoryList(0, 0, ab.index, magicalItems[i]);
           ab.futureInventoryList.add(fi);
       }

       apprentices.add(ab);        
       everyone.add(ab);
       EventLogger.debug("Apprentice Buyer: "+name+" created!");
       return commodity;
    }
   
    /**
     * Creates the Diagon Alley magical items.
     */    
    void createMagicalItems()
    {
        magicalItems=new MagicalItem[MAX_COMMODITY];
        magicalItemInfo = new ArrayList();
         try{
            InputStream instream = getClass().getResourceAsStream("DiagonAlleyMagicalItems.csv");
            InputStreamReader infile = new InputStreamReader(instream);
            BufferedReader br = new BufferedReader(infile);
            for(int i=0; i<MAX_COMMODITY; i++)
            {
                magicalItems[i]=new MagicalItem(i);
                if(br.ready())
                {
                    String line=br.readLine();
                    String elem[]=line.split(",");
                    magicalItems[i].magicalItemInfo= new MagicalItemInfo(elem[0], elem[1], elem[2], magicalItems[i].index);
                    magicalItemInfo.add(magicalItems[i].magicalItemInfo);
                }
            }
        }catch(IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }        
        EventLogger.debug("Magical items successfully created");
    }
    
    static void announceWinners()
    {
        ArrayList<Everyone> winners=new ArrayList();
        int maxscore=0;
        for(int k=0; k<everyone.size(); k++)
        {
            Everyone e=everyone.get(k);
            if(maxscore<e.getScore())
            {
                maxscore=e.getScore();
            }
        }
        for(int k=0; k<everyone.size(); k++)
        {
            Everyone e=everyone.get(k);
            if(maxscore==e.getScore())
            {
                winners.add(e);
            }
        }
        DailyProphet.EventLogger.writeln("Winner(s) of the auction game is ");
        for(int k=0; k<winners.size(); k++)
        {
            Everyone e=winners.get(k);
            DailyProphet.EventLogger.writeln(e.name+" and his/her score is "+e.getScore());
        }
    }
    
     /**
     * Creates a virtual wizard.
     */    
    void createVirtualWizards()
    {
     //TODO: Complete this code.  
        EventLogger.debug("Created virtual wizards");
    }

     /**
     * Creates a virtual buyerAccount.
     */    
    void createVirtualApprentices()
    {
     //TODO: Complete this code.   
        EventLogger.debug("Created virtual apprentices");
    }

    /**
     * Creates the Diagon Alley magical world.
     */    
    void createMagicalWorld()
    {
        createMagicalItems();
        createVirtualWizards();
        createVirtualApprentices();
    }
    
    public void run()
    {
        int i;
        int count=0;
        try{
           while(count<timeout)
           {
                Thread.sleep(1000);
                count++;
                if(everyone.size()==0)
                    continue;
                for(i=0; i<everyone.size(); i++)
                {
                    Everyone e=everyone.get(i);
                    if(!e.goalMetOrNot)
                        break;
                }
                if(i==everyone.size())
                {
                    break;
                }
           }
            ex.close();
            announceWinners();
            try{
                Thread.sleep(5000);
                registry.unbind("Main");
                registry.unbind("Executor");
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            System.exit(0);
        }
        catch(InterruptedException ie)
        {

        }
    }
    
    Main()
    {           
        EventLogger.writeln("Welcome to Diagon Alley Open Outcry Auction!");
        createMagicalWorld();
        wizards=new ArrayList();
        apprentices=new ArrayList();
        everyone=new ArrayList();
    }
    
    public static void main(String args[])
    {
        registry=null;
        ex=new Executor();
        ex.start();
        obj = new Main();
        obj.start();
       	try {
	    MainRemote stub1 = (MainRemote) UnicastRemoteObject.exportObject(obj, 0);
            ExecutorRemote stub2 = (ExecutorRemote) UnicastRemoteObject.exportObject(ex, 0);
	    // Bind the remote object's stub1 in the registry
            switch(args.length)
            {
                case 0:
                    System.err.println("No arguments supplied");
              	    registry = LocateRegistry.getRegistry();
                    break;
                case 1:
                    System.err.println("Port: "+args[0]);
                    registry = LocateRegistry.getRegistry(Integer.parseInt(args[0]));
                    break;
                case 3:
                    timeout=Integer.parseInt(args[2]);
                case 2:
                    System.err.println("Hostname:"+args[0]+" Port:"+args[1]);
                    registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
                    break;
            }
	    registry.bind("Main", stub1);
            registry.bind("Executor", stub2);
	    System.err.println("Server ready");
            EventLogger.debug("Server started successfully\r\n");
            System.err.println("<Press ENTER to quit>");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            br.readLine();
        } 
        catch (IOException ioe) {ioe.printStackTrace();}
	catch (Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    e.printStackTrace();
        }
        finally
        {
            ex.close();
            announceWinners();

            try{
                Thread.sleep(5000);
                registry.unbind("Main");
                registry.unbind("Executor");
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            System.exit(0);
        }
//        m.start();
    }
}
