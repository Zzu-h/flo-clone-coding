package com.example.flo.ui.main.look

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.data.datasource.SongNetworkDataSource
import com.example.flo.data.vo.FloChartResult
import com.example.flo.databinding.FragmentLookBinding
import com.example.flo.ui.main.look.LookView


class LookFragment : Fragment() {
    private lateinit var binding: FragmentLookBinding
    private lateinit var floCharAdapter: SongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLookBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getSongs()
    }

    private fun initRecyclerView(result: FloChartResult) {
        floCharAdapter = SongRVAdapter(requireContext(), result)

        binding.lookFloChartRv.adapter = floCharAdapter
    }

    private fun getSongs() {
        SongNetworkDataSource().getSongs(object: LookView{
            override fun onGetSongLoading() {
                binding.lookLoadingPb.visibility = View.VISIBLE
            }

            override fun onGetSongSuccess(code: Int, result: FloChartResult) {
                binding.lookLoadingPb.visibility = View.GONE
                initRecyclerView(result)
            }

            override fun onGetSongFailure(code: Int, message: String) {
                binding.lookLoadingPb.visibility = View.GONE
                Log.d("LOOK-FRAG/SONG-RESPONSE", message)
            }
        })
    }
}