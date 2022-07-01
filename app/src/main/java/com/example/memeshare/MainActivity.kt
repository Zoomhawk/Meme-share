package com.example.memeshare

import android.content.Intent
import kotlin.random.Random
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

class MainActivity : AppCompatActivity() {
    var currentImageUrl : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadmeme()
    }

    private fun loadmeme(){
        // Instantiate the RequestQueue.
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
            val url ="https://meme-api.herokuapp.com/gimme/wholesomememes"



// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->
                currentImageUrl = response.getString("url")

//                Our response is already fetched the imaged but glide is'nt able to show it in folder it take time for doing this
//                so we need to call some listeners where we can show our progressbar
//                '
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }


                }).into(memeImageView)
            },

            Response.ErrorListener {
//                Something like a prompt in javascript
                Toast.makeText(this,"something went wrong",Toast.LENGTH_LONG).show()
            })

//        Add the request to the RequestQueue
        queue.add(jsonObjectRequest)


    }

    fun shareMeme(view: View) {

//        used to define the action whether we have to open a camera or we have to upload something
//                or sharing some content like text ,image, etc etc
        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "text/plain"
//        Used to put the additional message while sending the meme
        intent.putExtra(Intent.EXTRA_TEXT, "Hey checkout this meme $currentImageUrl")

//        chooser is used to give the option to user where they can sedn the same thing using multipe
//        platforms like for pdf we have different sharing applications
        val chooser = Intent.createChooser(intent,"share this meme...")
        startActivity(chooser)
    }

    fun nextMeme(view: View) {
        loadmeme()
    }
}