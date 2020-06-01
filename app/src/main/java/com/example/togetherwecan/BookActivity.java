package com.example.togetherwecan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import com.timqi.sectorprogressview.SectorProgressView;

public class BookActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    private SectorProgressView spv;
    TextView bname,bsumm;
    Button startQuiz,viewTopics;
    public static int book_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        context=this;

        Toolbar toolbar=findViewById(R.id.my_tool_bar_ba);
        setSupportActionBar(toolbar);




        book_id=getIntent().getIntExtra("id",-1);
        bname=findViewById(R.id.vtopic_name);
        bname.setText(getIntent().getStringExtra("name"));
        bsumm=findViewById(R.id.vtopic_summary);
        bsumm.setText(getIntent().getStringExtra("summary"));

        FrameLayout frameLayout=findViewById(R.id.frame_accuracy);
        spv=frameLayout.findViewById(R.id.spv);



        viewTopics=findViewById(R.id.vb_view_subtopics_btn);
        viewTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ViewTopicsActivity.class);
                intent.putExtra("id",book_id);
                intent.putExtra("name",bname.getText().toString());
                startActivity(intent);
            }
        });

        startQuiz=findViewById(R.id.vtopics_attemp_quiz_btn);
        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(getApplicationContext(),QuizActivity.class);
                intent1.putExtra("name",bname.getText().toString());
                intent1.putExtra("mode","book");
                startActivity(intent1);
            }
        });



    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_book,menu);
      return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.view_nots_b:
                //TODO go to view notes activity

                return true;
            case R.id.view_substops_b:
                //TODO go to subtopics activity
                Intent intent=new Intent(getApplicationContext(),ViewSubtopicsActivity.class);
                intent.putExtra("mode","book");
                intent.putExtra("name",bname.getText().toString());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView=findViewById(R.id.book_percent);
        textView.setText(getPercentage());
    }

    String getPercentage(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(ContractSchema.QUIZ_PERCENT.SHAREDPREFS,MODE_PRIVATE);
        float anInt = sharedPreferences.getFloat(ContractSchema.QUIZ_PERCENT.PERCENTAGE, 0);
        spv.setPercent(anInt);


        return "Your last quiz score is \n "+ anInt;
    }

    static void setPercentage(float percent){
        SharedPreferences sharedPreferences=context.getSharedPreferences(ContractSchema.QUIZ_PERCENT.SHAREDPREFS,MODE_PRIVATE);
        sharedPreferences.edit().putFloat(ContractSchema.QUIZ_PERCENT.PERCENTAGE,percent).apply();

    }

}
