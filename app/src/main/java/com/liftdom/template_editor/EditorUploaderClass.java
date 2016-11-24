package com.liftdom.template_editor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Chris on 11/23/2016.
 */

public class EditorUploaderClass {

    // Singleton boilerplate
    private static EditorUploaderClass controller;
    static EditorUploaderClass getInstance() {
        if (controller == null) {
            controller = new EditorUploaderClass();
        }
        return controller;
    }

    private FirebaseAuth mAuth;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    DatabaseReference templatesRef = mRootRef.child("templates").child(uid);

    String templateName = "draft";

    int incrementorInt = 0;

    public void setSetSchemeValues(String setSchemeValue, String exName, String doWSelected){
        String incrementor = String.valueOf(incrementorInt);
        DatabaseReference specificTemplateRef = templatesRef.child(templateName);
        specificTemplateRef.child(doWSelected).child(exName).child(incrementor).setValue(setSchemeValue);
        incrementorInt++;
    }

    public void setTemplateName(String templateName){
        DatabaseReference specificTemplateRef = templatesRef.child("draft");
        specificTemplateRef.setValue(templateName);

    }
}
