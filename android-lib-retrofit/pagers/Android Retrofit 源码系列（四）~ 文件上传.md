





## 前言

今天我们来聊一聊 Retrofit 文件上传。为了调试 Retrofit 文件上传功能，我搭建了简单的服务器来接收客户端上传的文件。为了减少篇幅我就不将服务端的代码贴出来了，有兴趣的可以查看我的 GitHub ：[https://github.com/chiclaim/WebApp](https://github.com/chiclaim/WebApp)

本文主要讲 Retrofit 文件上传功能主要包括：

- Retrofit 文件上传
- 文件上传遇到的问题
- 分析问题原因以及如何解决该问题


## Retrofit 文件上传


在实际开发中我们可能经常遇到文件上传的功能，多文件上传，图文上传等。

比如我们要上传单个文件，外加一个描述字段，我们想一下在网页端我们是怎么做的（一个文件选择器和一个输入框）：

![FileUpload](https://img-blog.csdnimg.cn/20200118142841188.jpg)

那么我们通过 Retrofit 中如何来实现呢，大家在网上一搜很容易就能找到，例如：

```
/**
 * 单文件上传
 */
@Multipart
@POST("fileUpload")
fun upload(
    @Part("description") description: RequestBody,
    @Part file: MultipartBody.Part
): Call<ResponseBody>


fun fileUpload() {
    val fileRequestBody = RequestBody.create(mediaType, file)
    val filePart = MultipartBody.Part.createFormData("file_1", file.name, fileRequestBody)
    val formFieldPart = RequestBody.create(MultipartBody.FORM, "单文件上传")
    uploadService.upload(formFieldPart, filePart)
}
```

`MultipartBody.Part.createFormData` 方法有 `3` 个参数：

```
createFormData(String name, @Nullable String filename, RequestBody body){
    if (name == null) {
        throw new NullPointerException("name == null");
    }
    // ...
}
```
其中 `name` 和 `filename` 有什么区别？ filename 顾名思义就是文件的名称，那 name 代表的是什么呢？ 

有的开发者没有搞清楚其中的区别，就将 name 设置成 filename，这可能会产生问题，因为 filename 是可以为空的，而 name 是不能为空的，否则会抛出空指针异常。

其实这一行代码：

```
val filePart = MultipartBody.Part.createFormData("file_1", file.name, fileRequestBody)
```

相当于 HTML 页面里面的 `文件选择控件` 对应的代码：

```
<input type="file" name="file_1"/>
```

`createFormData` 方法的第一个参数 `name` 就相当于 `input` 控件的 `name` 属性

### 多文件上传(List<MultipartBody.Part>)

如果我们的需要上传的文件数量是可变的呢？我们可以将 `MultipartBody.Part` 放在一个集合中：

```
/**
 * 多文件上传（List<MultipartBody.Part>）
 */
@Multipart
@POST("fileUpload")
fun upload(
    @Part("description") description: RequestBody,
    @Part parts: List<MultipartBody.Part>
): Call<ResponseBody>


// ======文件上传
fun fileUpload() {
    val formFieldBody = RequestBody.create(MultipartBody.FORM, "通过 List<MultipartBody.Part> 多文件上传")
    execute(uploadService.upload(formFieldBody, buildListPart()))
}

private fun buildListPart(): List<MultipartBody.Part> {
    val list = arrayListOf<MultipartBody.Part>()

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

```

服务器端我们打印了文本字段以及上传的图片信息：

```
for (FileItem item : formItems) {
    // 处理文件
    if (!item.isFormField()) {

        if (Objects.isNull(item.getName()) || "".equals(item.getName().trim())) continue;

        String fileName = new File(item.getName()).getName();
        String filePath = uploadPath + File.separator + fileName;
        File storeFile = new File(filePath);
        item.write(storeFile);

        System.out.println("文件存储路径：" + storeFile.getAbsolutePath());
    } else { // 处理普通字段
        System.out.println(getFieldName -> " + item.getFieldName() + ", getString -> " + item.getString("UTF-8"));
    }
}

```

然后我们来测试下，服务器控制台输出结果：

```
getFieldName -> description, getString -> 通过 List<MultipartBody.Part> 多文件上传
文件存储路径：D:\xxx\WebApp\out\artifacts\ServletDemo_Web_exploded\upload\shumei.txt
```

如果需要上传的文本字段的数量也是可变的呢？我们可以将文本字段放在 `Map` 集合中，然后使用 `@PartMap` 注解来修饰：

```
@Multipart
@POST("fileUpload")
fun upload(
    @PartMap partMap: HashMap<String, RequestBody>,
    @Part parts: List<MultipartBody.Part>
): Call<ResponseBody>
```

我们来测试下，看看服务器控制台输出结果：

```
getFieldName -> description0, getString -> 通过 List<MultipartBody.Part> 多文件上传 PartMap0
getFieldName -> description1, getString -> 通过 List<MultipartBody.Part> 多文件上传 PartMap1
文件存储路径：D:\xxx\WebApp\out\artifacts\ServletDemo_Web_exploded\upload\shumei.txt
```

其实对于文本字段我们也可以放在 `List<MultipartBody.Part>` 中，也就是说不用单独声明一个 `Map` 集合来保存普通文本信息：

```
@Multipart
@POST("fileUpload")
fun upload(
    @Part parts: List<MultipartBody.Part>
): Call<ResponseBody>

private fun buildListPart(): List<MultipartBody.Part> {
    val list = arrayListOf<MultipartBody.Part>()
    list.add(MultipartBody.Part.createFormData("description0", "通过 List<MultipartBody.Part> 多文件上传 in list 参数0"))
    list.add(MultipartBody.Part.createFormData("description1", "通过 List<MultipartBody.Part> 多文件上传 in list 参数1"))

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

```

我们来测试下，看看服务器控制台输出结果：

```
getFieldName -> description0, getString -> éè¿ List<MultipartBody.Part> å¤æä»¶ä¸ä¼  in list åæ°0
getFieldName -> description1, getString -> éè¿ List<MultipartBody.Part> å¤æä»¶ä¸ä¼  in list åæ°1
文件存储路径：D:\xxx\WebApp\out\artifacts\ServletDemo_Web_exploded\upload\shumei.txt
```

### 乱码原因分析及解决

我们发现产生了乱码。我们先从服务器端的角度来看这个问题，上面乱码的内容是通过 `FileItem.getString` 方法获取的，我们就来看下该方法的源码：

```
public String getString() {
    byte[] rawdata = this.get();
    String charset = this.getCharSet();
    if (charset == null) {
        charset = "ISO-8859-1";
    }

    try {
        return new String(rawdata, charset);
    } catch (UnsupportedEncodingException var4) {
        return new String(rawdata);
    }
}
```
从中得知，如果没有设置字符集 Charset，那么则使用 `ISO-8859-1`，这肯定会产生乱码，所以调用 `FileItem.getString` 方法的时候传递 `UTF-8` 字符集：

```
item.getString("UTF-8")
```

此时，我们再来看看服务器控制台的输出：

```
getFieldName -> description0, getString -> 通过 List<MultipartBody.Part> 多文件上传 in list 参数0
getFieldName -> description1, getString -> 通过 List<MultipartBody.Part> 多文件上传 in list 参数1
文件存储路径：D:\xxx\WebApp\out\artifacts\ServletDemo_Web_exploded\upload\shumei.txt
```

发现乱码问题已经好了。有人可能会问了，服务器端使用 `UTF-8` 来读取，你怎么客户端是以 `UTF-8` 编码发送过来的呢？因为 Android 是使用 UTF-8 来进行编码的。

我们还可以通过另一个例子对乱码问题再讲解下。我在 WebApp 里新建了一个 `upload.jsp` ，如下图所示：

![FileUpload](https://img-blog.csdnimg.cn/20200118142841188.jpg)

如果服务器端不设置  `item.getString("UTF-8")`，网页端上传中文也会乱码的。因为网页端也是以 `UTF-8` 编码发送过来的，为什么呢？因为我们在 `jsp` 文件中设置了 `UTF-8` 字符集：

```
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
```

我们可以将其改成 GBK：

```
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK" %>
```

看看控制台输出：

```
getFieldName -> username, getString -> �ȸ�ռ��
文件存储路径：D:\xxx\WebApp\out\artifacts\ServletDemo_Web_exploded\upload\222.pdf
```

这是因为网页客户端通过 `GBK` 编码将参数传递服务器，服务器以 `UTF-8` 来解码，所以出现了乱码问题。

好，我们在回到 Android 端。在服务器端不设置 UTF-8 的情况下，为什么通过 `@PartMap` 的方法是传递中文就不会乱码，通过上面方式就会乱码呢？

对于这个问题，我们分别抓包看下就知道，它们两个在传输的过程中有什么差别？

通过 `@PartMap`：

```
--601c02cc-341c-4362-8a53-c7190066ee3f
Content-Disposition: form-data; name="description0"
Content-Transfer-Encoding: binary
Content-Type: multipart/form-data; charset=utf-8
Content-Length: 56

通过 List<MultipartBody.Part> 多文件上传 PartMap0
--601c02cc-341c-4362-8a53-c7190066ee3f
Content-Disposition: form-data; name="description1"
Content-Transfer-Encoding: binary
Content-Type: multipart/form-data; charset=utf-8
Content-Length: 56

通过 List<MultipartBody.Part> 多文件上传 PartMap1
```

通过 `List<MultipartBody.Part>`：

```
--601c02cc-341c-4362-8a53-c7190066ee3f
Content-Disposition: form-data; name="description0"
Content-Length: 63

通过 List<MultipartBody.Part> 多文件上传 in list 参数0
--601c02cc-341c-4362-8a53-c7190066ee3f
Content-Disposition: form-data; name="description1"
Content-Length: 63

通过 List<MultipartBody.Part> 多文件上传 in list 参数1

```

对比我们发现 `@PartMap` 比 `List<MultipartBody.Part>` 的方式多传输了：

```
Content-Transfer-Encoding: binary
Content-Type: multipart/form-data; charset=utf-8
```

也就是说客户端告知了服务器我是以 `utf-8` 编码的，那么服务器在解析这个请求的时候，封装 FileItem 的时候就会设置 charset，FileItem.getString() 的时候里面的 charset 就不会为空，自然就不会以 `ISO-8859-1` 来解码了。

同样都是 Retrofit API，为什么一个就会设置 charset，一个就不会呢？ 我们深入源码来看看究竟。

先看下我们是怎么创建 `MultipartBody.Part` 的：

```
list.add(MultipartBody.Part.createFormData("description0", "通过 List<MultipartBody.Part> 多文件上传 in list 参数0"))
list.add(MultipartBody.Part.createFormData("description1", "通过 List<MultipartBody.Part> 多文件上传 in list 参数1"))
```

我们是通过 `MultipartBody.Part.createFormData` 来创建 `Part` 然后放进集合的，那么我们就来看看该方法：

```
public static Part createFormData(String name, String value) {
  return createFormData(name, null, RequestBody.create(null, value));
}
```

再来看看 `RequestBody.create` 方法：

```
public static RequestBody create(@Nullable MediaType contentType, String content) {
    Charset charset = UTF_8;
    if (contentType != null) {
      charset = contentType.charset();
      if (charset == null) {
        charset = UTF_8;
        contentType = MediaType.parse(contentType + "; charset=utf-8");
      }
    }
    byte[] bytes = content.getBytes(charset);
    return create(contentType, bytes);
}
```

我们发现只有当 `MediaType contentType` 不为空的时候会设置 `content-type` 和 `charset`。

而 createFormData 方法将 MediaType 设置为 null，所以在抓包的时候为什么没有向服务器传递 charset 信息。

既然知道了原因，我们设置 MediaType 不就可以从客户端角度来解决这个问题吗？

所以只能我们自己来创建 RequestBody，然后将 RequestBody 传递给 createFormData 方法即可：

```
val param1 = RequestBody.create(MultipartBody.FORM, "通过 List<MultipartBody.Part> 多文件上传 in list 参数0")
list.add(MultipartBody.Part.createFormData("description0", null, param1))

val param2 = RequestBody.create(MultipartBody.FORM, "通过 List<MultipartBody.Part> 多文件上传 in list 参数1")
list.add(MultipartBody.Part.createFormData("description1", null, param2))

```

在服务器端不设置 UTF-8 的情况下，看看控制台输出的结果：

```
getFieldName -> description0, getString -> 通过 List<MultipartBody.Part> 多文件上传 in list 参数0
getFieldName -> description1, getString -> 通过 List<MultipartBody.Part> 多文件上传 in list 参数1
文件存储路径：D:\xxx\WebApp\out\artifacts\ServletDemo_Web_exploded\upload\shumei.tx
```

### 多文件上传(MultipartBody)

除了 `List<MultipartBody.Part>` 的方式来实现 `多图文` 上传，还可以通过 `MultipartBody` 来实现：

```
// UploadService.java
@POST("fileUpload")
fun upload(
    @Body multipartBody: MultipartBody
): Call<ResponseBody>

// 组装参数
private fun buildMultipartBody(): MultipartBody {
    val builder = MultipartBody.Builder()
    
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

// 执行上传文件
uploadService.upload(buildMultipartBody())
```

来看看服务器控制台输出结果：

```
getFieldName -> description0, getString -> 通过 MultipartBody 多文件上传 in buildMultipartBody 参数1
getFieldName -> description0, getString -> 通过 MultipartBody 多文件上传 in buildMultipartBody 参数1
文件存储路径：D:\dev\Workspace\MyGitHub\WebApp\out\artifacts\ServletDemo_Web_exploded\upload\shumei.tx
```


## 小结

本文主要介绍了 Retrofit 多图文上传功能，以及上传过程中遇到的中文乱码问题，我们从网页端、Android客户端、服务器端、Retrofit 源码角度 来分析了产生的原因及解决方案。

本文涉及到的服务器端程序代码地址：[WebApp](https://github.com/chiclaim/WebApp)

本文涉及到的客户端程序代码地址：[AndroidAll](https://github.com/chiclaim/AndroidAll)

**AndroidAll** 中除了 Retrofit，还有 Android 程序员需要掌握的技术栈，如：**程序架构、设计模式、性能优化、数据结构算法、Kotlin、Flutter、NDK、Router、RxJava、Glide、LeakCanary、Dagger2、Retrofit、OkHttp、ButterKnife、Router** 等等。持续更新，欢迎 star。



