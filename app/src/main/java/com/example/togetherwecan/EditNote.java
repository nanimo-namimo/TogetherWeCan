package com.example.togetherwecan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNote extends AppCompatActivity {

    NoteINFO noteINFO;
    SubTopicINFO subTopicINFO;
    EditText name,summary;
    Button update,close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note_window);
        Toolbar toolbar=findViewById(R.id.toolbar_3);
        setSupportActionBar(toolbar);
        update=findViewById(R.id.up_update);
        close=findViewById(R.id.un_close);
        name=findViewById(R.id.un_name);
        summary=findViewById(R.id.un_summ);
        name.setText(getIntent().getStringExtra("name"));
        summary.setText(getIntent().getStringExtra("summary"));
        if(getIntent().getStringExtra("mode").equals("note")) {
            noteINFO = new NoteINFO(getIntent().getIntExtra("id", 0), name.getText().toString(), summary.getText().toString());
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NoteINFO noteINFO1 = new NoteINFO(noteINFO.getId(), name.getText().toString(), summary.getText().toString());
                    int index = NoteActivity.notes.indexOf(noteINFO);

                    String[] args = {noteINFO1.getName(), noteINFO1.getSummary(), String.valueOf(noteINFO1.getId())};
                    if (new DBHelper(getApplicationContext()).modify("note", args)) {
                        NoteActivity.notes.remove(index);
                        NoteActivity.notes.add(index,noteINFO1);
                        NoteActivity.notesAdapter.notifyDataSetChanged();
                        NoteActivity.wasAltered=true;
                    }
                }
            });
        }
        else if(getIntent().getStringExtra("mode").equals("subtopic"))  {
            subTopicINFO = new SubTopicINFO(getIntent().getIntExtra("id", 0), name.getText().toString(), summary.getText().toString());
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SubTopicINFO subTopicINFO1 = new SubTopicINFO(subTopicINFO.getId(), name.getText().toString(), summary.getText().toString());
                    int index = ViewSubtopicsActivity.subtopics.indexOf(subTopicINFO);

                    String[] args = {subTopicINFO1.getName(), subTopicINFO1.getSummary(), String.valueOf(subTopicINFO1.getId())};
                    if (new DBHelper(getApplicationContext()).modify("subtopic", args)) {
                        ViewSubtopicsActivity.subtopics.remove(index);
                        ViewSubtopicsActivity.subtopics.add(index,subTopicINFO1);
                        ViewSubtopicsActivity.subTopicsAdapter.notifyDataSetChanged();
                        ViewSubtopicsActivity.wasAltered=true;
                    }
                }
            });
        }
        else if(getIntent().getStringExtra("mode").equals("book"))  {
            subTopicINFO = new SubTopicINFO(getIntent().getIntExtra("id", 0), name.getText().toString(), summary.getText().toString());
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SubTopicINFO subTopicINFO1 = new SubTopicINFO(subTopicINFO.getId(), name.getText().toString(), summary.getText().toString());
                    int index = ViewBooksActivity.books.indexOf(subTopicINFO);

                    String[] args = {subTopicINFO1.getName(), subTopicINFO1.getSummary(), String.valueOf(subTopicINFO1.getId())};
                    if (new DBHelper(getApplicationContext()).modify("book", args)) {
                        ViewBooksActivity.books.remove(index);
                        ViewBooksActivity.books.add(index,subTopicINFO1);
                        ViewBooksActivity.booksAdapter.notifyDataSetChanged();
                        ViewBooksActivity.wasAltered=true;
                    }
                }
            });
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

}
