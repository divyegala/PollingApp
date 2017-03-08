package shd.pollingapp;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Divye10 on 09-03-2017.
 */

public class FirebaseHelper {
    private DatabaseReference userDatabase;
    private DatabaseReference questionsDatabase;

    protected FirebaseHelper () {
        this.userDatabase = FirebaseDatabase.getInstance().getReference("/users");
        this.questionsDatabase = FirebaseDatabase.getInstance().getReference("/questions");
    }

    protected void addUser (User user) {
        HashMap<String, String> userObj = new HashMap<String, String>();
        userObj.put("password", user.getPassword());
        userObj.put("role", user.role);
        this.userDatabase.child(user.username).setValue(userObj);
    }

    protected DatabaseReference getUserDatabaseReference() {
        return this.userDatabase;
    }
}
