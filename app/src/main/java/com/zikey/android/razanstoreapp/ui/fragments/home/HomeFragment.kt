package com.zikey.android.razanstoreapp.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.databinding.FragmentHomeBinding
import com.zikey.android.razanstoreapp.model.tools.SessionManagement
import java.lang.Exception

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFonts()
        initListeners()
        initContent()

    }

    private fun initContent() {

        try {
            FontChanger().applyTitleFont(_binding!!.txtSelectDesk)
            _binding!!.txtSelectDesk.setText(
                activity?.let { SessionManagement.getInstance(it.applicationContext) }!!
                    .getCashDeskName()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initListeners() {

        binding.crdLyNewFactor.setOnClickListener {

            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNewFactorFragment())
        }

        binding.crdFactors.setOnClickListener {

            findNavController().navigate(HomeFragmentDirections.actionNavHomeToOrdersFragment())
        }

        binding.crdFinalizeCashDesk.setOnClickListener {

            findNavController().navigate(HomeFragmentDirections.actionNavHomeToFinalizeCashDeskFragment())
        }
   binding.crdKardex.setOnClickListener {

            findNavController().navigate(HomeFragmentDirections.actionNavHomeToKardexFragment())
        }


    }


    private fun initFonts() {
        FontChanger().applyTitleFont(_binding!!.lyCards)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}