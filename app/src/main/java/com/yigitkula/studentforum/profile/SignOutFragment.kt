package com.yigitkula.studentforum.profile

import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.yigitkula.studentforum.R
import com.yigitkula.studentforum.loginAndRegister.LoginActivity

class SignOutFragment : DialogFragment() {
        private var mActivity = ProfileActivity()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var alert = AlertDialog.Builder(requireActivity())
            .setTitle("Log Out")
            .setMessage("Are you sure?")
            .setPositiveButton("Log Out",object: OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    FirebaseAuth.getInstance().signOut()
                    activity?.finish()


                }
            } )
            .setNegativeButton("Cancel", object: OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dismiss()
                }
            }).create().show()
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

}