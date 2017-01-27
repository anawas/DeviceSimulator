/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simdevice;

/**
 * A sensor derived from Sensor class must implement this interface.
 * @author andreaswassmer
 */
public interface Measuring {
    /**
     * 
     * @return The the measurement as a String. 
     */
    public String measure();
}
