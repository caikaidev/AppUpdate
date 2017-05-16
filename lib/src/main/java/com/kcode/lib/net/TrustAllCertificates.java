package com.kcode.lib.net;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by caik on 2017/5/16.
 */

public class TrustAllCertificates implements X509TrustManager, HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public static void install() {
        try {
            TrustAllCertificates trustAll = new TrustAllCertificates();

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null,
                    new TrustManager[]{trustAll},
                    new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HttpsURLConnection.setDefaultHostnameVerifier(trustAll);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed setting up all thrusting certificate manager.", e);
        } catch (KeyManagementException e) {
            throw new RuntimeException("Failed setting up all thrusting certificate manager.", e);
        }
    }
}
