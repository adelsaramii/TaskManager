package com.example.taskmanager.utils

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody

class UploadUtility(private var activity: Activity) {

    //CLASS FOR UPLOAD IMAGES TO SERVER

    var dialog: ProgressDialog? = null
    var serverURL: String = "http://private-app-key.ravanfix.com/app/apphelper/upload.php"
    private val client = OkHttpClient()
    private var url = ""

    fun uploadFile(sourceFileUri: Uri, uploadedFileName: String? = null) :String{
        val pathFromUri = URIPathHelper().getPath(activity, sourceFileUri)
        return uploadFile(File(pathFromUri), uploadedFileName)
    }
    private fun uploadFile(sourceFile: File, uploadedFileName: String? = null) :String{
        val mimeType = getMimeType(sourceFile);
        if (mimeType == null) {
            Log.e("file error", "Not able to get mime type")
        }
        val fileName: String =
            if (uploadedFileName == null) sourceFile.name else uploadedFileName
        toggleProgressDialog(true)
        try {
            val requestBody: RequestBody =
                MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "uploaded_file",
                        fileName,
                        sourceFile.asRequestBody(mimeType!!.toMediaTypeOrNull())
                    )
                    .build()

            val request: Request = Request.Builder().url(serverURL).post(requestBody).build()

            val response: Response = client.newCall(request).execute()

            if (response.isSuccessful) {
                url = fileName
                toggleProgressDialog(false)
                return url
            } else {
                toggleProgressDialog(false)
                return url
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            toggleProgressDialog(false)
            return url
        }
    }
    private fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
    private fun toggleProgressDialog(show: Boolean) {
        activity.runOnUiThread {
            if (show) {
                dialog = ProgressDialog.show(activity, "", "Uploading file...", true);
            } else {
                dialog?.dismiss();
            }
        }
    }

}