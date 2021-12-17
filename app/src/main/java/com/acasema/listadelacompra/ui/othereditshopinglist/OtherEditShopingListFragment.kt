package com.acasema.listadelacompra.ui.othereditshopinglist

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.ui.adapter.AdapterListCreation
import com.acasema.listadelacompra.data.model.Permissions
import com.acasema.listadelacompra.databinding.FragmentListcreationBinding
import com.acasema.listadelacompra.service.FirebaseFirestoreService
import com.acasema.listadelacompra.ui.controller.ActionBarController
import com.acasema.listadelacompra.ui.main.MainActivity
import com.acasema.listadelacompra.ui.controller.FabController

import com.google.android.material.snackbar.Snackbar

import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

/**
 * autor: acasema (alfonso)
 *  clase derivada de fragment: para editar con la lista de otro
 */
class OtherEditShopingListFragment : Fragment() {

    private lateinit var permission: Permissions

    private lateinit var adapter: AdapterListCreation

    private lateinit var viewModel: OtherEditShopingListViewModel
    private var _binding: FragmentListcreationBinding? = null

    private val binding get() = _binding!!

    //region Lifecycle
    override fun onResume() {
        super.onResume()

        val a: FabController = (activity as MainActivity).fabController
        a.setVisibility(true)
        a.setImageResource(android.R.drawable.ic_menu_save)
        a.setOnClickListener {
            salveData()
        }
        val actionBar: ActionBarController = (activity as MainActivity).actionBarController

        actionBar.setTitle(permission.shopingList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_menu_listcreation, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this)[OtherEditShopingListViewModel::class.java]
        _binding = FragmentListcreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permission = arguments?.getSerializable(getString(R.string.KEY_BUNDLE_PERMISSION)) as Permissions

        initAdapter()
        viewModelObserver()

        initEdit(permission)

        setListener()
    }

    private fun initEdit(permission: Permissions) {
        binding.switchOniline.isEnabled = false
        binding.switchOniline.visibility = View.GONE
        binding.tieListName.text = Editable.Factory().newEditable(permission.shopingList)

        viewModel.getElementList(permission)
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        adapter = AdapterListCreation(requireContext())
        binding.recyclerView.adapter = adapter
        adapter.setShopingListName(permission.shopingList)
    }

    override fun onStop() {
        super.onStop()
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        FirebaseFirestoreService().setEditingOtherData(permission.owner, permission.shopingList, true)
        super.onDestroy()
    }
    //endregion

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_readBarcode -> {
                initScanner()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //region readBarcode
    private fun initScanner() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES)

        options.setOrientationLocked(true)
        options.setPrompt(getString(R.string.ScanBarcode))
        options.setBeepEnabled(true)

        val barcodeFormats : MutableList<String> = mutableListOf()
        barcodeFormats.add(ScanOptions.EAN_13)
        barcodeFormats.add(ScanOptions.EAN_8)
        barcodeFormats.add(ScanOptions.UPC_A)
        barcodeFormats.add(ScanOptions.UPC_E)

        options.setDesiredBarcodeFormats(barcodeFormats)

        barcodeLauncher.launch(options)
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) {
            result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            viewModel.resultReadCodePost(result.contents)
        }
    }
    //endregion

    /**
     * method to save data
     */
    private fun salveData() {
        viewModel.updateData(permission, adapter.getList())
    }

    /**
     * method that has all listeners
     */
    private fun setListener() {
        binding.btAdd.setOnClickListener {
            adapter.addNewElement()
        }
        binding.btDelete.setOnClickListener {
            if (adapter.itemCount > 1) {
                adapter.removeLastItem()
            } else {
                Snackbar.make(requireView(), getText(R.string.error_CantDeleteIsLast), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
        binding.tieListName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.shopingListNamePost(s.toString())
            }

        })
    }

    /**
     * method that has all observers
     */
    private fun viewModelObserver() {
        viewModel.getShopingListNameLiveData().observe(viewLifecycleOwner, { adapter.setShopingListName(it) })
        viewModel.getListElementLiveData().observe(viewLifecycleOwner, { adapter.setList(it) })
        viewModel.getResultReadBarcodeLiveData().observe(viewLifecycleOwner, {
            if (it.equals(""))
                Toast.makeText(
                    requireContext(),
                    "no encontrado en la base de datos",
                    Toast.LENGTH_LONG
                ).show()
            else {
                adapter.addElement(it)
            }
        })
        viewModel.getResultSalveDataLiveData().observe(viewLifecycleOwner, {
            if(it){
                requireView().findNavController().popBackStack()
            }else {
                binding.tieListName.error = getString(R.string.error_shopinglistname_is_exists)
            }
        })
    }

}