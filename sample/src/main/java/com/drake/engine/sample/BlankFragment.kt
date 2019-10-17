package com.drake.engine.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drake.engine.base.EngineNavFragment
import com.drake.engine.sample.databinding.FragmentBlankBinding


class BlankFragment : EngineNavFragment<FragmentBlankBinding>() {

    override fun initView() {
    }

    override fun initData() {
    }

    override fun view(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

}
