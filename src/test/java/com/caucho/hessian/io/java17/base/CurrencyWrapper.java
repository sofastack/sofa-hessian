/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.java17.base;

import java.io.Serializable;
import java.util.Currency;

/**
 *
 * @author junyuan
 * @version CurrencyWrapper.java, v 0.1 2023年08月09日 11:51 junyuan Exp $
 */
public class CurrencyWrapper implements Serializable {
    private static final long serialVersionUID = 6738644291381453889L;

    private Currency currency;

    /**
     * Getter method for property <tt>currency</tt>.
     *
     * @return property value of currency
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Setter method for property <tt>currency</tt>.
     *
     * @param currency  value to be assigned to property currency
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}