package shd.pollingapp;

import android.content.Intent;
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
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button addUserButton;
    private FirebaseHelper firebaseHelper;
    protected static User user;
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

        ShimmerTextView myShimmerTextView= (ShimmerTextView) findViewById(R.id.shimmer_tv);
        myShimmerTextView.setTypeface(EasyFonts.robotoThin(this));
        Shimmer shimmer = new Shimmer();
        shimmer.start(myShimmerTextView);
        //HOW TO AUTHENTICATE USER ON BUTTON CLICK--------------------
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User(null, null, null);
                final String user_name=username.getText().toString();
                final String pass_word=password.getText().toString();

                ValueEventListener getPasswordAndRole = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> temp = (HashMap<String, Object>) dataSnapshot.child(user_name).getValue();
                        if (!temp.get("password").equals(pass_word)) {
                            user.setAuthenticationStatus(0);
                        }
                        else if (temp.get("password").equals(pass_word) && temp.get("role").equals("HR")) {
                            user.username = user_name;
                            user.setAuthenticationStatus(1);
                            user.role = "HR";
                            Intent intent=new Intent(getApplicationContext(),HrPoll.class);
                            startActivity(intent);
                        }
                        else {
                            user.username = user_name;
//                            System.out.println
                            user.setAuthenticationStatus(2);
                            user.role = "EMP";
                            Intent intent=new Intent(getApplicationContext(),EmployeePoll.class);
                            startActivity(intent);
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


    }
}
