/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mandfer.tools.validation;

/**
 * @author marcandreuf on 24/10/2014.
 */
public class Validator {


    /**
     * Checking if value si int he range.
     *
     * @param value
     * @param min,  min value included.
     * @param max,  max value excluded.
     * @return
     */
    public boolean isInRange(int value, int min, int max) {

        return (min <= value && value < max);
    }


}
