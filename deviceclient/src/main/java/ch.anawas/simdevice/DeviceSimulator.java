/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.anawas.simdevice;

import com.google.gson.JsonObject;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ch.anawas.network.PlatformConnector;
import ch.anawas.sensor.*;

/**
 *
 * @author andreaswassmer
 */
public class DeviceSimulator implements SensorType {

    final String PROPERTIES_FILE_NAME = "javadevice2.properties";
    final String deviceName;
    ArrayList<Sensor> sensors;
    ArrayList<PlatformConnector> connectors;

    static Logger logger = Logger.getLogger(DeviceSimulator.class.getName());

    public DeviceSimulator(String deviceName) {
       this.deviceName = deviceName;
       sensors = new ArrayList<>();
       connectors = new ArrayList<>();

        /* todo: inject PlatformConnector */
        connectors.add(new PlatformConnector("device.properties"));
        connectors.add(new PlatformConnector("javadevice2.properties"));
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public void measure() {

        JsonObject event = new JsonObject();
        event.addProperty("name", this.deviceName);

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
            for (PlatformConnector conn : connectors) {
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
        ArrayList<DeviceSimulator> devices = new ArrayList<>();

        DeviceSimulator theDevice1 = new DeviceSimulator("rpi-001");
        theDevice1.addSensor(new TemperatureSensor());
        theDevice1.addSensor(new HumiditySensor());
        devices.add(theDevice1);
        DeviceSimulator theDevice2 = new DeviceSimulator("rpi-002");
        theDevice2.addSensor(new TemperatureSensor());
        theDevice2.addSensor(new HumiditySensor());
        devices.add(theDevice2);

        for (int i = 0; i < 10; i++) {
            for (DeviceSimulator device : devices) {
                device.measure();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DeviceSimulator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
