package info.ivicel.dynamicui;

import java.io.Serializable;

/**
 * Created by Ivicel on 07/10/2017.
 */

public class Article implements Serializable {
    private String title;
    private String body;
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
}
