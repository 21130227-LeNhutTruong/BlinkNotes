package com.example.blinknotes.ui.profile.settingProfile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.blinknotes.R
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@Composable
fun TopItems(
    title: String,
    @DrawableRes icon: Int
){
    Row (
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(id = icon),
            modifier = Modifier
                .padding(end = 4.dp)
                .size(16.dp),
            contentDescription = "icon_wifi",
        )
        Text(
            text = title,
            fontSize = 12.sp,
            color = colorResource(R.color.white)
        )
    }
}
@Composable
fun CameraPreview() {
    val context = LocalContext.current
    val cameraManager = remember { context.getSystemService(Context.CAMERA_SERVICE) as CameraManager }
    val surfaceView = remember { SurfaceView(context) }
    val barcodeValue = remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Hiển thị Camera Preview
        AndroidView(
            factory = { ctx ->
                surfaceView.apply {
                    holder.addCallback(object : SurfaceHolder.Callback {
                        override fun surfaceCreated(holder: SurfaceHolder) {
                            openCamera(cameraManager, holder, context, barcodeValue)
                        }

                        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

                        override fun surfaceDestroyed(holder: SurfaceHolder) {}
                    })
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Hiển thị thanh quét
        ScannerOverlay()
    }

    barcodeValue.value.takeIf { it.isNotEmpty() }?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Mở Camera bằng Camera2 API
 */
@SuppressLint("MissingPermission")
private fun openCamera(cameraManager: CameraManager, holder: SurfaceHolder, context: Context, barcodeValue: MutableState<String>) {
    val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
        cameraManager.getCameraCharacteristics(id).get(CameraCharacteristics.LENS_FACING) == CameraMetadata.LENS_FACING_BACK
    } ?: return

    val handlerThread = HandlerThread("CameraThread").apply { start() }
    val cameraHandler = Handler(handlerThread.looper)

    cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            val previewRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewRequestBuilder.addTarget(holder.surface)

            camera.createCaptureSession(
                listOf(holder.surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        session.setRepeatingRequest(previewRequestBuilder.build(), null, cameraHandler)
                        startBarcodeScanning(holder, barcodeValue)
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {}
                },
                cameraHandler
            )
        }

        override fun onDisconnected(camera: CameraDevice) {
            camera.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            camera.close()
        }
    }, cameraHandler)
}

/**
 * Xử lý quét mã QR bằng OpenCV hoặc Google MLKit
 */
private fun startBarcodeScanning(holder: SurfaceHolder, barcodeValue: MutableState<String>) {
    val barcodeScanner = BarcodeScanning.getClient()
    val imageReader = ImageReader.newInstance(640, 480, ImageFormat.YUV_420_888, 2)

    imageReader.setOnImageAvailableListener({ reader ->
        val image = reader.acquireLatestImage() ?: return@setOnImageAvailableListener
        val inputImage = InputImage.fromMediaImage(image, 0)
        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                barcodes.firstOrNull()?.rawValue?.let {
                    barcodeValue.value = it
                }
            }
            .addOnCompleteListener {
                image.close()
            }
    }, Handler(Looper.getMainLooper()))
}
@Composable
fun ScannerOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(4.dp)
                .background(Color.Red)
                .offset(y = offsetY.dp)
        )
    }
}

@Composable
fun BottomBar(selectedTabScan: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding( end = 16.dp, start = 16.dp, bottom = 38.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomBarItem(
            icon = R.drawable.scan,
            label = stringResource(R.string.scan_qr),
            isSelected = !selectedTabScan,
            modifier = Modifier.weight(1f)
        )
        BottomBarItem(
            icon = R.drawable.scan_barcode,
            label = stringResource(R.string.my_qr),
            isSelected = selectedTabScan,
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun OverlayScannerFrame() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val scannerSize = size.width * 0.8f
        val scannerHeight = scannerSize
        val cornerRadius = 12.dp.toPx()
        val cornerOuterPath = 32.dp.toPx()

        val left = (size.width - scannerSize) / 2
        val top = (size.height - scannerHeight) / 2
        val right = left + scannerSize
        val bottom = top + scannerHeight

        val outerPath = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(0f, 0f, size.width, size.height), // Toàn màn hình
                    bottomLeft = CornerRadius(cornerOuterPath, cornerOuterPath),
                    bottomRight = CornerRadius(cornerOuterPath, cornerOuterPath)
                )
            )
        }

        val scannerPath = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(left, top, right, bottom),
                    cornerRadius = CornerRadius(cornerRadius,cornerRadius)
                )
            )
        }

        clipPath(scannerPath, ClipOp.Difference) {
            drawPath(outerPath,color = Color.Black.copy(alpha = 0.7f))
        }
    }
}
@Composable
fun BottomBarItem(
    icon: Int,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.clickable {  }
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .width(65.dp)
                    .background(
                        color = colorResource(id = R.color.tab_line),
                        shape = RoundedCornerShape(bottomEnd = 4.dp, bottomStart = 4.dp)
                    )


            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 6.dp),
                colorFilter = if (isSelected) ColorFilter.tint(colorResource(R.color.tab_line)) else ColorFilter.tint(
                    colorResource(R.color.text_my_qr)
                )
            )

            Text(
                text = label,
                fontSize = 14.sp,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                color = if (isSelected) colorResource(R.color.tab_line) else colorResource(R.color.text_my_qr)
            )
        }
    }
}