package com.ray.common.lang;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author zyl
 * @date Created on 2018/2/5
 */
public class StringsTest {
    @Test
    public void isPhoneNum() throws Exception {
        boolean isPhone1 = Strings.isPhoneNum("15400000000");
        boolean isPhone2 = Strings.isPhoneNum("15500000000");
        boolean isPhone3 = Strings.isPhoneNum("16000000000");
        boolean isPhone4 = Strings.isPhoneNum("19000000000");
        assertEquals(isPhone1, false);
        assertEquals(isPhone2, true);
        assertEquals(isPhone3, true);
        assertEquals(isPhone4, true);
    }

    @Test
    public void isPhoneStart() throws Exception {
        boolean isPhone1 = Strings.isPhoneStart("15400000000");
        boolean isPhone2 = Strings.isPhoneStart("15500000000");
        boolean isPhone3 = Strings.isPhoneStart("16000000000");
        boolean isPhone4 = Strings.isPhoneStart("19000000000");
        assertEquals(isPhone1, false);
        assertEquals(isPhone2, true);
        assertEquals(isPhone3, true);
        assertEquals(isPhone4, true);
    }

}