/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.lunchtray.R
import com.example.lunchtray.data.DataSource
import java.text.NumberFormat
import java.util.*
import kotlin.math.absoluteValue

class OrderViewModel : ViewModel() {
    var entreeMenu: String? = null
    var sideMenu: String? = null
    var accompanimentMenu: String? = null
    // Map of menu items
    val menuItems = DataSource.menuItems

    // Giá trị mặc định cho giá mặt hàng
    // Giá trị món ăn đầu
    private var previousEntreePrice = 0.0

    // Giá trị món ăn sau
    private var previousSidePrice = 0.0

    // Giá trị món ăn kèm
    private var previousAccompanimentPrice = 0.0

    // Thuế suất mặc định
    private val taxRate = 0.08

    // Entree for the order
    private val _entree = MutableLiveData<MenuItem?>()
    val entree: LiveData<MenuItem?> = _entree

    // Side for the order
    private val _side = MutableLiveData<MenuItem?>()
    val side: LiveData<MenuItem?> = _side

    // Accompaniment for the order.
    private val _accompaniment = MutableLiveData<MenuItem?>()
    val accompaniment: LiveData<MenuItem?> = _accompaniment

    // Tổng phụ cho đơn đặt hàng
    private val _subtotal = MutableLiveData(0.0)
    val subtotal: LiveData<String> = Transformations.map(_subtotal) {
        NumberFormat.getCurrencyInstance(Locale.US).format(it)
    }

    // Tổng tiền của đơn đặt hàng
    private val _total = MutableLiveData(0.0)
    val total: LiveData<String> = Transformations.map(_total) {
        NumberFormat.getCurrencyInstance(Locale.US).format(it)
    }

    // Thuế cho đơn đặt hàng
    private val _tax = MutableLiveData(0.0)
    val tax: LiveData<String> = Transformations.map(_tax) {
        NumberFormat.getCurrencyInstance(Locale.US).format(it)
    }

    /**
     * Set the entree for the order.
     */
    fun setEntree(entree: String) {

        // TODO: nếu _entree.value không null, hãy đặt giá đầu vào trước đó thành hiện
        //  tại giá ưu đãi.
        if (_entree.value != null) {
            Log.d("OrderViewModel","Giá trị của  _entree.value: ${_entree.value}")
            previousEntreePrice = _entree.value!!.price
        }

        // TODO: nếu _subtotal.value không rỗng, trừ giá trước đó khỏi giá hiện tại
        //  giá trị tổng phụ. Điều này đảm bảo rằng chúng tôi chỉ tính phí cho những người được chọn hiện tại.
        if (_subtotal.value != null) {
            _subtotal.value = _subtotal.value!! - previousEntreePrice
            Log.d("OrderViewModel","Giá trị của  _subtotal.value: ${_subtotal.value}")
        }

        // TODO: đặt giá trị truy cập hiện tại cho mục menu tương ứng với chuỗi được truyền vào
        _entree.value = menuItems[entree]
        if(entree != null) {
            entreeMenu = entree
        }
        
        // TODO: cập nhật tổng phụ để phản ánh giá của nhóm hàng đã chọn.
        updateSubtotal(_entree.value!!.price)

    }

    /**
     * Set the side for the order.
     */
    fun setSide(side: String) {
        // TODO: if _side.value is not null, set the previous side price to the current side price.
        if (_side.value != null) {
            previousSidePrice = _side.value!!.price
        }

        // TODO: if _subtotal.value is not null subtract the previous side price from the current
        //  subtotal value. This ensures that we only charge for the currently selected side.
        if (_subtotal.value != null) {
            _subtotal.value = _subtotal.value!! - previousSidePrice
        }

        // TODO: set the current side value to the menu item corresponding to the passed in string
        _side.value = menuItems[side]
        if(side != null) {
            sideMenu = side
        }
        // TODO: update the subtotal to reflect the price of the selected side.
        updateSubtotal(_side.value!!.price)
    }

    /**
     * Set the accompaniment for the order.
     */
    fun setAccompaniment(accompaniment: String) {
        // TODO: if _accompaniment.value is not null, set the previous accompaniment price to the
        //  current accompaniment price.
        if (_accompaniment.value != null) {
            previousAccompanimentPrice = _accompaniment.value!!.price
        }
        // TODO: if _accompaniment.value is not null subtract the previous accompaniment price from
        //  the current subtotal value. This ensures that we only charge for the currently selected
        //  accompaniment.
        if (_subtotal.value != null) {
            _subtotal.value = _subtotal.value!! - previousAccompanimentPrice
        }

        // TODO: set the current accompaniment value to the menu item corresponding to the passed in
        //  string
        _accompaniment.value = menuItems[accompaniment]
        if(accompaniment != null) {
            accompanimentMenu = accompaniment
        }
        // TODO: update the subtotal to reflect the price of the selected accompaniment.
        updateSubtotal(_accompaniment.value!!.price)
    }

    /**
     * Cập nhật giá trị phụ
     */
    private fun updateSubtotal(itemPrice: Double) {
        // TODO: nếu _subtotal.value không phải là null, hãy cập nhật nó để phản ánh giá gần đây
        //  mục đã thêm.
        if (_subtotal.value != null) {
            _subtotal.value = _subtotal.value!! + itemPrice
        } else {
            _subtotal.value = itemPrice
        }


        //  Nếu không, hãy đặt _subtotal.value bằng với giá của mặt hàng.

        // TODO: tính thuế và tổng kết quả
        calculateTaxAndTotal()
    }

    /**
     * Calculate tax and update total.
     */
    fun calculateTaxAndTotal() {
        // TODO: set _tax.value based on the subtotal and the tax rate.
        _tax.value = _subtotal.value!!.times(taxRate)

        // TODO: set the total based on the subtotal and _tax.value.
        _total.value = _subtotal.value!! + _tax.value!!
    }

    /**
     * Reset all values pertaining to the order.
     */
    fun resetOrder() {
        // TODO: Reset all values associated with an order
        previousEntreePrice = 0.0
        previousSidePrice = 0.0
        previousAccompanimentPrice = 0.0

        _entree.value = null
        _side.value = null
        _accompaniment.value = null
        _subtotal.value = 0.0
        _total.value = 0.0
        _tax.value = 0.0
        entreeMenu = null
        sideMenu = null
        accompanimentMenu = null
    }


}
