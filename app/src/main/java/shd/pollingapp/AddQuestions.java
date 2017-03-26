package shd.pollingapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AddQuestions extends AppCompatActivity implements AdapterView.OnItemClickListener {


    ArrayList<String> questionList ;
    ArrayList<String> answerList ;
    Questions newQuestion = new Questions(questionList);
    FirebaseHelper firebaseHelper;
    ListView listView;
    QuestionCustomAdapter adapter;
    static Questions question_current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_questions);
        Intent intent = getIntent();
        if(intent.hasExtra("name")){
            String name=intent.getStringExtra("name");
            newQuestion.name=name;
        }
        if(intent.hasExtra("question")){
            String newquestion=intent.getStringExtra("question");
            newQuestion.questions.add(newquestion);
        }

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
        questionList = new ArrayList<>();
        ValueEventListener getQuestions = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> temp = new HashMap<>();
                temp = (HashMap<String, Object>) dataSnapshot.getValue();
                //for(Map.Entry<String, Object> entry : temp.entrySet()) {
                //questions.add(Questions(entry.getKey(), entry.getValue()));
                for(int i=0;i<newQuestion.questions.size();i++)
                    questionList.add(newQuestion.questions.get(i));
                System.out.println(questionList.get(0));
                //}

                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebaseHelper.getQuestionsDatabaseReference().addValueEventListener(getQuestions);
        final Intent goback=new Intent(this, EmployeePoll.class);
        final Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                DialogFragment dialogFragment=new DialogFrag();
                dialogFragment.show(getFragmentManager(),"Dialog");


            }


        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "YES", Toast.LENGTH_SHORT).show();
        System.out.print("YESSSYS");

    }

    // @Override
   /* public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        RadioGroup rg= (RadioGroup) findViewById(R.id.rg_list);
        Toast.makeText(PollQuestions.this, "YES", Toast.LENGTH_SHORT).show();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_yes:
                        // do operations specific to this selection

                        question_current.answers.add(position,"Y");
                        break;
                    case R.id.rb_no:
                        // do operations specific to this selection
                        question_current.answers.add(position,"N");
                        break;
                }
            }

        });
        System.out.println("Answers"+question_current.answers);

    }*/


    class QuestionCustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return questionList.size();
        }

        @Override
        public Object getItem(int position) {
            return questionList.get(position);
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
                convertView = layoutInflater.inflate(R.layout.poll_question_item,null);
            }
            TextView questionView = (TextView) convertView.findViewById(R.id.tv_li_question_item);
            String question = questionList.get(position);
            questionView.setText(question);

            return convertView;
        }
    }

}
