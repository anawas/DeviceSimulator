/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.anawas.network;

import com.google.gson.JsonObject;
import java.util.logging.Logger;

/**
 *
 * @author andreaswassmer
 */
public class PlatformConnector {

    private final String propertyFileName;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public PlatformConnector(String propertyFilename) {
        this.propertyFileName = propertyFilename;
    }

    public void sendToPlatform(JsonObject event) throws Exception {
        
        if (propertyFileName == null) {
            throw new Exception("Property file name missing");
        }
        System.out.println("Sending data to platform: " + event.toString());
    }
}
