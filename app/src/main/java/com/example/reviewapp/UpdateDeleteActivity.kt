package com.example.reviewapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import coil.load
import java.io.File
import kotlin.collections.get
import kotlin.math.log

class UpdateDeleteActivity : AppCompatActivity(){

    private var etName: EditText? = null
    private var etRating: EditText? = null
    private var etReview: EditText? = null

    private var btnPick: Button? = null
    private var btnUpdate: Button? = null
    private var btnDelete: Button? = null
    private var btnCancel: Button? = null

    private var ivPreview: ImageView? = null
    private lateinit var photoRepo: PhotoRepository

    private var current: Review? = null

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

    override fun finish() {
        super.setResult(200)
        super.finish()
        var i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_update_delete)
        photoRepo = PhotoRepository(this)
        etName = findViewById(R.id.etName)
        etRating = findViewById(R.id.etRating)
        etReview = findViewById(R.id.etReview)

        btnPick = findViewById(R.id.btnPick)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        btnCancel = findViewById(R.id.btnCancel)
        ivPreview = findViewById(R.id.ivPreview)

        val id = intent.getIntExtra("review_id", -1)
        if (id == -1) {
            Toast.makeText(this, "Missing review id", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        Thread {
            val dao = AppDatabase.get(applicationContext).reviewDao()
            val c = dao.getById(id)
            runOnUiThread {
                if (c == null) {
                    Toast.makeText(this, "Review not found", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    current = c
                    bindReview(c)
                }
            }
        }.start()

        btnPick?.setOnClickListener { pickImage.launch(arrayOf("image/*")) }
        btnUpdate?.setOnClickListener {
            val base = current ?: return@setOnClickListener
            val updated = Review(
                id = base.id,
                name = etName?.text.toString().trim(),
                rating = etRating?.text.toString().trim(),
                review = etReview?.text.toString().trim(),

                photoPath = pickedPhotoPath ?: base.photoPath,
                latitude = base.latitude,
                longitude = base.longitude
            )
            Thread {
                val dao = AppDatabase.get(applicationContext).reviewDao()
                if (pickedPhotoPath != null && base.photoPath != null && base.photoPath !=
                    pickedPhotoPath) {

                    photoRepo.deletePhoto(base.photoPath)
                }
                dao.update(updated)
                runOnUiThread {
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }
        btnDelete?.setOnClickListener {
            val toDelete = current ?: return@setOnClickListener
            Thread {

                val dao = AppDatabase.get(applicationContext).reviewDao()
                dao.delete(toDelete)
                photoRepo.deletePhoto(toDelete.photoPath)
                runOnUiThread {
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }
        btnCancel?.setOnClickListener { finish() }

    }
    private fun bindReview(c: Review) {
        etName?.setText(c.name)
        etRating?.setText(c.rating)
        etReview?.setText(c.review)
        when {
            !c.photoPath.isNullOrBlank() && File(c.photoPath).exists() ->
                ivPreview?.load(File(c.photoPath))
            else -> ivPreview?.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

}