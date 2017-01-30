/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor;

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
            rangeStart = new Double(end);
            rangeEnd = new Double(start);
        } else {
            rangeStart = new Double(start);
            rangeEnd = new Double(end);
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
        int res = reading.nextInt(rangeEnd.intValue()) + rangeStart.intValue();
        return res;
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
        double res = reading.nextDouble() * range + rangeStart;
        return res;
    }
    
    public boolean readStatus() {
        int value = reading.nextInt();
        boolean res = false;
        
        if (value == 0) res = true;
        else res = false;
        
        return res;
    }
}
