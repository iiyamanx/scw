/*
 * $RCSfile: DB.java,v $
 * $Revision: 1.5 $
 *
 * Comments:
 *
 * (C) Copyright ParaSoft Corporation 1996.  All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ParaSoft
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * $Author: rjaamour $          $Locker:  $
 * $Date: 2004/03/27 00:03:57 $
 * $Log: DB.java,v $
 * Revision 1.5  2004/03/27 00:03:57  rjaamour
 * set host to ws1 instead of localhost.
 *
 * Revision 1.4  2004/02/25 17:20:41  rjaamour
 * Minor, Added extra check to connect()
 *
 * Revision 1.3  2004/02/11 00:51:37  rjaamour
 * Create new connection only if current is null, otherwise reuse
 *
 * Revision 1.2  2003/12/10 00:20:04  rjaamour
 * removed a comment.
 *
 * Revision 1.1  2003/12/02 16:57:29  rjaamour
 * Original source code.
 *
 */

package webtool.soap.examples.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DB {
	private static final String IIYAMAN = "IIYAMAN" //20230212
    private static final String NL_DRIVER         = "com.mysql.jdbc.Driver";
    //private static final String NL_DRIVER         = "com.parasoft.xtest.jdbc.virt.driver.JDBCProxyDriver";
    // NYI make these settable by the deployment descriptor
    //private static final String NL_HOST           = "ws1.parasoft.com";
    private static final String NL_HOST           = "localhost";
    private static final String NL_PORT           = "";
    private static final String NL_DBNAME         = "bookstore";
    private static final String NL_USERNAME       = "soatest";
    private static final String NL_PASSWORD       = "soatest";
    private static final String NL_CONNECTION_URL = constructConnectionURL();
    private Connection con;

    private static final String constructConnectionURL() {
        return "jdbc:mysql://" + NL_HOST + NL_PORT + "/" + NL_DBNAME +
               "?" + "user=" + NL_USERNAME + "&password=" + NL_PASSWORD;// + "&characterEncoding=UTF8";
        //return "jdbc:parasoft:proxydriver:com.mysql.jdbc.Driver:@" +
        //		"jdbc:mysql://" + NL_HOST + NL_PORT + "/bookstore?user=soatest&password=soatest&characterEncoding=UTF8";
    }
	protected DB() throws SQLException, InstantiationException,
                          IllegalAccessException, ClassNotFoundException {
        Class.forName(NL_DRIVER).newInstance();
        connect();
	}
    protected void connect() throws SQLException {
        if (con == null || con.isClosed()) {
            con = DriverManager.getConnection(NL_CONNECTION_URL);
        }
    }
    /**
     * Call close() when finished.
     * @param  query an SQL statement
     */
    protected  PreparedStatement prepareStatement(String query) throws SQLException {
        return con.prepareStatement(query);
    }
    public boolean isClosed() throws SQLException {
        return con == null || con.isClosed();
    }
    public void close() throws SQLException {
        con.close();
    }
    /**
     * Don't rely on this, call close() manually.
     */
    public void finalize() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
