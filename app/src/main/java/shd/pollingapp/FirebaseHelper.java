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
    private DatabaseReference questionsCountDatabase;

    protected FirebaseHelper () {
        this.userDatabase = FirebaseDatabase.getInstance().getReference("/users");
        this.questionsDatabase = FirebaseDatabase.getInstance().getReference("/questions");
        this.questionsCountDatabase = FirebaseDatabase.getInstance().getReference("/count");
    }

    protected void addUser (User user) {
        HashMap<String, String> userObj = new HashMap<String, String>();
        userObj.put("password", user.getPassword());
        userObj.put("role", user.role);
        this.userDatabase.child(user.username).setValue(userObj);
    }

    protected void addQuestions (Questions questions) {
        String key = this.questionsDatabase.push().getKey();
        this.questionsDatabase.child(key).setValue(questions.questions);
    }

    protected DatabaseReference getUserDatabaseReference() {
        return this.userDatabase;
    }

    protected DatabaseReference getQuestionsDatabaseReference () { return this.questionsDatabase; }
}
