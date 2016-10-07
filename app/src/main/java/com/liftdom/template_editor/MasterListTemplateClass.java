package com.liftdom.template_editor;

import java.util.ArrayList;

/**
 * Created by Chris on 10/5/2016.
 */

public class MasterListTemplateClass {
    private String templateName;
    private ArrayList<ArrayList> arrayList;

    public MasterListTemplateClass() {
    }

    public MasterListTemplateClass(String name, ArrayList<ArrayList> list) {
        this.templateName = name;
        this.arrayList = list;
    }

    public String getName() {
        return templateName;
    }

    public ArrayList<ArrayList> getList() {
        return arrayList;
    }
}
