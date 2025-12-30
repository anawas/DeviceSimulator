/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.anawas.network;

import com.google.gson.JsonObject;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.Properties;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author andreaswassmer
 */
public class PlatformConnector {
    private final String serverUrl = "https://backend.local:8443/datastore";
    private final String propertyFileName;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public PlatformConnector(String propertyFilename) {
        this.propertyFileName = propertyFilename;
    }

    public void sendToPlatform(JsonObject event) throws Exception {

        if (propertyFileName == null) {
            throw new Exception("Property file name missing");
        }

        // Load properties file from resources
        Properties properties = new Properties();
        try (InputStream fis = getClass().getClassLoader()
                .getResourceAsStream(propertyFileName)) {
            if (fis == null) {
                throw new Exception("Property file '" + propertyFileName + "' not found in resources");
            }
            properties.load(fis);
        }

        // Get keystore password from properties
        String keystorePassword = properties.getProperty("keystore.password");
        if (keystorePassword == null) {
            throw new Exception("Keystore password not found in property file");
        }

        // Get truststore password from properties
        String truststorePassword = properties.getProperty("truststore.password");
        if (truststorePassword == null) {
            throw new Exception("Truststore password not found in property file");
        }

        // Load P12 keystore from resources (client certificate)
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream keystoreStream = getClass().getClassLoader()
                .getResourceAsStream("conf/pi-001.p12")) {
            if (keystoreStream == null) {
                throw new Exception("Keystore file 'conf/pi-001.p12' not found in resources");
            }
            keyStore.load(keystoreStream, keystorePassword.toCharArray());
        }

        // Initialize KeyManagerFactory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keystorePassword.toCharArray());

        // Load P12 truststore from resources (server certificate)
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        try (InputStream truststoreStream = getClass().getClassLoader()
                .getResourceAsStream("conf/server-truststore.p12")) {
            if (truststoreStream == null) {
                throw new Exception("Truststore file 'conf/server-truststore.p12' not found in resources");
            }
            trustStore.load(truststoreStream, truststorePassword.toCharArray());
        }

        // Initialize TrustManagerFactory
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        // Create SSL context with both key managers and trust managers
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        // Send data to platform
        logger.info("Sending data to platform: " + event.toString());
        URL url = new URL(serverUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(sslContext.getSocketFactory());
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Write JSON data
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = event.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Get response
        int responseCode = connection.getResponseCode();
        logger.info("Response code: " + responseCode);

        if (responseCode != HttpsURLConnection.HTTP_OK &&
            responseCode != HttpsURLConnection.HTTP_CREATED &&
            responseCode != HttpsURLConnection.HTTP_NO_CONTENT) {
            // Read error stream to get more details
            InputStream errorStream = connection.getErrorStream();
            if (errorStream != null) {
                String errorResponse = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
                logger.severe("Server error response: " + errorResponse);
            }
            throw new Exception("Failed to send data. Response code: " + responseCode);
        }
    }
}
