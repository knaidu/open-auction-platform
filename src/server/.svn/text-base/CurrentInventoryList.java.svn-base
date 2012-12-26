/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

/**
 *
 * @author Asish
 */
public class CurrentInventoryList extends Inventory{
    int sellingPriceTarget;
    int cost;
    DiagonAlleySellerAccount diagonAlleySellerAccount;
    CurrentInventoryList(int cost, int quantity, int index, MagicalItem magicalItem)
    {
        this.sellingPriceTarget=cost;
        this.quantity=quantity;
        this.magicalItem=magicalItem;
        diagonAlleySellerAccount=magicalItem.sellerAccount.get(index);
    }
}
