package com.tpu.thetower.fragments.lvl4

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentBookBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl4BookFragment(
    private val pages: List<Int>,
    private val texts: List<Pair<String, String>>,
    private val hasLink: Boolean,
    private val hintManager: HintManager?
) : Fragment(R.layout.fragment_book), Hintable {

    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!

    private var currPageNumber = 0
    private var pageCount: Int = 0

    @Inject lateinit var dialogManager: DialogManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentBookBinding.bind(view)

        setListeners()
        binding.tvTitle.text = texts[0].first
        binding.tvTitle.visibility = View.VISIBLE
        binding.tvPageTextLeft.visibility = View.GONE
        binding.tvPageTextLeft.text = texts[0].first
        binding.tvPageTextRight.text = texts[0].second
        binding.tvPageLinkLeft.movementMethod = LinkMovementMethod.getInstance()
        binding.tvPageLinkRight.movementMethod = LinkMovementMethod.getInstance()
        binding.ivPage.setImageResource(pages[0])
        pageCount = maxOf(pages.size, texts.size)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {

        if (hasLink) {
            if (texts.size == 3) {
                binding.tvPageLinkRight.text = HtmlCompat.fromHtml(
                    getString(R.string.lvl4_book_help_link),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            } else {
                binding.tvPageLinkLeft.text = HtmlCompat.fromHtml(
                    getString(R.string.lvl4_book_babel_link),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
        }

        binding.ivPage.setOnTouchListener { iv, event ->
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

                val newPage = if (currPageNumber >= pages.size)
                    R.drawable.lvl4_book_blank else pages[currPageNumber]

                val newTextLeft = if (currPageNumber >= texts.size)
                    getString(R.string.blank)
                    else HtmlCompat.fromHtml(texts[currPageNumber].first, HtmlCompat.FROM_HTML_MODE_LEGACY)

                val newTextRight = if (currPageNumber >= texts.size)
                    getString(R.string.blank)
                    else HtmlCompat.fromHtml(texts[currPageNumber].second, HtmlCompat.FROM_HTML_MODE_LEGACY)

                if (currPageNumber == 0) {
                    binding.tvTitle.text = newTextLeft
                    binding.tvTitle.visibility = View.VISIBLE
                    binding.tvPageTextLeft.visibility = View.GONE
                } else {
                    binding.tvTitle.visibility = View.GONE
                    binding.tvPageTextLeft.visibility = View.VISIBLE
                }
                if (hasLink) {

                    if (texts.size == 3 && currPageNumber == 1) {
                        binding.tvPageLinkRight.visibility = View.VISIBLE
                        binding.tvPageLinkLeft.visibility = View.GONE
                    } else if (currPageNumber == 1) {
                        binding.tvPageLinkLeft.visibility = View.VISIBLE
                        binding.tvPageLinkRight.visibility = View.GONE
                    } else {
                        binding.tvPageLinkLeft.visibility = View.GONE
                        binding.tvPageLinkRight.visibility = View.GONE
                    }
                } else {
                    binding.tvPageLinkLeft.visibility = View.GONE
                    binding.tvPageLinkRight.visibility = View.GONE
                }
                binding.ivPage.setImageResource(newPage)
                binding.tvPageTextLeft.text = newTextLeft
                binding.tvPageTextRight.text = newTextRight
                return@setOnTouchListener true
            }
            true
        }
    }

    override fun useHint() {
        if (hintManager != null) {
            hintManager.useHint(requireActivity())
        } else {
            dialogManager.startDialog(requireActivity(), "hint_is_not_here")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}