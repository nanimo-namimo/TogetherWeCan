package com.example.togetherwecan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SubTopicINFO {
    private String name,summary;
    int id;

    public SubTopicINFO(int id, String name, String summary) {
        this.name = name;
        this.summary = summary;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return getName()+"\n"+getSummary();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this.id==((SubTopicINFO) obj).getId();
    }
}
