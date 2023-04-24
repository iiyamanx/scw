/*
 * $RCSfile: BookStoreDB.java,v $
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
 * $Date: 2007/03/16 02:02:24 $
 * $Log: BookStoreDB.java,v $
 * Revision 1.7  2007/03/16 02:02:24  rjaamour
 * Jtest coding standards
 *
 * Revision 1.6  2005/10/19 21:25:30  gford
 * enum -> enumr for Java 1.5
 *
 * Revision 1.5  2005/09/17 00:49:38  jhendrick
 * Jtest fixes.
 *
 * Revision 1.4  2004/05/24 21:57:42  rjaamour
 * Better new book removal based on session expiration of the user who added them. Also added provisions to prevent many books from being added.
 *
 * Revision 1.3  2004/05/21 01:12:29  rjaamour
 * Use hash table and fixed bug in returning added books by id
 *
 * Revision 1.2  2004/02/11 00:50:42  rjaamour
 * Connect db if isClosed() in getDBInstance()
 *
 * Revision 1.1  2003/12/01 16:57:45  rjaamour
 * Original source code.
 *
 */

package webtool.soap.examples.store;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class BookStoreDB extends DB {
    private static final int MAX_BOOKS_TO_ADD = 1000;
    private static final String NL_TABLE_BOOK      = "book";
    private static final String NL_TABLE_AUTHOR    = "author";
    private static final String NL_TABLE_PUBLISHER = "publisher";
    // column names
    private static final String NL_ID          = "id";
    private static final String NL_ISBN        = "isbn";
    private static final String NL_TITLE       = "title";
    private static final String NL_YEAR        = "year";
    private static final String NL_AUTHOR      = "author";
    private static final String NL_NAME        = "name";
    private static final String NL_PUBLISHER   = "publisher";
    private static final String NL_DESCRIPTION = "description";
    private static final String NL_PRICE       = "price";
    private static final String NL_STOCK       = "stock";
    
    private static final String NL_SELECT = "SELECT ";
    private static final String NL_SELECT_DISTINCT = "SELECT DISTINCT ";
    private static final String NL_FROM = " FROM ";
    private static final String NL_WHERE = " WHERE ";
    private static final String NL_LIKE_AND = " LIKE ? AND ";
    private static final String NL_AND = " AND ";
    private static final String NL_PUBLISHER_ID_EQUALS = ".publisher_id = ";
    private static final String NL_EQUALS_QUESTION_AND = " = ? AND ";
    
    private static BookStoreDB db = null;
    // vertual database for books added by clients
    private static Hashtable addedBooks; // Hashtable<Integer (book id), Book>

    private BookStoreDB() throws SQLException, InstantiationException,
                                 IllegalAccessException, ClassNotFoundException {
        super();
    }
    public static BookStoreDB getDBInstance() throws SQLException,
                                                     InstantiationException,
                                                     IllegalAccessException,
                                                     ClassNotFoundException  {
        if (db == null) {
            db = new BookStoreDB();
        } else {
            if (db.isClosed()) {
                db.connect();
            }
        }
        return db; 
    }
    /**
     * @param titlePart a keyword in the title of the book
     * @return Vector <Book>
     */
    public static Book[] getByTitleLike(String titlePart) throws SQLException,
                                                         InstantiationException,
                                                         IllegalAccessException,
                                                         ClassNotFoundException,
                                                         ItemNotFoundException {
        BookStoreDB db = getDBInstance();
        String query = NL_SELECT_DISTINCT + NL_TABLE_BOOK + "." + NL_ID + "," +
                                   NL_TABLE_BOOK + "." + NL_ISBN + "," +
                                   NL_TABLE_BOOK + "." + NL_TITLE + "," +
                                   NL_TABLE_BOOK + "." + NL_YEAR + "," +
                                   NL_TABLE_PUBLISHER + "." + NL_NAME + "," +
                                   NL_TABLE_BOOK + "." + NL_DESCRIPTION + "," +
                                   NL_TABLE_BOOK + "." + NL_PRICE + "," +
                                   NL_TABLE_BOOK + "." + NL_STOCK +
                       NL_FROM + NL_TABLE_BOOK + "," +
                                          NL_TABLE_AUTHOR + "," +
                                          NL_TABLE_PUBLISHER +
                       NL_WHERE + NL_TABLE_BOOK + "." + NL_TITLE + NL_LIKE_AND +
                                   NL_TABLE_BOOK + "." + NL_ISBN + " = " +
                                   NL_TABLE_AUTHOR + "." + NL_ISBN + NL_AND +
                                   NL_TABLE_BOOK + NL_PUBLISHER_ID_EQUALS +
                                   NL_TABLE_PUBLISHER + "." + NL_ID;
        PreparedStatement stmt = db.prepareStatement(query);
        stmt.setString(1, "%" + titlePart + "%");
        ResultSet rs = stmt.executeQuery();
        try {
            boolean more = rs.first();
            Vector books = new Vector();
            while (more) {
                int id = rs.getInt(NL_ID);
                String isbn = rs.getString(NL_ISBN);
                String title = rs.getString(NL_TITLE);
                Date year = rs.getDate(NL_YEAR);
                String publisher = rs.getString(NL_PUBLISHER + "." + NL_NAME);
                String description = rs.getString(NL_DESCRIPTION);
                float price = rs.getFloat(NL_PRICE);
                int stock = rs.getInt(NL_STOCK);
                String query2 = NL_SELECT + NL_TABLE_AUTHOR + "." + NL_NAME +
                NL_FROM + NL_TABLE_BOOK + "," +
                NL_TABLE_AUTHOR + "," +
                NL_TABLE_PUBLISHER +
                NL_WHERE + NL_TABLE_BOOK + "." + NL_TITLE + NL_LIKE_AND +
                NL_TABLE_BOOK + "." + NL_ISBN + " = " +
                NL_TABLE_AUTHOR + "." + NL_ISBN + NL_AND +
                NL_TABLE_BOOK + NL_PUBLISHER_ID_EQUALS +
                NL_TABLE_PUBLISHER + "." + NL_ID + NL_AND +
                NL_TABLE_AUTHOR + "." + NL_ISBN + " = ?";
                PreparedStatement stmt2 = db.prepareStatement(query2);
                stmt2.setString(1, "%" + titlePart + "%");
                stmt2.setString(2, isbn);
                ResultSet rs2 = stmt2.executeQuery();
                try {
                    boolean more2 = rs2.first();
                    Vector authors = new Vector();
                    while (more2) {
                        String author = rs2.getString(NL_TABLE_AUTHOR + "." + NL_NAME);
                        authors.add(author);
                        more2 = rs2.next();
                    }
                    String arr[] = new String[authors.size()];
                    for (int i = 0; i < arr.length; ++i) {
                        arr[i] = (String)authors.elementAt(i);
                    }
                    Book book = new Book(id, isbn, title, year, arr, publisher,
                            description, price, stock);
                    books.add(book);
                    more = rs.next();
                } finally {
                    rs2.close();
                    stmt2.close();
                }
            }
            if (addedBooks != null) {
                Enumeration enumr = addedBooks.elements();
                while (enumr.hasMoreElements()) {
                    Book b = (Book)enumr.nextElement();
                    if (b.getName() != null && b.getName().indexOf(titlePart) != -1) {
                        books.add(b);
                    }
                }
            }
            Book arr[] = new Book[books.size()];
            for (int i = 0; i < arr.length; ++i) {
                arr[i] = (Book)books.elementAt(i);
            }
            if (arr.length == 0) {
                throw new ItemNotFoundException("no books with titles containing '" +
                        titlePart + "' were found");
            }
            return arr;
        } finally {
            rs.close();
            stmt.close();
        }
    }
    public static Book getById(int id) throws SQLException,
                                                     InstantiationException,
                                                     IllegalAccessException,
                                                     ClassNotFoundException,
                                                     ItemNotFoundException {
        BookStoreDB db = getDBInstance();
        String query = NL_SELECT + NL_TABLE_BOOK + "." + NL_ID + "," +
                                   NL_TABLE_BOOK + "." + NL_ISBN + "," +
                                   NL_TABLE_BOOK + "." + NL_TITLE + "," +
                                   NL_TABLE_BOOK + "." + NL_YEAR + "," +
                                   NL_TABLE_PUBLISHER + "." + NL_NAME + "," +
                                   NL_TABLE_BOOK + "." + NL_DESCRIPTION + "," +
                                   NL_TABLE_BOOK + "." + NL_PRICE + "," +
                                   NL_TABLE_BOOK + "." + NL_STOCK +
                       NL_FROM + NL_TABLE_BOOK + "," +
                                          NL_TABLE_AUTHOR + "," +
                                          NL_TABLE_PUBLISHER +
                       NL_WHERE + NL_TABLE_BOOK + "." + NL_ID + NL_EQUALS_QUESTION_AND +
                                   NL_TABLE_BOOK + "." + NL_ISBN + " = " +
                                   NL_TABLE_AUTHOR + "." + NL_ISBN + NL_AND +
                                   NL_TABLE_BOOK + NL_PUBLISHER_ID_EQUALS +
                                   NL_TABLE_PUBLISHER + "." + NL_ID;
        PreparedStatement stmt = db.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        try {
            boolean exists = rs.first();
            if (!exists) {
                if (addedBooks != null) {
                    Enumeration enumr = addedBooks.elements();
                    while (enumr.hasMoreElements()) {
                        Book b = (Book)enumr.nextElement();
                        if (b.getId() == id) {
                            return b;
                        }
                    }
                }
                throw new ItemNotFoundException("no book with the id " + id +
                " was found");
            }
            String isbn = rs.getString(NL_ISBN);
            String title = rs.getString(NL_TITLE);
            Date year = rs.getDate(NL_YEAR);
            String publisher = rs.getString(NL_PUBLISHER + "." + NL_NAME);
            String description = rs.getString(NL_DESCRIPTION);
            float price = rs.getFloat(NL_PRICE);
            int stock = rs.getInt(NL_STOCK);
            String query2 = NL_SELECT + NL_TABLE_AUTHOR + "." + NL_NAME +
            NL_FROM + NL_TABLE_BOOK + "," +
            NL_TABLE_AUTHOR + "," +
            NL_TABLE_PUBLISHER +
            NL_WHERE + NL_TABLE_BOOK + "." + NL_ID + NL_EQUALS_QUESTION_AND +
            NL_TABLE_BOOK + "." + NL_ISBN + " = " +
            NL_TABLE_AUTHOR + "." + NL_ISBN + NL_AND +
            NL_TABLE_BOOK + NL_PUBLISHER_ID_EQUALS +
            NL_TABLE_PUBLISHER + "." + NL_ID;
            PreparedStatement stmt2 = db.prepareStatement(query2);
            stmt2.setInt(1, id);
            ResultSet rs2 = stmt2.executeQuery();
            try {
                boolean more2 = rs2.first();
                Vector authors = new Vector();
                while (more2) {
                    String author = rs2.getString(NL_TABLE_AUTHOR + "." + NL_NAME);
                    authors.add(author);
                    more2 = rs2.next();
                }
                String arr[] = new String[authors.size()];
                for (int i = 0; i < arr.length; ++i) {
                    arr[i] = (String)authors.elementAt(i);
                }
                return new Book(id, isbn, title, year, arr, publisher,
                        description, price, stock);
            } finally {
                rs2.close();
                stmt2.close();
            }
        } finally {
            rs.close();
            stmt.close();
        }
    }
    public static Book getByISBN(String isbn) throws SQLException,
                                                     InstantiationException,
                                                     IllegalAccessException,
                                                     ClassNotFoundException,
                                                     ItemNotFoundException {
        BookStoreDB db = getDBInstance();
        String query = NL_SELECT + NL_TABLE_BOOK + "." + NL_ID + "," +
                                   NL_TABLE_BOOK + "." + NL_ISBN + "," +
                                   NL_TABLE_BOOK + "." + NL_TITLE + "," +
                                   NL_TABLE_BOOK + "." + NL_YEAR + "," +
                                   NL_TABLE_PUBLISHER + "." + NL_NAME + "," +
                                   NL_TABLE_BOOK + "." + NL_DESCRIPTION + "," +
                                   NL_TABLE_BOOK + "." + NL_PRICE + "," +
                                   NL_TABLE_BOOK + "." + NL_STOCK +
                       NL_FROM + NL_TABLE_BOOK + "," +
                                          NL_TABLE_AUTHOR + "," +
                                          NL_TABLE_PUBLISHER +
                       NL_WHERE + NL_TABLE_BOOK + "." + NL_ISBN + NL_EQUALS_QUESTION_AND +
                                   NL_TABLE_BOOK + "." + NL_ISBN + " = " +
                                   NL_TABLE_AUTHOR + "." + NL_ISBN + NL_AND +
                                   NL_TABLE_BOOK + NL_PUBLISHER_ID_EQUALS +
                                   NL_TABLE_PUBLISHER + "." + NL_ID;
        PreparedStatement stmt = db.prepareStatement(query);
        stmt.setString(1, isbn);
        ResultSet rs = stmt.executeQuery();
        try {
            boolean exists = rs.first();
            if (!exists) {
                if (addedBooks != null) {
                    Enumeration enumr = addedBooks.elements();
                    while (enumr.hasMoreElements()) {
                        Book b = (Book)enumr.nextElement();
                        if (isbn.equals(b.getISBN())) {
                            return b;
                        }
                    }
                }
                throw new ItemNotFoundException("no book with the ISBN " + isbn +
                " was found");
            }
            int id = rs.getInt(NL_ID);
            String title = rs.getString(NL_TITLE);
            Date year = rs.getDate(NL_YEAR);
            String publisher = rs.getString(NL_PUBLISHER + "." + NL_NAME);
            String description = rs.getString(NL_DESCRIPTION);
            float price = rs.getFloat(NL_PRICE);
            int stock = rs.getInt(NL_STOCK);
            String query2 = NL_SELECT + NL_TABLE_AUTHOR + "." + NL_NAME +
            NL_FROM + NL_TABLE_BOOK + "," +
            NL_TABLE_AUTHOR + "," +
            NL_TABLE_PUBLISHER +
            NL_WHERE + NL_TABLE_BOOK + "." + NL_ISBN + NL_EQUALS_QUESTION_AND +
            NL_TABLE_BOOK + "." + NL_ISBN + " = " +
            NL_TABLE_AUTHOR + "." + NL_ISBN + NL_AND +
            NL_TABLE_BOOK + NL_PUBLISHER_ID_EQUALS +
            NL_TABLE_PUBLISHER + "." + NL_ID;
            PreparedStatement stmt2 = db.prepareStatement(query2);
            stmt2.setString(1, isbn);
            ResultSet rs2 = stmt2.executeQuery();
            try {
                boolean more2 = rs2.first();
                Vector authors = new Vector();
                while (more2) {
                    String author = rs2.getString(NL_TABLE_AUTHOR + "." + NL_NAME);
                    authors.add(author);
                    more2 = rs2.next();
                }
                String arr[] = new String[authors.size()];
                for (int i = 0; i < arr.length; ++i) {
                    arr[i] = (String)authors.elementAt(i);
                }
                return new Book(id, isbn, title, year, arr, publisher,
                        description, price, stock);
            } finally {
                rs2.close();
                stmt2.close();
            }
        } finally {
            rs.close();
            stmt.close();
        }
    }
    public static void addNewItem(Book book) throws Exception {
        if (addedBooks == null) {
            addedBooks = new Hashtable();
        }
        if (addedBooks.size() >= MAX_BOOKS_TO_ADD) {
            throw new Exception("Too many books (" + MAX_BOOKS_TO_ADD +
                ") have been added already. Added books are removed as soon as the session of the user who added them expires, after 20 minutes of inactivity");
        } else {
            addedBooks.put(new Integer(book.getId()), book);
        }
    }
    public static synchronized void clearAddedBooks(TreeSet booksToClear) {
        if (addedBooks != null) {
            Iterator itr = booksToClear.iterator();
            while (itr.hasNext()) {
                Book book = (Book)itr.next();
                addedBooks.remove(new Integer(book.getId()));
            }
            addedBooks.clear();
        }
    }
}

// END OF FILE