package algonquin.cst2335.ju000013.recipeapi;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class RecipeSearchedViewModel extends ViewModel {
    public MutableLiveData<ArrayList<RecipeSearched>> searchedRecipes = new MutableLiveData<>();
}
