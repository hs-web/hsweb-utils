package org.hswebframework.utils.time;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsTest {


    @Test
    public void testToInt(){
        assertEquals(1991,Utils.toNumber("1991",0,4));

        assertEquals(2008,Utils.toNumber("2008",0,4));

        assertEquals(31,Utils.toNumber("31",0,2));
    }

}