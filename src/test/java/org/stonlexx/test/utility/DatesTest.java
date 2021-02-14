package org.stonlexx.test.utility;

import org.stonlexx.gamelibrary.utility.DateUtil;

public class DatesTest {

    public static void main(String[] args) {
        String pattern = DateUtil.DEFAULT_DATE_PATTERN;
        String currentDate = DateUtil.formatPattern(pattern);

        System.out.println( "Текущая дата - " + currentDate );
        System.out.println( "Текущее время - " + DateUtil.formatPattern(DateUtil.DEFAULT_TIME_PATTERN));
        System.out.println( "Текущая дата в миллисекундах - " + DateUtil.parseDate(pattern, currentDate).getTime() );
    }
}
