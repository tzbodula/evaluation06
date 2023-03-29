package edu.uncc.assessment06;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Product implements Serializable {
    private String docId, pid, name, img_url, price, description, review_count;


    public Product(){

    }

    @Override
    public String toString() {
        return "Product{" +
                "pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", img_url='" + img_url + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", review_count='" + review_count + '\'' +
                '}';
    }

    public Product(JSONObject json) throws JSONException {
        /*
        {
            "pid": "31b100f4-ec80-4ef7-8ba9-05575e54499f",
            "name": "Charlotte 49ers 24oz. Frosted Sport Bottle",
            "img_url": "https://www.theappsdr.com/items-imgs/Charlotte-49ers-24oz-Frosted-Sport-Bottle.png",
            "price": "26.99",
            "description": "Take your beverage with you on the go by grabbing this spirited Charlotte 49ers 24oz. Frosted Sport Bottle. A secure lid helps prevent any spills, and the carabiner clip makes it easy to attach the bottle to a strap for hands-free carrying. Crisp Charlotte 49ers graphics on this sweet gear will ensure everyone knows who you're rooting for on game day!",
            "review_count": "245"
        },
         */
        this.pid = json.getString("pid");
        this.name = json.getString("name");
        this.img_url = json.getString("img_url");
        this.price = json.getString("price");
        this.description = json.getString("description");
        this.review_count = json.getString("review_count");
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReview_count() {
        return review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }
}
