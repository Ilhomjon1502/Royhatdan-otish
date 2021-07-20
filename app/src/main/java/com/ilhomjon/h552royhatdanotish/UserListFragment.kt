package com.ilhomjon.h552royhatdanotish

import Adapters.RvAdapter
import Adapters.RvItemClick
import Models.User
import Utils.MySharedPrefarance
import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ilhomjon.h552royhatdanotish.databinding.FragmentUserListBinding
import kotlinx.android.synthetic.main.dialog_item.*
import kotlinx.android.synthetic.main.item_rv.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UserListFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding:FragmentUserListBinding
    lateinit var rvAdapter: RvAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserListBinding.inflate(LayoutInflater.from(context))

        MySharedPrefarance.init(context)

        rvAdapter = RvAdapter(MySharedPrefarance.obektString, object : RvItemClick {
            override fun itemClick(user: User, position: Int) {
                val bottomSheetDialog = BottomSheetDialog(binding.root.context, R.style.NewDialog)
                bottomSheetDialog.setContentView(
                    layoutInflater.inflate(
                        R.layout.dialog_item,
                        null,
                        false
                    )
                )
                if (user.imageUri != null){
                    val bitmap = BitmapFactory.decodeByteArray(user.imageUri, 0, user.imageUri?.size!!)
                    bottomSheetDialog.image_dialog.setImageBitmap(bitmap)
                }
                bottomSheetDialog.txt_name_dialog.text = user.name
                bottomSheetDialog.txt_number_dialog.text = "${user.country}, ${user.address}"

                bottomSheetDialog.call_dialog.setOnClickListener {
                    askPermission(Manifest.permission.CALL_PHONE) {
                        //all permissions already granted or just granted
                        val intent = Intent(Intent.ACTION_CALL)
                        intent.data = Uri.parse("tel:${user.telNumber}")
                        startActivity(intent)
                    }.onDeclined { e ->
                        if (e.hasDenied()) {

                            AlertDialog.Builder(context)
                                .setMessage("Please accept our permissions")
                                .setPositiveButton("yes") { dialog, which ->
                                    e.askAgain();
                                } //ask again
                                .setNegativeButton("no") { dialog, which ->
                                    dialog.dismiss();
                                }
                                .show();
                        }

                        if (e.hasForeverDenied()) {
                            //the list of forever denied permissions, user has check 'never ask again'

                            // you need to open setting manually if you really need it
                            e.goToSettings();
                        }
                    }
                }
                bottomSheetDialog.sms_dialog.setOnClickListener {
                    val it = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${user.telNumber}"))
                    it.putExtra("sms_body", "")
                    startActivity(it)
                }

                bottomSheetDialog.show()
            }
        })
        binding.rv.adapter = rvAdapter

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}