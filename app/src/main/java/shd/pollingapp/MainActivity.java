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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button addUserButton;
    private FirebaseHelper firebaseHelper;
    protected User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseHelper = new FirebaseHelper();
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        addUserButton = (Button)findViewById(R.id.add_user);
        user = new User(null, null, null);

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
    }
}
