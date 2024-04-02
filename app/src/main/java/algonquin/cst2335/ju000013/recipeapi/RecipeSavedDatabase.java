package algonquin.cst2335.ju000013.recipeapi;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/** This page is a database with entity is RecipeSearched.class.
 * Here also a abstract method connect with DAO.
 *
 * @author Fei Wu
 * @version 1.0
 * */
@Database(entities = {RecipeSearched.class}, version = 1, exportSchema = false)
public abstract class RecipeSavedDatabase extends RoomDatabase {
    public abstract RecipeSearchedDAO rsDAO();
}
