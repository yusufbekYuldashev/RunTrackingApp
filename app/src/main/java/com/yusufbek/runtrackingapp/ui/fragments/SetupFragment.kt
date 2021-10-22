package com.yusufbek.runtrackingapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yusufbek.runtrackingapp.R
import com.yusufbek.runtrackingapp.databinding.FragmentSetupBinding
import com.yusufbek.runtrackingapp.other.Constants.KEY_FIRST_RUN
import com.yusufbek.runtrackingapp.other.Constants.KEY_NAME
import com.yusufbek.runtrackingapp.other.Constants.KEY_WEIGHT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment() {

    private lateinit var binding: FragmentSetupBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @set:Inject
    var isFirstRun = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetupBinding.inflate(inflater, container, false)

        if (!isFirstRun) {
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.runFragment, true).build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }

        binding.tvContinue.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if (success) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            } else {
                Snackbar.make(requireView(), "Enter all details", Snackbar.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun writePersonalDataToSharedPref(): Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()
        if (name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPreferences.edit()
            .putBoolean(KEY_FIRST_RUN, false)
            .putString(KEY_NAME, name)
            .putFloat(
                KEY_WEIGHT, weight.toFloat()
            ).apply()

        val toolbarText = "Let's go $name"
        requireActivity().findViewById<TextView>(R.id.tvToolbarTitle).text = toolbarText
        return true
    }

}