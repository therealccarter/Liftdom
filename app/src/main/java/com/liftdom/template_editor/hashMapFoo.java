package com.liftdom.template_editor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chris on 10/6/2016.
 */

public class HashMapFoo {
    private HashMap<String, ArrayList> hashMap;

    public HashMap getHashMap(){
        return hashMap;
    }

    public void setHashMap(HashMap<String, ArrayList> hashMap){
        this.hashMap = hashMap;
    }
}
