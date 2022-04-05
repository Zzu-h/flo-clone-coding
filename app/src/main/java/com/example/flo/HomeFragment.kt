package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.example.flo.databinding.ItemAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.ArrayList

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val autoScrollDelayTime = 3000L
    var albumList = ArrayList<Album>()

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
        albumList.add(Album())
        albumList.add(Album(coverImg = R.drawable.img_album_exp))
        albumList.add(Album(coverImg = R.drawable.img_album_exp4))
        albumList.add(Album(coverImg = R.drawable.img_album_exp3))
        albumList.add(Album(coverImg = R.drawable.img_album_exp5))

        binding.homeTodayMusicAlbumRv.adapter = AlbumAdapter(albumList)
    }
}

data class Album(
    val title: String = "Lilac",
    val singer: String = "IU",
    val coverImg: Int = R.drawable.img_album_exp2
)

class AlbumAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumAdapter.ViewHolder
        = ViewHolder(ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(holder: AlbumAdapter.ViewHolder, position: Int)
        = holder.bind(albumList[position])

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg)
        }
    }
}