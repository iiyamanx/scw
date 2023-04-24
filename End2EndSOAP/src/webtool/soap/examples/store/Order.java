/*
 * $RCSfile: Order.java,v $
 * $Revision: 1.1 $
 *
 * Comments:
 *
 * (C) Copyright ParaSoft Corporation 1996.  All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ParaSoft
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * $Author: rjaamour $          $Locker:  $
 * $Date: 2003/12/01 16:58:17 $
 * $Log: Order.java,v $
 * Revision 1.1  2003/12/01 16:58:17  rjaamour
 * Original source code.
 *
 */

package webtool.soap.examples.store;

public class Order {
    private Book item;
    private int quantity;
    private int order_number;
	private int cart_id; //masashi

    public Order() {
        this(null, 0, 0);
    }
    public Order(Book item, int count, int orderNumber) {
        this.item = item;
        this.quantity = count;
        order_number = orderNumber;
    }
    public Item getItem() {
        return item;
    }
    public int getQuantity() {
        return quantity;
    }
    public int getOrderNumber() {
        return order_number;
    }
    public void modifyCount(int amount) {
        quantity += amount;
    }
    public int getCartId() { //masashi
        return cart_id;
    }
    public void setCartId(int cart_id) { //masashi
        this.cart_id = cart_id;
    }
}
