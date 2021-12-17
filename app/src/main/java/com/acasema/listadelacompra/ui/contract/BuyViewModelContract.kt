package com.acasema.listadelacompra.ui.contract

import com.acasema.listadelacompra.data.model.Element

/**
 * autor: acasema (alfonso)
 *  interface para poder usar un solo adapter para los dos buyFragment.
 */
interface BuyViewModelContract {
    fun setBought(element: Element)
}