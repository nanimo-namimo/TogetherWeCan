package com.example.togetherwecan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class Adapters {

    public static class SubTopicsAdapter  extends RecyclerView.Adapter<SubTopicsAdapter.MyHolder> implements Filterable {
        ArrayList<SubTopicINFO> subtopics ;
        ArrayList<SubTopicINFO> subtopics_search ;
        Context context;
        public static int FONT_SIZE;
        SubTopicsAdapter(Context context) {
            this.context = context;
            this.subtopics = ViewSubtopicsActivity.subtopics;
            subtopics_search= new ArrayList<>(subtopics);

        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.subtopics_holder, viewGroup, false);
            return new MyHolder(linearLayout);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
            LinearLayout linearLayout = myHolder.myLayout;
            final TextView note_name = linearLayout.findViewById(R.id.subt_holder_title);
            TextView note_summary = linearLayout.findViewById(R.id.subt_holder_desc);
            note_name.setText(subtopics_search.get(i).getName());
            note_name.setTextSize(FONT_SIZE);
            note_summary.setText(subtopics_search.get(i).getSummary());
            note_summary.setTextSize(FONT_SIZE);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,SubTopicActivity.class);
                    intent.putExtra("name",subtopics_search.get(i).getName());
                    intent.putExtra("summary",subtopics_search.get(i).getSummary());
                    intent.putExtra("id",subtopics_search.get(i).getId());
                    context.startActivity(intent);

                }
            });

            linearLayout.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

                    MenuItem item=contextMenu.add(0,1,1,"edit");
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {


                            Intent intent=new Intent(context,EditNote.class);
                            intent.putExtra("mode","subtopic");
                            intent.putExtra("name",subtopics_search.get(i).getName());
                            intent.putExtra("summary",subtopics_search.get(i).getSummary());
                            intent.putExtra("id",subtopics_search.get(i).getId());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                            return true;
                        }
                    });
                    item=contextMenu.add(0,2,2,"delete");
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            String s=subtopics_search.get(i).toString();

                            DBHelper dbHelper=new DBHelper(context);
                            String[] args={Integer.toString(subtopics_search.get(i).getId())};
                            if(dbHelper.delete("subtopic",args)) {
                                ViewSubtopicsActivity.subtopics.remove(subtopics_search.get(i));
                                subtopics_search.remove(i);
                                notifyDataSetChanged();
                            }

                            Toast.makeText(context, "deleted "+s, Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                }
            });

        }



        @Override
        public int getItemCount() {
            return subtopics_search.size();
        }

        @Override
        public Filter getFilter() {
            return subtopicsFilter;
        }

        private Filter subtopicsFilter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<SubTopicINFO>filteredItems=new ArrayList<>();
                if(charSequence==null || charSequence.length()==0){
                    filteredItems.addAll(subtopics);
                }
                else{
                    String filterPattern=charSequence.toString().toLowerCase().trim();
                    for (SubTopicINFO subTopicINFO:subtopics){
                        if(subTopicINFO.getName().toLowerCase().contains(filterPattern)){

                            filteredItems.add(subTopicINFO);
                        }
                    }
                }
                FilterResults results=new FilterResults();
                results.values=filteredItems;
                return results;
            }



            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                subtopics_search.clear();
                subtopics_search.addAll((List)filterResults.values);
                notifyDataSetChanged();
            }
        };

        class MyHolder extends RecyclerView.ViewHolder {
            LinearLayout myLayout;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                myLayout = (LinearLayout) itemView;
            }
        }
    }

    public static class BooksAdapter  extends RecyclerView.Adapter<BooksAdapter.MyHolder> implements Filterable {
        ArrayList<SubTopicINFO> subtopics ;
        ArrayList<SubTopicINFO> subtopics_search ;
        Context context;
        static int FONT_SIZE;
        BooksAdapter(Context context) {
            this.context = context;
            this.subtopics = ViewBooksActivity.books;
            subtopics_search= new ArrayList<>(subtopics);

        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.subtopics_holder, viewGroup, false);
            return new MyHolder(linearLayout);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
            LinearLayout linearLayout = myHolder.myLayout;
            final TextView note_name = linearLayout.findViewById(R.id.subt_holder_title);
            TextView note_summary = linearLayout.findViewById(R.id.subt_holder_desc);
            note_name.setText(subtopics_search.get(i).getName());
            note_name.setTextSize(FONT_SIZE);
            note_summary.setText(subtopics_search.get(i).getSummary());
            note_summary.setTextSize(FONT_SIZE);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO modify the activity I visit
                    Intent intent=new Intent(context,BookActivity.class);
                    intent.putExtra("name",subtopics_search.get(i).getName());
                    intent.putExtra("summary",subtopics_search.get(i).getSummary());
                    intent.putExtra("id",subtopics_search.get(i).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });

            linearLayout.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

                    MenuItem item=contextMenu.add(0,1,1,"edit");
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {


                            Intent intent=new Intent(context,EditNote.class);
                            intent.putExtra("mode","book");
                            intent.putExtra("name",subtopics_search.get(i).getName());
                            intent.putExtra("summary",subtopics_search.get(i).getSummary());
                            intent.putExtra("id",subtopics_search.get(i).getId());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                            return true;
                        }
                    });
                    item=contextMenu.add(0,2,2,"delete");
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            String s=subtopics_search.get(i).toString();

                            DBHelper dbHelper=new DBHelper(context);
                            String[] args={Integer.toString(subtopics_search.get(i).getId())};
                            if(dbHelper.delete("book",args)) {
                                ViewBooksActivity.books.remove(subtopics_search.get(i));
                                subtopics_search.remove(i);
                                notifyDataSetChanged();
                            }

                            Toast.makeText(context, "deleted "+s, Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                }
            });

        }



        @Override
        public int getItemCount() {
            return subtopics_search.size();
        }

        @Override
        public Filter getFilter() {
            return subtopicsFilter;
        }

        private Filter subtopicsFilter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<SubTopicINFO>filteredItems=new ArrayList<>();
                if(charSequence==null || charSequence.length()==0){
                    filteredItems.addAll(subtopics);
                }
                else{
                    String filterPattern=charSequence.toString().toLowerCase().trim();
                    for (SubTopicINFO subTopicINFO:subtopics){
                        if(subTopicINFO.getName().toLowerCase().contains(filterPattern)){

                            filteredItems.add(subTopicINFO);
                        }
                    }
                }
                FilterResults results=new FilterResults();
                results.values=filteredItems;
                return results;
            }



            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                subtopics_search.clear();
                subtopics_search.addAll((List)filterResults.values);
                notifyDataSetChanged();
            }
        };

        class MyHolder extends RecyclerView.ViewHolder {
            LinearLayout myLayout;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                myLayout = (LinearLayout) itemView;
            }
        }
    }


    public static class TopicsAdapter  extends RecyclerView.Adapter<TopicsAdapter.MyHolder> implements Filterable {
        ArrayList<SubTopicINFO> topics ;
        ArrayList<SubTopicINFO> subtopics_search ;
        Context context;
        static int FONT_SIZE;
        TopicsAdapter(Context context) {
            this.context = context;
            this.topics = ViewTopicsActivity.topics;
            subtopics_search= new ArrayList<>(topics);

        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.subtopics_holder, viewGroup, false);
            return new MyHolder(linearLayout);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
            LinearLayout linearLayout = myHolder.myLayout;
            final TextView note_name = linearLayout.findViewById(R.id.subt_holder_title);
            TextView note_summary = linearLayout.findViewById(R.id.subt_holder_desc);
            note_name.setText(subtopics_search.get(i).getName());
            note_name.setTextSize(FONT_SIZE);
            note_summary.setText(subtopics_search.get(i).getSummary());
            note_summary.setTextSize(FONT_SIZE);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,TopicActivity.class);
                    intent.putExtra("name",subtopics_search.get(i).getName());
                    intent.putExtra("summary",subtopics_search.get(i).getSummary());
                    intent.putExtra("id",subtopics_search.get(i).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });

            linearLayout.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

                    MenuItem item=contextMenu.add(0,1,1,"edit");
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {


                            Intent intent=new Intent(context,EditNote.class);
                            intent.putExtra("mode","topic");
                            intent.putExtra("name",subtopics_search.get(i).getName());
                            intent.putExtra("summary",subtopics_search.get(i).getSummary());
                            intent.putExtra("id",subtopics_search.get(i).getId());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                            return true;
                        }
                    });
                    item=contextMenu.add(0,2,2,"delete");
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            String s=subtopics_search.get(i).toString();

                            DBHelper dbHelper=new DBHelper(context);
                            String[] args={Integer.toString(subtopics_search.get(i).getId())};
                            if(dbHelper.delete("topic",args)) {
                                ViewTopicsActivity.topics.remove(subtopics_search.get(i));
                                subtopics_search.remove(i);
                                notifyDataSetChanged();
                            }

                            Toast.makeText(context, "deleted "+s, Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                }
            });

        }



        @Override
        public int getItemCount() {
            return subtopics_search.size();
        }

        @Override
        public Filter getFilter() {
            return subtopicsFilter;
        }

        private Filter subtopicsFilter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<SubTopicINFO>filteredItems=new ArrayList<>();
                if(charSequence==null || charSequence.length()==0){
                    filteredItems.addAll(topics);
                }
                else{
                    String filterPattern=charSequence.toString().toLowerCase().trim();
                    for (SubTopicINFO subTopicINFO:topics){
                        if(subTopicINFO.getName().toLowerCase().contains(filterPattern)){

                            filteredItems.add(subTopicINFO);
                        }
                    }
                }
                FilterResults results=new FilterResults();
                results.values=filteredItems;
                return results;
            }



            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                subtopics_search.clear();
                subtopics_search.addAll((List)filterResults.values);
                notifyDataSetChanged();
            }
        };

        class MyHolder extends RecyclerView.ViewHolder {
            LinearLayout myLayout;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                myLayout = (LinearLayout) itemView;
            }
        }
    }


    public static class NotesAdapter  extends RecyclerView.Adapter<NotesAdapter.MyHolder> implements Filterable {
        ArrayList<NoteINFO> notes ;
        ArrayList<NoteINFO> notes_search ;
        Context context;
        public static int FONT_SIZE;
        NotesAdapter(Context context) {
            this.context = context;
            this.notes = NoteActivity.notes;
            notes_search= new ArrayList<>(notes);

        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.note_template, viewGroup, false);
            return new MyHolder(linearLayout);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
            LinearLayout linearLayout = myHolder.myLayout;
            final TextView note_name = linearLayout.findViewById(R.id.vn_note_name);
            TextView note_summary = linearLayout.findViewById(R.id.vn_note_summ);
            note_name.setText(notes_search.get(i).getName());
            note_name.setTextSize(FONT_SIZE);
            note_summary.setText(notes_search.get(i).getSummary());
            note_summary.setTextSize(FONT_SIZE);

            linearLayout.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

                      MenuItem item=contextMenu.add(0,1,1,"edit");
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            Intent intent=new Intent(context,EditNote.class);
                            intent.putExtra("mode","note");
                            intent.putExtra("name",notes_search.get(i).getName());
                            intent.putExtra("summary",notes_search.get(i).getSummary());
                            intent.putExtra("id",notes_search.get(i).getId());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            return true;
                        }
                    });
                     item=contextMenu.add(0,2,2,"delete");
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            String s=notes.get(i).toString();

                            DBHelper dbHelper=new DBHelper(context);
                            String[] args={Integer.toString(notes_search.get(i).getId())};
                            if(dbHelper.delete("note",args)) {
                                NoteActivity.notes.remove(notes_search.get(i));
                                notes_search.remove(i);
                                notifyDataSetChanged();

                            }

                            Toast.makeText(context, "deleted "+s, Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                }
            });

        }



        @Override
        public int getItemCount() {
            return notes_search.size();
        }

        @Override
        public Filter getFilter() {
            return notesFilter;
        }

        private Filter notesFilter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<NoteINFO>filteredItems=new ArrayList<>();
                if(charSequence==null || charSequence.length()==0){
                    filteredItems.addAll(notes);
                }
                else{
                    String filterPattern=charSequence.toString().toLowerCase().trim();
                    for (NoteINFO noteINFO:notes){
                        if(noteINFO.getName().toLowerCase().contains(filterPattern)){

                            filteredItems.add(noteINFO);
                        }
                    }
                }
                FilterResults results=new FilterResults();
                results.values=filteredItems;
                return results;
            }



            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notes_search.clear();
                notes_search.addAll((List)filterResults.values);
                notifyDataSetChanged();
            }
        };

        class MyHolder extends RecyclerView.ViewHolder {
            LinearLayout myLayout;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                myLayout = (LinearLayout) itemView;
            }
        }
    }
    public static class QuizAdapter  extends RecyclerView.Adapter<QuizAdapter.MyHolder>{
        ArrayList<QuizActivity.Title> titles ;
        ArrayList<QuizActivity.Description> descriptions ;
        Context context;
        private String MODE;
        public static int FONT_SIZE;
        QuizAdapter(Context context,String mode) {
            this.context = context;
            titles=QuizActivity.titles;
            descriptions=QuizActivity.descriptions;
            MODE=mode;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if(MODE=="do quiz") {
                LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.row_template, viewGroup, false);
                return new MyHolder(linearLayout);
            }
            else{
                LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.feedback_row, viewGroup, false);
                return new MyHolder(linearLayout);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
            LinearLayout linearLayout = myHolder.myLayout;
            if(MODE.equals("do quiz")) {
                final TextView note_name = linearLayout.findViewById(R.id.quiz_nName);
                TextView note_summary = linearLayout.findViewById(R.id.quiz_nSumm);
                note_name.setText(titles.get(i).getName());
                note_name.setTextSize(FONT_SIZE);
                String s = (i + 1) + " : " + descriptions.get(i).getDescription();
                note_summary.setText(s);
                note_summary.setTextSize(FONT_SIZE);
                final EditText user_answer = linearLayout.findViewById(R.id.quiz_my_choice);
                user_answer.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if (!user_answer.getText().toString().isEmpty()) {
                            int ans = Integer.parseInt(s.toString());
                            QuizActivity.titles.get(i).setGiven_ans(ans);
                        } else {
                            QuizActivity.titles.get(i).reset_ans();
                        }
                    }
                });
            }else{
                //TODO do feedback menu
                final TextView note_name = linearLayout.findViewById(R.id.feedback_nName);
                TextView note_summary = linearLayout.findViewById(R.id.feedback_nSumm);
                note_name.setText(titles.get(i).getName());
                note_name.setTextSize(FONT_SIZE);
                String s = (i + 1) + " : " + descriptions.get(i).getDescription();
                note_summary.setText(s);
                note_summary.setTextSize(FONT_SIZE);
                TextView my_ans=linearLayout.findViewById(R.id.feed_my_ans);
                String myAns= String.valueOf(titles.get(i).getGiven_ans());
                if(myAns.equals("-1")){
                    myAns="None";
                }
                String text="your answer is "+myAns;
                my_ans.setText(text);
                if(titles.get(i).isCorrect()){
                    my_ans.setTextColor(Color.GREEN);
                }
                TextView c_ans=linearLayout.findViewById(R.id.feedback_corr_ans);
                text="the correct answer is "+titles.get(i).getC_ans();
                c_ans.setText(text);
            }
        }



        @Override
        public int getItemCount() {
            return titles.size();
        }



        class MyHolder extends RecyclerView.ViewHolder {
            LinearLayout myLayout;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                myLayout = (LinearLayout) itemView;
            }
        }
    }

}
