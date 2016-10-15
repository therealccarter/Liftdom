package com.liftdom.template_editor;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by Chris on 10/5/2016.
 */

public class MasterListTemplateClass {

    public String templateName;
    public List<String> testList;

    public MasterListTemplateClass() {
    }

    public MasterListTemplateClass(String templateName, List<String> testList) {
        this.templateName = templateName;
        this.testList = testList;
    }

    public String getTemplateName() {
        return templateName;
    }

    public LinkedList testList() {
        return testList();
    }

}
