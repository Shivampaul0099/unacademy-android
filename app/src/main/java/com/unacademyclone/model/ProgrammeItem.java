package com.unacademyclone.model;

public class ProgrammeItem {
    private int rank;
    private String created_at, name;

    public ProgrammeItem(int rank, String created_at, String name) {
        this.rank = rank;
        this.created_at = created_at;
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
