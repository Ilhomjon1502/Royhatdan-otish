package com.ilhomjon.h552royhatdanotish

import Models.User
import Utils.MySharedPrefarance
import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.ilhomjon.h552royhatdanotish.databinding.FragmentRegistrationBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegistrationFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentRegistrationBinding
    lateinit var absolutePath:ByteArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(LayoutInflater.from(context))

        MySharedPrefarance.init(context)

        binding.imageAdd.setOnClickListener {
            askPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA){
                //all permissions already granted or just granted

                val dialog = AlertDialog.Builder(context)
                dialog.setMessage("Rasmni kameradan yoki gallereyadan yuklashingiz mumkin:")

                dialog.setPositiveButton("Camera "
                ) { dialog, which ->
                    var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent,0)
                }
                dialog.setNegativeButton("Gallery "
                ) { dialog, which ->
                    startActivityForResult(
                        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "image/*"
                        },
                        1
                    )
                }
                dialog.show()

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

                if(e.hasForeverDenied()) {
                    //the list of forever denied permissions, user has check 'never ask again'

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }
        }

        binding.cardRegistration.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val telNumber = binding.edtTelNumberRg.text.toString().trim()
            val country = binding.edtCountry.selectedItem.toString()
            val address = binding.edtAddress.text.toString().trim()
            val password = binding.edtPasswordRg.text.toString().trim()

            if (name!="" && telNumber!="" && country!="" && address!="" && password!=""){
                val list = MySharedPrefarance.obektString
                list.add(User(name, telNumber, country, address, password, absolutePath))
                MySharedPrefarance.obektString = list
                Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.tizimgaKirishFragment)
            }else{
                Toast.makeText(context, "Ma'lumot yetarli emas", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == AppCompatActivity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            binding.circleImageview.setImageBitmap(bitmap)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            absolutePath = byteArray
        }else if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK){
            val uri = data?.data ?: return
            binding.circleImageview.setImageURI(uri)
            val inputStream = activity?.contentResolver?.openInputStream(uri)
            val format = SimpleDateFormat("yyMMdd_hhmmss").format(Date())
            val file = File(activity?.filesDir, "${format}_image.jpg")
            val fileOutputStream = FileOutputStream(file)
            inputStream?.copyTo(fileOutputStream)
            inputStream?.close()
            fileOutputStream.close()
            val fileInputStream = FileInputStream(file)
            val readBytes = fileInputStream.readBytes()
            absolutePath = readBytes
            Toast.makeText(context, "$absolutePath", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}