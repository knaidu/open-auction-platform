/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import library.MagicalItemInfo;
import DailyProphet.EventLogger;
import java.util.ArrayList;
import java.io.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Asish
 */
public class MagicalItem{
    int index;
    MagicalItemInfo magicalItemInfo;
    volatile int minimumSellingPrice;
    ArrayList<DiagonAlleySellerAccount> sellerAccount;
    ArrayList<DiagonAlleyBuyerAccount> buyerAccount;
    
    MagicalItem(int index)
    {
        sellerAccount=new ArrayList();
        buyerAccount=new ArrayList();
        this.index=index;
    }
    
     /**
     * Checks if a bid for a magical item meets the expectation of the seller.
     */       
    void executeTrade()
    {
        EventLogger.debug("Trying to execute trades for "+magicalItemInfo.getName() + " Number of buyers: "+ buyerAccount.size()+" Number of sellers: "+sellerAccount.size());
        for(int i=0; i<buyerAccount.size(); i++)
        {
            //Get the buyer account
            DiagonAlleyBuyerAccount daba=buyerAccount.get(i);
            if(daba.time!=null)
            {
                for(int j=0; j<sellerAccount.size(); j++)
                {
                    DiagonAlleySellerAccount dasa=sellerAccount.get(j);
                    if(dasa.time!=null)
                    {
                        if(daba.price>=dasa.price) //Matching criterion
                        {
                            //Execute trade
                            if(daba.quantity<dasa.quantity) //Check if the buyer needs less quantity than seller
                            {
                                EventLogger.debug("Quantity needed by the buyer is less than what the seller is selling!");
                                dasa.quantity-=daba.quantity;  //Reduce the quantity in the sale
                                CurrentInventoryList cil=daba.e.currentInventoryList.get(index);
                                int cost=(daba.price+dasa.price)/2; //Calculate the cost for the sale
                                cil.quantity+=daba.quantity; //Update the quantity bought for the buyer
                                FutureInventoryList fil=daba.e.futureInventoryList.get(index);
                                if(fil.quantity+fil.quantityLocked-daba.quantity>0)
                                {
                                    fil.quantityLocked-=daba.quantity;//Update the goal for the buyer
                                    if(fil.quantityLocked == 0 && fil.quantity == 0)//Set Goal Met
                                    {
                                        daba.e.goalMetOrNot=true;
                                    }
                                }
                                else
                                {
                                    fil.quantity=0;
                                    fil.quantityLocked=0;
                                }
                                if(fil.buyingTargetPrice!=0)
                                    daba.e.score+=(fil.buyingTargetPrice-cost)*daba.quantity;//Update score for buyer
                                cil=dasa.e.currentInventoryList.get(index);
                                cil.quantityLocked-=daba.quantity; //Update the quantity locked for the seller
                                if(cil.quantityLocked==0 && cil.quantity == 0) //Set Goal Met
                                    dasa.e.goalMetOrNot=true;
                                dasa.e.score+=(cost-cil.sellingPriceTarget)*daba.quantity; //Update score for seller
                                EventLogger.debug("Buyer Score: "+daba.e.score+" Seller Score: "+dasa.e.score);
                                EventLogger.writeln("Sold "+daba.quantity+" of "+cil.magicalItem.magicalItemInfo.getName()+" for $"+cost);
                                daba.quantity=0; //Update the bid quantity
                                dasa.modify();
                                daba.modify();
                            }
                            else
                            {
                                EventLogger.debug("Quantity available in the market less than what buyer needs");
                                daba.quantity-=dasa.quantity; //Reduce the bid quantity
                                CurrentInventoryList cil=daba.e.currentInventoryList.get(index);
                                int cost=(daba.price+dasa.price)/2; //Calculate the cost for the sale
                                cil.quantity+=dasa.quantity; //Update the quantity bought for the buyer
                                FutureInventoryList fil=daba.e.futureInventoryList.get(index);
                                if(fil.quantity+fil.quantityLocked-dasa.quantity>0)
                                {
                                    fil.quantityLocked-=dasa.quantity;//Update the goal for the buyer
                                    if(fil.quantityLocked == 0 && fil.quantity == 0 && fil.buyingTargetPrice!=0)//Check if goals are met
                                    {
                                        daba.e.goalMetOrNot=true;
                                    }
                                }
                                else
                                {
                                    fil.quantity=0;
                                    fil.quantityLocked=0;
                                }
                                if(fil.buyingTargetPrice!=0)
                                    daba.e.score+=(fil.buyingTargetPrice-cost)*dasa.quantity;//Update score for buyer
                                cil=dasa.e.currentInventoryList.get(index);
                                cil.quantityLocked-=dasa.quantity; //Update the quantity locked for the seller
                                if(cil.quantityLocked==0 && cil.quantity == 0 && cil.sellingPriceTarget!=0) //Check if goals are met
                                    dasa.e.goalMetOrNot=true;
                                dasa.e.score+=(cost-cil.sellingPriceTarget)*dasa.quantity; //Update score for seller
                                EventLogger.debug("Buyer Score: "+daba.e.score+" Seller Score: "+dasa.e.score);
                                EventLogger.writeln("Sold "+dasa.quantity+" of "+cil.magicalItem.magicalItemInfo.getName()+" for $"+cost);
                                dasa.quantity=0; //Update the sale quantity
                                dasa.modify();
                                daba.modify();
                            }
                        }
                        else
                            EventLogger.debug("Buyer's offer less than Seller's demand! ");
                    }
                    else
                    {
                        EventLogger.debug("No trade placed by "+ dasa.e.name+ " for "+ this.magicalItemInfo.getName());
                    }
                }
            }

        }
        EventLogger.debug("Exiting executeTrade");
    }
}
