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

public class NoteActivity extends AppCompatActivity implements CommonFunctions {
    public static boolean wasAltered;
    public static int subtopic_id,topic_id,book_id;
    public static ArrayList<NoteINFO>notes;
    private Dialog dialog;
    public static RecyclerView notes_list;
    public static Adapters.NotesAdapter notesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_note);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //dialog=new Dialog(this);

        topic_id=TopicActivity.topic_id;
        book_id= BookActivity.book_id;

        notes_list=findViewById(R.id.vn_list_view);
        notes_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        String s="notes of "+getIntent().getStringExtra("name");
        TextView textView=findViewById(R.id.vn_subtopic_name);
        textView.setText(s);
        subtopic_id=getIntent().getIntExtra("id",-1);
        String[] args={Integer.toString(subtopic_id)};
        fetchThread(subtopic_id);
        Adapters.NotesAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.medium);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.note_menu,menu);
        MenuItem item=menu.findItem(R.id.search_notes);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
               notesAdapter.getFilter().filter(s);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_note:
                dialog=new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
                dialog.setContentView(R.layout.add_note_layout);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                final EditText note_name=dialog.findViewById(R.id.an_name);
                final EditText note_sum=dialog.findViewById(R.id.an_sum);
                final Button add_note=dialog.findViewById(R.id.an_add_note);

                add_note.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(note_name.getText().toString().isEmpty() || note_sum.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(),"fill all credentials",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String[] args={note_name.getText().toString(),note_sum.getText().toString(),String.valueOf(subtopic_id) ,
                                String.valueOf(topic_id), String.valueOf(book_id)};
                        add(args);
                    }
                });

                Button close=dialog.findViewById(R.id.an_close);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                dialog.show();
                return true;
            case R.id.search_notes:
                return true;
            case R.id.note_s_font1:
                //TODO small font
                    Adapters.NotesAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.small);
                    notesAdapter=new Adapters.NotesAdapter(this);
                    notes_list.setAdapter(notesAdapter);
                return true;
                case R.id.note_s_font2:
                    Adapters.NotesAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.medium);
                    notesAdapter=new Adapters.NotesAdapter(this);
                    notes_list.setAdapter(notesAdapter);
                    return true;
                case R.id.note_s_font3:
                    Adapters.NotesAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.large);
                    notesAdapter=new Adapters.NotesAdapter(this);
                    notes_list.setAdapter(notesAdapter);
                    return true;
                    case R.id.note_s_font4:
                        Adapters.NotesAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.extra_large);
                        notesAdapter=new Adapters.NotesAdapter(this);
                        notes_list.setAdapter(notesAdapter);
                    return true;
                    case R.id.note_s_font5:
                        Adapters.NotesAdapter.FONT_SIZE=(int) getResources().getDimension(R.dimen.super_large);
                        notesAdapter=new Adapters.NotesAdapter(this);
                        notes_list.setAdapter(notesAdapter);
                    return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void add(String args[]) {
        //TODO add note
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        if (dbHelper.add("note", args)) {
            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
            fetchThread(subtopic_id);
            //fetch(null);
        }
        else
            Toast.makeText(this,"note not added",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fetch(String args[]) {
        //TODO fetch notes
        Cursor cursor=new DBHelper(this).fetch("note",args,"subtopic");
        notes=new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                notes.add(new NoteINFO(cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchThread(final int subtopic_id) {
        new AsyncTask<Void,Void,Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {
                String[] args={Integer.toString(subtopic_id)};
                fetch(args);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                notesAdapter=new Adapters.NotesAdapter(getApplicationContext());
                notes_list.setAdapter(notesAdapter);
            }
        }.execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(wasAltered) {
            notesAdapter = new Adapters.NotesAdapter(this);
            notes_list.setAdapter(notesAdapter);
            wasAltered=false;
        }
    }
}
