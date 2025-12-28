/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.anawas.simdevice;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ch.anawas.sensor.TemperatureSensor;

/**
 *
 * @author andreaswassmer
 */
public class TemperatureSensorTest {
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of measure method, of class TemperatureSensor.
     */
    @Test
    public void testMeasure() {
        System.out.println("measure");
        TemperatureSensor instance = new TemperatureSensor();
        String result = instance.measure();
        assertTrue(Integer.parseInt(result) >= -10 && Integer.parseInt(result) <= 100);
        // TODO review the generated test code and remove the default call to fail.
    }
    
}
