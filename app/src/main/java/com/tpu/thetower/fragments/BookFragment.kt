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
import androidx.core.text.HtmlCompat
import com.tpu.thetower.FragmentManager

import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentBookBinding


class BookFragment(
    private val pages : List<Int>,
    private val texts : List<Pair<String, String>>,
    private val hasLink : Boolean
) : Fragment(R.layout.fragment_book) {

    private lateinit var binding : FragmentBookBinding

    private var currPageNumber = 0

    private  lateinit var ivPage : ImageView
    private lateinit var tvTextLeft : TextView
    private lateinit var tvTextRight : TextView
    private lateinit var btnLink : Button
    private var pageCount : Int = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBookBinding.bind(view)
        bindView()
        setListeners()
        tvTextLeft.text = texts[0].first
        tvTextRight.text = texts[0].second
        ivPage.setImageResource(pages[0])
        pageCount = maxOf(pages.size , texts.size)



    }

    private fun bindView() {
        ivPage = binding.ivPage
        tvTextLeft = binding.tvPageTextLeft
        tvTextRight = binding.tvPageTextRight
        btnLink = binding.btnLink

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {

        if (hasLink) {
            btnLink.visibility = View.VISIBLE
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
            val url = if (texts.size == 3) getString(R.string.lvl4_book_help_link) // Scan QR link
                else getString(R.string.lvl4_book_babel_link)                      // Babel link
            intent.data = android.net.Uri.parse(url)
            btnLink.setOnClickListener {
                startActivity(intent)
            }

        }


        ivPage.setOnTouchListener { iv, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                iv.performClick()
                val width = iv.width
                val x = event.x

                if (x > width / 2) {
                    // Правая часть — вперёд
                    if (currPageNumber < pageCount - 1) {
                        currPageNumber++
                    }

                } else {
                    // Левая часть — назад
                    if (currPageNumber > 0) {
                        currPageNumber--
                    }

                }

                val newPage = if (currPageNumber >= pages.size) R.drawable.lvl4_book_blank else pages[currPageNumber]

                val newTextLeft = if (currPageNumber >= texts.size) getString(R.string.blank)
                    else HtmlCompat.fromHtml(texts[currPageNumber].first , HtmlCompat.FROM_HTML_MODE_LEGACY)

                val newTextRight = if (currPageNumber >= texts.size) getString(R.string.blank)
                    else HtmlCompat.fromHtml(texts[currPageNumber].second, HtmlCompat.FROM_HTML_MODE_LEGACY)

                ivPage.setImageResource(newPage)
                tvTextLeft.text = newTextLeft
                tvTextRight.text = newTextRight
                return@setOnTouchListener true
            }
            true
        }
    }

}