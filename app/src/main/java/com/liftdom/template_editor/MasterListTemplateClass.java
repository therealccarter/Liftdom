package com.liftdom.template_editor;


import java.util.LinkedList;

/**
 * Created by Chris on 10/5/2016.
 */

public class MasterListTemplateClass {

    public String templateName;
    public LinkedList LinkedList;

    public MasterListTemplateClass() {
    }

    public MasterListTemplateClass(String templateName, LinkedList LinkedList) {
        this.templateName = templateName;
        this.LinkedList = LinkedList;
    }

    public String getTemplateName() {
        return templateName;
    }

    public LinkedList getLinkedList() {
        return LinkedList;
    }

}
