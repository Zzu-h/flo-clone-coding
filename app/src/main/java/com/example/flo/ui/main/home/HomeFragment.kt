package com.example.flo.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.R
import com.example.flo.data.datasource.AlbumNetworkDataSource
import com.example.flo.data.model.SongCode
import com.example.flo.ui.main.home.adapter.AlbumRVAdapter
import com.example.flo.ui.main.home.adapter.BannerVPAdapter
import com.example.flo.ui.main.home.adapter.PanelVPAdapter
import com.example.flo.data.vo.Album
import com.example.flo.databinding.FragmentHomeBinding
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.album.AlbumFragment
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.ArrayList

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val autoScrollDelayTime = 3000L
    private var albumDatas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setRecyclerView()
        setViewPager()

        return binding.root
    }
    private fun setViewPager(){
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment())
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val PanelAdapter = PanelVPAdapter(this)
        PanelAdapter.addFragment(PanelFragment())
        PanelAdapter.addFragment(PanelFragment(R.drawable.img_album_exp6))
        PanelAdapter.addFragment(PanelFragment(R.drawable.img_album_exp5))

        binding.homePanelBackgroundVp.adapter = PanelAdapter
        binding.homePanelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.homePanelViewpagerCi.setViewPager(binding.homePanelBackgroundVp,
            R.drawable.default_dot,
            R.drawable.selected_dot
        )

        CoroutineScope(Dispatchers.Main).launch {
            while(true){
                delay(autoScrollDelayTime)
                val size = binding.homePanelBackgroundVp.adapter!!.itemCount
                val currentIdx = binding.homePanelBackgroundVp.currentItem
                binding.homePanelBackgroundVp.currentItem = (currentIdx + 1) % size
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            while(true){
                delay(autoScrollDelayTime)
                val size = binding.homeBannerVp.adapter!!.itemCount
                val currentIdx = binding.homeBannerVp.currentItem
                binding.homeBannerVp.currentItem = (currentIdx + 1) % size
            }
        }
    }
    private fun setRecyclerView(){
        /*val songdb = SongDatabase.getInstance(this.requireContext())!!
        albumDatas = songdb.albumDao().getAlbums() as ArrayList<Album>*/

        AlbumNetworkDataSource().getAlbums(object: AlbumNetworkDataSource.AlbumInterface {
            override fun onLoadSuccess(albumList: List<Album>) {
                albumDatas = albumList as ArrayList<Album>
                binding.homeTodayMusicAlbumRv.adapter = AlbumRVAdapter(albumDatas)
                    .apply {
                        onItemClick = this@HomeFragment::albumItemClick
                        playAlbum = this@HomeFragment::playAlbum
                    }
            }

            override fun onLoadFailure(message: String?) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun playAlbum(album: Album)
        = (context as MainActivity).playAlbum(album)

    private fun albumItemClick(album: Album){
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_frm, AlbumFragment()
                .apply {
                    arguments = Bundle().apply {
                        val gson = Gson()
                        val albumJson = gson.toJson(album)
                        putString(SongCode.album, albumJson)
                    }
                }
            )
            .commitAllowingStateLoss()
    }
}