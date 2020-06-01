package com.example.togetherwecan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.togetherwecan.QuizActivity.titles;

public class FeedBackActivity extends AppCompatActivity {

    RecyclerView recyclerView1;
    Button close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back_layout);
        TextView my_score=findViewById(R.id.feedback_my_score);
        float score=getIntent().getFloatExtra("score",0);
        if(score>50){
            my_score.setTextColor(Color.GREEN);
        }
        String text="your score is "+score+"%";
        my_score.setText(text);
        RecyclerView recyclerView1=findViewById(R.id.review_recycler);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView1.setAdapter(new Adapters.QuizAdapter(getApplicationContext(),"do review"));
        close=findViewById(R.id.finish_review);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(QuizActivity.Title title:titles){
                    title.reset_ans();
                }
                finish();
            }
        });
    }
}
