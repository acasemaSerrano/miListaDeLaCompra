package com.acasema.listadelacompra.ui.listoflist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.adapter.AdapterList
import com.acasema.listadelacompra.databinding.FragmentRecyclerviewBasicBinding
import com.acasema.listadelacompra.ui.main.MainActivity
import com.acasema.listadelacompra.ui.controller.FabController
import com.acasema.listadelacompra.ui.controller.NavHeaderController
import kotlinx.coroutines.launch

class ListOfListFragment : Fragment() {

    lateinit var adapter: AdapterList

    private lateinit var listOfListViewModel: ListOfListViewModel
    private var _binding: FragmentRecyclerviewBasicBinding? = null

    private val binding get() = _binding!!

    //region Lifecycle
    override fun onResume() {
        super.onResume()
        val fab: FabController = (activity as MainActivity).fabController
        fab.setVisibility(true)

        fab.setImageResource(android.R.drawable.ic_input_add)
        fab.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_listOfListFragment_to_listCreationFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        val navHeader: NavHeaderController = (activity as MainActivity).navHeaderController
        navHeader.init()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        listOfListViewModel = ViewModelProvider(this)[ListOfListViewModel::class.java]

        _binding = FragmentRecyclerviewBasicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        adapter = AdapterList(requireContext())
        binding.recyclerView.adapter = adapter

        listOfListViewModel.getListLiveData().observe(viewLifecycleOwner, {adapter.setList(it)})

        lifecycleScope.launch {
            listOfListViewModel.updateList()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion

}