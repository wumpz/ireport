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

import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.net.ssl.X509TrustManager;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @version $Id: IReportTrustManager.java 0 2010-07-19 19:40:50 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IReportTrustManager implements X509TrustManager {

        public static final String TRUSTED_CERTIFICATE_FINGERPRINTS = "trustedCertificateFingerprints";
        private List<String>temporaryCertificates = new ArrayList<String>();
        private List<String>trustedCertificates = new ArrayList<String>();

        private boolean storingFingerprints = false;

	public IReportTrustManager() {

            reloadTrustedCertificates();

            JasperServerManager.getPreferences().addPreferenceChangeListener(new PreferenceChangeListener() {
                public void preferenceChange(PreferenceChangeEvent evt) {
                        if (!isStoringFingerprints() && evt.getKey().equals(TRUSTED_CERTIFICATE_FINGERPRINTS))
                        {
                            reloadTrustedCertificates();
                        }
                    }
                });

            
	}


        private void reloadTrustedCertificates() {
            trustedCertificates.clear();
            temporaryCertificates.clear();
            String trustedSites = JasperServerManager.getPreferences().get(TRUSTED_CERTIFICATE_FINGERPRINTS, "");
            String[] fingerprints = trustedSites.split(":");
            trustedCertificates.addAll( Arrays.asList(fingerprints));
        }

        private void saveCertificates()
        {
            
            String trustedCertificateFingerprints = "";
            for (String s : trustedCertificates)
            {
                trustedCertificateFingerprints += s + ":";
            }

            setStoringFingerprints(true);
            JasperServerManager.getPreferences().put(TRUSTED_CERTIFICATE_FINGERPRINTS, trustedCertificateFingerprints);
            setStoringFingerprints(false);
        }


	public X509Certificate[] getAcceptedIssuers() {
	   return new X509Certificate[]{};
	}

	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
 
                // Check the first certificate...
                if (chain == null || chain.length == 0)
                {
                    throw new CertificateException("No certificate sent by the server");
                }

                 MessageDigest sha1;
                 try {
                    sha1 = MessageDigest.getInstance("SHA1");
                 } catch (NoSuchAlgorithmException ex) {
                    throw new CertificateException("Unable to instance the SHA1 Algorithm!");
                 }

                 MessageDigest md5;
                 try {
                    md5 = MessageDigest.getInstance("MD5");
                 } catch (NoSuchAlgorithmException ex) {
                    throw new CertificateException("Unable to instance the MD5 Algorithm!");
                 }

                 final StringBuffer certsText = new StringBuffer();

                 List<String> currentCertificates = new ArrayList<String>();

                for (int i = 0; i < chain.length; i++) {
                    X509Certificate cert = chain[i];

                    String digest = toHexString(sha1.digest());

                    if (isTrusted(digest))
                    {
                        return;
                    }

                    if (i==0)
                    {
                        currentCertificates.add(digest);
                    }

                    certsText.append(" " + (i + 1) + " Subject " + cert.getSubjectDN()+"\n");
                    certsText.append("   Issuer  " + cert.getIssuerDN()+"\n");
                    sha1.update(cert.getEncoded());
                    certsText.append("   sha1    " + digest +"\n");
                    md5.update(cert.getEncoded());
                    certsText.append("   md5     " + toHexString(md5.digest())+"\n\n");
                }

                final CertificatesDialog dialog = new CertificatesDialog(Misc.getMainFrame(), true);
                dialog.setCertsText(certsText.toString());

                try {


                        SwingUtilities.invokeAndWait(new Runnable() {

                            public void run() {
                               dialog.setVisible(true);
                            }
                        });


                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    }

                 if (dialog.getDialogResult() == JOptionPane.OK_OPTION)
                 {
                     if (dialog.getAnswer() == 0) // Accept the certificates permanently
                     {
                         this.trustedCertificates.addAll(currentCertificates);
                         saveCertificates();
                         return;
                     }
                     else if (dialog.getAnswer() == 1)
                     {
                         this.temporaryCertificates.addAll(currentCertificates);
                         return;
                     }
                 }

                 throw new CertificateException("Untrusted certificate");
        }


        private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

        private static String toHexString(byte[] bytes) {
            StringBuilder sb = new StringBuilder(bytes.length * 3);
            for (int b : bytes) {
                b &= 0xff;
                sb.append(HEXDIGITS[b >> 4]);
                sb.append(HEXDIGITS[b & 15]);
                sb.append(' ');
            }
            return sb.toString();
        }

    /**
     * @return the storingFingerprints
     */
    public boolean isStoringFingerprints() {
        return storingFingerprints;
    }

    /**
     * @param storingFingerprints the storingFingerprints to set
     */
    public void setStoringFingerprints(boolean storingFingerprints) {
        this.storingFingerprints = storingFingerprints;
    }


    private boolean isTrusted(String fingerprint)
    {
        for (String s: trustedCertificates)
        {
            if (s != null && s.equals(fingerprint)) return true;
        }

        for (String s: temporaryCertificates)
        {
            if (s != null && s.equals(fingerprint)) return true;
        }

        return false;
    }

}
