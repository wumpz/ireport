/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.transformer;

/**
 *
 * @version $Id: TransformException.java 0 2010-04-15 10:40:48 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
class TransformException extends Exception {

    TransformException(Exception ex) {
        super(ex);
    }

}
