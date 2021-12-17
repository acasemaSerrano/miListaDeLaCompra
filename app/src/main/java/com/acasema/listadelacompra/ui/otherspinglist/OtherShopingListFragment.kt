package com.acasema.listadelacompra.ui.otherspinglist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.ui.adapter.AdapterListView
import com.acasema.listadelacompra.data.model.Permissions
import com.acasema.listadelacompra.databinding.FragmentRecyclerviewBasicBinding
import com.acasema.listadelacompra.ui.controller.ActionBarController
import com.acasema.listadelacompra.ui.controller.FabController
import com.acasema.listadelacompra.ui.main.MainActivity
/**
 * autor: acasema (alfonso)
 *  clase derivada de fragment: para ver la lista de otro
 */
class OtherShopingListFragment: Fragment() {

    private lateinit var permissions: Permissions
    lateinit var adapter: AdapterListView

    private lateinit var viewModel: OtherShopingListViewModel
    private var _binding: FragmentRecyclerviewBasicBinding? = null

    private val binding get() = _binding!!

    //region Lifecycle
    override fun onResume() {
        super.onResume()
        val fab: FabController = (activity as MainActivity).fabController
        fab.setVisibility(false)

        val actionBar: ActionBarController = (activity as MainActivity).actionBarController
        actionBar.setTitle(permissions.shopingList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        viewModel = ViewModelProvider(this)[OtherShopingListViewModel::class.java]

        _binding = FragmentRecyclerviewBasicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissions = (arguments?.getSerializable(getString(R.string.KEY_BUNDLE_PERMISSION)) as Permissions)
        viewModel.retarIsEditing(permissions)


        initAdapter()
        setObserves()

        viewModel.updateList(permissions)
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        adapter = AdapterListView(requireContext())
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
        viewModel.getEmailEditing().observe(viewLifecycleOwner, {
            Toast.makeText(
                requireContext(),
                it + getString(R.string.emailEditing),
                Toast.LENGTH_LONG
            ).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        permissions = (arguments?.getSerializable(getString(R.string.KEY_BUNDLE_PERMISSION)) as Permissions)

        inflater.inflate(R.menu.fragment_menu_otherlistview_history, menu)
        if (permissions.permissions == Permissions.PermissionsType.buyer || permissions.permissions == Permissions.PermissionsType.editor)
            inflater.inflate(R.menu.fragment_menu_otherlistview_buy, menu)
        if (permissions.permissions == Permissions.PermissionsType.editor)
            inflater.inflate(R.menu.fragment_menu_otherlistview_edit, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_buy -> {
                if (!viewModel.setEditing(permissions)){
                    isEditing()
                    return super.onOptionsItemSelected(item)
                }
                requireView().findNavController().navigate(R.id.action_otherShopingListFragment_to_otherBuyShoppingListFragment,getBundle())
                return true
            }
            R.id.action_edit -> {
                if (!viewModel.setEditing(permissions)){
                    isEditing()
                    return super.onOptionsItemSelected(item)
                }
                requireView().findNavController().navigate(R.id.action_otherShopingListFragment_to_otherEditShopingListFragment, getBundle())
                return true
            }
            R.id.action_history -> {
                requireView().findNavController().navigate(R.id.action_otherShopingListFragment_to_otherHistoryShopingFragment, getBundle())
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion

    private fun isEditing() {
        viewModel.getEmailEditing(permissions)
    }

    private fun getBundle(): Bundle {
        val bundle = bundleOf()
        bundle.putSerializable(getString(R.string.KEY_BUNDLE_PERMISSION), permissions)
        return bundle
    }
}