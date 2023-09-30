package com.zahra.yummyrecipes.ui
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.ActivityMainBinding
import com.zahra.yummyrecipes.utils.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    //Binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    //Other
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
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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