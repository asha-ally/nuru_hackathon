package com.openinstitute.nuru.Database;

public class TagList {

    public String tag_name;

    public TagList(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getName() {
        return tag_name;
    }

    public void setName(String _name) {
        this.tag_name = _name;
    }

}
