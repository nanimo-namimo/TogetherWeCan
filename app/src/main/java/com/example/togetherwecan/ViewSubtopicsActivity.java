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

import static android.view.WindowManager.LayoutParams.FLAG_BLUR_BEHIND;

public class ViewSubtopicsActivity extends AppCompatActivity implements CommonFunctions{

    String mode;
    public static boolean wasAltered;
    int topic_id,book_id;   //to be used for database purposes
    RecyclerView recyclerView;
    public static ArrayList<SubTopicINFO> subtopics;
    public static Adapters.SubTopicsAdapter subTopicsAdapter;
    Dialog dialog;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subtopics);


        Adapters.SubTopicsAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.medium);
        topic_id=TopicActivity.topic_id;
        book_id=TopicActivity.book_id;

        recyclerView=findViewById(R.id.vtopics_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar=findViewById(R.id.vtt_topics_toolbar);
        setSupportActionBar(toolbar);

        final TextView topic_name=findViewById(R.id.my_books_txt_v);
        String s="Subtopics of "+getIntent().getStringExtra("name");
        topic_name.setText(s);

        mode= getIntent().getStringExtra("mode");

        fetchThread(topic_id);

    }

    @SuppressLint("StaticFieldLeak")
    private void fetchThread(final int topic_id) {
        new AsyncTask<Void,Void,Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {
                String[] args={Integer.toString(topic_id)};
                fetch(args);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                subTopicsAdapter=new Adapters.SubTopicsAdapter(getApplicationContext());
                recyclerView.setAdapter(subTopicsAdapter);
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.subtopic_menu,menu);
        MenuItem item=menu.findItem(R.id.search_subtopics);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //notesAdapter.getFilter().filter(s);
                subTopicsAdapter.getFilter().filter(s);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_subtopic:

                dialog=new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
                dialog.setContentView(R.layout.add_subtopic_layout);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                final EditText note_name=dialog.findViewById(R.id.ast_name);
                final EditText note_sum=dialog.findViewById(R.id.ast_sum);
                final Button add_note=dialog.findViewById(R.id.ast_add_subtopic);

                add_note.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(note_name.getText().toString().isEmpty() || note_sum.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(),"fill all credentials",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String[] args={note_name.getText().toString(),note_sum.getText().toString(),Integer.toString(topic_id),Integer.toString(book_id)};
                        add(args);
                    }
                });

                Button close=dialog.findViewById(R.id.ast_close);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                dialog.show();

                return true;
            case R.id.subtopic_s_font1:
                //TODO small font
                Adapters.SubTopicsAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.small);
                subTopicsAdapter=new Adapters.SubTopicsAdapter(this);
                recyclerView.setAdapter(subTopicsAdapter);
                return true;
            case R.id.subtopic_s_font2:
                Adapters.SubTopicsAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.medium);
                subTopicsAdapter=new Adapters.SubTopicsAdapter(this);
                recyclerView.setAdapter(subTopicsAdapter);
                return true;
            case R.id.subtopic_s_font3:
                Adapters.SubTopicsAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.large);
                subTopicsAdapter=new Adapters.SubTopicsAdapter(this);
                recyclerView.setAdapter(subTopicsAdapter);
                return true;
            case R.id.subtopic_s_font4:
                Adapters.SubTopicsAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.extra_large);
                subTopicsAdapter=new Adapters.SubTopicsAdapter(this);
                recyclerView.setAdapter(subTopicsAdapter);
                return true;
            case R.id.subtopic_s_font5:
                Adapters.SubTopicsAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.super_large);
                subTopicsAdapter=new Adapters.SubTopicsAdapter(this);
                recyclerView.setAdapter(subTopicsAdapter);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void add(String[] args) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        if (dbHelper.add("subtopic", args)) {
            Toast.makeText(this, "Subtopic added", Toast.LENGTH_SHORT).show();
            fetchThread(topic_id);
        }
        else
            Toast.makeText(this,"Subtopic not added",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fetch(String[] args) {
        DBHelper dbHelper=new DBHelper(this);
        Cursor cursor=dbHelper.fetch("subtopic",args,mode);
        subtopics=new ArrayList<>();
        if(cursor.moveToNext()){
            do {
                subtopics.add(new SubTopicINFO(cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
            }while (cursor.moveToNext());
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(wasAltered) {
            subTopicsAdapter = new Adapters.SubTopicsAdapter(this);
            recyclerView.setAdapter(subTopicsAdapter);
            wasAltered=false;
        }
    }
}
