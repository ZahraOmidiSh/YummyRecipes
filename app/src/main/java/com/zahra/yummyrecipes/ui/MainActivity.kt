package com.zahra.yummyrecipes.ui
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.ActivityMainBinding
import com.zahra.yummyrecipes.utils.BaseActivity
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    //Binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    //Other
    private lateinit var viewModel: SearchViewModel
    private lateinit var navHost : NavHostFragment
    private val callback = object : OnBackPressedCallback(false){
        override fun handleOnBackPressed() {
            if (navHost.navController.currentDestination?.id == R.id.recipeFragment){
                finish()
            }else{
                navHost.navController.navigate(R.id.actionToRecipe)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //load data
        viewModel.loadExpandedIngredientsList()
        viewModel.expandedIngredientsList.observe(this, Observer { expandedIngredients ->
            Log.e("viewModel", expandedIngredients.toString() )
        })
        //onBackPress
        onBackPressedDispatcher.addCallback(this,callback)
        //Setup nav host
        navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        binding.mainBottomNav.background=null
        binding.mainBottomNav.setupWithNavController(navHost.navController)
        //Gone bottom menu
        navHost.navController.addOnDestinationChangedListener{_,destination,_ ->
            when(destination.id){
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
                R.id.recipeFragment ->{
                    visibilityBottomMenu(true)
                    if (!callback.isEnabled){
                        callback.isEnabled = true
                    }
                }
                R.id.searchFragment ->{
                    visibilityBottomMenu(true)
                    if (!callback.isEnabled){
                        callback.isEnabled = true
                    }
                }
                R.id.collectionFragment ->{
                    visibilityBottomMenu(true)
                    if (!callback.isEnabled){
                        callback.isEnabled = true
                    }
                }
                R.id.mealPlannerFragment->{
                    visibilityBottomMenu(true)
                    if (!callback.isEnabled){
                        callback.isEnabled = true
                    }
                }
                else -> visibilityBottomMenu(true)
            }
        }

    }
    private fun visibilityBottomMenu(isVisibility: Boolean){
        binding.apply {
            if(isVisibility){
                mainBottomAppbar.isVisible=true
                cartFab.isVisible=true
            }else{
                mainBottomAppbar.isVisible=false
                cartFab.isVisible=false
            }
        }
    }
    override fun onNavigateUp(): Boolean {
        return navHost.navController.navigateUp() || super.onNavigateUp()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}