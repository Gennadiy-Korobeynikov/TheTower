package com.tpu.thetower.fragments.lvl4

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentBookBinding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl4BookFragment : Fragment(R.layout.fragment_book), Hintable {

    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!

    private var currPageNumber = 0
    private var pageCount: Int = 0

    @Inject lateinit var dialogManager: DialogManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory
    @Inject lateinit var loadManager: LoadManager

    private lateinit var bookKey: String

    private lateinit var pages: List<Int>
    private lateinit var texts: List<Pair<String, String>>
    private var hasLink: Boolean = false
    private var hints: HintManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = requireArguments()
        bookKey = args.getString(ARG_BOOK_KEY)
            ?: error("Lvl4BookFragment requires bookKey argument. Use Lvl4BookFragment.newInstance(bookKey).")

        // Сбор данных по ключу (без конструктора), чтобы переживать пересоздание активности
        buildBookContent(bookKey)
    }

    private fun buildBookContent(key: String) {
        val bookHasLink = mapOf(
            "babel" to true,
            "askii_a" to false,
            "askii_b" to false,
            "qr" to false,
            "blur" to false,
            "history" to false,
            "help" to true
        )

        val bookPages = mapOf(
            "babel" to listOf(
                R.drawable.lvl4_book_blue,
                R.drawable.lvl4_book_babel1,
                R.drawable.lvl4_book_babel2,
            ),
            "askii_a" to listOf(
                R.drawable.lvl4_book_orange,
            ),
            "askii_b" to listOf(
                R.drawable.lvl4_book_orange,
            ),
            "qr" to listOf(
                R.drawable.lvl4_book_red,
                R.drawable.lvl4_qr3,
                R.drawable.lvl4_qr1,
                R.drawable.lvl4_qr2,
            ),
            "blur" to listOf(
                R.drawable.lvl4_book_blue,
                R.drawable.lvl4_book_blur1,
                R.drawable.lvl4_book_blur2,
            ),
            "history" to listOf(
                R.drawable.lvl4_book_purple,
            ),
            "help" to listOf(
                R.drawable.lvl4_book_green,
                R.drawable.lvl4_book_blank,
                R.drawable.lvl4_book_help1,
            ),
        )

        val bookTexts = mapOf(
            "babel" to listOf(
                Pair(getString(R.string.lvl4_book_babel_title), ""),
                Pair(getString(R.string.lvl4_book_babel_text1), ""),
            ),
            "askii_a" to listOf(
                Pair(getString(R.string.lvl4_book_askii_title), ""),
                Pair(getString(R.string.lvl4_book_askii_text1_a), getString(R.string.lvl4_book_askii_text2_a)),
            ),
            "askii_b" to listOf(
                Pair(getString(R.string.lvl4_book_askii_title), ""),
                Pair(getString(R.string.lvl4_book_askii_text1_b), getString(R.string.lvl4_book_askii_text2_b)),
            ),
            "qr" to listOf(
                Pair(getString(R.string.lvl4_book_qr_title), ""),
                Pair("", getString(R.string.lvl4_book_qr_text1)),
            ),
            "blur" to listOf(
                Pair(getString(R.string.lvl4_book_blur_title), ""),
            ),
            "history" to listOf(
                Pair(getString(R.string.lvl4_book_history_title), ""),
                Pair(getString(R.string.lvl4_book_history_text1), getString(R.string.lvl4_book_history_text2)),
                Pair(getString(R.string.lvl4_book_history_text3), ""),
            ),
            "help" to listOf(
                Pair(getString(R.string.lvl4_book_help_title), ""),
                Pair(getString(R.string.lvl4_book_help_text1), getString(R.string.lvl4_book_help_text2)),
                Pair(getString(R.string.lvl4_book_help_text3), ""),
            ),
        )

        val bookHints = mapOf(
            "babel" to hintManagerFactory.create(
                hints = listOf(
                    "lvl4_book_babel_hint1", "lvl4_book_babel_hint2",
                    "lvl4_book_babel_hint3", "lvl4_book_babel_hint4", "lvl4_book_babel_hint5"
                ),
                level = 4,
                puzzle = "book_babel"
            ),
            "askii_a" to null,
            "askii_b" to hintManagerFactory.create(
                hints = listOf("lvl4_book_askii_hint1"),
                level = 4,
                puzzle = "book_askii"
            ),
            "qr" to hintManagerFactory.create(
                hints = listOf("lvl4_book_qr_hint1", "lvl4_book_qr_hint2", "lvl4_book_qr_hint3", "lvl4_book_qr_hint4"),
                level = 4,
                puzzle = "book_qr"
            ),
            "blur" to hintManagerFactory.create(
                hints = listOf("lvl4_book_blur_hint1", "lvl4_book_blur_hint2", "lvl4_book_blur_hint3"),
                level = 4,
                puzzle = "book_blur"
            ),
            "history" to hintManagerFactory.create(
                hints = listOf(
                    "lvl4_book_history_hint1", "lvl4_book_history_hint2", "lvl4_book_history_hint3",
                    "lvl4_book_history_hint4", "lvl4_book_history_hint5", "lvl4_book_history_hint6"
                ),
                level = 4,
                puzzle = "book_history"
            ),
            "help" to null
        )

        hasLink = bookHasLink[key] ?: false
        pages = bookPages[key] ?: error("Unknown book key=$key")
        texts = bookTexts[key] ?: error("Unknown book key=$key")
        hints = bookHints[key]
    }

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
        if (hints != null) {
            hints!!.useHint(requireActivity())
        } else {
            dialogManager.startDialog(requireActivity(), "hint_is_not_here")
        }
    }

    override fun skipPuzzle() {
        Snackbar.make(requireView(), "Книги можно не смотреть", Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_BOOK_KEY = "book_key"

        fun newInstance(bookKey: String): Lvl4BookFragment {
            return Lvl4BookFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_BOOK_KEY, bookKey)
                }
            }
        }
    }
}