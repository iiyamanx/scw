/*
 * $RCSfile: Cart.java,v $
 * $Revision: 1.7 $
 *
 * Comments:
 *
 * (C) Copyright ParaSoft Corporation 1996.  All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ParaSoft
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * $Author: rjaamour $          $Locker:  $
 * $Date: 2004/05/24 21:57:44 $
 * $Log: Cart.java,v $
 * Revision 1.7  2004/05/24 21:57:44  rjaamour
 * Better new book removal based on session expiration of the user who added them. Also added provisions to prevent many books from being added.
 *
 * Revision 1.6  2004/05/21 01:13:14  rjaamour
 * make invocationCounter not static and clear added books on valueUnbound()
 *
 * Revision 1.5  2004/02/25 17:22:27  rjaamour
 * Connect and disconnect from the DB in valueBound() and valueUnbound()
 *
 * Revision 1.4  2004/01/28 18:36:40  jeehongm
 * Removed ^Ms.
 *
 * Revision 1.3  2003/12/10 00:18:53  rjaamour
 * Added a sleep to confirm().
 *
 * Revision 1.2  2003/12/01 23:26:15  rjaamour
 * Fixed price inflation bug and modified and fixed a user message spacing.
 *
 * Revision 1.1  2003/12/01 16:57:51  rjaamour
 * Original source code.
 *
 */

package webtool.soap.examples.store;

import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.*;

public class Cart implements ICart, HttpSessionBindingListener {
    private Vector pendingOrders = new Vector();
    private Vector confirmedOrders = new Vector();
    private static int lastOrderNumber = 0;
    private int invocationCounter = 0;
    private TreeSet addedBookIds = new TreeSet(); // TreeSet<Integer>
    private int cart_id = 0;

    public Order placeOrder(int itemId, int count) throws Exception {
    	Iterator itr = pendingOrders.iterator();
        while (itr.hasNext()) {
        	int i=0;
            Order o = (Order)itr.next();
            if (o.getItem().getId() == itemId) {
                o.modifyCount(count);
                return o;
            } else {
            	if(i == 0){
            		cart_id = o.getCartId();
            	}
            }
            i++;
        }
        Order newOrder = new Order(BookStoreDB.getById(itemId), count, ++lastOrderNumber);
        pendingOrders.add(newOrder);
        if(cart_id == 0){
        	newOrder.setCartId((int) (Math.random()*100000));
        } else {
        	newOrder.setCartId(cart_id);
        }
        return newOrder;
    }
    public String removeOrder(int orderNumber) {
        if (pendingOrders.size() == 0) {
            return "did not remove order #" + orderNumber +
                   ", no orders were submitted.";
        }
        Iterator itr = pendingOrders.iterator();
        while (itr.hasNext()) {
            Order order = (Order)itr.next();
            if (order.getOrderNumber() == orderNumber) {
                String name = order.getItem().getName();
                int quantity = order.getQuantity();
                itr.remove();
                return "Order #" + orderNumber + " " + quantity + " \"" + name +
                       "\" was removed successfully";
            }
        }
        return "your cart does not contain the order #" + orderNumber;
    }
    public Book[] getItemByTitle(String title) throws Exception {
        ++invocationCounter;
        Book[] books = BookStoreDB.getByTitleLike(title);
        if (books.length > 0) {
            // have the service do wierd stuff
            books[0].inflatePrice(invocationCounter/5);
        }
        return books;
    }
    public Book getItemById(int id) throws Exception {
        return BookStoreDB.getById(id);
    }
    public Order[] getPendingOrders() {
        return getArray(pendingOrders);
    }
    
    /**
     * @author masashi
     */
    public Order[] getPendingOrder(int cart_id) {
        return getArray(cart_id, pendingOrders);
    }
    
    public synchronized Book addNewItem(Book book) throws Exception {
        Book existing = null;
        try {
            existing = getItemById(book.getId());
        } catch (Exception e) {
            addedBookIds.add(new Integer(book.getId()));
            BookStoreDB.addNewItem(book);
        }
        if (existing != null) {
            throw new Exception("An item with ID=" + book.getId() +
                                " already exists and it has the title: " +
                                existing.getName());
        }
        return book;
    }
    public Order[] getConfirmedOrders() {
        return getArray(confirmedOrders);
    }
    public boolean confirm() {
        if (pendingOrders.size() > 0) {
            confirmedOrders.addAll(pendingOrders);
            pendingOrders.clear();
            cart_id = 0;
            return true;
        }
        return false;
    }
    private static Order[] getArray(Vector ordersVec) {
        Object[] objects = ordersVec.toArray();
        Order[] orders = new Order[objects.length];
        for (int i = 0; i < objects.length; ++i) {
            orders[i] = (Order)objects[i];
        }
        return orders;
    }
    
    /**
     * @author masashi
     */
    private static Order[] getArray(int cart_id, Vector ordersVec) {
        Object[] objects = ordersVec.toArray();
        Order[] orders = new Order[objects.length];
        for (int i = 0; i < objects.length; ++i) {
        	Order order = (Order) ordersVec.get(i);
        	if(order.getCartId() == cart_id){
        		orders[i] = (Order)objects[i];
        	}
        }
        return orders;
    }
    
    public void valueBound(HttpSessionBindingEvent event) {
        try {
			BookStoreDB.getDBInstance().connect();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    public void valueUnbound(HttpSessionBindingEvent event) {
        try {
			BookStoreDB.getDBInstance().close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
            // clear the books added by this user/session when the session
            // expires
            BookStoreDB.clearAddedBooks(addedBookIds);
        }
    }
}

// END OF FILE
