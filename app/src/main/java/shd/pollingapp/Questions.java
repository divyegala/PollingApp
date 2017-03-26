package shd.pollingapp;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Divye10 on 09-03-2017.
 */

public class Questions implements Serializable{


    protected String key;
    protected String name;
    protected ArrayList<String> questions = new ArrayList<>();
    protected ArrayList<String> answers = new ArrayList<>();
    protected User user;
    protected boolean completeStatus;
    protected Questions (ArrayList<String> questions) {
        this.questions = questions;
    }
}
