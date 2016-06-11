package org.my.util;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class TableItemValues {
    private String values = "(";
    private String[] itemsValue;

    public TableItemValues(String[] itemsValue){
        this.itemsValue = itemsValue;
    }

    public String getItemValues(){
        for (String s : itemsValue)
            values += "\'" + s + "\',";
        values = values.substring(0,values.length()-1);
        values += ")";
        return values;
    }
}
