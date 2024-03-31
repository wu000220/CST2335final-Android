package algonquin.cst2335.ju000013.recipeapi;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class RecipeSearched {

    @ColumnInfo (name = "title")
    public String title;
    @ColumnInfo (name = "image_url")
    public String image_url;
    @PrimaryKey
    @ColumnInfo (name = "id")
    public int id;
    @ColumnInfo (name = "source_url")
    private String source_url;

    public RecipeSearched() {
    }

    public RecipeSearched(String title, String image_url, int id, String source_url) {
        this.title = title;
        this.image_url = image_url;
        this.id = id;
        this.source_url = source_url;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
