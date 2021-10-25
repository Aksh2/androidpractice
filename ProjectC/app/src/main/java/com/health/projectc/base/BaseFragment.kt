package com.health.projectc.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<VB: ViewBinding, VM: ViewModel>: Fragment() {

    companion object{
        private const val TAG_ERROR_DIALOG = "error_dialog"
    }

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract val viewModel: VM

    private var errorDialog: ErrorDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    fun showErrorDialog(title: String, message: String){
        if(errorDialog == null){
            errorDialog = ErrorDialog()
        }

        errorDialog?.apply{
            this.title = title
            this.message = message
        }

        errorDialog?.let{
            if(!it.isVisible){
                it.show(requireActivity().supportFragmentManager, TAG_ERROR_DIALOG )
            }
        }
    }

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?):VB


}