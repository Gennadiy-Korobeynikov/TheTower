package com.tpu.thetower.fragments
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentBookBinding


class BooksFragment : Fragment(R.layout.fragment_books) {

    // lateinit var binding : FragmentBookBinding

    private lateinit var  bookPages : Map<String, List<Int>>
    private lateinit var  bookTexts : Map<String, List<Pair<String, String>>>
    private lateinit var  bookHasLink : Map<String, Boolean>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookHasLink = mapOf(
            "babel" to true,
            "askii_a" to false,
            "askii_b" to  false,
            "qr" to  false,
            "blur" to  false ,
            "history" to false,
            "help" to  true
        )

        bookPages = mapOf(
            "babel" to listOf(
                R.drawable.lvl4_book_cover_blue,
                R.drawable.lvl4_book_babel1,
                R.drawable.lvl4_book_babel2,
            ),
            "askii_a" to  listOf(
                R.drawable.lvl4_book_cover_blue,
            ),
            "askii_b" to  listOf(
                R.drawable.lvl4_book_cover_blue,

            ),
            "qr" to  listOf(
                R.drawable.lvl4_book_cover_blue,
                R.drawable.lvl4_qr3,
                R.drawable.lvl4_qr1,
                R.drawable.lvl4_qr2,
            ),
            "blur" to  listOf(
                R.drawable.lvl4_book_cover_blue,
                R.drawable.lvl4_book_blur1,
                R.drawable.lvl4_book_blur2,
            ),
            "history" to  listOf(
                R.drawable.lvl4_book_cover_blue,
            ),
            "help" to  listOf(
                R.drawable.lvl4_book_cover_blue,
                R.drawable.lvl4_book_blank,
                R.drawable.lvl4_book_help1,
            ),
        )

        bookTexts = mapOf(
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
                Pair("",getString(R.string.lvl4_book_qr_text1)),
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

        requireActivity().supportFragmentManager
            .setFragmentResultListener("bookOpening", viewLifecycleOwner) { _, bundle ->
                val book = bundle.getString("book") ?: ""
                openBook(book)
            }



    }

    private fun openBook(book : String) {

        parentFragmentManager.beginTransaction()
            .replace(R.id.fcv_book, BookFragment(bookPages[book]!!, bookTexts[book]!!, bookHasLink[book]!!), "BookFragment")
            .commit()
    }




}