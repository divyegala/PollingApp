package shd.pollingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                    //questions.add(Questions(entry.getKey(), entry.getValue()));
                    Questions q = new Questions((ArrayList<String>) entry.getValue());
                    q.key = entry.getKey();
                    questions.add(q);
                    System.out.println(q.key + q.questions.get(0));
                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebaseHelper.getQuestionsDatabaseReference().addValueEventListener(getQuestions);




    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, questions.get(position).key, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,PollQuestions.class);
        intent.putExtra("Question", (Serializable) questions.get(position));
        startActivity(intent);
     //   questions.remove(position);
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
        questionView.setText(question.key);

        return convertView;
    }
}

}
