package com.example.note

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import com.example.note.EditActivity
import com.example.note.translate.TranslateActivity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class EditActivity : BaseActivity() {
    private var editText //编辑的笔记
            : EditText? = null
    private var btn_cemera : Button? = null
    private var btn_image : Button? = null
    private var btn_video : Button? = null
    private var img_photo: ImageView? = null
    private var v_video: VideoView? = null
    private var old_content: String? = ""
    private var old_time: String? = ""
    private var old_tag = 1
    private var old_img: String? = "null"
    private var old_video: String? = "null"
    private var id: Long = 0
    private var openMode = 0
    private val tag = 1
    private var noteToolbar: Toolbar? = null
    private var action = 0  // 0: 没干事情 1: 拍照 2: 插图  3: 视频
    private var imageUri: Uri? = null
    private var videoUri: Uri? = null
    private var photo_file: File? = null
    private var insert_file: File? = null
    private var video_file: File? = null

    /**
     * 引入menu菜单栏
     *
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_layout)

        btn_cemera = findViewById(R.id.button_cemera)
        btn_image = findViewById(R.id.button_image)
        btn_video = findViewById(R.id.button_video)
        img_photo = findViewById(R.id.img_photo)
        v_video = findViewById(R.id.v_video)

        v_video!!.setOnClickListener {
            v_video!!.start()    //播放
        }

        editText = findViewById(R.id.editText1)
        noteToolbar = findViewById(R.id.noteToolbar)
        editText!!.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                menu.add(0, 100, 0, "translate")
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                if (item.itemId == 100) {
                    val sta = editText!!.getSelectionStart()
                    val end = editText!!.getSelectionEnd()
                    val words = editText!!.getText().toString().substring(sta, end)
                    val intent = Intent(this@EditActivity, TranslateActivity::class.java)
                    intent.putExtra("translate_words", words)
                    startActivity(intent)
                }
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {}
        }

        btn_cemera!!.setOnClickListener {
            val camera = Intent("android.media.action.IMAGE_CAPTURE")   //开启系统相机

            photo_file = File(externalCacheDir, getDocumentName() + ".jpg")
            if (photo_file!!.exists()) {
                photo_file!!.delete()
            }
            photo_file!!.createNewFile()

            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this, "com.example.cameraalbumtest1.fileprovider",
                    photo_file!!
                );
            } else {
                Uri.fromFile(photo_file);
            }

            camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(camera, 1)
        }

        btn_image!!.setOnClickListener {
            // 打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            // 指定只显示照片
            intent.type = "image/*"
            startActivityForResult(intent, 2)
        }

        btn_video!!.setOnClickListener {
            val camera = Intent("android.media.action.VIDEO_CAPTURE")   //开启系统相机

            video_file = File(externalCacheDir, getDocumentName() + ".mp4")
            if (video_file!!.exists()) {
                video_file!!.delete()
            }
            video_file!!.createNewFile()

            videoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this, "com.example.cameraalbumtest1.fileprovider",
                    video_file!!
                );
            } else {
                Uri.fromFile(video_file);
            }

            camera.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
            startActivityForResult(camera, 3)
        }

        //设置Action Bar，自定义Toolbar
        setSupportActionBar(noteToolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //设置toolber取代action bar
        //设置Navigation返回键的事件监听，与系统的返回键功能相同
        noteToolbar?.setNavigationOnClickListener(View.OnClickListener {
            autoSetMessage(intent) //自动更新笔记
            setResult(RESULT_OK, intent)
            finish()
        })
        val getIntent = getIntent()
        //定义意图
        openMode = getIntent.getIntExtra("mode", 0)
        Log.d("System=====", "openMode:$openMode")
        if (openMode == 3) {
            //打开已存在的note，将内容写入到已编辑的笔记中，实现继续编辑
            id = getIntent.getLongExtra("id", 0)
            old_content = getIntent.getStringExtra("content")
            old_time = getIntent.getStringExtra("time")
            old_tag = getIntent.getIntExtra("tag", 1)
            if (getIntent.getStringExtra("image") == "null") {
                img_photo!!.visibility = View.GONE
            } else {
                img_photo!!.visibility = View.VISIBLE
                val bitmap = BitmapFactory.decodeFile(getIntent.getStringExtra("image"))
                old_img = getIntent.getStringExtra("image")
                img_photo!!.setImageBitmap(bitmap)
            }
            if (getIntent.getStringExtra("video") == "null") {
                v_video!!.visibility = View.GONE
            } else {
                v_video!!.visibility = View.VISIBLE
                old_video = getIntent.getStringExtra("video")
                v_video!!.setVideoURI(Uri.parse(intent.getStringExtra("video")))
            }
            editText!!.setText(old_content) //填充内容
            editText!!.setSelection(old_content!!.length) //移动光标的位置（最后），方便再次书写
        }
    }

    private fun getDocumentName(): String? {
        val format = SimpleDateFormat("yyyy-mm-dd_HHmmss")
        val date = Date()
        return format.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                action = 1
                img_photo!!.visibility = View.VISIBLE
                v_video!!.visibility = View.GONE
                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
                img_photo!!.setImageBitmap(rotateIfRequired(bitmap))
            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                data.data?.let { uri ->
                    // 将选择的照片显示
                    action = 2
                    img_photo!!.visibility = View.VISIBLE
                    v_video!!.visibility = View.GONE
                    insert_file = uriToFileQ(this , uri)
                    val bitmap = BitmapFactory.decodeFile(insert_file.toString())
                    img_photo!!.setImageBitmap(bitmap)
                }
            }
        }

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                action = 3
                img_photo!!.visibility = View.GONE
                v_video!!.visibility = View.VISIBLE
                v_video!!.setVideoURI(videoUri)
            }
        }
    }


    /**
     * 设置菜单栏按钮监听
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent()
        when (item.itemId) {
            R.id.delete -> AlertDialog.Builder(this@EditActivity)
                .setMessage("您确定要删除吗？")
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    if (openMode == 4) { //如果是新增笔记，则不创建
                        intent.putExtra("mode", -1)
                        setResult(RESULT_OK, intent)
                    } else { //如果是修改笔记，则删除
                        intent.putExtra("mode", 2)
                        intent.putExtra("id", id)
                        setResult(RESULT_OK, intent)
                    }
                    finish()
                }
                .setNegativeButton(android.R.string.no) { dialog, which ->
                    dialog.dismiss() //关闭窗口
                }.create().show()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 系统按钮监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent()
            autoSetMessage(intent)
            //按下返回键，将页面的文本获取，并返回
//            intent.putExtra("content", editText.getText().toString());
//            intent.putExtra("time", dateToStr());
            setResult(RESULT_OK, intent)
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 判断是新增笔记还是修改笔记,mode值是3为修改，为4是新增
     */
    fun autoSetMessage(intent: Intent) {
        if (openMode == 4) {
            Log.d("input", "input11111")
            //判断笔记是否为空，若为空，则不新增笔记
            if (editText!!.text.toString().length == 0 && action == 0) {
                intent.putExtra("mode", -1)
            } else {
                intent.putExtra("mode", 0)
                intent.putExtra("content", editText!!.text.toString())
                intent.putExtra("time", dateToStr())
                intent.putExtra("tag", tag)
                if (img_photo!!.visibility == View.GONE) {
                    intent.putExtra("image", "null")
                } else {
                    if (action == 2) {
                        intent.putExtra("image", insert_file.toString())
                    } else {
                        intent.putExtra("image", photo_file?.toString())
                    }
                }
                if (v_video!!.visibility == View.GONE) {
                    intent.putExtra("video", "null")
                } else {
                    intent.putExtra("video", video_file?.absolutePath)
                }
            }
        } else {
            //判断笔记是否被修改，或者标签是否更换，否则不更新笔记
            if (editText!!.text.toString() == old_content && action == 0) {
                intent.putExtra("mode", -1)
            } else {
                intent.putExtra("mode", 1)
                intent.putExtra("content", editText!!.text.toString())
                intent.putExtra("time", dateToStr())
                intent.putExtra("id", id)
                intent.putExtra("tag", tag)
                if (img_photo!!.visibility == View.GONE) {
                    intent.putExtra("image", "null")
                } else {
                    if (action == 2) {
                        intent.putExtra("image", insert_file.toString())
                    }
                    if (action == 1) {
                        intent.putExtra("image", photo_file.toString())
                    }
                    if (action == 0) {
                        intent.putExtra("image", old_img)
                    }

                }
                if (v_video!!.visibility == View.GONE) {
                    intent.putExtra("video", "null")
                } else {
                    if (action == 0) {
                        intent.putExtra("video", old_video)
                    }
                    if (action == 3) {
                        intent.putExtra("video", video_file.toString())
                    }
                }
            }
        }
    }

    /**
     * 转换时间格式
     *
     * @return
     */
    fun dateToStr(): String {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return simpleDateFormat.format(date)
    }

    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(photo_file!!.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedBitmap
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun uriToFileQ(context: Context, uri: Uri): File? =
        if (uri.scheme == ContentResolver.SCHEME_FILE)
            File(requireNotNull(uri.path))
        else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //把文件保存到沙盒
            val contentResolver = context.contentResolver
            val displayName = "${System.currentTimeMillis()}${Random.nextInt(0, 9999)}.${
                MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(contentResolver.getType(uri))}"
            val ios = contentResolver.openInputStream(uri)
            if (ios != null) {
                File("${context.cacheDir.absolutePath}/$displayName")
                    .apply {
                        val fos = FileOutputStream(this)
                        FileUtils.copy(ios, fos)
                        fos.close()
                        ios.close()
                    }
            } else null
        } else null
}