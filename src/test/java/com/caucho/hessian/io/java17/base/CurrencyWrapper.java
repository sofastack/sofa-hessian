/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    private int               cent;

    private Currency          currency;

    public CurrencyWrapper() {
    }

    public CurrencyWrapper(Currency currency) {
        this.cent = currency.getCurrencyCode().hashCode();
        this.currency = currency;
    }

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

    /**
     * Getter method for property <tt>cent</tt>.
     *
     * @return property value of cent
     */
    public int getCent() {
        return cent;
    }
}