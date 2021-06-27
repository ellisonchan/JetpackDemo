package com.ellison.jetpackdemo.cameraX.analysis

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ellison.jetpackdemo.R
import com.ellison.jetpackdemo.cameraX.analysis.ChooseAnalysisAdapter.AnalysisChooseListener
import com.ellison.jetpackdemo.cameraX.utils.Constants
import com.ellison.jetpackdemo.cameraX.viewmodel.CameraViewModel
import com.ellison.jetpackdemo.databinding.FragmentAnalysisListBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseAnalysisFragment: BottomSheetDialogFragment() {
    private val viewModel: CameraViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(Constants.TAG_CAMERA_UI, "onCreateView")
        val binding = FragmentAnalysisListBinding.inflate(inflater, container, false)
        initAnalysisList(binding.analysisList)
        return binding.root
    }

    override fun onStart() {
        Log.d(Constants.TAG_CAMERA, "onStart()")
        super.onStart()

        dialog?.let{
            // Ensure status bar not shown.
            it.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val bottomView: FrameLayout = it.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            bottomView.setBackgroundResource(R.drawable.ic_analysis_picker_bg)
        }
    }

    private fun initAnalysisList(recyclerView: RecyclerView) {
        Log.d(Constants.TAG_CAMERA, "initAnalysisList() viewModel:$viewModel")
        val layoutManager = LinearLayoutManager(activity)
        isCancelable = false

        recyclerView.run{
            setLayoutManager(layoutManager)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            adapter = ChooseAnalysisAdapter(typeList, object : AnalysisChooseListener {
                override fun onAnalysisChoose(type: AnalysisType) {
                    Log.d(Constants.TAG_CAMERA, "onAnalysisChoose() type:$type")
                    viewModel.chooseAnalysis(type.clazz)
                    dismissAllowingStateLoss()
                }
            })
        }
    }
}