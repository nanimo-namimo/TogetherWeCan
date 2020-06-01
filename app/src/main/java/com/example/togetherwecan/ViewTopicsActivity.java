package com.example.togetherwecan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
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

public class ViewTopicsActivity extends AppCompatActivity implements CommonFunctions {
    //TODO link recyclerView,menu and on_clicks
    RecyclerView recyclerView;
    Dialog dialog;
    public static ArrayList<SubTopicINFO> topics;
    public  static int book_id;
    private String bookName;
    public static boolean wasAltered;
    Adapters.TopicsAdapter topicsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_topics);

        Adapters.TopicsAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.medium);

        Toolbar toolbar=findViewById(R.id.vtt_topics_toolbar);
        setSupportActionBar(toolbar);

        book_id=getIntent().getIntExtra("id",-1);
        bookName="Topics of "+getIntent().getStringExtra("name");

        TextView bName=findViewById(R.id.my_books_txt_v);
        bName.setText(bookName);

        recyclerView=findViewById(R.id.vtopics_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchThread(book_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.topics_menu,menu);
        MenuItem item=menu.findItem(R.id.search_topics);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                topicsAdapter.getFilter().filter(s);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_topic:

                dialog=new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
                dialog.setContentView(R.layout.add_topic_layout);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                final EditText note_name=dialog.findViewById(R.id.at_name);
                final EditText note_sum=dialog.findViewById(R.id.at_sum);
                final Button add_book=dialog.findViewById(R.id.at_add_topic_btn);

                add_book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(note_name.getText().toString().isEmpty() || note_sum.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(),"fill all credentials",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String[] args={note_name.getText().toString(),note_sum.getText().toString(),Integer.toString(book_id)};
                        add(args);
                    }
                });

                Button close=dialog.findViewById(R.id.ad_close_btn);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                dialog.show();

                return true;
            case R.id.topic_s_font1:
                Adapters.TopicsAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.small);
                topicsAdapter= new Adapters.TopicsAdapter(this);
                recyclerView.setAdapter(topicsAdapter);
                return true;
            case R.id.topic_s_font2:
                Adapters.TopicsAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.medium);
                topicsAdapter= new Adapters.TopicsAdapter(this);
                recyclerView.setAdapter(topicsAdapter);
                return true;
            case R.id.topic_s_font3:
                Adapters.TopicsAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.large);
                topicsAdapter= new Adapters.TopicsAdapter(this);
                recyclerView.setAdapter(topicsAdapter);
                return true;
            case R.id.topic_s_font4:
                Adapters.TopicsAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.extra_large);
                topicsAdapter= new Adapters.TopicsAdapter(this);
                recyclerView.setAdapter(topicsAdapter);
                return true;
            case R.id.topic_s_font5:
                Adapters.TopicsAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.super_large);
                topicsAdapter= new Adapters.TopicsAdapter(this);
                recyclerView.setAdapter(topicsAdapter);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(wasAltered) {
            //TODO create new adapter and add it to recyclerview
            wasAltered=false;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchThread(final int book_id) {
        new AsyncTask<Void,Void,Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {
                String[] args={Integer.toString(book_id)};
                fetch(args);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                topicsAdapter=new Adapters.TopicsAdapter(getApplicationContext());
                recyclerView.setAdapter(topicsAdapter);
            }
        }.execute();
    }

    @Override
    public void add(String[] args) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        if (dbHelper.add("topic", args)) {
            Toast.makeText(this, "Topic added", Toast.LENGTH_SHORT).show();
            fetchThread(book_id);
        }
        else
            Toast.makeText(this,"Topic not added",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fetch(String[] args) {
        DBHelper dbHelper=new DBHelper(this);
        Cursor cursor=dbHelper.fetch("topic",args,null);
        topics=new ArrayList<>();
        if(cursor.moveToNext()){
            do {
                topics.add(new SubTopicINFO(cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
            }while (cursor.moveToNext());
        }
    }
}
