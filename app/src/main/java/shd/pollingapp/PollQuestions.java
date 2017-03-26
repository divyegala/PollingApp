package shd.pollingapp;

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

public class PollQuestions extends AppCompatActivity implements AdapterView.OnItemClickListener {


    ArrayList<String> questionList ;
    ArrayList<String> answerList ;

    FirebaseHelper firebaseHelper;
    ListView listView;
    QuestionCustomAdapter adapter;
    RadioGroup rg;
    RadioButton radioAnswerButton;
    static Questions question_current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_questions);
        Intent intent = getIntent();
        final Questions currentQuestion = (Questions)intent.getSerializableExtra("Question");
        question_current=currentQuestion;
        System.out.print("Key :"+currentQuestion.key);
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
                    for(int i=0;i<currentQuestion.questions.size();i++)
                    questionList.add(currentQuestion.questions.get(i));
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
                View view;
                int count=0;
                for(int i=0;i<adapter.getCount();i++) {

                    view = getViewByPosition(i, listView);
                    //System.out.println(view);
                    rg = (RadioGroup) view.findViewById(R.id.rg_list);
                    //System.out.println(rg);
                    int selectedId = rg.getCheckedRadioButtonId();
                    radioAnswerButton = (RadioButton) view.findViewById(selectedId);
                    //System.out.println(selectedId);
                    TextView textView = (TextView) view.findViewById(R.id.tv_li_question_item);
                    //System.out.println(textView);
                    //System.out.println(textView.getText());
                    //Text of that id
                    if (selectedId == R.id.rb_yes) {
                        System.out.println("Yes");
                        question_current.answers.add(i, "Yes");
                        count++;

                    } else if (selectedId == R.id.rb_no)  {
                        System.out.println("No");
                        question_current.answers.add(i, "No");
                        count++;
                    }
                }
                if(count<adapter.getCount()){
                    Toast.makeText(PollQuestions.this, "Please answer all questions!", Toast.LENGTH_SHORT).show();
                }
                else{
                    question_current.completeStatus=true;
                    Toast.makeText(PollQuestions.this, "Thank you for participating!", Toast.LENGTH_SHORT).show();
                    User user = new User("divyegala", "d", "EMP");
                    firebaseHelper.addUserAnswers(user, question_current);
                    startActivity(goback);
                }

                }


        });


    }


    public View getViewByPosition(int pos,ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
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
