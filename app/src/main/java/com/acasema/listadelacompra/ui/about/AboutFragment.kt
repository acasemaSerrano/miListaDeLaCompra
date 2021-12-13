package com.acasema.listadelacompra.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.acasema.listadelacompra.R
import android.content.Intent
import android.net.Uri
import com.acasema.listadelacompra.ui.controller.FabController
import com.acasema.listadelacompra.ui.main.MainActivity


class AboutFragment : Fragment() {

    private val uri: Uri = Uri.parse("https://github.com/acasemaSerrano")

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnURL: Button = view.findViewById(R.id.btnURL)
        btnURL.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        val a: FabController = (activity as MainActivity).fabController
        a.setVisibility(false)
    }

}