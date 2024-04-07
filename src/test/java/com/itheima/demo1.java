package com.itheima;

import com.itheima.reggie.utils.SMSUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class demo1 {

    @Test
    public int findPoisonedDuration(int[] timeSeries, int duration) {
        ArrayList<Integer> list1 = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();
        for(int i = 0; i<timeSeries.length; i++) {
            for(int j = 0; j<duration; j++) {
                list2.add(timeSeries[i] + j);
            }
            list1.addAll(list2);
            list2.clear();
        }
        return list1.size();
    }

}
