package com.zahra.yummyrecipes.utils

object Constants {
    const val BASE_URL = "https://api.spoonacular.com/"
    const val BASE_URL_IMAGE_INGREDIENTS = "https://spoonacular.com/cdn/ingredients_250x250/"
    const val BASE_URL_IMAGE_EQUIPMENT = "https://spoonacular.com/cdn/equipment_250x250/"
    const val BASE_URL_IMAGE_RECIPES = "https://spoonacular.com/recipeImages/"
    const val NETWORK_TIMEOUT = 60L

    const val MY_API_KEY = "da7a28daf8874c15be20272ffa0953c5"
//    const val MY_API_KEY = "a4b8ec5f10364b5ab29353dfd26175de"


    //APIs key
    const val API_KEY = "apiKey"
    const val TYPE = "type"
    const val NUMBER = "number"
    const val ADD_RECIPE_INFORMATION = "addRecipeInformation"
    const val SORT = "sort"
    const val SORT_DIRECTION = "sortDirection"
    const val DIET = "diet"

    //APIs value
    const val MAIN_COURSE = "main course"
    const val TRUE = "true"
    const val RANDOM = "random"
    const val POPULARITY = "popularity"
    const val PRICE = "price"
    const val TIME = "time"
    const val ASCENDING = "asc"
    const val DESCENDING = "desc"
    const val GLUTEN_FREE = "Gluten Free"
    const val KETOGENIC = "Ketogenic"
    const val VEGETARIAN = "Vegetarian"
    const val LACTO_VEGETARIAN = "Lacto-Vegetarian"
    const val OVO_VEGETARIAN = "VegOvo-Vegetarianan"
    const val VEGAN = "Vegan"
    const val PESCETARIAN = "Pescetarian"
    const val PALEO = "Paleo"
    const val PRIMAL = "Primal"
    const val LOW_FODMAP = "Low FODMAP"
    const val WHOLE30 = "Whole30"
    const val HEALTHINESS = "healthiness"


    //Register datastore
    const val REGISTER_USER_INFO = "register_user_info"
    const val REGISTER_USERNAME = "register_username"
    const val REGISTER_HASH = "register_hash"

    //Database
    const val RECIPE_TABLE_NAME = "recipe_table_name"
    const val DATABASE_NAME = "database_name"
    const val DETAIL_TABLE_NAME = "detail_table_name"

    //Other
    const val LIMITED_COUNT = 10
    const val OLD_IMAGE_SIZE = "312x231.jpg"
    const val NEW_IMAGE_SIZE = "636x393.jpg"
    const val REPEAT_TIME = 100
    const val DELAY_TIME = 5000L
}