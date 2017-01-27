/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simdevice;

import com.google.gson.JsonObject;
import com.ibm.iotf.client.device.DeviceClient;
import java.io.File;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author andreaswassmer
 */
public class DeviceSimulator {

    private final static String PROPERTIES_FILE_NAME = "device.properties";
    private String propertyFileName;
    Random rand = new Random();
    
    static Logger logger = Logger.getLogger(DeviceSimulator.class.getName());
    /**
     * @param args the command line arguments
     */
    public DeviceSimulator(String propertyFile) {
        this.propertyFileName = propertyFile;
    }
    
    public void measure() {
       Properties options = DeviceClient.parsePropertiesFile(new File(PROPERTIES_FILE_NAME));
        DeviceClient myClient = null;
        try {
            myClient = new DeviceClient(options);
        } catch (Exception ex) {
            logger.severe("Could not create a connection client to Watson\nReason: " + ex.getLocalizedMessage());
            System.exit(1);
        }
        
        if (myClient == null) {
            logger.severe("Could not connect to Watson\nTerminating...\n");
            System.exit(1);
        }
        
        try {
            myClient.connect();
        } catch (MqttException ex) {
            logger.severe("Could not connect to Watson\nReason: " + ex.getLocalizedMessage());
        }


        JsonObject event = new JsonObject();
        event.addProperty("name", "foo");
        
        int cpuUsage = rand.nextInt(100)+1;
        event.addProperty("cpu", cpuUsage);
        event.addProperty("mem", 70);
        
        myClient.publishEvent("status", event, 1);
        logger.info("SUCCESSFULLY POSTED.......");
        
        myClient.disconnect();      
    }
    public static void main(String[] args) {
        DeviceSimulator theDevice = new DeviceSimulator(PROPERTIES_FILE_NAME);
        for (int i = 0; i < 10; i++) {
            theDevice.measure();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(DeviceSimulator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
