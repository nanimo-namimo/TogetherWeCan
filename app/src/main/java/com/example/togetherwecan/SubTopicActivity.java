package com.example.togetherwecan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.timqi.sectorprogressview.SectorProgressView;

public class SubTopicActivity extends AppCompatActivity implements View.OnClickListener {
    public static int subtopicID; //to be used for database operations
    private String subtopicNAME,subtopicSUMMARY;
    Button v_notes,begin_quiz;
    TextView subtopic_name,subtopic_summary;
    @SuppressLint("StaticFieldLeak")
    static Context context;

    private SectorProgressView spv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtopic);

        context=this;

        Toolbar toolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        FrameLayout frameLayout=findViewById(R.id.frame_accuracy);
        spv=frameLayout.findViewById(R.id.spv);

        subtopicID=getIntent().getIntExtra("id",-1);
        subtopicNAME=getIntent().getStringExtra("name");
        subtopicSUMMARY=getIntent().getStringExtra("summary");

        v_notes=findViewById(R.id.vb_view_subtopics_btn);
        v_notes.setOnClickListener(this);
        begin_quiz=findViewById(R.id.vtopics_attemp_quiz_btn);
        begin_quiz.setOnClickListener(this);

        subtopic_name=findViewById(R.id.vtopic_name);
        subtopic_summary=findViewById(R.id.vtopic_summary);
        subtopic_name.setText(subtopicNAME);
        subtopic_summary.setText(subtopicSUMMARY);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.vb_view_subtopics_btn:
                //startActivity(new Intent(this,NoteActivity.class));
                intent=new Intent(this,NoteActivity.class);
                intent.putExtra("name",subtopicNAME);
                intent.putExtra("summary",subtopicSUMMARY);
                intent.putExtra("id",subtopicID);
                startActivity(intent);
                break;
            case R.id.vtopics_attemp_quiz_btn:
                intent=new Intent(this,QuizActivity.class);
                intent.putExtra("mode","subtopic");
                intent.putExtra("id",subtopicID);
                intent.putExtra("name",subtopicNAME);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView=findViewById(R.id.book_percent);
        textView.setText(getPercentage());
    }

    String getPercentage(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(ContractSchema.QUIZ_PERCENT.SHAREDPREFS_SUBTOPIC,MODE_PRIVATE);
        float anInt = sharedPreferences.getFloat(ContractSchema.QUIZ_PERCENT.PERCENTAGE_SUBTOPIC, 0);
        spv.setPercent(anInt);


        return "Your last quiz score is \n "+ anInt;
    }

    static void setPercentage(float percent){
        SharedPreferences sharedPreferences=context.getSharedPreferences(ContractSchema.QUIZ_PERCENT.SHAREDPREFS_SUBTOPIC,MODE_PRIVATE);
        sharedPreferences.edit().putFloat(ContractSchema.QUIZ_PERCENT.PERCENTAGE_SUBTOPIC,percent).apply();

    }

}
