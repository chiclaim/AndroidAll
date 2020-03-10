package com.chiclaim.android.retrofit_sample

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.chiclaim.android.retrofit_sample.helper.UriHelper
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
import retrofit2.http.*


/**
 * 单文件、多文件上传、每个参数的意义。
 *
 * 主要涉及到的注解：
 *
 * @POST
 * @Multipart
 * @Part
 * @PartMap
 * @Body
 *
 * 主要涉及到的类：
 *
 * MultipartBody
 * MultipartBody.Part
 * RequestBody
 *
 */
class FileUploadActivity : BaseActivity() {

    companion object {
        const val PICK_FILE_RESULT_CODE = 1
        const val MAX_FILE_NUMBER = 2

        // set your base url
        const val API_URL = "http://192.168.50.195:8080/"

        const val MENU_ID_SINGLE_FILE = 1
        const val MENU_ID_MULTI_FILES_BY_BODY = 2
        const val MENU_ID_MULTI_FILES_BY_LIST_PART = 3
        const val MENU_ID_PART_MAP = 4


    }

    private val files = hashMapOf<String, Uri>()

    private var mode = MENU_ID_PART_MAP


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

        switchTitle()

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
                MENU_ID_SINGLE_FILE -> {
                    singleFileUri?.let {
                        uploadFile(it)
                    }
                }
                MENU_ID_MULTI_FILES_BY_BODY -> {
                    uploadFileListByMultipartBody()
                }
                MENU_ID_MULTI_FILES_BY_LIST_PART -> {
                    uploadFileListByListPart()
                }
                MENU_ID_PART_MAP -> {
                    uploadFileListByListPart2()
                }
            }


        }
    }

    private fun getTitlePlaceholder(mode: CharSequence): CharSequence {
        return "${getString(R.string.label_file_upload)}($mode)"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(1, MENU_ID_SINGLE_FILE, 1, getString(R.string.menu_upload_mode_single))
        menu.add(1, MENU_ID_MULTI_FILES_BY_BODY, 2, getString(R.string.menu_upload_mode_multi_by_body))
        menu.add(1, MENU_ID_MULTI_FILES_BY_LIST_PART, 3, getString(R.string.menu_upload_mode_multi_by_list))
        menu.add(1, MENU_ID_PART_MAP, 4, getString(R.string.menu_upload_mode_part_map))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mode = item.itemId
        switchTitle()
        return super.onOptionsItemSelected(item)
    }

    private fun switchTitle() {
        when (mode) {
            MENU_ID_SINGLE_FILE -> {
                title = getTitlePlaceholder(getString(R.string.menu_upload_mode_single))
            }
            MENU_ID_MULTI_FILES_BY_BODY -> {
                title = getTitlePlaceholder(getString(R.string.menu_upload_mode_multi_by_body))

            }
            MENU_ID_MULTI_FILES_BY_LIST_PART -> {
                title = getTitlePlaceholder(getString(R.string.menu_upload_mode_multi_by_list))
            }
            MENU_ID_PART_MAP -> {
                title = getTitlePlaceholder(getString(R.string.menu_upload_mode_part_map))
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

    private fun getMediaType(uri: Uri): MediaType? {
        val mimeType = getMimeType(uri)
        if (mimeType != null) {
            return MediaType.parse(mimeType)
        }
        return null
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
        //val formFieldBody = RequestBody.create(MultipartBody.FORM, "通过 MultipartBody 多文件上传")
        //execute(uploadService.upload(formFieldBody, buildMultipartBody()))
        execute(uploadService.upload(buildMultipartBody()))
    }

    private fun uploadFileListByListPart() {
        val formFieldBody = RequestBody.create(MultipartBody.FORM, "通过 List<MultipartBody.Part> 多文件上传")
        execute(uploadService.upload(formFieldBody, buildListPart()))
    }

    private fun uploadFileListByListPart2() {

        val partMap = hashMapOf<String, RequestBody>().apply {
            this["description0"] = RequestBody.create(MultipartBody.FORM, "通过 List<MultipartBody.Part> 多文件上传 PartMap0")
            this["description1"] = RequestBody.create(MultipartBody.FORM, "通过 List<MultipartBody.Part> 多文件上传 PartMap1")
        }
        execute(uploadService.upload(partMap, buildListPart()))
    }

    private fun singleFileCall(fileUri: Uri): Call<ResponseBody>? {

        // 多媒体类型，例如： image/jpeg
        val mediaType = getMediaType(fileUri)
        val file = UriHelper.getFileFromUri(applicationContext, fileUri)

        val fileRequestBody = RequestBody.create(mediaType, file)

        // 相当于 HTML 页面里面的 文件选择控件： <input type="file" name="file_1"/>
        val filePart = MultipartBody.Part.createFormData("file_1", file.name, fileRequestBody)

        // 创建 form 字段，相当于 HTML 页面里面的 <input type="text" name="description"/>
        val formFieldPart = RequestBody.create(MultipartBody.FORM, "单文件上传")

        return uploadService.upload(formFieldPart, filePart)
    }


    private fun buildMultipartBody(): MultipartBody {
        val builder = MultipartBody.Builder()

        // 方式 3 不会发送请求头，charset=utf-8 可能会导致乱码
        // builder.addFormDataPart("description0", "通过 List<MultipartBody.Part> 多文件上传 in build MultipartBody")

        // 会发送请求头：Content-Type: multipart/form-data; charset=utf-8
        val param1 = RequestBody.create(MultipartBody.FORM, "通过 MultipartBody 多文件上传 in buildMultipartBody 参数1")
        builder.addFormDataPart("description0", null, param1)

        val param2 = RequestBody.create(MultipartBody.FORM, "通过 MultipartBody 多文件上传 in buildMultipartBody 参数1")
        builder.addFormDataPart("description0", null, param2)


        var index = 0
        files.forEach { entry: Map.Entry<String, Uri> ->
            val uri = entry.value
            val mediaType = getMediaType(uri)
            val file = UriHelper.getFileFromUri(applicationContext, uri)
            val requestBody = RequestBody.create(mediaType, file)
            builder.addFormDataPart("file${++index}", file.name, requestBody)
        }

        return builder.build()
    }

    private fun buildListPart(): List<MultipartBody.Part> {
        val list = arrayListOf<MultipartBody.Part>()

        // 方式 1
        // 通过 @Part("description") description: RequestBody 注解的方式来传递普通 form 参数

        // 方式 2 （不可以）
        //val formFieldBody = RequestBody.create(MultipartBody.FORM, "通过 List<MultipartBody.Part> 多文件上传 in list")
        //list.add(MultipartBody.Part.create(formFieldBody))

        // 方式 3 不会发送请求头，charset=utf-8 可能会导致乱码
        // list.add(MultipartBody.Part.createFormData("description0", "通过 List<MultipartBody.Part> 多文件上传 in list 参数0"))
        // list.add(MultipartBody.Part.createFormData("description1", "通过 List<MultipartBody.Part> 多文件上传 in list 参数1"))

        // 会发送请求头：Content-Type: multipart/form-data; charset=utf-8
        val param1 = RequestBody.create(MultipartBody.FORM, "通过 List<MultipartBody.Part> 多文件上传 in list 参数0")
        list.add(MultipartBody.Part.createFormData("description0", null, param1))

        val param2 = RequestBody.create(MultipartBody.FORM, "通过 List<MultipartBody.Part> 多文件上传 in list 参数1")
        list.add(MultipartBody.Part.createFormData("description1", null, param2))


        var index = 0
        files.forEach {
            val fileUri = it.value
            val mediaType = getMediaType(fileUri)
            val file = UriHelper.getFileFromUri(applicationContext, fileUri)
            val fileRequestBody = RequestBody.create(mediaType, file)
            val filePart = MultipartBody.Part.createFormData("file_${++index}", file.name, fileRequestBody)
            list.add(filePart)
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


        @Multipart
        @POST("fileUpload")
        fun upload(
            // @PartMap partMap: Map<String, RequestBody>,
            // java.lang.IllegalArgumentException: Parameter type must not include a type variable or wildcard: java.util.Map<java.lang.String, ? extends okhttp3.RequestBody> (parameter #1)
            // kotlin 和 java 泛型的不同导致的，具体可以查看 我的博客： https://blog.csdn.net/johnny901114/article/details/85575213
            @PartMap partMap: HashMap<String, RequestBody>,
            @Part parts: List<MultipartBody.Part>
        ): Call<ResponseBody>


        /**
         * 多文件上传（MultipartBody）
         */
        // @Multipart，参数中使用了 @Body 注解，该注解不能和 @Multipart 一起使用
        // java.lang.IllegalArgumentException: @Body parameters cannot be used with form or multi-part encoding. (parameter #1)
        @POST("fileUpload")
        fun upload(
            // @Part("description") description: RequestBody, 该注解只能配合 @Multipart 使用
            // java.lang.IllegalArgumentException: @Part parameters can only be used with multipart encoding. (parameter #1)
            @Body multipartBody: MultipartBody
        ): Call<ResponseBody>
    }


}