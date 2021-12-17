package com.acasema.listadelacompra.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation

import com.google.android.material.navigation.NavigationView

import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.databinding.ActivityMainBinding
import com.acasema.listadelacompra.service.FirebaseFirestoreService
import com.acasema.listadelacompra.ui.controller.ActionBarController
import com.acasema.listadelacompra.ui.controller.FabController
import com.acasema.listadelacompra.ui.controller.NavHeaderController
import com.acasema.listadelacompra.utils.Miscellany
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.RuntimeException

/**
 * autor: acasema (alfonso)
 *  clase derivada de AppCompatActivity: es la base de la aplicación
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            if (!Miscellany.isOnlineNet()){
                showErrorNotNotAvailable()
            }
        }

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.listOfListFragment, R.id.otherListsFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        liveDataObserve()

        binding.navView.menu.findItem(R.id.itSignOff).setOnMenuItemClickListener {
            signOff()
            return@setOnMenuItemClickListener true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }
    //endregion

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun signOff() {
        //bloque de código para cerrar sesión
        mainViewModel.signOff()

        val prefs =
            this.getSharedPreferences(getString(R.string.prefs_file_key), MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()

        Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
            .navigate(R.id.loginFragment)

        //cerrar el menu
        binding.drawerLayout.closeDrawers()
    }

    private fun liveDataObserve() {

        val header: View = binding.navView.getHeaderView(0)

        val textEmail: TextView = header.findViewById(R.id.tvEmail)
        val textName: TextView = header.findViewById(R.id.tvName)
        val imageView: ImageView = header.findViewById(R.id.imageView)

        mainViewModel.emailLiveData().observe(this, {
            textEmail.text = it
        })

        mainViewModel.nameLiveData().observe(this, {
            textName.text = it
        })
        mainViewModel.photoUserLiveData().observe(this, {
            imageView.setImageDrawable(it)
        })
    }


    //region Controller
    var fabController = object : FabController {
        override fun setOnClickListener(listener: View.OnClickListener) {
            binding.appBarMain.fab.setOnClickListener(listener)
        }

        override fun setVisibility(isVisibility: Boolean) {
            if(isVisibility)
                binding.appBarMain.fab.visibility = View.VISIBLE
            else
                binding.appBarMain.fab.visibility = View.GONE
        }

        override fun setImageResource(resource: Int) {
            binding.appBarMain.fab.setImageResource(resource)
        }
    }

    fun showErrorNotNotAvailable() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.error_net_not_Available))
            .setOnDismissListener { finish() }
            .create().show()
    }

    var actionBarController = object : ActionBarController {
        override fun disable() {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setHomeButtonEnabled(false)
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
        override fun setTitle(title: String) {
            supportActionBar!!.title = title
        }
    }

    var navHeaderController = object : NavHeaderController{
        override fun init() {
            if (Miscellany.isOnlineNet()){
                mainViewModel.navHeaderInit(resources)
            }
        }
    }
    //endregion

}