package com.tpu.thetower.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.tpu.thetower.FragmentManager

import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentBookBinding


class BookFragment(
    private val pages : List<Int>,
    private val texts : List<String>
) : Fragment(R.layout.fragment_book) {

    private lateinit var binding : FragmentBookBinding

    private var currPageNumber = 0

    private  lateinit var ivPage : ImageView
    private lateinit var tvText : TextView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBookBinding.bind(view)
        bindView()
        setListeners()
        tvText.text = texts[0]

    }

    private fun bindView() {
        ivPage = binding.ivPage
        tvText = binding.tvPageText

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {

        ivPage.setOnTouchListener { iv, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                iv.performClick()
                val width = iv.width
                val x = event.x

                if (x > width / 2) {
                    // Правая часть — вперёд
                    if (currPageNumber < pages.count() - 1) {
                        currPageNumber++
                    }

                } else {
                    // Левая часть — назад
                    if (currPageNumber > 0) {
                        currPageNumber--
                    }

                }
                ivPage.setImageResource(pages[currPageNumber])
                tvText.text = texts[currPageNumber]
                return@setOnTouchListener true
            }
            true
        }
    }

}