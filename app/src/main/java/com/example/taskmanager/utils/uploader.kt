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

class UploadUtility(activity: Activity) {

    var activity = activity;
    var dialog: ProgressDialog? = null
    var serverURL: String = "http://private-app-key.ravanfix.com/app/apphelper/upload.php"
    var serverUploadDirectoryPath: String =
        "http://private-app-key.ravanfix.com/app/apphelper/data/"
    val client = OkHttpClient()
    var Url: String = ""

    fun uploadFile(sourceFilePath: String, uploadedFileName: String? = null) {
        uploadFile(File(sourceFilePath), uploadedFileName)
    }

    fun uploadFile(sourceFileUri: Uri, uploadedFileName: String? = null) :String{
        val pathFromUri = URIPathHelper().getPath(activity, sourceFileUri)
        return uploadFile(File(pathFromUri), uploadedFileName)

    }

    fun uploadFile(sourceFile: File, uploadedFileName: String? = null) :String{
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
                Url = fileName
                toggleProgressDialog(false)
                return Url
            } else {
                toggleProgressDialog(false)
                return Url
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            toggleProgressDialog(false)
            return Url
        }
    }

    // url = file path or whatever suitable URL you want.
    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun showToast(message: String) {
        activity.runOnUiThread {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }

    fun toggleProgressDialog(show: Boolean) {
        activity.runOnUiThread {
            if (show) {
                dialog = ProgressDialog.show(activity, "", "Uploading file...", true);
            } else {
                dialog?.dismiss();
            }
        }
    }

}