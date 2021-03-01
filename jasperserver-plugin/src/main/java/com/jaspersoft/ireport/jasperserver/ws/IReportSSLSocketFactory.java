/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2013 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 * 
 * This program is part of iReport.
 * 
 * iReport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * iReport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with iReport. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.ireport.jasperserver.ws;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

/**
 *
 * @version $Id: IReportSSLSocketFactory.java 0 2010-07-19 19:59:15 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IReportSSLSocketFactory implements org.apache.commons.httpclient.protocol.ProtocolSocketFactory  {



    private SSLContext sslcontext = null;


    /**

     * Constructor for EasySSLProtocolSocketFactory.

     */

    public IReportSSLSocketFactory() {

        super();

    }


    private static SSLContext createIRSSLContext() {

        try {

            SSLContext context = SSLContext.getInstance("SSL");

            context.init(null, new TrustManager[] {new IReportTrustManager()}, null);

            return context;

        } catch (Exception ex) {

            ex.printStackTrace();

            throw new HttpClientError(ex.toString());

        }

    }


    private SSLContext getSSLContext() {

        if (this.sslcontext == null) {

            this.sslcontext = createIRSSLContext();

        }

        return this.sslcontext;

    }


    /**

     * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int,java.net.InetAddress,int)

     */

    public Socket createSocket(

        String host,

        int port,

        InetAddress clientHost,

        int clientPort)

        throws IOException, UnknownHostException {


        return getSSLContext().getSocketFactory().createSocket(

            host,

            port,

            clientHost,

            clientPort

        );

    }


    /**

     * Attempts to get a new socket connection to the given host within the given time limit.

     * <p>

     * To circumvent the limitations of older JREs that do not support connect timeout a

     * controller thread is executed. The controller thread attempts to create a new socket

     * within the given limit of time. If socket constructor does not return until the

     * timeout expires, the controller terminates and throws an {@link ConnectTimeoutException}

     * </p>

     *

     * @param host the host name/IP

     * @param port the port on the host

     * @param clientHost the local host name/IP to bind the socket to

     * @param clientPort the port on the local machine

     * @param params {@link HttpConnectionParams Http connection parameters}

     *

     * @return Socket a new socket

     *

     * @throws IOException if an I/O error occurs while creating the socket

     * @throws UnknownHostException if the IP address of the host cannot be

     * determined

     */

    public Socket createSocket(

        final String host,

        final int port,

        final InetAddress localAddress,

        final int localPort,

        final HttpConnectionParams params

    ) throws IOException, UnknownHostException, ConnectTimeoutException {

        if (params == null) {

            throw new IllegalArgumentException("Parameters may not be null");

        }

        int timeout = params.getConnectionTimeout();

        SocketFactory socketfactory = getSSLContext().getSocketFactory();

        if (timeout == 0) {

            return socketfactory.createSocket(host, port, localAddress, localPort);

        } else {

            Socket socket = socketfactory.createSocket();

            SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);

            SocketAddress remoteaddr = new InetSocketAddress(host, port);

            socket.bind(localaddr);

            socket.connect(remoteaddr, timeout);

            return socket;

        }

    }


    /**

     * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int)

     */

    public Socket createSocket(String host, int port)

        throws IOException, UnknownHostException {

        return getSSLContext().getSocketFactory().createSocket(

            host,

            port

        );

    }


    /**

     * @see SecureProtocolSocketFactory#createSocket(java.net.Socket,java.lang.String,int,boolean)

     */

    public Socket createSocket(

        Socket socket,

        String host,

        int port,

        boolean autoClose)

        throws IOException, UnknownHostException {

        return getSSLContext().getSocketFactory().createSocket(

            socket,

            host,

            port,

            autoClose

        );

    }


    public boolean equals(Object obj) {

        return ((obj != null) && obj.getClass().equals(IReportSSLSocketFactory.class));

    }


    public int hashCode() {

        return IReportSSLSocketFactory.class.hashCode();

    }







}
