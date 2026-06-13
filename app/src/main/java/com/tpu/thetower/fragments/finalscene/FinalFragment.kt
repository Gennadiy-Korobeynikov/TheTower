package com.tpu.thetower.fragments.finalscene

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import androidx.core.animation.addListener
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentFinalBinding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.utils.SoundEffect
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

                        val root = binding.root

                        val scaleUp = ObjectAnimator.ofPropertyValuesHolder(root,
                            PropertyValuesHolder.ofFloat("scaleX", 1f, 1.04f),
                            PropertyValuesHolder.ofFloat("scaleY", 1f, 1.04f)
                        ).apply { duration = 150; startDelay = 200 }
                        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(root,
                            PropertyValuesHolder.ofFloat("scaleX", 1.04f, 1f),
                            PropertyValuesHolder.ofFloat("scaleY", 1.04f, 1f)
                        ).apply { duration = 100; startDelay = 350 }

                        val flash = binding.viewFlash
                        val flashIn  = ObjectAnimator.ofFloat(flash, "alpha", 0f, 1f).apply { duration = 60; startDelay = 320 }
                        val flashOut = ObjectAnimator.ofFloat(flash, "alpha", 1f, 0f).apply {
                            duration = 300; startDelay = 380
                            addListener(
                                onStart = {
                                    binding.ivBg.setImageResource(R.drawable.final_titles)
                                    UiVisibilityController.hide(requireActivity(), UiVisibilityController.UiContainer.GO_BACK_ARROW)
                                },
                                onEnd = {
                                    binding.root.postDelayed(1500) {
                                        binding.ivBg.setOnClickListener {
                                            FragmentNavigation.changeBG(this@FinalFragment, R.id.action_global_titleScreenFragment)
                                            musicManager.playMusic(R.raw.soundtrack_1)
                                            UiVisibilityController.show(requireActivity(), UiVisibilityController.UiContainer.TITLE)
                                        }
                                    }
                                }
                            )
                        }

                        AnimatorSet().apply {
                            playTogether(scaleUp, scaleDown, flashIn, flashOut)
                            start()
                            soundManager.playSound(SoundEffect.CROW)
                        }
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