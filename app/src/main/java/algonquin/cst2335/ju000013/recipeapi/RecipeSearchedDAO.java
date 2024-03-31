package algonquin.cst2335.ju000013.recipeapi;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeSearchedDAO {
    @Insert
    public long insertRecipe(RecipeSearched result);

    @Query("select * from RecipeSearched")
    public List<RecipeSearched> getAllSavedResults();

    @Delete
    public void deleteSavedResult(RecipeSearched result);

    @Query("delete from RecipeSearched")
    public void deleteAll();
}
