package ru.focusstart.kireev.homeworknetwork

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.focusstart.kireev.homeworknetwork.network.ApiFactory
import ru.focusstart.kireev.homeworknetwork.network.DocumentHelper
import ru.focusstart.kireev.homeworknetwork.network.ImageResponse
import java.io.File


class MainFragment : Fragment(R.layout.fragment_main) {
    private var photoFile: File? = null
    private var uriImage: Uri? = null

    companion object {
        fun newInstance() = MainFragment()
        private const val REQUEST_PHOTO_FILE = 123
        private const val REQUEST_CHOOSE_PHOTO = 321
        private const val TAG = "MainFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.buttonTakePhoto.setOnClickListener { takePhoto() }
        view.buttonShareOnImgur.setOnClickListener { shareOnImgur() }
        view.buttonChoosePhoto.setOnClickListener { choosePhoto() }
    }

    private fun takePhoto() {
        val fragment = PhotoFragment.newInstance()
        fragment.setTargetFragment(this, REQUEST_PHOTO_FILE)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.add(R.id.main_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun choosePhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO)
    }

    private fun shareOnImgur() {
        if (photoFile == null) {
            Toast.makeText(
                requireContext(),
                "Требуется выбрать фото для отправки и заполнить все поля",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            makeToast("Отправка..")
            progressBar.visibility = View.VISIBLE
            photoFile?.let { photo ->
                val body = RequestBody.create(MediaType.parse("image/*"), photo)
                val multiPartBody = MultipartBody.Part.createFormData("image", photo.name, body)
                val photoName = editTextPhotoName.text.toString().trim()
                val photoTitle = editTextPhotoTitle.text.toString().trim()
                val photoDescription = editTextPhotoDescription.text.toString().trim()
                val factory = ApiFactory.getInstance()
                val apiService = factory.getApiService()
                apiService.postImage(
                    token = getString(R.string.TOKEN),
                    name = createMultiPart("name", "$photoName.jpg"),
                    title = createMultiPart("title", photoTitle),
                    description = createMultiPart("description", photoDescription),
                    file = multiPartBody
                ).enqueue(object : Callback<ImageResponse> {
                    override fun onResponse(
                        call: Call<ImageResponse>,
                        response: Response<ImageResponse>
                    ) {
                        progressBar.visibility = View.INVISIBLE
                        if (response.isSuccessful) {
                            makeToast("Отправлено успешно - ${response.body()?.data?.link}")
                            disableUi()
                        } else {
                            makeToast("Ошибка при отправке - $response")
                        }
                    }

                    override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                        progressBar.visibility = View.INVISIBLE
                        makeToast("Ошибка при отправке - ${t.message}")
                    }
                })
            }
        }
    }

    private fun makeToast(msg: String) {
        Toast.makeText(
            requireContext(),
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun getFilePath() {
        val filePath = DocumentHelper.getPath(requireContext(), uriImage)
        if (filePath == null || filePath.isEmpty()) {
            return
        }
        photoFile = File(filePath)
    }

    private fun createMultiPart(name: String, value: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(name, value)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_PHOTO_FILE -> {
                photoFile = data?.getSerializableExtra(PhotoFragment.EXTRA_PHOTO_FILE) as File
                enableUi()
            }
            REQUEST_CHOOSE_PHOTO -> {
                data?.let {
                    val uri = it.data ?: return
                    uriImage = uri
                    getFilePath()
                }
                enableUi()
            }
        }
    }

    private fun enableUi() {
        editTextPhotoName.visibility = View.VISIBLE
        editTextPhotoDescription.visibility = View.VISIBLE
        editTextPhotoTitle.visibility = View.VISIBLE
        buttonShareOnImgur.visibility = View.VISIBLE
    }

    private fun disableUi() {
        editTextPhotoName.apply {
            visibility = View.INVISIBLE
            setText("")
        }
        editTextPhotoDescription.apply {
            visibility = View.INVISIBLE
            setText("")
        }
        editTextPhotoTitle.apply {
            visibility = View.INVISIBLE
            setText("")
        }
        buttonShareOnImgur.visibility = View.INVISIBLE
        photoFile = null
        uriImage = null
    }
}