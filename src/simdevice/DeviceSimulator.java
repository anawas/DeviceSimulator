/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simdevice;

import com.google.gson.JsonObject;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.IBMWatsonConnector;
import sensor.*;

/**
 *
 * @author andreaswassmer
 */
public class DeviceSimulator implements SensorType {

    final String PROPERTIES_FILE_NAME = "javadevice2.properties";
    Random rand = new Random();
    ArrayList<Sensor> sensors;
    ArrayList<IBMWatsonConnector> connectors;

    static Logger logger = Logger.getLogger(DeviceSimulator.class.getName());

    /**
     * @param propertyFile Full path to property file
     */
    public DeviceSimulator() {
        sensors = new ArrayList<>();
        connectors = new ArrayList<>();

        connectors.add(new IBMWatsonConnector("device.properties"));
        connectors.add(new IBMWatsonConnector("javadevice2.properties"));
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public void measure() {

        JsonObject event = new JsonObject();
        event.addProperty("name", "Simulated Device");

        for (Sensor aSensor : sensors) {
            switch (aSensor.getType()) {
                case SensorType.TEMPERATURE:
                    event.addProperty("temperature", ((TemperatureSensor) aSensor).measure());
                    break;
                case SensorType.HUMIDITY:
                    event.addProperty("humidity", ((HumiditySensor) aSensor).measure());
                    break;
            }
        }

        try {
            for (IBMWatsonConnector conn : connectors) {
                conn.sendToPlatform(event);
            }
        } catch (UnknownHostException ex) {
            //Logger.getLogger(DeviceSimulator.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(DeviceSimulator.class.getName()).log(Level.SEVERE, "Could not open connection to platform");
        } catch (Exception ex) {
            Logger.getLogger(DeviceSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        logger.info(event.toString());
        logger.info("SUCCESSFULLY POSTED.......");
    }

    public static void main(String[] args) {
        DeviceSimulator theDevice = new DeviceSimulator();
        theDevice.addSensor(new TemperatureSensor());
        theDevice.addSensor(new HumiditySensor());

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
