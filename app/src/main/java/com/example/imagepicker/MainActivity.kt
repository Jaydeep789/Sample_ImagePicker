package com.example.imagepicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.*
import androidx.core.content.ContextCompat
import java.io.InputStream
import java.io.OutputStream
import java.lang.IllegalStateException
import java.net.URI
import java.nio.charset.Charset
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

//    lateinit var imageView : ImageView
    lateinit var button: Button
    lateinit var button1 : Button
    lateinit var textView: TextView
    lateinit var editText: EditText

    companion object{
        const val PERMISSION_CODE = 10
        const val REQUEST_CODE = 11
        const val REQUEST_SAF_OPEN = 12
        const val REQUEST_SAF_CREATE = 13
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        imageView = findViewById(R.id.image)
        button = findViewById(R.id.buttton)
        button1 = findViewById(R.id.buttton1)
        textView = findViewById(R.id.text)
        editText = findViewById(R.id.edit)



        button.setOnClickListener {
//            versionCheck()
            openText()
        }

        button1.setOnClickListener {
            createText()
        }
    }

    private fun createText() {
        val intent1 = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent1.type = "text/plain"
        intent1.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent1, REQUEST_SAF_CREATE)
    }

//    fun versionCheck(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            // Greater than marshmellow
//            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
//                // Permission denied
//                val array = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                requestPermissions(array, PERMISSION_CODE)
//            }else{
//                // Permission already granted
//                pickImage();
//            }
//        }else{
//            //Version less than Marshmellow
//            pickImage()
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == PERMISSION_CODE){
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                pickImage()
//            }else{
//                Toast.makeText(this,"Permission...denied",Toast.LENGTH_SHORT).show()
//            }
//        }
//    }



    private fun openText() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "text/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, REQUEST_SAF_OPEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SAF_OPEN && resultCode == Activity.RESULT_OK){
            data?.data.let {
                textView.text = it?.let { it1 -> readURI(this, it1) }
            }
        }
        if (requestCode == REQUEST_SAF_CREATE && resultCode == Activity.RESULT_OK){
            data?.data.let {
                it?.let {
                    write(this,it,editText.text.toString())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        editText.text = null
    }

    fun readURI(context: Context, uri: Uri): String{
        val resolver = context.contentResolver
        return resolver.openInputStream(uri).use {
            it?.readText()
        }.toString()
    }

    private fun InputStream.readText(charset: Charset = Charsets.UTF_8):String = readBytes().toString(charset)

    fun write(context: Context, uri: Uri, text : String){
        val resolver = context.contentResolver

        resolver.openOutputStream(uri)?.use {
            it.writeText(text)
        }
    }

    private fun OutputStream.writeText(text: String, charset: Charset = Charsets.UTF_8) : Unit = write(text.toByteArray(charset))
}
