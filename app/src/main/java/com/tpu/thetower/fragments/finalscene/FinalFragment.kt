package com.tpu.thetower.fragments.finalscene

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentFinalBinding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FinalFragment : Fragment(R.layout.fragment_final) {


    private var _binding: FragmentFinalBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var musicManager: MusicManager
    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFinalBinding.bind(view)


        dialogManager.startDialog(requireActivity(), "final_court") {
            binding.ivBg.setImageResource(R.drawable.final_judge2)
            dialogManager.startDialog(requireActivity(), "final_court_2") {
                binding.ivBg.setImageResource(R.drawable.final_judge3)
                dialogManager.startDialog(requireActivity(), "final_court_3") {
                    binding.ivBg.setImageResource(R.drawable.final_hospital)
                    dialogManager.startDialog(requireActivity(), "final_hospital") {
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}