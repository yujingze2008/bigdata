package com.luckypeng.study.algorithm.sorting;

import org.junit.Test;

import static org.junit.Assert.*;

public class SelectSortTest extends AbstractSort{

    @Test
    public void selectSort() {
        SelectSort.selectSort(test);
        assertArrayEquals(refers, test);
    }
}