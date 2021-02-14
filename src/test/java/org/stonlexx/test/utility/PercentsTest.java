package org.stonlexx.test.utility;

import org.stonlexx.gamelibrary.utility.PercentUtil;

public class PercentsTest {

    public static void main(String[] args) {

        // Получить процент от максимального числа
        System.out.println( PercentUtil.getPercent(2505, 5555) + "%" );

        // Получить число по проценту от максимального числа
        System.out.println( PercentUtil.getNumberByPercent(55, 150) );

        // Проверка твоей удачи на процент (Тест на п*дораса :D)
        System.out.println( PercentUtil.acceptRandomPercent(50) );
    }

}
