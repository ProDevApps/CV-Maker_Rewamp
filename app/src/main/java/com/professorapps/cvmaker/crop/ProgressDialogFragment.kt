package com.professorapps.cvmaker.crop

import android.app.Dialog
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import com.professorapps.cvmaker.R

class ProgressDialogFragment : DialogFragment() {
    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        val inflate =
            layoutInflater.inflate(R.layout.fragment_progress_dialog, null as ViewGroup?, false)
        (inflate.findViewById<View>(R.id.progress) as ProgressBar).indeterminateDrawable.setColorFilter(
            requireContext().resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN
        )
        return inflate
    }

    override fun onCreateDialog(bundle: Bundle?): Dialog {
        val onCreateDialog = super.onCreateDialog(bundle)
        onCreateDialog.window!!.requestFeature(1)
        onCreateDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        onCreateDialog.setCancelable(false)
        onCreateDialog.setCanceledOnTouchOutside(false)
        onCreateDialog.setOnKeyListener { dialogInterface, i, keyEvent -> i == 4 || i == 84 }
        return onCreateDialog
    }

    companion object {
        const val TAG: String = "ProgressDialogFragment"

        val instance: ProgressDialogFragment
            get() {
                val progressDialogFragment = ProgressDialogFragment()
                progressDialogFragment.arguments = Bundle()
                return progressDialogFragment
            }
    }
}
