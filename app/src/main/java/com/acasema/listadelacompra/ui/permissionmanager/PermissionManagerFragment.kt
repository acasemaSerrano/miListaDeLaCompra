package com.acasema.listadelacompra.ui.permissionmanager

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.ui.adapter.AdapterPermissionManager
import com.acasema.listadelacompra.data.model.Permissions
import com.acasema.listadelacompra.databinding.FragmentRecyclerviewBasicBinding
import com.acasema.listadelacompra.ui.controller.ActionBarController
import com.acasema.listadelacompra.ui.controller.FabController
import com.acasema.listadelacompra.ui.main.MainActivity

/**
 * autor: acasema (alfonso)
 *  clase derivada de fragment: para gestionar los permisos
 */
class PermissionManagerFragment : Fragment() {

    lateinit var adapter: AdapterPermissionManager
    private lateinit var listName: String

    private lateinit var viewModel: PermissionManagerViewViewModel
    private var _binding: FragmentRecyclerviewBasicBinding? = null

    private val binding get() = _binding!!

    //region Lifecycle
    override fun onResume() {
        super.onResume()

        val fab: FabController = (activity as MainActivity).fabController
        fab.setVisibility(true)

        fab.setImageResource(android.R.drawable.ic_input_add)
        fab.setOnClickListener {
            newEmail()
        }
        checklist(adapter.getList().size)

        val actionBar: ActionBarController = (activity as MainActivity).actionBarController
        actionBar.setTitle(listName)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        viewModel = ViewModelProvider(this)[PermissionManagerViewViewModel::class.java]

        _binding = FragmentRecyclerviewBasicBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listName = arguments?.getString(getString(R.string.KEY_BUNDLE_LISTNAME))!!

        initAdapter()
        setObserves()

        viewModel.getListPermissions(listName)

    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        adapter = AdapterPermissionManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setObserves() {
        viewModel.getListLiveData().observe(viewLifecycleOwner, {
            adapter.setList(it)
        })
        viewModel.getEmail().observe(viewLifecycleOwner, {
            if (it != null) {
                val permissions = Permissions(it, viewModel.getOwner(), Permissions.PermissionsType.observer, listName)
                adapter.addList(permissions)
                checklist(adapter.getList().size)

            }else
                Toast.makeText(
                    requireContext(),
                    getString(R.string.emailNotFound),
                    Toast.LENGTH_LONG
                ).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_menu_permissionmanager, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    fun checklist(size: Int){
        if(size == 0)
            binding.ivNotData.visibility = View.VISIBLE
        else
            binding.ivNotData.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                viewModel.updateList(listName, adapter.getList())
                viewModel.usersDelete(listName, adapter.getUsersDelete())
                requireView().findNavController().popBackStack()
                true
            }
            else -> return super.onOptionsItemSelected(item)

        }
    }

    private fun newEmail() {

        val input = EditText(requireContext())
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        input.layoutParams = lp;

            AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.SearchMail))
            .setView(input)
            .setPositiveButton(R.string.Search) { _, _ -> viewModel.searchEmail(input.text.toString().trimEnd()) }
            .setNegativeButton(R.string.Cancel) { _, _ ->  }
            .create().show()

    }

}