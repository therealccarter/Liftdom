package com.liftdom.user_profile;



public class ProfileTabController {

    // Singleton boilerplate
    private static ProfileTabController controller;
    static ProfileTabController getInstance() {
        if (controller == null) {
            controller = new ProfileTabController();
        }
        return controller;
    }

    public int exID = 0;

    public String exName;
}
