package com.angler.anglernavimultilang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [ViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adpater: MyAdpater
    private lateinit var recyclerView: RecyclerView
    private lateinit var anglerArrayList : ArrayList<Angler>


    private lateinit var toggleButton: ToggleButton
    private var isGridViewShowing = false

    lateinit var imageId : Array<Int>
    lateinit var heading : Array<String>
    lateinit var angler: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adpater = MyAdpater(anglerArrayList)
        recyclerView.adapter = adpater

    }

    private fun dataInitialize(){
        anglerArrayList = arrayListOf<Angler>()

        imageId = arrayOf(
            R.drawable.angler1,
            R.drawable.angler2,
            R.drawable.angler3,
            R.drawable.angler4,
            R.drawable.angler5,
            R.drawable.angler6,
            R.drawable.angler7,
            R.drawable.angler8
        )

        heading = arrayOf(
            getString(R.string.angler1),
            getString(R.string.angler2),
            getString(R.string.angler3),
            getString(R.string.angler4),
            getString(R.string.angler5),
            getString(R.string.angler6),
            getString(R.string.angler7),
            getString(R.string.angler8),
        )

        for (i in imageId.indices){
            val angler = Angler(imageId[i], heading[i])
            anglerArrayList.add(angler)
        }
    }
}