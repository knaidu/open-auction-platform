/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

/**
 *
 * @author Asish
 */
public class FutureInventoryList extends Inventory{
    int buyingTargetPrice;
    DiagonAlleyBuyerAccount diagonAlleyBuyerAccount;
    FutureInventoryList(int cost, int quantity, int index, MagicalItem magicalItem)
    {
        this.buyingTargetPrice=cost;
        this.quantity=quantity;
        this.magicalItem=magicalItem;
        diagonAlleyBuyerAccount=magicalItem.buyerAccount.get(index);
    }
}
