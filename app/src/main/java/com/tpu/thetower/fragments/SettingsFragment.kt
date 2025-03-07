package com.tpu.thetower.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.tpu.thetower.FragmentManager
import com.tpu.thetower.R


class SettingsFragment : Fragment(R.layout.fragment_settings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val languageSpinner = view.findViewById<Spinner>(R.id.language_spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.language_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            languageSpinner.adapter = adapter
        }

        // Настройка кнопки "Назад"
        val backButton = view.findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener {
            // Возврат к предыдущему экрану
            FragmentManager.goBack(this)
        }
    }
}