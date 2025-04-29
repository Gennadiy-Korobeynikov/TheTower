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
    private lateinit var  bookTexts : Map<String, List<String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding = FragmentBookBinding.bind(view)

        bookPages = mapOf(
            "babel" to listOf(
                R.drawable.page1_test,
                R.drawable.page2_test,
                R.drawable.page3_test,
            ),
            "askii_a" to  listOf(
                R.drawable.page1_test,
                R.drawable.page2_test,
            ),
            "askii_b" to  listOf(
                R.drawable.page1_test,
                R.drawable.page2_test,
            ),
        )

        bookTexts = mapOf(
            "babel" to listOf(
                getString(R.string.lvl4_book_babel_text1),
                getString(R.string.lvl4_book_babel_text2),
                getString(R.string.lvl4_book_babel_text3),
            ),
            "askii_a" to listOf(
                getString(R.string.lvl4_book_askii_text1_a),
                getString(R.string.lvl4_book_askii_text2_a),
            ),
            "askii_b" to listOf(
                getString(R.string.lvl4_book_askii_text1_b),
                getString(R.string.lvl4_book_askii_text2_b),
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
            .replace(R.id.fcv_book, BookFragment(bookPages[book]!!, bookTexts[book]!!), "BookFragment")
            .commit()
    }




}