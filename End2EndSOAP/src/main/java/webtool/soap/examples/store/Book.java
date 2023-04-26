/*
 * $RCSfile: Book.java,v $
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
 * $Date: 2003/12/22 19:59:50 $
 * $Log: Book.java,v $
 * Revision 1.3  2003/12/22 19:59:50  rjaamour
 * use java.util.Date to fix addNewItem() bug.
 *
 * Revision 1.2  2003/12/02 23:11:43  rjaamour
 * Added get methods.
 *
 * Revision 1.1  2003/12/01 16:57:37  rjaamour
 * Original source code.
 *
 */

package webtool.soap.examples.store;

import java.util.Date;
import java.sql.SQLException;

public class Book extends Item {
    protected String isbn;
    protected Date publication_date;
    protected String description;
    protected String[] authors;
    protected String publisher;
    
    public Book() {
        // for serialization only
    }
	protected Book(int id, String isbn, String title, Date year, String[] authors,
                   String publisher, String description,
                   float price, int stock) throws ItemNotFoundException {
		super(id, title, price, stock);
        this.isbn = isbn;
        this.publication_date = year;
        this.authors = authors;
        this.publisher = publisher;
        this.description = description;
	}

    public static Book getByISBN(String isbn) throws SQLException,
                                                     InstantiationException,
                                                     IllegalAccessException,
                                                     ClassNotFoundException,
                                                     ItemNotFoundException {
        return BookStoreDB.getByISBN(isbn);
    }
    
    public String getISBN() {
        return isbn;
    }
    public Date getPublicationDate() {
        return publication_date;
    }
    public String getDescription() {
        return description;
    }
    public String[] getAuthors() {
        return authors;
    }
    public String getPublisher() {
        return publisher;
    }
}
