package com.mandriv.keepfit.utilities

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mandriv.keepfit.R

open class FragmentWithSettingsMenu : Fragment() {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.settings_item -> {
                findNavController().navigate(R.id.open_settings)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}