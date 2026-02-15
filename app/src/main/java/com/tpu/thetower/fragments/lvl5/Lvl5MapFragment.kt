package com.tpu.thetower.fragments.lvl5

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl5MapBinding
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl5MapFragment : Fragment(R.layout.fragment_lvl5_map), Hintable {

    private var _binding: FragmentLvl5MapBinding? = null
    private val binding get() = _binding!!


    private lateinit var hintManager: HintManager

//    @Inject lateinit var saveRepo : SaveRepository
//    @Inject lateinit var soundManager : SoundManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLvl5MapBinding.bind(view)

        setListeners()
    }

    private fun setListeners() {
        binding.mainScreen.setOnClickListener {
            FragmentNavigation.goBack(this)
        }
    }



    override fun useHint() {
        // todo
    }

    override fun skipPuzzle() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}