package com.example.module_appbarlayout.item;

import java.util.List;

public class Item {
    public String name;

    public List<SubItem> mSubItems;

    public static class SubItem {
        public String name;
        public String desc;

        public SubItem(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }
    }
}
