package cn.oreo.common.util.common;

import cn.oreo.common.util.entity.util.Distance;

import java.util.Comparator;

//比较器类
public class CompareClass implements Comparator<Distance> {
    @Override
    public int compare(Distance d1, Distance d2) {
        return d1.getDisatance() > d2.getDisatance() ? 20 : -1;
    }
}
