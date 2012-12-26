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
public class WizardSeller extends Everyone{
    public WizardSeller(String name, int index)
    {
        wizardOrNot=true;
        this.name=name;
        this.index=index;
    }
    
         /**
     * Gets the target quantity for the magical item to sell.
     */    
    public int getTargetQuantity()
    {        
        for(int i=0; i<currentInventoryList.size(); i++)
        {
            CurrentInventoryList cil=currentInventoryList.get(i);
            if (cil.sellingPriceTarget != 0)
            {
                return cil.quantity+cil.quantityLocked;
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
     * Gets the target cost for the magical item to sell.
     */    
    public int getTargetCost()
    {
        for(int i=0; i<currentInventoryList.size(); i++)
        {
            CurrentInventoryList cil=currentInventoryList.get(i);
            if (cil.sellingPriceTarget != 0)
            {
                return cil.sellingPriceTarget;
            }
        }
        return 0;        
    }

    /**
     * Gets the target magical item to sell for the wizard.
     */    
    public MagicalItemInfo getTargetCommodityInfo()
    {
        for(int i=0; i<currentInventoryList.size(); i++)
        {
            CurrentInventoryList cil=currentInventoryList.get(i);
            if (cil.sellingPriceTarget != 0)
            {
                return cil.magicalItem.magicalItemInfo;
            }
        }
        return null;        
    }

    /**
     * Gets the current price for the magical item present on CIL
     */
    int getCurrentPrice(int magicalItemNumber)
    {
        CurrentInventoryList cil = this.currentInventoryList.get(magicalItemNumber);
        return cil.diagonAlleySellerAccount.price;
    }

    /**
     * Gets the current price for the magical item present on CIL
     */
    int getCurrentQuantity(int magicalItemNumber)
    {
        CurrentInventoryList cil = this.currentInventoryList.get(magicalItemNumber);
        return cil.diagonAlleySellerAccount.quantity;
    }
}
