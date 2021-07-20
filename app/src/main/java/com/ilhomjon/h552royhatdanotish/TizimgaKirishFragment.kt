package com.ilhomjon.h552royhatdanotish

import Utils.MyData
import Utils.MySharedPrefarance
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ilhomjon.h552royhatdanotish.databinding.FragmentTizimgaKirishBinding
import kotlinx.android.synthetic.main.fragment_tizimga_kirish.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TizimgaKirishFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding:FragmentTizimgaKirishBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTizimgaKirishBinding.inflate(LayoutInflater.from(context))

        MySharedPrefarance.init(context)

        binding.txtRegistration.setOnClickListener {
            findNavController().navigate(R.id.registrationFragment)
        }

        binding.btnSign.setOnClickListener {
            val telNumber = binding.edtTelNumber.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            var has = false
            for (user in MySharedPrefarance.obektString) {
                if (telNumber == user.telNumber && password == user.password){
                    has = true
                    break
                }
            }
            if (has){
                findNavController().navigate(R.id.userListFragment)
            }else{
                Toast.makeText(context, "Bunday foydalanuvchi topilmadi. Ro'yhatdan o'tishingiz mumkin", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TizimgaKirishFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}