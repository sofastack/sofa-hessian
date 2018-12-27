package com.alipay.hessian.internal;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class InternalNameBlackListFilterDuplicateTest {

    @Test
    public void testDuplicate() {
        Set<String> sets = new HashSet<String>(InternalNameBlackListFilter.INTERNAL_BLACK_LIST);
        Assert.assertEquals(sets.size(), InternalNameBlackListFilter.INTERNAL_BLACK_LIST.size());
    }

}