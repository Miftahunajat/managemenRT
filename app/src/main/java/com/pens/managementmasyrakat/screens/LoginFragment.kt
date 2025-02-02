package com.pens.managementmasyrakat.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pens.managementmasyrakat.*
import com.pens.managementmasyrakat.extension.finishLoading
import com.pens.managementmasyrakat.extension.saveUser
import com.pens.managementmasyrakat.extension.showmessage
import com.pens.managementmasyrakat.extension.toLoading
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        view.tv_masuk.setOnClickListener {
            login(et_nama.text.toString(), et_password.text.toString())
        }
        checkUser()
        return view
    }

    private fun checkUser() {
        Repository.getUser(context!!)?.let {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        }
    }

    fun login(nama: String, password: String){
        Repository.login(nama, password).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.i("Loggin", it.status.toString())
                    view!!.tv_masuk.toLoading()
                }
                Resource.SUCCESS ->{
                    Log.d("@@@", it.data!!.toString())
                    view!!.tv_masuk.finishLoading()
                    context!!.saveUser(it.data!!)
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                }
                Resource.ERROR ->{
                    view!!.tv_masuk.finishLoading()
                    context?.showmessage("$nama $password")
                    context?.showmessage(it.message!!)
                    Log.i("Error", it.message!!)
                }
            }
        })
    }


}