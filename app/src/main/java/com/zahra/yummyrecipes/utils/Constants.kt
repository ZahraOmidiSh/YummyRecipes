package com.zahra.yummyrecipes.utils

import android.util.Log
import kotlin.random.Random

object Constants {
    const val BASE_URL = "https://api.spoonacular.com/"
    const val BASE_URL_IMAGE_INGREDIENTS = "https://spoonacular.com/cdn/ingredients_250x250/"
    const val BASE_URL_IMAGE_EQUIPMENT = "https://spoonacular.com/cdn/equipment_250x250/"
    const val BASE_URL_IMAGE_RECIPES = "https://spoonacular.com/recipeImages/"
    const val NETWORK_TIMEOUT = 60L

    //        const val MY_API_KEY = "da7a28daf8874c15be20272ffa0953c5"
//    const val MY_API_KEY = "a4b8ec5f10364b5ab29353dfd26175de"
//    const val MY_API_KEY = "fd80793063ce4faea1e5515031576a77"   //nouri
//    const val MY_API_KEY = "82c37db267e047cdae8f0d81188a3357"   //outlook
    fun setAPIKEY(): String {
        var MY_API_KEY = ""
        val randomNumber = Random.nextInt(1, 5)
        when (randomNumber) {
            1 -> MY_API_KEY = "da7a28daf8874c15be20272ffa0953c5"
            2 -> MY_API_KEY = "a4b8ec5f10364b5ab29353dfd26175de"
            3 -> MY_API_KEY = "fd80793063ce4faea1e5515031576a77"
            4 -> MY_API_KEY = "82c37db267e047cdae8f0d81188a3357"
        }
        Log.e("MY_API_KEY", "randomNumber=$randomNumber --- MY_API_KEY=$MY_API_KEY")
        return MY_API_KEY
    }

    //APIs key
    const val API_KEY = "apiKey"
    const val TYPE = "type"
    const val EQUIPMENT = "equipment"
    const val CUISINE = "cuisine"
    const val NUMBER = "number"
    const val ADD_RECIPE_INFORMATION = "addRecipeInformation"
    const val SORT = "sort"
    const val SORT_DIRECTION = "sortDirection"
    const val DIET = "diet"
    const val INTOLERANCES = "intolerances"
    const val INCLUDE_INGREDIENTS = "includeIngredients"
    const val MAX_READY_TIME = "maxReadyTime"
    const val MAX_CALORIES = "maxCalories"
    const val MIN_CALORIES = "minCalories"

    //APIs value
    const val MAIN_COURSE = "main course"
    const val TRUE = "true"
    const val RANDOM = "random"
    const val POPULARITY = "popularity"
    const val PRICE = "price"
    const val TIME = "time"
    const val ASCENDING = "asc"
    const val DESCENDING = "desc"
    const val GLUTEN_FREE = "gluten free"
    const val KETOGENIC = "ketogenic"
    const val VEGETARIAN = "vegetarian"
    const val LACTO_VEGETARIAN = "lacto vegetarian"
    const val OVO_VEGETARIAN = "ovo vegetarian"
    const val LACTO_OVO_VEGETARIAN = "lacto ovo vegetarian"
    const val VEGAN = "vegan"
    const val PESCETARIAN = "pescatarian"
    const val PALEO = "paleo"
    const val PRIMAL = "primal"
    const val LOW_FODMAP = "low fodmap"
    const val DAIRY_FREE = "dairy free"
    const val WHOLE30 = "whole30"
    const val HEALTHINESS = "healthiness"


    //Register datastore
    const val REGISTER_USER_INFO = "register_user_info"
    const val REGISTER_USERNAME = "register_username"
    const val REGISTER_HASH = "register_hash"

    //Database
    const val RECIPE_TABLE_NAME = "recipe_table_name"
    const val DATABASE_NAME = "database_name"
    const val DETAIL_TABLE_NAME = "detail_table_name"
    const val FAVORITE_TABLE_NAME = "favorite_table_name"
    const val MEAL_PLANNER_TABLE_NAME = "meal_planner_table_name"
    const val SHOPPING_LIST_TABLE_NAME = "shopping_list_table_name"

    //Other
    const val LIMITED_COUNT = 10
    const val FULL_COUNT = 50
    const val OLD_IMAGE_SIZE = "312x231.jpg"
    const val NEW_IMAGE_SIZE = "636x393.jpg"
    const val REPEAT_TIME = 100
    const val DELAY_TIME = 5000L

    //Search
    const val INGREDIENTS = "ingredients"
}