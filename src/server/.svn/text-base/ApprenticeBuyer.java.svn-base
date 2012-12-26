/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import library.MagicalItemInfo;

/**
 *
 * @author Asish
 */
public class ApprenticeBuyer extends Everyone{
 
    ApprenticeBuyer(String name, int index)
    {
        wizardOrNot = false;
        this.name=name;
        this.index=index;
    }
    
            /**
     * Gets the target quantity to buy for the buyerAccount.
     */    
    public int getTargetQuantity()
    {        
        for(int i=0; i<futureInventoryList.size(); i++)
        {
            FutureInventoryList fil=futureInventoryList.get(i);
            if (fil.buyingTargetPrice != 0)
            {
                return fil.quantity;
            }
        }
        return 0;
    }
    
         /**
     * Gets the target quantity locked to buy for the buyerAccount.
     */    
    public int getTargetQuantityLocked()
    {        
        for(int i=0; i<futureInventoryList.size(); i++)
        {
            FutureInventoryList fil=futureInventoryList.get(i);
            if (fil.buyingTargetPrice != 0)
            {
                return fil.quantityLocked;
            }
        }
        return 0;
    }
    
        /**
     * Gets the target cost for the magical item to buy for the buyerAccount.
     */    
    public int getTargetCost()
    {
        for(int i=0; i<futureInventoryList.size(); i++)
        {
            FutureInventoryList fil=futureInventoryList.get(i);
            if (fil.buyingTargetPrice != 0)
            {
                return fil.buyingTargetPrice;
            }
        }
        return 0;        
    }
    
     /**
     * Gets the target magical item to buy for the buyerAccount.
     */    
    public MagicalItemInfo getTargetCommodityInfo()
    {
        for(int i=0; i<futureInventoryList.size(); i++)
        {
            FutureInventoryList fil=futureInventoryList.get(i);
            if (fil.buyingTargetPrice != 0)
            {
                return fil.magicalItem.magicalItemInfo;
            }
        }
        return null;        
    }
}
