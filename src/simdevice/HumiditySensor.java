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
public class HumiditySensor extends Sensor implements Measuring, SensorType {

    public HumiditySensor() {
        super();
        this.setRange(0.0, 100.0);
        name = "Humidity";
        type = SensorType.HUMIDITY;
    }
    
    @Override
    public String measure() {
        double res = this.readValueAsDouble();
        return String.format("%.2f", res);        
    }
}
