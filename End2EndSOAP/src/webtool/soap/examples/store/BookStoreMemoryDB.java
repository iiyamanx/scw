/*
 * $RCSfile$
 * $Revision$
 *
 * Comments:
 *
 * (C) Copyright ParaSoft Corporation 2004.  All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ParaSoft
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * $Author$          $Locker$
 * $Date$
 * $Log$
 */
package webtool.soap.examples.store;

import java.sql.SQLException;

/**
 * BookStoreMemoryDB
 */
public class BookStoreMemoryDB extends DB {
    
    private static BookStoreMemoryDB db = null;
    /**
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public BookStoreMemoryDB() throws SQLException, InstantiationException,
                    IllegalAccessException, ClassNotFoundException {
        super();
        // RJ Auto-generated constructor stub
    }
    public static BookStoreMemoryDB getDBInstance() throws SQLException,
        InstantiationException, IllegalAccessException, ClassNotFoundException  {
        if (db == null) {
            db = new BookStoreMemoryDB();
        }
        return db; 
    }
}
