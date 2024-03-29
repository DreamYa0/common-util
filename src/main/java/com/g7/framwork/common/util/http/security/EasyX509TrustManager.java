package com.g7.framwork.common.util.http.security;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author dreamyao
 * @title
 * @date 2020/9/1 6:31 PM
 * @since 1.0.0
 */
public class EasyX509TrustManager implements X509TrustManager {

    private X509TrustManager standardTrustManager = null;

    public EasyX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException,
            KeyStoreException {
        super();
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(keystore);
        TrustManager[] trustManagers = factory.getTrustManagers();
        if (trustManagers.length == 0) {
            throw new NoSuchAlgorithmException("no trust manager found");
        }
        this.standardTrustManager = (X509TrustManager) trustManagers[0];
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certificates, String authType)
            throws CertificateException {
        standardTrustManager.checkClientTrusted(certificates, authType);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certificates, String authType)
            throws CertificateException {
        if ((certificates != null) && (certificates.length == 1)) {
            certificates[0].checkValidity();
        } else {
            standardTrustManager.checkServerTrusted(certificates, authType);
        }
    }

    public X509Certificate[] getAcceptedIssuers() {
        return this.standardTrustManager.getAcceptedIssuers();
    }
}
