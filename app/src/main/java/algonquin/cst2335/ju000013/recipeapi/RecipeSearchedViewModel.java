package algonquin.cst2335.ju000013.recipeapi;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/** This page is a model with ArrayList which contains RecipeSearched object.
 * All the objects come from search results.
 *
 * @author Fei Wu
 * @version 1.0
 * */
public class RecipeSearchedViewModel extends ViewModel {
    public MutableLiveData<ArrayList<RecipeSearched>> searchedRecipes = new MutableLiveData<>();
}
