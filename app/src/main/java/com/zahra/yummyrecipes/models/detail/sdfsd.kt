package com.zahra.yummyrecipes.models.detail


import com.google.gson.annotations.SerializedName

data class sdfsd(
    @SerializedName("aggregateLikes")
    val aggregateLikes: Int?, // 209
    @SerializedName("analyzedInstructions")
    val analyzedInstructions: List<Any?>?,
    @SerializedName("cheap")
    val cheap: Boolean?, // false
    @SerializedName("cookingMinutes")
    val cookingMinutes: Int?, // -1
    @SerializedName("creditsText")
    val creditsText: String?, // Full Belly Sisters
    @SerializedName("cuisines")
    val cuisines: List<Any?>?,
    @SerializedName("dairyFree")
    val dairyFree: Boolean?, // false
    @SerializedName("diets")
    val diets: List<Any?>?,
    @SerializedName("dishTypes")
    val dishTypes: List<String?>?,
    @SerializedName("extendedIngredients")
    val extendedIngredients: List<ExtendedIngredient?>?,
    @SerializedName("gaps")
    val gaps: String?, // no
    @SerializedName("glutenFree")
    val glutenFree: Boolean?, // false
    @SerializedName("healthScore")
    val healthScore: Int?, // 18
    @SerializedName("id")
    val id: Int?, // 716429
    @SerializedName("image")
    val image: String?, // https://spoonacular.com/recipeImages/716429-556x370.jpg
    @SerializedName("imageType")
    val imageType: String?, // jpg
    @SerializedName("instructions")
    val instructions: String?,
    @SerializedName("license")
    val license: String?, // CC BY-SA 3.0
    @SerializedName("lowFodmap")
    val lowFodmap: Boolean?, // false
    @SerializedName("occasions")
    val occasions: List<Any?>?,
    @SerializedName("originalId")
    val originalId: Any?, // null
    @SerializedName("preparationMinutes")
    val preparationMinutes: Int?, // -1
    @SerializedName("pricePerServing")
    val pricePerServing: Double?, // 157.06
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int?, // 45
    @SerializedName("servings")
    val servings: Int?, // 2
    @SerializedName("sourceName")
    val sourceName: String?, // Full Belly Sisters
    @SerializedName("sourceUrl")
    val sourceUrl: String?, // http://fullbellysisters.blogspot.com/2012/06/pasta-with-garlic-scallions-cauliflower.html
    @SerializedName("spoonacularSourceUrl")
    val spoonacularSourceUrl: String?, // https://spoonacular.com/pasta-with-garlic-scallions-cauliflower-breadcrumbs-716429
    @SerializedName("summary")
    val summary: String?, // You can never have too many main course recipes, so give Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs a try. One serving contains <b>543 calories</b>, <b>17g of protein</b>, and <b>16g of fat</b>. For <b>$1.57 per serving</b>, this recipe <b>covers 22%</b> of your daily requirements of vitamins and minerals. This recipe serves 2. A mixture of butter, white wine, pasta, and a handful of other ingredients are all it takes to make this recipe so yummy. 209 people have tried and liked this recipe. It is brought to you by fullbellysisters.blogspot.com. From preparation to the plate, this recipe takes approximately <b>45 minutes</b>. Taking all factors into account, this recipe <b>earns a spoonacular score of 83%</b>, which is tremendous. If you like this recipe, take a look at these similar recipes: <a href="https://spoonacular.com/recipes/pasta-with-garlic-scallions-cauliflower-breadcrumbs-1230187">Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs</a>, <a href="https://spoonacular.com/recipes/pasta-with-garlic-scallions-cauliflower-breadcrumbs-1229807">Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs</a>, and <a href="https://spoonacular.com/recipes/pasta-with-garlic-scallions-cauliflower-breadcrumbs-1229669">Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs</a>.
    @SerializedName("sustainable")
    val sustainable: Boolean?, // false
    @SerializedName("title")
    val title: String?, // Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs
    @SerializedName("vegan")
    val vegan: Boolean?, // false
    @SerializedName("vegetarian")
    val vegetarian: Boolean?, // false
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean?, // false
    @SerializedName("veryPopular")
    val veryPopular: Boolean?, // false
    @SerializedName("weightWatcherSmartPoints")
    val weightWatcherSmartPoints: Int?, // 16
    @SerializedName("winePairing")
    val winePairing: WinePairing?
) {
    data class ExtendedIngredient(
        @SerializedName("aisle")
        val aisle: String?, // Milk, Eggs, Other Dairy
        @SerializedName("amount")
        val amount: Double?, // 1.0
        @SerializedName("consistency")
        val consistency: String?, // SOLID
        @SerializedName("id")
        val id: Int?, // 1001
        @SerializedName("image")
        val image: String?, // butter-sliced.jpg
        @SerializedName("measures")
        val measures: Measures?,
        @SerializedName("meta")
        val meta: List<String?>?,
        @SerializedName("name")
        val name: String?, // butter
        @SerializedName("nameClean")
        val nameClean: String?, // butter
        @SerializedName("original")
        val original: String?, // 1 tbsp butter
        @SerializedName("originalName")
        val originalName: String?, // butter
        @SerializedName("unit")
        val unit: String? // tbsp
    ) {
        data class Measures(
            @SerializedName("metric")
            val metric: Metric?,
            @SerializedName("us")
            val us: Us?
        ) {
            data class Metric(
                @SerializedName("amount")
                val amount: Double?, // 1.0
                @SerializedName("unitLong")
                val unitLong: String?, // Tbsp
                @SerializedName("unitShort")
                val unitShort: String? // Tbsp
            )

            data class Us(
                @SerializedName("amount")
                val amount: Double?, // 1.0
                @SerializedName("unitLong")
                val unitLong: String?, // Tbsp
                @SerializedName("unitShort")
                val unitShort: String? // Tbsp
            )
        }
    }

    data class WinePairing(
        @SerializedName("pairedWines")
        val pairedWines: List<Any?>?,
        @SerializedName("pairingText")
        val pairingText: String?, // No one wine will suit every pasta dish. Pasta in a tomato-based sauce will usually work well with a medium-bodied red, such as a montepulciano or chianti. Pasta with seafood or pesto will fare better with a light-bodied white, such as a pinot grigio. Cheese-heavy pasta can pair well with red or white - you might try a sangiovese wine for hard cheeses and a chardonnay for soft cheeses. We may be able to make a better recommendation if you ask again with a specific pasta dish.
        @SerializedName("productMatches")
        val productMatches: List<Any?>?
    )
}