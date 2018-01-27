package com.lions.app.sportsstore.structs;

/**
 * Created by Panwar on 21/01/18.
 */

import java.util.Comparator;
import java.util.Map;

/**
 * Created by Sparsh23 on 01/09/16.
 */

class MapComparators implements Comparator<Products>
{

    public MapComparators()
    {
    }

    @Override
    public int compare(Products products, Products t1) {
        Double firstValue = Double.valueOf(products.getProduct_price());
        Double secondValue = Double.valueOf(t1.getProduct_price());
        return firstValue.compareTo(secondValue);
    }
}