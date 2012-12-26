/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DailyProphet;

/**
 *
 * @author Asish
 */
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class EventLogger implements Serializable{	
	@SuppressWarnings("serial")
	class ClosedLogException extends Exception implements Serializable{}
        private final int maxBuffSize=1000; //The maximum size of the buffer, declared as final to prevent extending(child) classes from modifying the Buffer Size 
        private boolean closeFlag=false; //If log is closed then closeFlag=true else closeFlag=false
        private String buffer[] = new String[maxBuffSize]; //buffer that contains messages which needs to be written into Writer 
	private int size=0; //size of the buffer
        private int in=0; //index where new messages are placed 
        private int out=0; //index where messages are read
        private LazyWriter lw=null; //Object of LazyWriter
        private MulticastSocket socket;
        private static EventLogger obj=null;
        //Constructor
	private EventLogger() {            
                try{
                socket=new MulticastSocket();
                }catch(IOException ioe)
                {
                    ioe.printStackTrace();
                }
	}
	
        /*
        * Only called by lazy writer thread.  Blocks if the log has not been closed and there is nothing to write.
        * If the log has been closed and the log is empty, return null, otherwise, remove and return the first item 
        * in the log buffer.
        */
	 synchronized private String get()
	 {
            String msg=null;
            //await((closeFlag = false) v (size > 0))
            while(!(closeFlag == true || size > 0)) 
                try{ wait(); } catch(InterruptedException ie){ie.printStackTrace();}
            if(size>0)
            {
               msg=buffer[out]; //Take mesage out of buffer
               out=(out+1)%maxBuffSize;
               size--;
               notifyAll(); //Wake up all waiting threads

            }
            return msg;
         }
	

        /*  Method called by application program to add a message to the log.  Blocks if necessary to maintain 
        *    the buffer size invariant.  If the log is closed before the method is called, or while it is blocked, a 
        *    LogClosedException is thrown.  This method is also responsible for lazily instantiating a writer thread *    to, at low priority, write the contents of the Log buffer to the writer passed into the constructor.
        */

        synchronized private void add(String msg) throws ClosedLogException
        {
            if(lw==null) //Start the lazy writer, if no lazy writer thread exists. It is started first for better performance.
            {
                 lw=new LazyWriter(); //Create a new instance of lazy writer
                 lw.setPriority(Thread.MIN_PRIORITY); //Set the lowest priority for the lazy writer thread
                 lw.start();          //Start the lazy writer thread          
            }
            //await ((closeFlag=false) v (size<maxBuffSize))
            while(!(closeFlag==true || size<maxBuffSize)) 
                try{ wait(); }catch(InterruptedException ie) {ie.printStackTrace();}
            if(closeFlag) //assert(!closeFlag)
                throw new ClosedLogException();
            buffer[in]=msg; //Put message in buffer
            in=(in+1)%maxBuffSize;
            size++;
            notifyAll(); //Wake up all waiting threads
        }
	
        public static void write (String msg)
        {
            if(obj==null)
            {
                obj=new EventLogger();
            }
            try{
                obj.add(msg);
            }catch (ClosedLogException cle)
            {
                cle.printStackTrace();
            }
        }
        
        public static void writeln (String msg)
        {
            if(obj==null)
            {
                obj=new EventLogger();
            }
            try{
                obj.add(msg+"\n");
            }catch (ClosedLogException cle)
            {
                cle.printStackTrace();
            }
        }
        
        public static void debug (String msg)
        {
/*            if(obj==null)
            {
                obj=new EventLogger();
            }
            try{
                obj.add("DEBUG: "+msg+"\r\n");
            }catch (ClosedLogException cle)
            {
                cle.printStackTrace();
            }*/
        }

        /*  close the Log.  After the log has been closed, no additional message may be added to the log. */
         public synchronized void close()
         {
             closeFlag=true;
             notifyAll(); //Wake up all waiting threads
         }



        /*  Thread that copies data written to Log buffer to the writer passed into the constructor.  
         *  After the Log has been closed and the buffer is empty, the thread should flush
         *  and close the writer and then 
         *  terminate.  
        */
	class LazyWriter extends Thread implements Serializable{   
		public void run() 
                {
                    String msg;
                    byte[] buf = new byte[256];
                    try{
                        do{
                            msg=get(); //Get the next message from the buffer
                            if(msg != null)   
                            {   
                                buf=msg.getBytes();
                                // send it
                                InetAddress group = InetAddress.getByName("230.0.0.1");
                                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
                                socket.send(packet);
                            }
                        }while(msg != null); //Get messages till msg == null
                    }catch(IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
                }
        }		
}

