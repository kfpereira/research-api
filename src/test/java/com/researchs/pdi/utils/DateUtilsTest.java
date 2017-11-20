package com.researchs.pdi.utils;

import com.researchs.pdi.FunctionalTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@FunctionalTest
public class DateUtilsTest {

    @Test
    public void getParse() throws Exception {
        LocalDate dataLocal = DateUtils.getParse("01/12/2016");

        Assert.assertEquals("Ano", 2016, dataLocal.getYear());
        Assert.assertEquals("Mes", 12, dataLocal.getMonthValue());
        Assert.assertEquals("Dia", 1, dataLocal.getDayOfMonth());
    }

    @Test
    public void getDate() throws Exception {
        LocalDate now = DateUtils.getParse("01/12/2016");;
        Date date = DateUtils.getDate(now);

        Assert.assertEquals("getDate", "Thu Dec 01 00:00:00 BRST 2016", date.toString());
    }

    @Test
    public void media() {
        Assert.assertTrue(DateUtils.isAlmostPalindrome("abccba"));
        Assert.assertTrue(DateUtils.isAlmostPalindrome("abccbx"));
        Assert.assertFalse(DateUtils.isAlmostPalindrome("abccfg"));
    }

    @Test
    public void media2() {

        Assert.assertEquals("34", 34, DateUtils.getMostPopularNumber(new int[]{34,31,34,77,82}));
        Assert.assertEquals("101", 101, DateUtils.getMostPopularNumber(new int[]{22,101,102,101,102,525,88}));
        Assert.assertEquals("66", 66, DateUtils.getMostPopularNumber(new int[]{66}));
        Assert.assertEquals("2342", 2342, DateUtils.getMostPopularNumber(new int[]{14,14,2342,2342,2342}));

    }

}