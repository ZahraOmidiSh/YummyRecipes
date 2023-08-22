package com.zahra.yummyrecipes.models.detail


import com.google.gson.annotations.SerializedName

data class ResponseNutrient(
    @SerializedName("bad")
    val bad: List<Bad?>?,
    @SerializedName("caloricBreakdown")
    val caloricBreakdown: CaloricBreakdown?,
    @SerializedName("calories")
    val calories: String?, // 899
    @SerializedName("carbs")
    val carbs: String?, // 111g
    @SerializedName("expires")
    val expires: Long?, // 1692095820085
    @SerializedName("fat")
    val fat: String?, // 45g
    @SerializedName("flavonoids")
    val flavonoids: List<Flavonoid?>?,
    @SerializedName("good")
    val good: List<Good?>?,
    @SerializedName("ingredients")
    val ingredients: List<Ingredient?>?,
    @SerializedName("isStale")
    val isStale: Boolean?, // true
    @SerializedName("nutrients")
    val nutrients: List<Nutrient?>?,
    @SerializedName("properties")
    val properties: List<Property?>?,
    @SerializedName("protein")
    val protein: String?, // 11g
    @SerializedName("weightPerServing")
    val weightPerServing: WeightPerServing?
) {
    data class Bad(
        @SerializedName("amount")
        val amount: String?, // 899
        @SerializedName("indented")
        val indented: Boolean?, // false
        @SerializedName("percentOfDailyNeeds")
        val percentOfDailyNeeds: Double?, // 44.96
        @SerializedName("title")
        val title: String? // Calories
    )

    data class CaloricBreakdown(
        @SerializedName("percentCarbs")
        val percentCarbs: Double?, // 49.47
        @SerializedName("percentFat")
        val percentFat: Double?, // 45.35
        @SerializedName("percentProtein")
        val percentProtein: Double? // 5.18
    )

    data class Flavonoid(
        @SerializedName("amount")
        val amount: Double?, // 2.35
        @SerializedName("name")
        val name: String?, // Cyanidin
        @SerializedName("unit")
        val unit: String? // mg
    )

    data class Good(
        @SerializedName("amount")
        val amount: String?, // 11g
        @SerializedName("indented")
        val indented: Boolean?, // false
        @SerializedName("percentOfDailyNeeds")
        val percentOfDailyNeeds: Double?, // 23.28
        @SerializedName("title")
        val title: String? // Protein
    )

    data class Ingredient(
        @SerializedName("amount")
        val amount: Double?, // 0.19
        @SerializedName("id")
        val id: Int?, // 9050
        @SerializedName("name")
        val name: String?, // blueberries
        @SerializedName("nutrients")
        val nutrients: List<Nutrient?>?,
        @SerializedName("unit")
        val unit: String? // cups
    ) {
        data class Nutrient(
            @SerializedName("amount")
            val amount: Double?, // 0.01
            @SerializedName("name")
            val name: String?, // Mono Unsaturated Fat
            @SerializedName("percentOfDailyNeeds")
            val percentOfDailyNeeds: Double?, // 24.69
            @SerializedName("unit")
            val unit: String? // g
        )
    }

    data class Nutrient(
        @SerializedName("amount")
        val amount: Double?, // 899.16
        @SerializedName("name")
        val name: String?, // Calories
        @SerializedName("percentOfDailyNeeds")
        val percentOfDailyNeeds: Double?, // 44.96
        @SerializedName("unit")
        val unit: String? // kcal
    )

    data class Property(
        @SerializedName("amount")
        val amount: Double?, // 33.51
        @SerializedName("name")
        val name: String?, // Glycemic Index
        @SerializedName("unit")
        val unit: String?
    )

    data class WeightPerServing(
        @SerializedName("amount")
        val amount: Int?, // 265
        @SerializedName("unit")
        val unit: String? // g
    )
}