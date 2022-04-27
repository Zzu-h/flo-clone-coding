package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.adapter.AlbumRVAdapter
import com.example.flo.adapter.BannerVPAdapter
import com.example.flo.adapter.PanelVPAdapter
import com.example.flo.vo.Album
import com.example.flo.databinding.FragmentHomeBinding
import com.example.flo.vo.Song
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
        binding.homePanelViewpagerCi.setViewPager(binding.homePanelBackgroundVp, R.drawable.default_dot, R.drawable.selected_dot)

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
        albumDatas.add(Album(
            songs = ArrayList<Song>(5).apply {
                add(Song("라일락", "아이유 (IU)", 0, 60, false, 0f, "music_lilac", isTitle = true))
                add(Song("Flu", "아이유 (IU)", 0, 60, false, 0f, "music_flu", isTitle = false))
                add(Song("Coin", "아이유 (IU)", 0, 60, false, 0f, "music_coin", isTitle = true))
                add(Song("봄 안녕 봄", "아이유 (IU)", 0, 60, false, 0f, "music_hispringbye", isTitle = false))
            }
        ))
        albumDatas.add(Album(coverImg = R.drawable.img_album_exp,
            songs = ArrayList<Song>(5).apply {
                add(Song("봄 안녕 봄", "아이유 (IU)", 0, 60, false, 0f, "music_hispringbye", isTitle = false))
            },
            title = "봄 안녕 봄",
            singer = "IU"
        ))
        albumDatas.add(Album(coverImg = R.drawable.img_album_exp4,
            songs = ArrayList<Song>(5).apply {
                add(Song("Coin", "아이유 (IU)", 0, 60, false, 0f, "music_coin", isTitle = true))
            },
            title = "Coin",
            singer = "아이유"
        ))
        albumDatas.add(Album(coverImg = R.drawable.img_album_exp3,
            songs = ArrayList<Song>(5).apply {
                add(Song("Flu", "아이유 (IU)", 0, 60, false, 0f, "music_flu", isTitle = false))
            },
            title = "Flu",
            singer = "아이유 (IU)"
        ))
        albumDatas.add(Album(coverImg = R.drawable.img_album_exp5,
            songs = ArrayList<Song>(5).apply {
                add(Song("라일락", "아이유 (IU)", 0, 60, false, 0f, "music_lilac", isTitle = true))
            }
        ))

        binding.homeTodayMusicAlbumRv.adapter = AlbumRVAdapter(albumDatas)
            .apply {
                onItemClick = this@HomeFragment::albumItemClick
                playAlbum = this@HomeFragment::playAlbum
            }
    }
    private fun playAlbum(album: Album)
        = (context as MainActivity).playAlbum(album)

    private fun albumItemClick(album: Album){
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment()
                .apply {
                    arguments = Bundle().apply {
                        val gson = Gson()
                        val albumJson = gson.toJson(album)
                        putString(CODE.album, albumJson)
                    }
                }
            )
            .commitAllowingStateLoss()
    }
}