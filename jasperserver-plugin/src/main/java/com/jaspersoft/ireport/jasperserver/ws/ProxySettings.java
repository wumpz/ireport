package com.jaspersoft.ireport.jasperserver.ws;

import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * This class has been taken by org.netbeans.modules.subversion.util package
 * in order to reuse the proxy settings provided by NetBeans preferences....
 * 
 * 
 * @author Tomas Stupka
 */
public class ProxySettings {

    private static final String PROXY_HTTP_HOST                 = "proxyHttpHost";
    private static final String PROXY_HTTP_PORT                 = "proxyHttpPort";
    private static final String PROXY_HTTPS_HOST                = "proxyHttpsHost";
    private static final String PROXY_HTTPS_PORT                = "proxyHttpsPort";
    private static final String NOT_PROXY_HOSTS                 = "proxyNonProxyHosts";
    private static final String USE_PROXY_AUTHENTICATION        = "useProxyAuthentication";
    private static final String PROXY_AUTHENTICATION_USERNAME   = "proxyAuthenticationUsername";
    private static final String PROXY_AUTHENTICATION_PASSWORD   = "proxyAuthenticationPassword";
   
    private static final String PROXY_TYPE                      = "proxyType";
    private static final String DIRECT_CONNECTION               = "0";           
    private static final String AUTO_DETECT_PROXY               = "1"; // as default
    private static final String MANUAL_SET_PROXY                = "2";
           
    private String username;
    private String password;
    private String notProxyHosts;
    private boolean useAuth;
    private String httpHost;
    private String httpPort;
    private String httpsHost;
    private String httpsPort;
    private String proxyType;
     
    private String toString = null;
   
    public ProxySettings() {
        init();
    };
       
    private void init() {
        Preferences prefs = org.openide.util.NbPreferences.root ().node ("org/netbeans/core");                              // NOI18N   
        proxyType           = prefs.get        ( PROXY_TYPE,                     ""    );                                   // NOI18N                                       

        if(proxyType.equals(DIRECT_CONNECTION)) {
            useAuth             = false;
            username            = "";                                                                                       // NOI18N
            password            = "";                                                                                       // NOI18N           
       
            notProxyHosts       = "";                                                                                       // NOI18N
            httpHost            = "";                                                                                       // NOI18N
            httpPort            = "";                                                                                       // NOI18N
            httpsHost           = "";                                                                                       // NOI18N
            httpsPort           = "";                                                                                       // NOI18N
        } else if(isManualSetProxy()) {
            useAuth             = prefs.getBoolean ( USE_PROXY_AUTHENTICATION,       false );                               // NOI18N           
            username            = prefs.get        ( PROXY_AUTHENTICATION_USERNAME,  ""    );                               // NOI18N
            password            = prefs.get        ( PROXY_AUTHENTICATION_PASSWORD,  ""    );                               // NOI18N               

            notProxyHosts       = prefs.get        ( NOT_PROXY_HOSTS,                ""    ).replace("|", " ,");            // NOI18N               
            httpHost            = prefs.get        ( PROXY_HTTP_HOST,                ""    );                               // NOI18N               
            httpPort            = prefs.get        ( PROXY_HTTP_PORT,                ""    );                               // NOI18N               
            httpsHost           = prefs.get        ( PROXY_HTTPS_HOST,               ""    );                               // NOI18N               
            httpsPort           = prefs.get        ( PROXY_HTTPS_PORT,               ""    );                               // NOI18N                                           
        } else { // AUTO_DETECT_PROXY or DEFAULT
            useAuth             = false; // no way known yet!
            username            = "";                                                                                       // NOI18N
            password            = "";                                                                                       // NOI18N           
       
            notProxyHosts       = System.getProperty("http.nonProxyHosts", "");                                             // NOI18N           
            httpHost            = System.getProperty("http.proxyHost",     "");                                             // NOI18N           
            httpPort            = System.getProperty("http.proxyPort",     "");                                             // NOI18N               
            httpsHost           = System.getProperty("https.proxyHost",    "");                                             // NOI18N           
            httpsPort           = System.getProperty("https.proxyPort",    "");                                             // NOI18N           
        }
        
        
    }
   
    public boolean isDirect() {
        return proxyType.equals(DIRECT_CONNECTION);
    }

    public boolean isManualSetProxy() {
        return proxyType.equals(MANUAL_SET_PROXY);
    }
   
    public boolean hasAuth() {
        return useAuth;
    }
   
    public String getHttpHost() {
        return httpHost;
    }
   
    public int getHttpPort() {
        if(httpPort.equals("")) {
            return 8080;
        }
        return Integer.parseInt(httpPort);
    }

    public String getHttpsHost() {
        return httpsHost;
    }
   
    public int getHttpsPort() {
        if(httpsPort.equals("")) {
            return 443;
        }
        return Integer.parseInt(httpsPort);
    }
   
    public String getUsername() {
        return username;
    }
   
    public String getPassword() {
        return password;
    }   
   
    public String getNotProxyHosts() {
        return notProxyHosts;
    }
   
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(! (obj instanceof ProxySettings) ) {
            return false;
        }
        ProxySettings ps = (ProxySettings) obj;       
        return ps.httpHost.equals(httpHost) &&
               ps.httpPort.equals(httpPort) &&
               ps.httpsHost.equals(httpsHost) &&
               ps.httpsPort.equals(httpsPort) &&
               ps.notProxyHosts.equals(notProxyHosts) &&
               ps.password.equals(password) &&
               ps.proxyType.equals(proxyType) &&
               ps.username.equals(username) &&
               ps.useAuth == useAuth;                  
    }
   
    public String toString() {
        if(toString == null) {
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            sb.append(httpHost);
            sb.append(",");       
            sb.append(httpPort);
            sb.append(",");       
            sb.append(httpsHost);
            sb.append(",");       
            sb.append(httpsPort);
            sb.append(",");       
            sb.append(notProxyHosts);
            sb.append(",");       
            sb.append(password);
            sb.append(",");       
            sb.append(proxyType);
            sb.append(",");       
            sb.append(username);
            sb.append(",");       
            sb.append(useAuth);               
            sb.append("]");
            toString = sb.toString();
        }       
        return toString;
    }

    public int hashCode() {
        return toString().hashCode();
    }
   
    
    
    
    /**
     * Check if the give hosts requires the use of a proxy
     * 
     * @param host  the host to check
     * 
     * @return 
     */
    public boolean isHostInNonProxyList(String host)
    {
        if (host == null || host.equals("")) return false;
        
        return match(getNotProxyHosts(), host);
    }
    
    
    
    /**
     * Evaluates if the given hostaname or IP address is in the given value String.
     *
     * @param value the value String. A list of host names or IP addresses delimited by ",". 
     *                          (e.g 192.168.0.1,*.168.0.1, some.domain.com, *.anything.com, ...)
     * @param host the hostname or IP address
     * @return true if the host name or IP address was found in the values String, otherwise false.
     */
    private boolean match(String value, String host) {                    
        String[] values = value.split(",");                                     // NOI18N
        for (int i = 0; i < values.length; i++) {
            value = values[i].trim();

            if(value.equals("*") || value.equals(host) ) {                      // NOI18N
                return true;
            }

            int idx = value.indexOf("*");                                       // NOI18N
            if(idx > -1 && matchSegments(value, host) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Evaluates if the given hostaname or IP address matches with the given value String representing 
     * a hostaname or IP adress with one or more "*" wildcards in it.
     *
     * @param value the value String. A host name or IP addresse with a "*" wildcard. (e.g *.168.0.1 or *.anything.com)
     * @param host the hostname or IP address
     * @return true if the host name or IP address matches with the values String, otherwise false.
     */
    private boolean matchSegments(String value, String host) {
        value = value.replace(".", "\\.");
        value = value.replace("*", ".*");
        Matcher m = Pattern.compile(value).matcher(host);
        return m.matches();
    }

   
}