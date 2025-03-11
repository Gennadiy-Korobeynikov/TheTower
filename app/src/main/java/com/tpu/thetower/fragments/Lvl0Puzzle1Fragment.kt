package com.tpu.thetower.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.adapters.ImageCodeAdapter
import com.tpu.thetower.databinding.FragmentLvl0Puzzle1Binding
import com.tpu.thetower.puzzles.Lvl0Puzzle1


class Lvl0Puzzle1Fragment : Fragment(R.layout.fragment_lvl0_puzzle1) {

    private lateinit var binding : FragmentLvl0Puzzle1Binding

    private lateinit var rv1 : RecyclerView
    private lateinit var rv2 : RecyclerView
    private lateinit var rv3 : RecyclerView
    private lateinit var rv4 : RecyclerView

    private val puzzle: Puzzle = Lvl0Puzzle1("Lvl0Puzzle1")

    private var solution = charArrayOf('0','0','0','0')

    private val images = arrayOf(arrayOf(R.drawable.lvl0_puzzle1_num_0, R.drawable.lvl0_puzzle1_num_1, R.drawable.lvl0_puzzle1_num_2, R.drawable.lvl0_puzzle1_num_3, R.drawable.lvl0_puzzle1_num_4_2, R.drawable.lvl0_puzzle1_num_5, R.drawable.lvl0_puzzle1_num_6, R.drawable.lvl0_puzzle1_num_7, R.drawable.lvl0_puzzle1_num_8, R.drawable.lvl0_puzzle1_num_9),
        arrayOf(R.drawable.lvl0_puzzle1_num_0, R.drawable.lvl0_puzzle1_num_1, R.drawable.lvl0_puzzle1_num_2, R.drawable.lvl0_puzzle1_num_3, R.drawable.lvl0_puzzle1_num_4, R.drawable.lvl0_puzzle1_num_5, R.drawable.lvl0_puzzle1_num_6_1, R.drawable.lvl0_puzzle1_num_7, R.drawable.lvl0_puzzle1_num_8, R.drawable.lvl0_puzzle1_num_9),
        arrayOf(R.drawable.lvl0_puzzle1_num_0, R.drawable.lvl0_puzzle1_num_1_1, R.drawable.lvl0_puzzle1_num_2, R.drawable.lvl0_puzzle1_num_3, R.drawable.lvl0_puzzle1_num_4_1, R.drawable.lvl0_puzzle1_num_5, R.drawable.lvl0_puzzle1_num_6, R.drawable.lvl0_puzzle1_num_7, R.drawable.lvl0_puzzle1_num_8, R.drawable.lvl0_puzzle1_num_9),
        arrayOf(R.drawable.lvl0_puzzle1_num_0, R.drawable.lvl0_puzzle1_num_1, R.drawable.lvl0_puzzle1_num_2, R.drawable.lvl0_puzzle1_num_3, R.drawable.lvl0_puzzle1_num_4, R.drawable.lvl0_puzzle1_num_5, R.drawable.lvl0_puzzle1_num_6, R.drawable.lvl0_puzzle1_num_7_1, R.drawable.lvl0_puzzle1_num_8, R.drawable.lvl0_puzzle1_num_9))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl0Puzzle1Binding.bind(view)
        bindView()
        setupWheels(images)

    }

    private fun bindView(){
        rv1 = binding.rvImage1
        rv2 = binding.rvImage2
        rv3 = binding.rvImage3
        rv4 = binding.rvImage4


    }


    private fun setupWheels(data: Array<Array<Int>>){
        setupWheel(rv1, data[0], 0)
        setupWheel(rv2, data[1], 1)
        setupWheel(rv3, data[2], 2)
        setupWheel(rv4, data[3], 3)
    }

    private fun setupWheel(rv: RecyclerView, data: Array<Int>, rvIndex: Int){
        val adapter = ImageCodeAdapter(data)
        rv.adapter = adapter

        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv.layoutManager = layoutManager

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rv)
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                super.onScrollStateChanged(rv, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView = snapHelper.findSnapView(layoutManager) ?: return
                    val position = layoutManager.getPosition(centerView)
                    if (position != RecyclerView.NO_POSITION) {
                        val digit = position % 10
                        solution[rvIndex] = digit.digitToChar()

                        Log.i("Puzzle1", "Колесо $rvIndex выбрало цифру $digit")
                        val isSolved = puzzle.checkSolution(String(solution))
                        Log.i("Puzzle1", "Код ${String(solution)}: $isSolved")
                    }
                }
            }
        })
        rv.post {
            val midPosition = adapter.itemCount / 2
            layoutManager.scrollToPositionWithOffset(midPosition, 0)
        }

    }

    private fun solved(){
        TODO("Not implemented")
    }

}