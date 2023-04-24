/*
 * $RCSfile: ItemNotFoundException.java,v $
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
 * $Date: 2003/12/01 16:58:10 $
 * $Log: ItemNotFoundException.java,v $
 * Revision 1.1  2003/12/01 16:58:10  rjaamour
 * Original source code.
 *
 */

package webtool.soap.examples.store;

public class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String msg) {
        super(msg);
    }
}
