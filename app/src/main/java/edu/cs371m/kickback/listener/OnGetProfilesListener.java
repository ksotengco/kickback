package edu.cs371m.kickback.listener;

import java.util.ArrayList;

import edu.cs371m.kickback.model.Profile;

public interface OnGetProfilesListener {
    void onGetProfiles(ArrayList<Profile> profiles);
}
