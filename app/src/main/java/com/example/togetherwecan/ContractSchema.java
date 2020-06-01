package com.example.togetherwecan;

class ContractSchema {

     static class QUIZ_PERCENT{
       static String SHAREDPREFS="QUIZ_PREFS";
       static String SHAREDPREFS_TOPIC="QUIZ_PREFS_TOPIC";
       static String SHAREDPREFS_SUBTOPIC="QUIZ_PREFS_SUBTOPIC";
       static String PERCENTAGE="QUIZ_PERCENTAGE_TOPIC";
       static String PERCENTAGE_TOPIC="QUIZ_PERCENTAGE_TOPIC";
       static String PERCENTAGE_SUBTOPIC="QUIZ_PERCENTAGE_SUBTOPIC";
    }

    static class DATABASE{
        final static String DATABASE_NAME = "TogetherWeCan.db";
        static int DATABASE_VERSION = 9;
    }

    static class BOOK{

       static String BOOK_TABLE="BOOK";
       static String BOOK_ID="BOOK_ID";
       static String BOOK_NAME="BOOK_NAME";
       static String BOOK_SUMMARY="SUMMARY";

       final static String ADD="insert into "+BOOK_TABLE+"("
        +BOOK_NAME+","
        +BOOK_SUMMARY
        +") values(?,?)";
       final static String DELETE="delete from "+BOOK_TABLE+" where "+BOOK_ID+"=?";
       final static String DROP_TABLE="drop table if exists "+BOOK_TABLE;
       final static String FETCH="select "+BOOK_ID+","+BOOK_NAME+","+BOOK_SUMMARY+" from "+BOOK_TABLE;
       final static String UPDATE="update "+BOOK_TABLE+" set "+BOOK_NAME+"=?,"+BOOK_SUMMARY+"=? where "+BOOK_ID+"=?";
       final static String CREATE_TABLE="create table "+BOOK_TABLE+"("+
                BOOK_ID+" integer primary key autoincrement,"+
                BOOK_NAME+" text,"+
                BOOK_SUMMARY+" text);";

    }

    static class TOPIC{
        final static String TABLE="TOPIC";
        final static String ID="TOPIC_ID";
        final static String NAME="TOPIC_NAME";
        final static String SUMMARY="SUMMARY";
        final static String BOOK_ID="BOOK_ID";

        final static String DELETE="delete from "+TABLE+" where "+ID+"=?";
        final static String DELETE_BOOK="delete from "+TABLE+" where "+BOOK_ID+"=?";
        final static String DROP_TABLE="drop table if exists "+TABLE;
        final static String FETCH="select "+ID+","+NAME+","+SUMMARY+" from "+TABLE+" where "+BOOK_ID+" = ?";
        final static String UPDATE="update "+TABLE+" set "+NAME+"=?,"+SUMMARY+"=? where "+ID+"=?";

        final static String CREATE_TABLE="create table "+TABLE+"("+
        ID+" integer primary key autoincrement,"+
        NAME+" text,"+
        SUMMARY+" text,"+
          BOOK_ID+" integer, FOREIGN KEY("+BOOK_ID+") REFERENCES "+BOOK.BOOK_TABLE+"("+BOOK_ID+") );";
        final static String ADD="insert into "+TABLE+"("
                +NAME+","
                +SUMMARY+","
                +BOOK_ID
                +") values(?,?,?)";

    }
    static class SUBTOPIC{
        final static String TABLE="SUBTOPIC";
        final static String ID="SUBTOPIC_ID";
        final static String NAME="SUBTOPIC_NAME";
        final static String SUMMARY="SUMMARY";
        final static String TOPIC_ID="TOPIC_ID";
        final static String BOOK_ID="BOOK_ID";

        final static String DELETE="delete from "+TABLE+" where "+ID+"=?";
        final static String DELETE_BOOK="delete from "+TABLE+" where "+BOOK_ID+"=?";
        final static String DELETE_TOPIC="delete from "+TABLE+" where "+TOPIC_ID+"=?";
        final static String DROP_TABLE="drop table if exists "+TABLE;
        final static String FETCH="select "+ID+","+NAME+","+SUMMARY+" from "+TABLE+" where "+TOPIC_ID+" = ?";
        final static String FETCH_BOOK="select "+ID+","+NAME+","+SUMMARY+" from "+TABLE+" where "+BOOK_ID+" = ?";
        final static String UPDATE="update "+TABLE+" set "+NAME+"=?,"+SUMMARY+"=? where "+ID+"=?";


        final static String CREATE_TABLE="create table "+TABLE+"" +
                "("+
                ID+" integer primary key autoincrement,"+
                NAME+" text,"+
                SUMMARY+" text,"+
                BOOK_ID+" integer,"+
                TOPIC_ID+" integer, FOREIGN KEY("+TOPIC_ID+") REFERENCES "+TOPIC.TABLE+"("+TOPIC_ID+"),"+
                "FOREIGN KEY("+BOOK_ID+") REFERENCES "+BOOK.BOOK_TABLE+"("+BOOK_ID+")"+
                " );";

        final static String ADD="insert into "+TABLE+"("
                +NAME+","
                +SUMMARY+","
                +TOPIC_ID+","
                +BOOK_ID
                +") values(?,?,?,?)";

    }

    static class NOTE {

        final static String NOTE_TABLE = "NOTE";
        final static String NOTE_ID = "ID";
        final static String NOTE_NAME = "NAME";
        final static String SUBTOPIC_ID = "SUBTOPIC_ID";
        final static String TOPIC_ID = "TOPIC_ID";
        final static String BOOK_ID = "BOOK_ID";
        final static String NOTE_SUMMARY = "SUMMARY";

         final static String ADD_NOTE="insert into "+NOTE_TABLE+"("
                +NOTE_NAME+","
                +NOTE_SUMMARY+","
                +SUBTOPIC_ID+","
                +TOPIC_ID+","
                +BOOK_ID
                +") values(?,?,?,?,?)";
        final static String DELETE_NOTE="delete from "+NOTE_TABLE+" where "+NOTE_ID+"=?";
        final static String DELETE_BOOK="delete from "+NOTE_TABLE+" where "+BOOK_ID+"=?";
        final static String DELETE_TOPIC="delete from "+NOTE_TABLE+" where "+TOPIC_ID+"=?";
        final static String DELETE_SUBTOPIC="delete from "+NOTE_TABLE+" where "+SUBTOPIC_ID+"=?";
        final static String DROP_TABLE="drop table if exists "+NOTE_TABLE;
        final static String FETCH_NOTES="select "+NOTE_ID+","+NOTE_NAME+","+NOTE.NOTE_SUMMARY+" from "+NOTE_TABLE+" where "+SUBTOPIC_ID+" = ?";
        final static String FETCH_TOPIC="select "+NOTE_ID+","+NOTE_NAME+","+NOTE.NOTE_SUMMARY+" from "+NOTE_TABLE+" where "+TOPIC_ID+" = ?";
        final static String FETCH_BOOK="select "+NOTE_ID+","+NOTE_NAME+","+NOTE.NOTE_SUMMARY+" from "+NOTE_TABLE+" where "+BOOK_ID+" = ?";
        final static String UPDATE_NOTE="update "+NOTE_TABLE+" set "+NOTE_NAME+"=?,"+NOTE.NOTE_SUMMARY+"=? where "+NOTE_ID+"=?";

        final static String CREATE_TABLE="create table "+NOTE_TABLE+"("+
                NOTE_ID+" integer primary key autoincrement,"+
                NOTE_NAME+" text,"+
                NOTE_SUMMARY+" text,"+
                BOOK_ID+" integer,"+
                TOPIC_ID+" integer,"+
                SUBTOPIC_ID+" integer, FOREIGN KEY("+SUBTOPIC_ID+") REFERENCES "+ ContractSchema.SUBTOPIC.TABLE+"("+SUBTOPIC_ID+")," +
                " FOREIGN KEY("+BOOK_ID+") REFERENCES "+ ContractSchema.BOOK.BOOK_TABLE+"("+BOOK_ID+"),"+
                " FOREIGN KEY("+TOPIC_ID+") REFERENCES "+ ContractSchema.TOPIC.TABLE+"("+TOPIC_ID+")"+
                " );";

    }

}
