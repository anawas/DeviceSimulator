/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor;

import simdevice.Measuring;

/**
 *
 * @author andreaswassmer
 */
public class TemperatureSensor extends Sensor implements Measuring, SensorType {

    public TemperatureSensor() {
        super();
        this.setRange(-10, 100);
        type = SensorType.TEMPERATURE;
        name = "Temperature";
    }
    
    @Override
    public String measure() {
        int res = this.readValueAsInt();
        return String.valueOf(res);
    }  
}
