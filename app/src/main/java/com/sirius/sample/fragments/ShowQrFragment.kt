package com.sirius.sample.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.sirius.sample.R
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.helpers.ScenarioHelper
import com.sirius.sdk_android.scenario.impl.InviterScenario
import java.util.*


class ShowQrFragment : Fragment() {

    var qrImage : ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_qr, container,false)
        val openCameraBtn : TextView = view.findViewById(R.id.openCameraBtn)
        val backBtnLayout : LinearLayout = view.findViewById(R.id.backBtnLayout)
        qrImage = view.findViewById(R.id.qrImage)
        backBtnLayout.setOnClickListener {
           activity?.onBackPressed()
        }
        openCameraBtn.setOnClickListener {
            parentFragmentManager?.beginTransaction().addToBackStack("1").replace(R.id.mainFrame, ScanQrFragment()).commit()
        }
        val inviterScenario =  ScenarioHelper.getInstance().getScenarioBy("Inviter") as? InviterScenario
        val invitation = inviterScenario?.generateInvitation()
        invitation?.let {
            updateQrCode(it)
        }
        return view
    }


    private fun updateQrCode(qrCode: String) {
        val hintsMap: MutableMap<EncodeHintType, Any?> = EnumMap(EncodeHintType::class.java)
        hintsMap[EncodeHintType.CHARACTER_SET] = "utf-8"
        hintsMap[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.M

        val mWidth = 256
        val mHeight = 256

        try {
            val bitMatrix = QRCodeWriter().encode(qrCode,
                BarcodeFormat.QR_CODE, mWidth, mHeight, hintsMap)
            val pixels = IntArray(mWidth * mHeight)
            for (i in 0 until mHeight) {
                for (j in 0 until mWidth) {
                    if (bitMatrix[j, i]) {
                        pixels[i * mWidth + j] = 0x00000000
                    } else {
                        pixels[i * mWidth + j] = -0x1
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(pixels, 0, mWidth, mWidth, mHeight,
                Bitmap.Config.RGB_565
            )
            qrImage?.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }


}