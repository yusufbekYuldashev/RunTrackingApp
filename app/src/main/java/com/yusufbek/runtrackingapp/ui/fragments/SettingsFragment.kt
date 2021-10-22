package com.yusufbek.runtrackingapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.yusufbek.runtrackingapp.R
import com.yusufbek.runtrackingapp.databinding.FragmentSettingsBinding
import com.yusufbek.runtrackingapp.other.Constants.KEY_NAME
import com.yusufbek.runtrackingapp.other.Constants.KEY_WEIGHT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFieldFromSharedPref()

        binding.btnApplyChanges.setOnClickListener {
            if (applyChangesToSharedPref()) {
                Snackbar.make(requireView(), "Changes are saved", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(requireView(), "All fields must be entered", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun loadFieldFromSharedPref() {
        binding.etName.setText(sharedPreferences.getString(KEY_NAME, ""))
        binding.etWeight.setText(sharedPreferences.getFloat(KEY_WEIGHT, 80f).toString())
    }

    private fun applyChangesToSharedPref(): Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()
        if (name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPreferences.edit().putString(KEY_NAME, name).putFloat(KEY_WEIGHT, weight.toFloat())
            .apply()

        "Let's go $name".also {
            requireActivity().findViewById<TextView>(R.id.tvToolbarTitle).text = it
        }

        return true
    }
}