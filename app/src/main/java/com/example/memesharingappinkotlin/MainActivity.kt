package com.example.memesharingappinkotlin

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


var currentImageUrl: String? = null

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    private  fun loadMeme(){
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
            var jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    currentImageUrl = response.getString("url")
                    Glide.with(this).load(currentImageUrl).listener(object :
                        RequestListener<Drawable> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = GONE
                            return false
                        }
                    }).into(imageView)
                },
                Response.ErrorListener {
                    Toast.makeText(this, "check internet connection", Toast.LENGTH_LONG).show()
                })


        queue.add(jsonObjectRequest)
    }

    fun getLocalBitmapUri(imageView: ImageView): Uri? {
        // Extract Bitmap from ImageView drawable
        val drawable: Drawable = imageView.getDrawable()
        var bmp: Bitmap? = null
        bmp = if (drawable is BitmapDrawable) {
            (imageView.getDrawable() as BitmapDrawable).bitmap
        } else {
            return null
        }
        // Store image to default external storage directory
        var bmpUri: Uri? = null
        try {

            val file = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
//            bmpUri = Uri.fromFile(file)

            bmpUri = FileProvider.getUriForFile(
                this,
                "com.codepath.fileprovider",
                file
            )// use this version for API >= 24
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    fun ShareMeme(view: View) {
        val bmpUri = getLocalBitmapUri(imageView)

        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "text/plain"
//        intent.putExtra(Intent.EXTRA_TEXT, "Meme : $currentImageUrl")

        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, bmpUri)

        val chooser = Intent.createChooser(intent, "Share this meme using...")
        startActivity(chooser)
    }
    fun NextMeme(view: View) {
        loadMeme()
    }


}
