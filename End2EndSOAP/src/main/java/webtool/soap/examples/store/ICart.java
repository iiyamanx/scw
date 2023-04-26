/*
 * $RCSfile: ICart.java,v $
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
 * $Date: 2004/05/24 21:57:45 $
 * $Log: ICart.java,v $
 * Revision 1.3  2004/05/24 21:57:45  rjaamour
 * Better new book removal based on session expiration of the user who added them. Also added provisions to prevent many books from being added.
 *
 * Revision 1.2  2004/01/28 18:37:48  jeehongm
 * Removed ^Ms.
 *
 * Revision 1.1  2003/12/01 16:57:56  rjaamour
 * Original source code.
 *
 */

package webtool.soap.examples.store;

public interface ICart {
    Order placeOrder(int itemId, int quantity) throws Exception;
    String removeOrder(int orderNumber);
    Book[] getItemByTitle(String titleKeyword) throws Exception;
    Book getItemById(int id) throws Exception;
    // Order[] getConfirmedOrders();
    Book addNewItem(Book book) throws Exception;
    boolean confirm();
    Order[] getPendingOrders();
    Order[] getPendingOrder(int cart_id);
}

// END OF FILE