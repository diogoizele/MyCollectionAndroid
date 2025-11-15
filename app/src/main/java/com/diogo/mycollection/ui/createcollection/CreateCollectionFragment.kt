package com.diogo.mycollection.ui.createcollection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.diogo.mycollection.R
import com.diogo.mycollection.databinding.FragmentCreateCollectionBinding

class CreateCollectionFragment : Fragment() {

    private var _binding: FragmentCreateCollectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateCollectionBinding.inflate(inflater, container, false)

        val root = binding.root

        return root
    }
}