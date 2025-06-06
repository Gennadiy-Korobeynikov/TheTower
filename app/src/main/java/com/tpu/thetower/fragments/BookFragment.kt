package com.tpu.thetower.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.tpu.thetower.DialogManager
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentBookBinding


class BookFragment(
    private val pages : List<Int>,
    private val texts : List<Pair<String, String>>,
    private val hintManager: HintManager?,
    private val hasLink: Boolean
) : Fragment(R.layout.fragment_book), Hintable {

    private lateinit var binding : FragmentBookBinding

    private var currPageNumber = 0

    private  lateinit var ivPage : ImageView
    private lateinit var tvTextLeft : TextView
    private lateinit var tvTextRight : TextView
    private lateinit var tvLinkLeft : TextView
    private lateinit var tvLinkRight : TextView
    private lateinit var tvTitle : TextView
    private var pageCount : Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBookBinding.bind(view)
        bindView()
        setListeners()
        tvTitle.text = texts[0].first
        tvTitle.visibility = View.VISIBLE
        tvTextLeft.visibility = View.GONE
        tvTextLeft.text = texts[0].first
        tvTextRight.text = texts[0].second
        tvLinkLeft.movementMethod = LinkMovementMethod.getInstance()
        tvLinkRight.movementMethod = LinkMovementMethod.getInstance()
        ivPage.setImageResource(pages[0])
        pageCount = maxOf(pages.size , texts.size)
    }

    private fun bindView() {
        ivPage = binding.ivPage
        tvTextLeft = binding.tvPageTextLeft
        tvTextRight = binding.tvPageTextRight
        tvLinkLeft = binding.tvPageLinkLeft
        tvLinkRight = binding.tvPageLinkRight
        tvTitle = binding.tvTitle
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {

        if (hasLink) {
            if (texts.size == 3) {
                tvLinkRight.text = HtmlCompat.fromHtml(getString(R.string.lvl4_book_help_link) , HtmlCompat.FROM_HTML_MODE_LEGACY)
            } else {
                tvLinkLeft.text = HtmlCompat.fromHtml(getString(R.string.lvl4_book_babel_link) , HtmlCompat.FROM_HTML_MODE_LEGACY)
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

                if(currPageNumber == 0) {
                    tvTitle.text = newTextLeft
                    tvTitle.visibility = View.VISIBLE
                    tvTextLeft.visibility = View.GONE
                }
                else{
                    tvTitle.visibility = View.GONE
                    tvTextLeft.visibility = View.VISIBLE
                }
                if (hasLink) {

                    if (texts.size == 3 && currPageNumber == 1) {
                        tvLinkRight.visibility = View.VISIBLE
                        tvLinkLeft.visibility = View.GONE
                    } else if (currPageNumber == 1) {
                        tvLinkLeft.visibility = View.VISIBLE
                        tvLinkRight.visibility = View.GONE
                    }
                    else{
                        tvLinkLeft.visibility = View.GONE
                        tvLinkRight.visibility = View.GONE
                    }
                }
                else {
                    tvLinkLeft.visibility = View.GONE
                    tvLinkRight.visibility = View.GONE
                }
                ivPage.setImageResource(newPage)
                tvTextLeft.text = newTextLeft
                tvTextRight.text = newTextRight
                return@setOnTouchListener true
            }
            true
        }
    }

    override fun useHint() {
        if (hintManager != null) {
            hintManager.useHint(requireActivity())
        }
        else
            DialogManager.startDialog(requireActivity(), "hint_is_not_here")
    }

}