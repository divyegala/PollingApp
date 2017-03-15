package shd.pollingapp;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Divye10 on 09-03-2017.
 */

public class Questions {
    protected String key;
    protected ArrayList<String> questions = new ArrayList<>();
    protected ArrayList<String> answers = new ArrayList<>();
    protected User user;

    protected Questions (ArrayList<String> questions) {
        this.questions = questions;
    }
}
