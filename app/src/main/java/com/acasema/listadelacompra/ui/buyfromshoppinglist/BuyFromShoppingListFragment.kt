package com.acasema.listadelacompra.ui.buyfromshoppinglist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.ui.adapter.AdapterListBuy
import com.acasema.listadelacompra.databinding.FragmentRecyclerviewBasicBinding
import com.acasema.listadelacompra.ui.controller.ActionBarController
import com.acasema.listadelacompra.ui.controller.FabController
import com.acasema.listadelacompra.ui.main.MainActivity

/**
 * autor: acasema (alfonso)
 *  clase derivada de fragment: muestra la lista de la compra para comprar
 */
class BuyFromShoppingListFragment : Fragment() {

    lateinit var adapter: AdapterListBuy
    private lateinit var listName: String

    private lateinit var viewModel: BuyFromShoppingListViewModel
    private var _binding: FragmentRecyclerviewBasicBinding? = null

    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()

        val a: FabController = (activity as MainActivity).fabController
        a.setVisibility(false)

        val actionBar: ActionBarController = (activity as MainActivity).actionBarController
        actionBar.setTitle(listName)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this)[BuyFromShoppingListViewModel::class.java]

        _binding = FragmentRecyclerviewBasicBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listName = arguments?.getString(getString(R.string.KEY_BUNDLE_LISTNAME))!!
        viewModel.isOnline = arguments?.getBoolean(getString(R.string.KEY_BUNDLE_ONLINE))!!

        initAdapter()
        setObserves()

        viewModel.updateList(listName)

    }

    override fun onPause() {
        requireView().findNavController().popBackStack()
        super.onPause()
    }

    override fun onStop() {
        viewModel.cancel(listName)
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        adapter = AdapterListBuy(requireContext(), viewModel)
        binding.recyclerView.adapter = adapter
    }

    private fun setObserves() {
        viewModel.getListLiveData().observe(viewLifecycleOwner, {
            if(it.size == 0)
                binding.ivNotData.visibility = View.VISIBLE
            else
                binding.ivNotData.visibility = View.GONE
            adapter.setList(it)
        })
    }

}