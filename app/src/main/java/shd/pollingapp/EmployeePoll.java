package shd.pollingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vstechlab.easyfonts.EasyFonts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmployeePoll extends AppCompatActivity implements AdapterView.OnItemClickListener {


    ArrayList<Questions> questions ;
    FirebaseHelper firebaseHelper;
    ListView listView;
    QuestionCustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_poll);

        adapter = new QuestionCustomAdapter();

        listView = (ListView) findViewById(R.id.mobile_list);
        listView.setOnItemClickListener(this);
        TextView tv= (TextView) findViewById(R.id.title_action);
        tv.setTypeface(EasyFonts.robotoThin(this));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

       /* int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView yourTextView = (TextView) findViewById(titleId);
        yourTextView.setTypeface(EasyFonts.robotoThin(this));*/

        /*
        HOW TO ADD QUESTIONS-------------------
        ArrayList<String> temp = new ArrayList<>();
        temp.add("yo?");
        temp.add("yay?");
        temp.add("works?");
        questions = new Questions(temp);
        firebaseHelper.addQuestions(questions);
        */

        //HOW TO DYNAMICALLY RETRIEVE QUESTIONS-----------------------------

        firebaseHelper= new FirebaseHelper();
        questions = new ArrayList<>();
        ValueEventListener getQuestions = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> temp = new HashMap<>();
                temp = (HashMap<String, Object>) dataSnapshot.getValue();
                for(Map.Entry<String, Object> entry : temp.entrySet()) {
                    HashMap<String, Object> temp1 = (HashMap<String, Object>) entry.getValue();
                    //questions.add(Questions(entry.getKey(), entry.getValue()));
                    Questions q = new Questions((ArrayList<String>) temp1.get("questions"));
                    q.key = entry.getKey();
                    q.name=temp1.get("name").toString();
                    if (!temp1.containsKey(MainActivity.user.username)) {
                        questions.add(q);
                        System.out.println(q.key + q.questions.get(0));
                    }
                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebaseHelper.getQuestionsDatabaseReference().addValueEventListener(getQuestions);


        /*
        ValueEventListener getAnsweredQuestions = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Questions> temp = questions;
                for (int i = 0; i < temp.size(); i++) {
                    String k = temp.get(i).key;
                    if (dataSnapshot.child(k).hasChild("divyegala")) {
                        System.out.println(k + "     hi");
                        for (int j = 0; j < questions.size(); j++) {
                            if (k.equals(questions.get(j).key)) {
                                questions.remove(j);
                                break;
                            }
                        }
                    }
                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebaseHelper.getAnswersDatabaseReference().addValueEventListener(getAnsweredQuestions);
        */
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, questions.get(position).key, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,PollQuestions.class);
        intent.putExtra("Question", (Serializable) questions.get(position));
        startActivity(intent);
     //   questions.remove(position);
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        // optional depending on your needs
    }


    class QuestionCustomAdapter extends BaseAdapter{

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int position) {
        return questions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater layoutInflater = getLayoutInflater();
            convertView = layoutInflater.inflate(R.layout.list_item_question,null);
        }
        TextView questionView = (TextView) convertView.findViewById(R.id.tv_li_question);
        Questions question = questions.get(position);
        questionView.setTypeface(EasyFonts.robotoMedium(getApplicationContext()));
        questionView.setText(question.name);

        return convertView;
    }
}


}
