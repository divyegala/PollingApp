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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HrPoll extends AppCompatActivity implements AdapterView.OnItemClickListener {


    ArrayList<Questions> questions;
   // ArrayList<String> questionList ;

    FirebaseHelper firebaseHelper;
    ListView listView;
    QuestionCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_add_poll);
        Intent intent = getIntent();

        adapter = new QuestionCustomAdapter();

        listView = (ListView) findViewById(R.id.mobile_list);
        listView.setOnItemClickListener(this);
        TextView tv= (TextView) findViewById(R.id.title_action);
        tv.setTypeface(EasyFonts.robotoThin(this));
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
        questions = new ArrayList<>();
        firebaseHelper= new FirebaseHelper();
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
        final Intent goback=new Intent(this, EmployeePoll.class);
        ImageButton imageButton= (ImageButton) findViewById(R.id.add_poll_action);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                DialogFragment dialogFragment=new DialogFrag();
                dialogFragment.show(getFragmentManager(),"Dialog");


            }


        });


    }

    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        // optional depending on your needs
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "YES", Toast.LENGTH_SHORT).show();
        System.out.print("YESSSYS");
        Intent intent=new Intent(this,DataGraph.class);
        intent.putExtra("key",questions.get(position).key);
        startActivity(intent);

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
