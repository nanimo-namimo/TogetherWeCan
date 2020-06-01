package com.example.togetherwecan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.timqi.sectorprogressview.SectorProgressView;

public class TopicActivity extends AppCompatActivity implements View.OnClickListener {
    static  int topic_id,book_id;
    Button s_quiz,view_sub;
    String name_topic;
    @SuppressLint("StaticFieldLeak")
    static Context context;
    SectorProgressView spv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        context=this;
        FrameLayout frameLayout=findViewById(R.id.frame_accuracy_topic);
        spv=frameLayout.findViewById(R.id.spv_topic);

        Toolbar toolbar=findViewById(R.id.my_tool_bar_ba);
        setSupportActionBar(toolbar);

        TextView topicName=findViewById(R.id.vtopic_name);
        name_topic=getIntent().getStringExtra("name");
        topicName.setText(name_topic);
        TextView topicSummary=findViewById(R.id.vtopic_summary);
        String summ=getIntent().getStringExtra("summary");
        topicSummary.setText(summ);

        topic_id=getIntent().getIntExtra("id",-1); //TODO modify after creating view topics activity
        book_id=BookActivity.book_id;   //TODO modify after creating view topics activity

        s_quiz=findViewById(R.id.vtopics_attemp_quiz_btn);
        s_quiz.setOnClickListener(this);
        view_sub=findViewById(R.id.vb_view_subtopics_btn);
        view_sub.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.vtopics_attemp_quiz_btn:
                //TODO start quiz activity
                Intent intent1=new Intent(this,QuizActivity.class);
                intent1.putExtra("mode","topic");
                intent1.putExtra("name",name_topic);
                startActivity(intent1);
                break;
            case R.id.vb_view_subtopics_btn:
                //TODO start view subtopics activity
                Intent intent=new Intent(this,ViewSubtopicsActivity.class);
                intent.putExtra("topic_id",topic_id);
                intent.putExtra("book_id",book_id);
                intent.putExtra("name",name_topic);
                intent.putExtra("mode","null");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView=findViewById(R.id.topic_percent);
        textView.setText(getPercentage());
    }

    String getPercentage(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(ContractSchema.QUIZ_PERCENT.SHAREDPREFS_TOPIC,MODE_PRIVATE);
        float anInt = sharedPreferences.getFloat(ContractSchema.QUIZ_PERCENT.PERCENTAGE_TOPIC, 0);
        spv.setPercent(anInt);


        return "Your last quiz score is \n "+ anInt;
    }

    static void setPercentage(float percent){
        SharedPreferences sharedPreferences=context.getSharedPreferences(ContractSchema.QUIZ_PERCENT.SHAREDPREFS_TOPIC,MODE_PRIVATE);
        sharedPreferences.edit().putFloat(ContractSchema.QUIZ_PERCENT.PERCENTAGE_TOPIC,percent).apply();

    }
}
