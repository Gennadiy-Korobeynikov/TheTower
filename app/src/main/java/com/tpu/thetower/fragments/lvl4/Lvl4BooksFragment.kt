package com.tpu.thetower.fragments.lvl4
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl4BooksFragment : Fragment(R.layout.fragment_books) {

    // lateinit var binding : FragmentBookBinding

    private lateinit var bookPages: Map<String, List<Int>>
    private lateinit var bookTexts: Map<String, List<Pair<String, String>>>
    private lateinit var bookHints: Map<String, HintManager?>
    private lateinit var bookHasLink: Map<String, Boolean>

    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

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
                R.drawable.lvl4_book_blue,
                R.drawable.lvl4_book_babel1,
                R.drawable.lvl4_book_babel2,
            ),
            "askii_a" to  listOf(
                R.drawable.lvl4_book_orange,
            ),
            "askii_b" to  listOf(
                R.drawable.lvl4_book_orange,

            ),
            "qr" to  listOf(
                R.drawable.lvl4_book_red,
                R.drawable.lvl4_qr3,
                R.drawable.lvl4_qr1,
                R.drawable.lvl4_qr2,
            ),
            "blur" to  listOf(
                R.drawable.lvl4_book_blue,
                R.drawable.lvl4_book_blur1,
                R.drawable.lvl4_book_blur2,
            ),
            "history" to  listOf(
                R.drawable.lvl4_book_purple,
            ),
            "help" to  listOf(
                R.drawable.lvl4_book_green,
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


        bookHints = mapOf(
            "babel" to hintManagerFactory.create(
                hints = listOf(
                    "lvl4_book_babel_hint1","lvl4_book_babel_hint2",
                    "lvl4_book_babel_hint3" ,"lvl4_book_babel_hint4", "lvl4_book_babel_hint5"
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
                hints = listOf("lvl4_book_qr_hint1", "lvl4_book_qr_hint2", "lvl4_book_qr_hint3","lvl4_book_qr_hint4"),
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
                    "lvl4_book_history_hint4","lvl4_book_history_hint5", "lvl4_book_history_hint6"
                ),
                level = 4,
                puzzle = "book_history"
            ),
            "help" to null
        )

        requireActivity().supportFragmentManager
            .setFragmentResultListener("bookOpening", viewLifecycleOwner) { _, bundle ->
                val book = bundle.getString("book") ?: ""
                openBook(book)
            }

    }

    private fun openBook(book : String) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fcv_book,
                Lvl4BookFragment(bookPages[book]!!, bookTexts[book]!!, bookHasLink[book]!!, bookHints[book]),
                "BookFragment"
            )
            .commit()
    }

}