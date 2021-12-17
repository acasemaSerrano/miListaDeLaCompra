package com.acasema.listadelacompra.ui.listcreation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.ui.adapter.AdapterListCreation
import com.acasema.listadelacompra.data.model.ShopingList
import com.acasema.listadelacompra.databinding.FragmentListcreationBinding
import com.acasema.listadelacompra.ui.controller.ActionBarController
import com.acasema.listadelacompra.ui.main.MainActivity
import com.acasema.listadelacompra.ui.controller.FabController
import com.acasema.listadelacompra.utils.Pattern

import com.google.android.material.snackbar.Snackbar

import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class ListCreationFragment : Fragment() {

    lateinit var adapter: AdapterListCreation

    private lateinit var viewModel: ListCreationViewModel
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

        val shopingListName = arguments?.getString(getString(R.string.KEY_BUNDLE_LISTNAME))
        if (shopingListName != null)
            actionBar.setTitle(shopingListName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_menu_listcreation, menu)
        if (arguments?.getString(getString(R.string.KEY_BUNDLE_LISTNAME)) != null){
            inflater.inflate(R.menu.fragment_menu_listcreation_edit, menu)
        }

        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        viewModel = ViewModelProvider(this)[ListCreationViewModel::class.java]

        _binding = FragmentListcreationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shopingListName = arguments?.getString(getString(R.string.KEY_BUNDLE_LISTNAME))

        initAdapter()
        viewModelObserver()

        if (shopingListName != null){
            initEdit(shopingListName)
        }else {
            initCreation()
        }

        setListener()
    }

    private fun initCreation() {
        adapter.setGoneSwBought()
        adapter.setShopingListName("")
        adapter.addNewElement()
    }

    private fun initEdit(shopingListName: String) {
        binding.switchOniline.isEnabled = false
        binding.switchOniline.isChecked =
            arguments?.getBoolean(getString(R.string.KEY_BUNDLE_ONLINE))!!

        viewModel.getElementList(shopingListName, binding.switchOniline.isChecked)
        binding.tieListName.text = Editable.Factory().newEditable(shopingListName)
        binding.tieListName.isEnabled = false
        viewModel.shopingListNamePost(shopingListName)
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        adapter = AdapterListCreation(requireContext())
        binding.recyclerView.adapter = adapter
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
    //endregion

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_readBarcode -> {
                initScanner()
                true
            }
            R.id.action_delete -> {
                isDelete()
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
        val nameShopingList = viewModel.getShopingListNameLiveData().value

        if(Pattern.isText25MaxValid(nameShopingList!!)) {

            val shopingList = ShopingList(viewModel.getShopingListNameLiveData().value!!)
            shopingList.online = binding.switchOniline.isChecked

            if (binding.switchOniline.isEnabled){
                viewModel.uploadData(shopingList, adapter.getList())
            } else{
                viewModel.updateData(shopingList, adapter.getList())
            }

        }else{
            if (nameShopingList == ""){
                binding.tieListName.error = getText(R.string.error_nameShopinListIsVoid)
            }else{
                binding.tieListName.error = getText(R.string.error_nameShopinListMaxReached)
            }
        }
    }

    private fun isDelete() {

        val shopingList = ShopingList(viewModel.getShopingListNameLiveData().value!!)
        shopingList.online = binding.switchOniline.isChecked

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.adDeleteTitle)
            .setMessage(getString(R.string.adDeleteMessage))
            .setPositiveButton(R.string.adDeleteAccept) { _, _ ->
                viewModel.delete(shopingList, adapter.getList())
                requireView().findNavController().popBackStack()
                requireView().findNavController().popBackStack()
            }
            .setNegativeButton(R.string.adDeleteCancel) { _, _ ->  }
            .create().show()

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