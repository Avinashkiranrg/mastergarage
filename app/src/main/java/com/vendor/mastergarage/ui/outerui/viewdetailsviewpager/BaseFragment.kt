package com.vendor.mastergarage.ui.outerui.viewdetailsviewpager

import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {
    abstract fun handleEvent(value: Int)
}