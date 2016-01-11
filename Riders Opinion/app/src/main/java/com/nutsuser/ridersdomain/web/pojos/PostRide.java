package com.nutsuser.ridersdomain.web.pojos;

/**
 * Created by user on 1/7/2016.
 */
public class PostRide {
    private String Type;

    private String DocumentId;

    private String Estimated;

    public String getEstimated() {
        return Estimated;
    }



    public PostRide(String Type,String DocumentId,String Estimated) {
        this.Type = Type;

        this.DocumentId = DocumentId;



        this.Estimated = Estimated;


    }

    public String getType() {
        return Type;
    }


    public String getDocumentId() {
        return DocumentId;
    }


}
