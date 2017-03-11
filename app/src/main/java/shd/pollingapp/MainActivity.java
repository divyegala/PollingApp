package shd.pollingapp;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button addUserButton;
    private FirebaseHelper firebaseHelper;
    protected User user;
    protected ArrayList<Questions> questions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseHelper = new FirebaseHelper();
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        addUserButton = (Button)findViewById(R.id.add_user);
        user = new User(null, null, null);

        /*
        HOW TO AUTHENTICATE USER ON BUTTON CLICK--------------------
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User(null, null, null);
                ValueEventListener getPasswordAndRole = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> temp = (HashMap<String, Object>) dataSnapshot.child("sidbag").getValue();
                        if (!temp.get("password").equals("sid")) {
                            user.setAuthenticationStatus(0);
                        } else if (temp.get("password").equals("sid") && temp.get("role").equals("HR")) {
                            user.username = "sidbag";
                            user.setAuthenticationStatus(1);
                            user.role = "HR";
                        } else {
                            user.username = "sidbag";
                            user.setAuthenticationStatus(2);
                            user.role = "EMP";
                            System.out.println("3");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("MainApp", "Get Password Failed", databaseError.toException());
                        user.setAuthenticationStatus(0);
                    }
                };
                firebaseHelper.getUserDatabaseReference().addListenerForSingleValueEvent(getPasswordAndRole);
            }
        });
        */

        /*
        HOW TO ADD QUESTIONS-------------------
        ArrayList<String> temp = new ArrayList<>();
        temp.add("yo?");
        temp.add("yay?");
        temp.add("works?");
        questions = new Questions(temp);
        firebaseHelper.addQuestions(questions);
        */
        /*
        HOW TO DYNAMICALLY RETRIEVE QUESTIONS-----------------------------
        questions = new ArrayList<>();
        ValueEventListener getQuestions = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> temp = new HashMap<>();
                temp = (HashMap<String, Object>) dataSnapshot.getValue();
                for(Map.Entry<String, Object> entry : temp.entrySet()) {
                    //questions.add(Questions(entry.getKey(), entry.getValue()));
                    Questions q = new Questions(entry.getKey(),(ArrayList<String>) entry.getValue());
                    questions.add(q);
                    System.out.println(q.key + q.questions.get(0));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebaseHelper.getQuestionsDatabaseReference().addValueEventListener(getQuestions);
        */
    }
}
