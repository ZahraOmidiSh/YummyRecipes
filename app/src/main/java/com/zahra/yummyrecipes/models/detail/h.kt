package com.zahra.yummyrecipes.models.detail


import com.google.gson.annotations.SerializedName

data class h(
    @SerializedName("aggregateLikes")
    val aggregateLikes: Int?, // 32767
    @SerializedName("analyzedInstructions")
    val analyzedInstructions: List<AnalyzedInstruction?>?,
    @SerializedName("cheap")
    val cheap: Boolean?, // false
    @SerializedName("cookingMinutes")
    val cookingMinutes: Int?, // 20
    @SerializedName("creditsText")
    val creditsText: String?, // Jen West
    @SerializedName("cuisines")
    val cuisines: List<Any?>?,
    @SerializedName("dairyFree")
    val dairyFree: Boolean?, // false
    @SerializedName("diets")
    val diets: List<Any?>?,
    @SerializedName("dishTypes")
    val dishTypes: List<Any?>?,
    @SerializedName("extendedIngredients")
    val extendedIngredients: List<ExtendedIngredient?>?,
    @SerializedName("gaps")
    val gaps: String?, // no
    @SerializedName("glutenFree")
    val glutenFree: Boolean?, // false
    @SerializedName("healthScore")
    val healthScore: Int?, // 20
    @SerializedName("id")
    val id: Int?, // 715449
    @SerializedName("image")
    val image: String?, // https://spoonacular.com/recipeImages/715449-556x370.jpg
    @SerializedName("imageType")
    val imageType: String?, // jpg
    @SerializedName("instructions")
    val instructions: String?, // InstructionsTake a package of OREO cookies and crush them up finely.Take softened cream cheese and mix well with cookie crumbs.Roll into one inch cookie balls, and then freeze for 10 minutes.Dip cookie balls into melted chocolate and place on a prepared cookie sheet covered with wax paper.Place into the refrigerator for 15 minutes to an hour before decorating.Add 5 candy corn to the back of the ball as tail feathers.Use icing as glue to attach the candy eyes.Cut one candy corn into pieces, using the white tip as the nose, and the orange part (cut in half) as feet.
    @SerializedName("lowFodmap")
    val lowFodmap: Boolean?, // false
    @SerializedName("occasions")
    val occasions: List<String?>?,
    @SerializedName("originalId")
    val originalId: Any?, // null
    @SerializedName("preparationMinutes")
    val preparationMinutes: Int?, // 20
    @SerializedName("pricePerServing")
    val pricePerServing: Double?, // 633.3
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int?, // 40
    @SerializedName("servings")
    val servings: Int?, // 48
    @SerializedName("sourceName")
    val sourceName: String?, // Pink When
    @SerializedName("sourceUrl")
    val sourceUrl: String?, // https://www.pinkwhen.com/oreo-cookie-balls-thanksgiving-turkey/
    @SerializedName("spoonacularSourceUrl")
    val spoonacularSourceUrl: String?, // https://spoonacular.com/how-to-make-oreo-turkeys-for-thanksgiving-715449
    @SerializedName("summary")
    val summary: String?, // How to Make OREO Turkeys for Thanksgiving requires about <b>40 minutes</b> from start to finish. This recipe serves 48. One serving contains <b>835 calories</b>, <b>47g of protein</b>, and <b>45g of fat</b>. For <b>$6.33 per serving</b>, this recipe <b>covers 27%</b> of your daily requirements of vitamins and minerals. 32767 people were impressed by this recipe. Head to the store and pick up oreo cookies 3 cups, icing, semi baking chocolate, and a few other things to make it today. It can be enjoyed any time, but it is especially good for <b>Thanksgiving</b>. It is brought to you by Pink When. Taking all factors into account, this recipe <b>earns a spoonacular score of 18%</b>, which is rather bad. Similar recipes are <a href="https://spoonacular.com/recipes/how-to-make-oreo-turkeys-for-thanksgiving-1364303">How to Make OREO Turkeys for Thanksgiving</a>, <a href="https://spoonacular.com/recipes/oreo-turkeys-thanksgiving-snack-138063">Oreo Turkeys (Thanksgiving Snack)</a>, and <a href="https://spoonacular.com/recipes/cakespy-thanksgiving-cookie-turkeys-50158">Cakespy: Thanksgiving Cookie Turkeys</a>.
    @SerializedName("sustainable")
    val sustainable: Boolean?, // false
    @SerializedName("title")
    val title: String?, // How to Make OREO Turkeys for Thanksgiving
    @SerializedName("vegan")
    val vegan: Boolean?, // false
    @SerializedName("vegetarian")
    val vegetarian: Boolean?, // false
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean?, // false
    @SerializedName("veryPopular")
    val veryPopular: Boolean?, // true
    @SerializedName("weightWatcherSmartPoints")
    val weightWatcherSmartPoints: Int?, // 32
    @SerializedName("winePairing")
    val winePairing: WinePairing?
) {
    data class AnalyzedInstruction(
        @SerializedName("name")
        val name: String?,
        @SerializedName("steps")
        val steps: List<Step?>?
    ) {
        data class Step(
            @SerializedName("equipment")
            val equipment: List<Equipment?>?,
            @SerializedName("ingredients")
            val ingredients: List<Ingredient?>?,
            @SerializedName("length")
            val length: Length?,
            @SerializedName("number")
            val number: Int?, // 1
            @SerializedName("step")
            val step: String? // Take a package of OREO cookies and crush them up finely.
        ) {
            data class Equipment(
                @SerializedName("id")
                val id: Int?, // 404727
                @SerializedName("image")
                val image: String?, // baking-sheet.jpg
                @SerializedName("localizedName")
                val localizedName: String?, // baking sheet
                @SerializedName("name")
                val name: String? // baking sheet
            )

            data class Ingredient(
                @SerializedName("id")
                val id: Int?, // 10018166
                @SerializedName("image")
                val image: String?, // oreos.png
                @SerializedName("localizedName")
                val localizedName: String?, // oreo cookies
                @SerializedName("name")
                val name: String? // oreo cookies
            )

            data class Length(
                @SerializedName("number")
                val number: Int?, // 10
                @SerializedName("unit")
                val unit: String? // minutes
            )
        }
    }

    data class ExtendedIngredient(
        @SerializedName("aisle")
        val aisle: String?, // Sweet Snacks
        @SerializedName("amount")
        val amount: Double?, // 36.0
        @SerializedName("consistency")
        val consistency: String?, // SOLID
        @SerializedName("id")
        val id: Int?, // 10018166
        @SerializedName("image")
        val image: String?, // oreos.png
        @SerializedName("measures")
        val measures: Measures?,
        @SerializedName("meta")
        val meta: List<String?>?,
        @SerializedName("name")
        val name: String?, // oreo cookies 3 cups
        @SerializedName("nameClean")
        val nameClean: String?, // oreo cookies
        @SerializedName("original")
        val original: String?, // 36 OREO cookies finely crushed about 3 cups
        @SerializedName("originalName")
        val originalName: String?, // OREO cookies finely crushed about 3 cups
        @SerializedName("unit")
        val unit: String?
    ) {
        data class Measures(
            @SerializedName("metric")
            val metric: Metric?,
            @SerializedName("us")
            val us: Us?
        ) {
            data class Metric(
                @SerializedName("amount")
                val amount: Double?, // 36.0
                @SerializedName("unitLong")
                val unitLong: String?,
                @SerializedName("unitShort")
                val unitShort: String?
            )

            data class Us(
                @SerializedName("amount")
                val amount: Double?, // 36.0
                @SerializedName("unitLong")
                val unitLong: String?,
                @SerializedName("unitShort")
                val unitShort: String?
            )
        }
    }

    data class WinePairing(
        @SerializedName("pairedWines")
        val pairedWines: List<String?>?,
        @SerializedName("pairingText")
        val pairingText: String?, // Oreo Cookies works really well with Wine, Alcoholic Drink, and Ingredient. A common wine pairing rule is to make sure your wine is sweeter than your food. Delicate desserts go well with Moscato d'Asti, nutty desserts with cream sherry, and caramel or chocolate desserts pair well with port. The Engel Wines Riesling - Kosher with a 5 out of 5 star rating seems like a good match. It costs about 20 dollars per bottle.
        @SerializedName("productMatches")
        val productMatches: List<ProductMatche?>?
    ) {
        data class ProductMatche(
            @SerializedName("averageRating")
            val averageRating: Double?, // 1.0
            @SerializedName("description")
            val description: String?, // A lively off-dry Riesling, with accents of green apple, citrus and tropical fruits, complemented by delicate floral aromas, and a crisp acidity. Enjoy this wine chilled.
            @SerializedName("id")
            val id: Int?, // 431252
            @SerializedName("imageUrl")
            val imageUrl: String?, // https://spoonacular.com/productImages/431252-312x231.jpg
            @SerializedName("link")
            val link: String?, // https://www.amazon.com/Engel-Wines-Finger-Lakes-Riesling/dp/B06WRRCM11?tag=spoonacular-20
            @SerializedName("price")
            val price: String?, // $19.989999771118164
            @SerializedName("ratingCount")
            val ratingCount: Double?, // 1.0
            @SerializedName("score")
            val score: Double?, // 0.75
            @SerializedName("title")
            val title: String? // Engel Wines Riesling - Kosher
        )
    }
}