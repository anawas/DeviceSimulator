/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simdevice;

/**
 *
 * @author andreaswassmer
 */
public class TemperatureSensor extends Sensor implements Measuring, SensorType {

    public TemperatureSensor() {
        super();
        this.setRange(-10, 100);
        this.type = SensorType.TEMPERATURE;
        this.name = "Temperature";
    }
    
    @Override
    public String measure() {
        int res = this.readValueAsInt();
        return String.valueOf(res);
    }  
}
