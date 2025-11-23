package com.example.reviewapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import okio.Inflater
import kotlin.collections.get

class MainActivity : AppCompatActivity() {

    private var etName: EditText? = null
    private var etRating: EditText? = null
    private var etReview: EditText? = null


    private var btnPick: Button? = null
    private var btnAdd: Button? = null
    private var ivPreview : ImageView? = null
    private var rvReviews : RecyclerView? = null
    private var  adapter : ReviewAdapter? = null


    private var pickedLat: Double? = null
    private var pickedLng: Double? = null


    private val vm: ReviewViewModel by viewModels()
    private lateinit var photoRepo: PhotoRepository


    private var pickedPhotoPath: String? = null

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(
                it, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            pickedPhotoPath = photoRepo.saveFromUri(it)
            ivPreview?.load(pickedPhotoPath)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        etRating = findViewById(R.id.etRating)
        etReview = findViewById(R.id.etReview)

        btnPick = findViewById(R.id.btnPick)
        btnAdd = findViewById(R.id.btnAdd)
        ivPreview = findViewById(R.id.ivPreview)
        rvReviews = findViewById(R.id.rvReviews)

        etRating?.filters = arrayOf(android.text.InputFilter { source, start, end, dest, dstart, dend ->
            val result = dest.toString().substring(0, dstart) +
                    source.substring(start, end) +
                    dest.toString().substring(dend)

            if (result.isEmpty()) return@InputFilter null

            val num = result.toIntOrNull()
            if (num == null || num !in 1..5) "" else null
        })

        adapter = ReviewAdapter()
        rvReviews?.layoutManager = LinearLayoutManager(this)
        rvReviews?.adapter = adapter
        photoRepo = PhotoRepository(this)

        adapter?.lambdaOnClick = {  contact ->
            val i = contact?.let {
                Intent(applicationContext, UpdateDeleteActivity::class.java)
                    .putExtra("review_id", it.id)
            }
            //startActivity(i)
            if (i != null) {
                startActivityForResult(i, 200)
            }
        }
        adapter?.lambdaOnMapClick = { contact ->

            val intent = Intent(this, MapActivity::class.java).apply {
                if (contact != null) {
                    putExtra("extra_name", contact.name)
                }
                if (contact != null) {
                    putExtra("extra_rating", contact.rating)
                }
                if (contact != null) {
                    putExtra("extra_review", contact.review)
                }
                if (contact != null) {
                    putExtra("extra_lat", contact.latitude)
                }
                if (contact != null) {
                    putExtra("extra_lon", contact.longitude)
                }
            }
            startActivity(intent)
        }

        RefreshList()

        btnPick?.setOnClickListener { pickImage.launch(arrayOf("image/*")) }

        btnAdd?.setOnClickListener {
            val review = Review(
                name = etName?.text.toString().trim(),
                rating = etRating?.text.toString().trim(),
                review = etReview?.text.toString().trim(),
                photoPath = pickedPhotoPath,
                latitude = pickedLat,
                longitude = pickedLng
            )
            var t = Thread{
                var db = AppDatabase.get(applicationContext)
                db.reviewDao().insert(review)
                runOnUiThread {
                    RefreshList()
                    etName?.text?.clear()
                    etRating?.text?.clear()
                    etReview?.text?.clear()
                    pickedPhotoPath = null
                    ivPreview?.setImageResource(android.R.drawable.ic_menu_gallery)

                }
            }
            t.start()
        }
    }
    private fun RefreshList() {
        var t = Thread {
            var listReviews = AppDatabase.get(applicationContext).reviewDao().getAll()

            runOnUiThread {
                adapter?.submitList(listReviews)
            }
        }
        t.start()
    }
}