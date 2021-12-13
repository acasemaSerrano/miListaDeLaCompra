package com.acasema.listadelacompra.ui.listview

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.adapter.AdapterListView
import com.acasema.listadelacompra.databinding.FragmentRecyclerviewBasicBinding
import com.acasema.listadelacompra.ui.controller.ActionBarController
import com.acasema.listadelacompra.ui.controller.FabController
import com.acasema.listadelacompra.ui.main.MainActivity
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController


class ListViewFragment : Fragment() {

    lateinit var adapter: AdapterListView
    private lateinit var listName: String

    private lateinit var viewModel: ListViewViewModel
    private var _binding: FragmentRecyclerviewBasicBinding? = null

    private val binding get() = _binding!!

    //region Lifecycle
    override fun onResume() {
        super.onResume()

        val a: FabController = (activity as MainActivity).fabController
        a.setVisibility(false)

        val actionBar: ActionBarController = (activity as MainActivity).actionBarController
        actionBar.setTitle(listName)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        viewModel = ViewModelProvider(this)[ListViewViewModel::class.java]

        _binding = FragmentRecyclerviewBasicBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listName = arguments?.getString(getString(R.string.KEY_BUNDLE_LISTNAME))!!
        viewModel.isOnline = arguments?.getBoolean(getString(R.string.KEY_BUNDLE_ONLINE))!!
        viewModel.retarIsEditing(listName)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        adapter = AdapterListView(requireContext())
        binding.recyclerView.adapter = adapter


        viewModel.getListLiveData().observe(viewLifecycleOwner, {adapter.setList(it)})
        viewModel.getEmailEditingLiveData().observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it + getString(R.string.emailEditing), Toast.LENGTH_LONG).show()
        })

        viewModel.updateList(listName)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_menu_listview, menu)
         if(arguments?.getBoolean(getString(R.string.KEY_BUNDLE_ONLINE))!!){
             inflater.inflate(R.menu.fragment_menu_listview_online, menu)
         }
        super.onCreateOptionsMenu(menu, inflater)

    }
    //endregion

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_buy -> {
                if (viewModel.isOnline  && !viewModel.setEditing(listName)){
                    isEditing()
                    return super.onOptionsItemSelected(item)
                }
                requireView().findNavController().navigate(
                    R.id.action_listViewfragment_to_buyFromShoppingListFragment,
                    getBundle()
                )
                return true
            }
            R.id.action_edit -> {
                if (viewModel.isOnline && !viewModel.setEditing(listName)){
                    isEditing()
                    return super.onOptionsItemSelected(item)
                }
                requireView().findNavController()
                    .navigate(R.id.listCreationFragment, getBundle())
                return true
            }
            R.id.action_history -> {
                requireView().findNavController()
                    .navigate(R.id.action_listViewfragment_to_listHistoryFragment, getBundle())
                return true
            }
            R.id.action_permissionManager-> {
                requireView().findNavController()
                    .navigate(R.id.action_listViewfragment_to_permissionManagerFragment, getBundle())
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }

    private fun isEditing() {
        viewModel.getEmailEditing(listName)
    }

    private fun getBundle(): Bundle {
        val bundle = bundleOf()
        bundle.putString(requireContext().getString(R.string.KEY_BUNDLE_LISTNAME), listName)
        bundle.putBoolean(requireContext().getString(R.string.KEY_BUNDLE_ONLINE), viewModel.isOnline)
        return bundle
    }
}