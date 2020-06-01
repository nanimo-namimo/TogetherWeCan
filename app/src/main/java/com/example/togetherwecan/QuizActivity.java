package com.example.togetherwecan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static android.view.WindowManager.LayoutParams.FLAG_BLUR_BEHIND;

public class QuizActivity extends AppCompatActivity {
    private String mode;
    private boolean isFirstTime;
    static Adapters.QuizAdapter quizAdapter;
    static RecyclerView  recyclerView;
    static ArrayList<Title>titles;
    static ArrayList<Title>fixedTitles;
    static ArrayList<Title>titles222;
    static ArrayList<Description>descriptions;
    ArrayList<Description>descriptions222;
    ArrayList<Description>fixedDescriptions;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar=findViewById(R.id.toolbar_quiz);
        setSupportActionBar(toolbar);
        isFirstTime=true;
        mode=getIntent().getStringExtra("mode");
        submit=findViewById(R.id.quiz_submit);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),FeedBackActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                float results=getResults();
                switch (mode){
                    case "book":
                        BookActivity.setPercentage(results);
                        break;
                    case "topic":
                        TopicActivity.setPercentage(results);
                        break;
                    case "subtopic":
                        SubTopicActivity.setPercentage(results);
                        break;
                }
                intent.putExtra("score",getResults());
                startActivity(intent);
            }

        });
        recyclerView=findViewById(R.id.quiz_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        TextView textView=findViewById(R.id.quiz_on);
        textView.setText(String.format("Quiz on %s", getIntent().getStringExtra("name")));

        Adapters.QuizAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.medium);
        fetchThread(mode);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.quiz_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.quiz_s_font1:
                //TODO small font
                Adapters.QuizAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.small);
                quizAdapter=new Adapters.QuizAdapter(this,"do quiz");
                recyclerView.setAdapter(quizAdapter);
                //fetch(null);
                return true;
            case R.id.quiz_s_font2:
                //TODO medium font
                Adapters.QuizAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.medium);
                quizAdapter=new Adapters.QuizAdapter(this,"do quiz");
                recyclerView.setAdapter(quizAdapter);
               // fetch(null);
                return true;
            case R.id.quiz_s_font3:
                //TODO large font
                Adapters.QuizAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.large);
                quizAdapter=new Adapters.QuizAdapter(this,"do quiz");
                recyclerView.setAdapter(quizAdapter);
                //fetch(null);
                return true;
            case R.id.quiz_s_font4:
                //TODO extra large font
                Adapters.QuizAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.extra_large);
                quizAdapter=new Adapters.QuizAdapter(this,"do quiz");
                recyclerView.setAdapter(quizAdapter);
                //fetch(null);
                return true;
            case R.id.quiz_s_font5:
                //TODO extra large font
                Adapters.QuizAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.super_large);
                quizAdapter=new Adapters.QuizAdapter(this,"do quiz");
                recyclerView.setAdapter(quizAdapter);
                //fetch(null);
                return true;

            default:
                //TODO cases for others later
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("StaticFieldLeak")
    private void fetchThread(final String mode) {
        new AsyncTask<Void,Void,Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {
                switch (mode){
                    case "subtopic":
                        fetchNotes(SubTopicActivity.subtopicID);
                        break;
                    case "topic":
                        fetchNotes(TopicActivity.topic_id);
                        break;
                    case "book":
                        fetchNotes(BookActivity.book_id);
                        break;
                }

                descriptions222 = new ArrayList<>(fixedDescriptions);
                descriptions=getRandomizedDescriptions();
                titles=getRandomizedTitles();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                titles222=new ArrayList<>(fixedTitles);
                titles=getRandomizedTitles();
                quizAdapter=new Adapters.QuizAdapter(getApplicationContext(),"do quiz");
                recyclerView.setAdapter(quizAdapter);
            }
        }.execute();
    }


    private void fetchNotes(int id){
        //args contains mode like subtopic ,topic or book
                titles222=new ArrayList<>();
                descriptions222=new ArrayList<>();
                descriptions=new ArrayList<>();
                fixedTitles=new ArrayList<>();
                fixedDescriptions=new ArrayList<>();
                String[]args={String.valueOf(id)};
                Cursor cursor=new DBHelper(this).fetch("note",args,mode);
                int i=1;

                if(cursor.moveToFirst()){
                    do {
                        String title = cursor.getString(1);
                        String description = cursor.getString(2);
                        Title title1=new Title(title);
                        titles222.add(title1);
                        fixedTitles.add(title1);
                        Description description1=new Description(description,i);
                        descriptions222.add(description1);
                        fixedDescriptions.add(description1);

                        i++;
                    }while (cursor.moveToNext());
                }


    }

    private ArrayList<Description> getRandomizedDescriptions(){
        ArrayList<Description>descri=new ArrayList<>();
        for(int i=0;i<fixedTitles.size();i++){
            Description description=getRandomDescription();
            int len=descri.size();
            fixedTitles.get(description.getNumber()-1).setC_ans(len+1);
            descri.add(description);
        }
        return descri;
    }

    private Description getRandomDescription(){
        if (descriptions222.size()==1) {
            fixedTitles.get(descriptions222.get(0).number - 1).setC_ans(descriptions222.get(0).number);
            return descriptions222.get(0);
        }
        Random random=new Random(System.currentTimeMillis());
        int index=random.nextInt(descriptions222.size());
        return descriptions222.remove(index);
    }
    private ArrayList<Title> getRandomizedTitles(){
            ArrayList<Title>descri=new ArrayList<>();
            for(int i=0;i<fixedTitles.size();i++){
                Title title=getRandomTitle();
               descri.add(title);
            }
            return descri;
        }

        private Title getRandomTitle(){
            if (titles222.size()==1) {
                return titles222.get(0);
            }
            Random random=new Random(System.currentTimeMillis());
            int index=random.nextInt(titles222.size());
            return titles222.remove(index);
        }

    private float getResults(){
        float correct=0;
        for(Title title:titles){
            if(title.isCorrect())
                correct+=1;
        }
        if(titles.size()==0)
            return 100;
        return (correct*100)/titles.size();
    }

     class Title{
        private String name;
        private int c_ans;
        private int given_ans;

         public String getName() {
             return name;
         }

         public int getC_ans() {
             return c_ans;
         }

         public int getGiven_ans() {
             return given_ans;
         }

         public Title(String name) {
            this.name = name;
             this.given_ans=-1;
        }

         public void setC_ans(int c_ans) {
             this.c_ans = c_ans;
         }

         public void setGiven_ans(int given_ans) {
            this.given_ans = given_ans;
        }

        public void reset_ans(){
             this.setGiven_ans(-1);
        }

        public boolean isCorrect(){

            if(given_ans==-1)
                return false;
           return c_ans==given_ans;
        }
    }

     class Description{
       private String description;
       private int number;

        public Description(String description, int number) {
            this.description = description;
            this.number = number;
        }

         public String getDescription() {
             return description;
         }

         public int getNumber() {
             return number;
         }
     }

    @Override
    protected void onResume() {
        super.onResume();
        if(isFirstTime)
            return;
        Random random = new Random(System.currentTimeMillis());
        if (random.nextBoolean()) {
            titles222 = new ArrayList<>(fixedTitles);
            titles = getRandomizedTitles();
        } else {
            descriptions222 = new ArrayList<>(fixedDescriptions);
            descriptions = getRandomizedDescriptions();
        }
        quizAdapter = new Adapters.QuizAdapter(this, "do quiz");
        recyclerView.setAdapter(quizAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirstTime=false;
    }


}
