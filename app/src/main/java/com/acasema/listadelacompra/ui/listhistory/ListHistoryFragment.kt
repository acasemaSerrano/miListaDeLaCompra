package com.acasema.listadelacompra.ui.listhistory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.adapter.AdapterListHistory
import com.acasema.listadelacompra.databinding.FragmentRecyclerviewBasicBinding
import com.acasema.listadelacompra.ui.controller.ActionBarController
import com.acasema.listadelacompra.ui.controller.FabController
import com.acasema.listadelacompra.ui.main.MainActivity
import kotlinx.coroutines.launch

class ListHistoryFragment : Fragment() {

    lateinit var adapter: AdapterListHistory
    private lateinit var listName: String

    private lateinit var viewModel: ListHistoryViewModel
    private var _binding: FragmentRecyclerviewBasicBinding? = null

    private val binding get() = _binding!!

    //region Lifecycle
    override fun onResume() {
        super.onResume()

        val a: FabController = (activity as MainActivity).fabController
        a.setVisibility(false)

        val actionBar: ActionBarController = (activity as MainActivity).actionBarController
        actionBar.setTitle(getString(R.string.history) + listName)

    }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            viewModel = ViewModelProvider(this)[ListHistoryViewModel::class.java]


            _binding = FragmentRecyclerviewBasicBinding.inflate(inflater, container, false)

            return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listName = arguments?.getString(getString(R.string.KEY_BUNDLE_LISTNAME))!!
        val isOnline = arguments?.getBoolean(getString(R.string.KEY_BUNDLE_ONLINE))!!

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        adapter = AdapterListHistory(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.getListLiveData().observe(viewLifecycleOwner, {adapter.setList(it)})

        lifecycleScope.launch {
            viewModel.updateList(listName, isOnline)
        }

    }
    //endregion

}