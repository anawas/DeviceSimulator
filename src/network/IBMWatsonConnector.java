/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import com.google.gson.JsonObject;
import com.ibm.iotf.client.device.DeviceClient;
import com.sun.istack.internal.logging.Logger;
import java.io.File;
import java.net.UnknownHostException;
import java.util.Properties;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author andreaswassmer
 */
public class IBMWatsonConnector {

    private String propertyFileName;
    private Logger logger = Logger.getLogger(this.getClass());

    public IBMWatsonConnector(String propertyFilename) {
        this.propertyFileName = propertyFilename;
    }

    public void sendToPlatform(JsonObject event) throws MqttException, UnknownHostException, Exception {
        
        if (propertyFileName == null) {
            throw new Exception("Property file name missing");
        }
        
        Properties options = DeviceClient.parsePropertiesFile(new File(propertyFileName));
        
        System.out.println("Sending data to platform: " + event.toString());
        DeviceClient myClient = null;

        myClient = new DeviceClient(options);
        if (myClient == null) {
            logger.severe("Could not connect to Watson\nTerminating...\n");
            System.exit(1);
        }

        myClient.connect();
        myClient.publishEvent("status", event, 1);
        myClient.disconnect();
    }
}
