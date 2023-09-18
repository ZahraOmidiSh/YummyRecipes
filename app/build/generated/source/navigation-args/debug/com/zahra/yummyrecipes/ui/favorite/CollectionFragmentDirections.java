package com.zahra.yummyrecipes.ui.favorite;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import com.zahra.yummyrecipes.NavigationMainDirections;
import java.lang.String;

public class CollectionFragmentDirections {
  private CollectionFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionToRegister() {
    return NavigationMainDirections.actionToRegister();
  }

  @NonNull
  public static NavDirections actionToRecipe() {
    return NavigationMainDirections.actionToRecipe();
  }

  @NonNull
  public static NavigationMainDirections.ActionToDetail actionToDetail(int recipeID) {
    return NavigationMainDirections.actionToDetail(recipeID);
  }

  @NonNull
  public static NavigationMainDirections.ActionToWebView actionToWebView(@NonNull String url) {
    return NavigationMainDirections.actionToWebView(url);
  }
}
