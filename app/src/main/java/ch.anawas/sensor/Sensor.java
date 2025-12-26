/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.anawas.sensor;
import java.util.Random;

/**
 * Implements a simple virtual sensor
 * @author andreaswassmer
 */
public abstract class Sensor {
    String name;
    int type;
    Double rangeStart, rangeEnd, range;
    Random reading = new Random();
    
    public void setRange(int start, int end) {
        if (start > end) {
            rangeStart = (double)end;
            rangeEnd = (double)start;
        } else {
            rangeStart = (double)start;
            rangeEnd = (double)end;
        }
        range = rangeEnd - rangeStart;
    }
    
    public void setRange(double start, double end) {
        if (start > end) {
            rangeStart = end;
            rangeEnd = start;
        } else {
            rangeStart = start;
            rangeEnd = end;
        }
        range = rangeEnd - rangeStart;
    }

    public int readValueAsInt() {
        return reading.nextInt(rangeEnd.intValue()) + rangeStart.intValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public double readValueAsDouble() {
        return reading.nextDouble() * range + rangeStart;
    }
    
    public boolean readStatus() {
        int value = reading.nextInt();

       return value == 0;
    }
}
