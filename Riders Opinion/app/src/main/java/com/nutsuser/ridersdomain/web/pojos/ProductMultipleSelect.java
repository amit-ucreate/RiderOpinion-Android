package com.nutsuser.ridersdomain.web.pojos;

public class ProductMultipleSelect {
    public boolean box;
    public String bikename;
    public String bikeid;

    public ProductMultipleSelect(boolean _box, String name_,String bikeid) {
        this.box = _box;
        this.bikename = name_;
        this.bikeid=bikeid;
    }
}
