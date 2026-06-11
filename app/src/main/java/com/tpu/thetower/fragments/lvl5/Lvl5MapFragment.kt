package com.tpu.thetower.fragments.lvl5

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl5MapBinding
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.utils.getOrCreateBlur
import com.tpu.thetower.viewmodels.BlurViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl5MapFragment : Fragment(R.layout.fragment_lvl5_map), Hintable {

    private var _binding: FragmentLvl5MapBinding? = null
    private val binding get() = _binding!!

    private val blurVM: BlurViewModel by navGraphViewModels(R.id.nav_lvl5)

    private lateinit var hintManager: HintManager

    @Inject lateinit var hintManagerFactory: HintManager.Factory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLvl5MapBinding.bind(view)

        val levelSnapshot = blurVM.getBlur(Lvl5Fragment.KEY_LVL5_SNAPSHOT)
            ?: error("Snapshot must be set before opening puzzle")

        val blur = getOrCreateBlur(
            blurVM = blurVM,
            blurKey = Lvl5Fragment.KEY_LVL5_BLUR,
            sourceBitmap = levelSnapshot,
            radius = 220f,
            context = requireContext()
        )

        binding.ivBg.setImageBitmap(blur)

        hintManager = hintManagerFactory.create(
            hints = listOf("lvl5_map_hint1", "lvl5_map_hint2", "lvl5_map_hint3"),
            level = 5,
            puzzle = "map"
        )

        setListeners()
    }

    private fun setListeners() {
        binding.mainScreen.setOnClickListener {
            FragmentNavigation.goBack(this)
        }
    }


    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    override fun skipPuzzle() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}