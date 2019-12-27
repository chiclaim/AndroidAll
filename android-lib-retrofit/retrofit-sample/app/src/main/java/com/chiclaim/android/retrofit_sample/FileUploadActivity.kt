package com.chiclaim.android.retrofit_sample

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_file_load_activity.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File


/**
 * 单文件、多文件上传、每个参数的意义
 */
class FileUploadActivity : BaseActivity() {

    companion object {
        const val PICK_FILE_RESULT_CODE = 1
        const val MAX_FILE_NUMBER = 2

        // set your base url
        const val API_URL = "http://10.1.81.240:8080/"

        const val MODE_SINGLE_FILE = 1
        const val MODE_MULTIPLE_FILE_BY_MULTIPLE_BODY = 2
        const val MODE_MULTIPLE_FILE_BY_LIST_PART = 3




    }

    private val files = hashMapOf<String, Uri>()

    private val mode = MODE_MULTIPLE_FILE_BY_MULTIPLE_BODY


    private val uploadService: FileUploadService by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(FileUploadService::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_load_activity)

        btn_choose_file.setOnClickListener {
            startChooseFile()
        }


        btn_file_upload.setOnClickListener {
            if (files.isEmpty()) {
                Toast.makeText(this, "请选择文件", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var singleFileUri: Uri? = null
            files.forEach { entry: Map.Entry<String, Uri> ->
                if (singleFileUri == null) {
                    singleFileUri = entry.value
                }
                Log.e("FileUploadActivity", "file path : ${entry.value.path}")
            }

            when (mode) {
                MODE_SINGLE_FILE -> {
                    singleFileUri?.let {
                        uploadFile(it)
                    }
                }
                MODE_MULTIPLE_FILE_BY_MULTIPLE_BODY -> {
                    uploadFileListByMultipartBody()
                }
                MODE_MULTIPLE_FILE_BY_LIST_PART -> {
                    uploadFileListByListPart()
                }
            }


        }
    }

    private fun startChooseFile() {
        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.type = "*/*"
        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(chooseFile, PICK_FILE_RESULT_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            PICK_FILE_RESULT_CODE -> {
                val fileUri = data?.data
                if (fileUri == null) {
                    Toast.makeText(this, "fileUri is null", Toast.LENGTH_SHORT).show()
                    return
                }
                val filePath = fileUri.path
                filePath?.let {
                    if (files.size >= MAX_FILE_NUMBER) {
                        Toast.makeText(this, "最多只能上传 $MAX_FILE_NUMBER 个文件", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (!files.contains(filePath)) {
                        files[filePath] = fileUri
                        text_file_path.append("\n")
                        text_file_path.append(filePath)
                    } else {
                        Toast.makeText(this, "文件已经存在", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun getMimeType(uri: Uri) = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        contentResolver.getType(uri)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
    }


    private fun execute(call: Call<ResponseBody>?) {
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(applicationContext, "upload success", Toast.LENGTH_SHORT).show()
                files.clear()
                text_file_path.setText(R.string.file_upload_prefix)
            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uploadFile(fileUri: Uri) {
        execute(singleFileCall(fileUri))
    }

    private fun uploadFileListByMultipartBody() {
        val formFieldBody = RequestBody.create(MultipartBody.FORM, "通过 MultipartBody 多文件上传")
        execute(uploadService.upload(formFieldBody, buildMultipartBody()))
    }

    private fun uploadFileListByListPart() {
        val formFieldBody = RequestBody.create(MultipartBody.FORM, "通过 List<MultipartBody.Part> 多文件上传")
        execute(uploadService.upload(formFieldBody, buildListPart()))
    }

    private fun singleFileCall(fileUri: Uri): Call<ResponseBody>? {

        // 多媒体类型，例如： image/jpeg
        val mineType = getMimeType(fileUri) ?: return null

        val file = File(fileUri.path)

        val fileRequestBody = RequestBody.create(MediaType.parse(mineType), file)

        // 相当于 HTML 页面里面的 文件选择控件： <input type="file" name="file_1"/>
        val filePart = MultipartBody.Part.createFormData("file_1", file.name, fileRequestBody)

        // 创建 form 字段，相当于 HTML 页面里面的 <input type="text" name="description"/>
        val formFieldPart = RequestBody.create(MultipartBody.FORM, "单文件上传")

        return uploadService.upload(formFieldPart, filePart)
    }


    private fun buildMultipartBody(): MultipartBody {
        val builder = MultipartBody.Builder()
        var index = 0
        files.forEach { entry: Map.Entry<String, Uri> ->
            val uri = entry.value
            val mineType = getMimeType(uri)
            mineType?.let {
                val file = File(uri.path)
                val requestBody = RequestBody.create(MediaType.parse(it), file)
                builder.addFormDataPart("file${++index}", file.name, requestBody)
            }
        }
        // 方式 3
//        builder.addFormDataPart("description0", "通过 List<MultipartBody.Part> 多文件上传 in build MultipartBody")
//        builder.addPart(MultipartBody.Part.createFormData("description0", "通过 List<MultipartBody.Part> 多文件上传 in build MultipartBody"))
        return builder.build()
    }

    private fun buildListPart(): List<MultipartBody.Part> {
        val list = arrayListOf<MultipartBody.Part>()

        // 方式 1
        // 通过 @Part("description") description: RequestBody 注解的方式来传递普通 form 参数

        // 方式 2 （不可以）
        //val formFieldBody = RequestBody.create(MultipartBody.FORM, "通过 List<MultipartBody.Part> 多文件上传 in list")
        //list.add(MultipartBody.Part.create(formFieldBody))

        // 方式 3
        list.add(MultipartBody.Part.createFormData("description0", "通过 List<MultipartBody.Part> 多文件上传 in list"))


        var index = 0
        files.forEach {
            val fileUri = it.value
            val mineType = getMimeType(fileUri)
            mineType?.let {
                val file = File(fileUri.path)
                val fileRequestBody = RequestBody.create(MediaType.parse(mineType), file)
                val filePart = MultipartBody.Part.createFormData("file_${++index}", file.name, fileRequestBody)
                list.add(filePart)
            }
        }


        return list
    }


    interface FileUploadService {
        /**
         * 单文件上传
         */
        @Multipart
        @POST("fileUpload")
        fun upload(
            @Part("description") description: RequestBody,
            @Part file: MultipartBody.Part
        ): Call<ResponseBody>


        /**
         * 多文件上传（List<MultipartBody.Part>）
         */
        @Multipart
        @POST("fileUpload")
        fun upload(
            @Part("description") description: RequestBody,
            @Part parts: List<MultipartBody.Part>
        ): Call<ResponseBody>


        /**
         * 多文件上传（MultipartBody）
         */
        @POST("fileUpload")
        fun upload(
            @Part("description") description: RequestBody,
            @Body multipartBody: MultipartBody
        ): Call<ResponseBody>
    }


}