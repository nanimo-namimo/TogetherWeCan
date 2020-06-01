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
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.WindowManager.LayoutParams.FLAG_BLUR_BEHIND;

public class ViewBooksActivity extends AppCompatActivity implements CommonFunctions {
    RecyclerView recyclerView;
    public static boolean wasAltered;
    public static Adapters.BooksAdapter booksAdapter;
    public static ArrayList<SubTopicINFO>books;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);

        Adapters.BooksAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.medium);

        Toolbar toolbar=findViewById(R.id.vtt_topics_toolbar);
        setSupportActionBar(toolbar);

        recyclerView=findViewById(R.id.vtopics_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchThread();

    }

    @SuppressLint("StaticFieldLeak")
    private void fetchThread() {
        new AsyncTask<Void,Void,Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {
                String[] args=null;
                fetch(args);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                booksAdapter= new Adapters.BooksAdapter(getApplicationContext());
                recyclerView.setAdapter(booksAdapter);
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.books_menu,menu);
        MenuItem item=menu.findItem(R.id.search_book);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //notesAdapter.getFilter().filter(s);
                booksAdapter.getFilter().filter(s);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_book:

                dialog=new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
                dialog.setContentView(R.layout.add_book_layout);
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
                        String[] args={note_name.getText().toString(),note_sum.getText().toString()};
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
            case R.id.book_s_font1:
                //TODO small font
                Adapters.BooksAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.small);
               booksAdapter=new Adapters.BooksAdapter(this);
                recyclerView.setAdapter(booksAdapter);
                return true;
            case R.id.book_s_font2:
                Adapters.BooksAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.medium);
                booksAdapter=new Adapters.BooksAdapter(this);
                recyclerView.setAdapter(booksAdapter);
                return true;
            case R.id.book_s_font3:
                Adapters.BooksAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.large);
                booksAdapter=new Adapters.BooksAdapter(this);
                recyclerView.setAdapter(booksAdapter);
                return true;
            case R.id.book_s_font4:
                Adapters.BooksAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.extra_large);
                booksAdapter=new Adapters.BooksAdapter(this);
                recyclerView.setAdapter(booksAdapter);
                return true;
            case R.id.book_s_font5:
                Adapters.BooksAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.super_large);
                booksAdapter=new Adapters.BooksAdapter(this);
                recyclerView.setAdapter(booksAdapter);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void add(String[] args) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        if (dbHelper.add("book", args)) {
            Toast.makeText(this, "book added", Toast.LENGTH_SHORT).show();
            fetchThread();
        }
        else
            Toast.makeText(this,"book not added",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fetch(String[] args) {
        DBHelper dbHelper=new DBHelper(this);
        Cursor cursor=dbHelper.fetch("book",null,null);
        books=new ArrayList<>();
        if(cursor.moveToNext()){
            do {
                books.add(new SubTopicINFO(cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
            }while (cursor.moveToNext());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(wasAltered) {
            booksAdapter = new Adapters.BooksAdapter(this);
            recyclerView.setAdapter(booksAdapter);
            wasAltered=false;
        }
    }
}
