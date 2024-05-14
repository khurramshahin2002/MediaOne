package com.example.mediaone

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ImageViewer : Fragment() {

    private lateinit var bottomNav: BottomNavigationView
    private var imageRecycler: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var allPictures: ArrayList<Pair<String, String>>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_viewer, container, false)
        imageRecycler = view.findViewById(R.id.image_recycler)
        progressBar = view.findViewById(R.id.recycler_progress)

        imageRecycler?.layoutManager = GridLayoutManager(requireContext(), 3)
        imageRecycler?.setHasFixedSize(true)

        // Storage permissions
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101
            )
        }
        allPictures = ArrayList()

        if (allPictures!!.isEmpty()) {
            progressBar?.visibility = View.VISIBLE
            allPictures = getAllImages()
            imageRecycler?.adapter = ImageAdapter(requireContext(), allPictures!!)
        }

        return view
    }

    private fun getAllImages(): ArrayList<Pair<String, String>> {
        val images = ArrayList<Pair<String, String>>()
        val allImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val cursor = requireContext().contentResolver.query(allImageUri, projection, null, null, null)
        try {
            cursor?.moveToFirst()
            cursor?.let {
                do {
                    val imagePath = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                    val imageName = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                    images.add(Pair(imagePath, imageName))
                } while (it.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return images
    }
}
