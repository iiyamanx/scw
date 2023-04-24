/*
 * $RCSfile: Item.java,v $
 * $Revision: 1.3 $
 *
 * Comments:
 *
 * (C) Copyright ParaSoft Corporation 1996.  All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ParaSoft
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * $Author: rjaamour $          $Locker:  $
 * $Date: 2003/12/03 02:35:24 $
 * $Log: Item.java,v $
 * Revision 1.3  2003/12/03 02:35:24  rjaamour
 * Added getPrice().
 *
 * Revision 1.2  2003/12/01 23:26:58  rjaamour
 * inflatePrice() now takes an amount.
 *
 * Revision 1.1  2003/12/01 16:58:01  rjaamour
 * Original source code.
 *
 */

package webtool.soap.examples.store;

public class Item {
    protected int id;
    protected String title;
    protected int quantity_in_stock;
    protected float price;

    public Item() {
        // for serialization only
    }
    public Item(int id, String name, float price, int quantity)
        throws ItemNotFoundException {
        this.id = id;
        this.title = name;
        this.price = price;
        quantity_in_stock = quantity;
    }
    public String getName() {
        return title;
    }
    public int getId() {
        return id;
    }
    public int getStockQuantity() {
        return quantity_in_stock;
    }
    public float getPrice() {
        return price;
    }
    public void inflatePrice(int amount) {
        price += amount; 
    }
}
