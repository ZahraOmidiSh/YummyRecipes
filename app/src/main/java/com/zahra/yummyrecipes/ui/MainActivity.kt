package com.zahra.yummyrecipes.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.ActivityMainBinding
import com.zahra.yummyrecipes.utils.BaseActivity
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    //Binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    //Other
    private var isThemeChanged: Boolean = false
    private lateinit var viewModel: SearchViewModel
    private lateinit var navHost: NavHostFragment
    private val callback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            if (navHost.navController.currentDestination?.id == R.id.recipeFragment) {
                finish()
            } else {
                navHost.navController.navigate(R.id.actionToRecipe)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the theme change state
        outState.putBoolean("themeChanged", true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            // Check if the activity is being recreated due to a theme change
            isThemeChanged = savedInstanceState.getBoolean("themeChanged", false)
        }
        if (!isThemeChanged) {
            // This line will only execute if the activity is not recreated due to a theme change
            viewModel.loadExpandedIngredientsList()
        }
        //load data

        viewModel.expandedIngredientsList.observe(this) { expandedIngredients ->
        }
        //onBackPress
        onBackPressedDispatcher.addCallback(this, callback)
        //Setup nav host
        navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        binding.mainBottomNav.background = null
        binding.mainBottomNav.setupWithNavController(navHost.navController)
        //Gone bottom menu
        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> visibilityBottomMenu(false)
                R.id.registerFragment -> visibilityBottomMenu(false)
                R.id.webViewFragment -> visibilityBottomMenu(false)

                R.id.detailFragment -> {
                    visibilityBottomMenu(false)
                    if (callback.isEnabled) {
                        callback.isEnabled = false
                    }

                }

                R.id.searchAllIngredientsFragment -> {
                    visibilityBottomMenu(false)
                    if (callback.isEnabled) {
                        callback.isEnabled = false
                    }

                }

                R.id.dietsFragment -> {
                    visibilityBottomMenu(false)
                    if (callback.isEnabled) {
                        callback.isEnabled = false
                    }

                }

                R.id.filtersFragment -> {
                    visibilityBottomMenu(false)
                    if (callback.isEnabled) {
                        callback.isEnabled = false
                    }

                }

                R.id.recipeFragment -> {
                    visibilityBottomMenu(true)
                    if (!callback.isEnabled) {
                        callback.isEnabled = true
                    }
                }

                R.id.searchFragment -> {
                    visibilityBottomMenu(true)
                    if (!callback.isEnabled) {
                        callback.isEnabled = true
                    }
                }

                R.id.collectionFragment -> {
                    visibilityBottomMenu(true)
                    if (!callback.isEnabled) {
                        callback.isEnabled = true
                    }
                }

                R.id.mealPlannerFragment -> {
                    visibilityBottomMenu(true)
                    if (!callback.isEnabled) {
                        callback.isEnabled = true
                    }
                }

                else -> visibilityBottomMenu(true)
            }
        }

    }

    private fun visibilityBottomMenu(isVisibility: Boolean) {
        binding.apply {
            if (isVisibility) {
                mainBottomAppbar.isVisible = true
                cartFab.isVisible = true
            } else {
                mainBottomAppbar.isVisible = false
                cartFab.isVisible = false
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        return navHost.navController.navigateUp() || super.onNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}