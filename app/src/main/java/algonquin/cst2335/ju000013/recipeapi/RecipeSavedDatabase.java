package algonquin.cst2335.ju000013.recipeapi;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {RecipeSearched.class}, version = 1, exportSchema = false)
public abstract class RecipeSavedDatabase extends RoomDatabase {
    public abstract RecipeSearchedDAO rsDAO();
}
