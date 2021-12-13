package com.acasema.listadelacompra.ui.otherlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.acasema.listadelacompra.adapter.AdapterOtherList
import com.acasema.listadelacompra.databinding.FragmentRecyclerviewBasicBinding
import com.acasema.listadelacompra.ui.controller.FabController
import com.acasema.listadelacompra.ui.controller.NavHeaderController
import com.acasema.listadelacompra.ui.main.MainActivity

class OtherListsFragment: Fragment() {

    lateinit var adapter: AdapterOtherList

    private lateinit var ViewModel: OtherListsViewModel
    private var _binding: FragmentRecyclerviewBasicBinding? = null

    private val binding get() = _binding!!

    //region Lifecycle
    override fun onResume() {
        super.onResume()
        val fab: FabController = (activity as MainActivity).fabController
        fab.setVisibility(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        ViewModel = ViewModelProvider(this)[OtherListsViewModel::class.java]

        _binding = FragmentRecyclerviewBasicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        adapter = AdapterOtherList(requireContext())
        binding.recyclerView.adapter = adapter

        ViewModel.getListLiveData().observe(viewLifecycleOwner, {adapter.setList(it)})

        ViewModel.updateList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion
}