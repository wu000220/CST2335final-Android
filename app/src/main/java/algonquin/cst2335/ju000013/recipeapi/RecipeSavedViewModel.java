package algonquin.cst2335.ju000013.recipeapi;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/** This page is a model with ArrayList which contains RecipeSearched object
 *
 * @author Fei Wu
 * @version 1.0
 * */
public class RecipeSavedViewModel extends ViewModel {
    public MutableLiveData<ArrayList<RecipeSearched>> savedRecipes = new MutableLiveData<>();
}
