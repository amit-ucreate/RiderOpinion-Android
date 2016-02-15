package com.nutsuser.ridersdomain.utils;

import java.util.ArrayList;

/**
 * Created by noor on 01/04/15.
 */
public class UtilsSubcribe {

    public static final String IMAGE_URL = "imageUrl";
    public static final String TAB_PAGER_ADAPTER="PagerAdapter";
    public static final String TAB_FRAGMENT_PAGER_ADAPTER="FragmentPagerAdapter";
    public static final String TAB_FRAGMENT_STATE_PAGER_ADAPTER="FragmentStatePagerAdapter";
    public static final String EXTRA_TITLE ="title";
    public static final String EXTRA_IMAGE_URL ="imageUrl";

    public static class DummyItem{
        private String imageUrl;
        private String imageTitle;

        public DummyItem(String imageUrl, String imageTitle) {
            this.imageUrl = imageUrl;
            this.imageTitle = imageTitle;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getImageTitle() {
            return imageTitle;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if(getClass() != obj.getClass()){
                return false;
            }

            final DummyItem other = (DummyItem) obj;
            if (!this.imageUrl.equals(other.imageUrl)) {
                return false;
            }
            if (!this.imageTitle.equals(other.imageTitle)) {
                return false;
            }

            /*if(obj == this){
                return true;
            }*/
            return true;
        }


        //The hashCode() method of objects is used when you insert them into a HashTable, HashMap or HashSet.
        //Since we are using these objects to store in List, we are not going to override it.
        /*@Override
        public int hashCode() {
            return super.hashCode();
        }*/
    }



    public final static String[] imageUrls = new String[] {
            "https://lh6.googleusercontent.com/-h-ALJt7kSus/URqvIThqYfI/AAAAAAAAAbs/ejiv35olWS8/s1024/Tokyo%252520Heights.jpg",
            "https://lh5.googleusercontent.com/-Hy9k-TbS7xg/URqvIjQMOxI/AAAAAAAAAbs/RSpmmOATSkg/s1024/Tokyo%252520Highway.jpg",
            "https://lh6.googleusercontent.com/-83oOvMb4OZs/URqvJL0T7lI/AAAAAAAAAbs/c5TECZ6RONM/s1024/Tokyo%252520Smog.jpg",
            "https://lh3.googleusercontent.com/-FB-jfgREEfI/URqvJI3EXAI/AAAAAAAAAbs/XfyweiRF4v8/s1024/Tufa%252520at%252520Night.jpg",
            "https://lh4.googleusercontent.com/-vngKD5Z1U8w/URqvJUCEgPI/AAAAAAAAAbs/ulxCMVcU6EU/s1024/Valley%252520Sunset.jpg",
//            "https://lh6.googleusercontent.com/-DOz5I2E2oMQ/URqvKMND1kI/AAAAAAAAAbs/Iqf0IsInleo/s1024/Windmill%252520Sunrise.jpg",
//            "https://lh5.googleusercontent.com/-biyiyWcJ9MU/URqvKculiAI/AAAAAAAAAbs/jyPsCplJOpE/s1024/Windmill.jpg",
//            "https://lh4.googleusercontent.com/-PDT167_xRdA/URqvK36mLcI/AAAAAAAAAbs/oi2ik9QseMI/s1024/Windmills.jpg",
//            "https://lh5.googleusercontent.com/-kI_QdYx7VlU/URqvLXCB6gI/AAAAAAAAAbs/N31vlZ6u89o/s1024/Yet%252520Another%252520Rockaway%252520Sunset.jpg",
//            "https://lh4.googleusercontent.com/-e9NHZ5k5MSs/URqvMIBZjtI/AAAAAAAAAbs/1fV810rDNfQ/s1024/Yosemite%252520Tree.jpg",
    };

    public static ArrayList<DummyItem> getFullImageList(){
        ArrayList<DummyItem> fullImageList = new ArrayList<>();

        for (int i = 0; i < imageUrls.length; i++) {
            fullImageList.add(new DummyItem(imageUrls[i], "Full Image:"+i));
        }

        return fullImageList;
    }

}
