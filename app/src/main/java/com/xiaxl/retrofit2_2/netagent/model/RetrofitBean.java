package com.xiaxl.retrofit2_2.netagent.model;

import java.util.ArrayList;
import java.util.List;

public class RetrofitBean {

    public Integer total_count;
    public Boolean incompleteResults;
    public List<Item> items = new ArrayList<Item>();

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("total_count: ");
        sb.append(total_count);
        sb.append(" incompleteResults: ");
        sb.append(incompleteResults);
        sb.append(" items: ");
        sb.append(items.toString());
        return sb.toString();
    }

    public static class Item {

        public String name;
        public String full_name;
        public String description;


        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("name: ");
            sb.append(name);
            sb.append(" full_name: ");
            sb.append(full_name);
            sb.append(" description: ");
            sb.append(description);
            return sb.toString();
        }
    }

}
