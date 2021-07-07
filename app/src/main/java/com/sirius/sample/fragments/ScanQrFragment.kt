package com.sirius.sample.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.zxing.Result
import com.sirius.sample.PermissionHelper
import com.sirius.sample.R
import com.sirius.sample.view.SiriusScannerView
import com.sirius.sdk_android.SiriusSDK


class ScanQrFragment : Fragment(), SiriusScannerView.ResultHandler {

    var mScannerView: SiriusScannerView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scan_qr, container, false)
        mScannerView = view.findViewById(R.id.mScannerView)
        val backBtnLayout : LinearLayout = view.findViewById(R.id.backBtnLayout)
        backBtnLayout.setOnClickListener {
            activity?.onBackPressed()
        }
        if (PermissionHelper.checkPermissionsForCamera(activity, 1098)) {
            startCamera()
        }
        return view
    }


    private fun startCamera() {
        mScannerView?.setResultHandler(this)
        mScannerView?.startCamera()
    }

    override fun onResume() {
        super.onResume()
        mScannerView?.resumeCameraPreview(this)
    }

    override fun onPause() {
        super.onPause()
        mScannerView?.stopCameraPreview()
    }

    override fun onDestroy() {
        mScannerView?.stopCamera()
        super.onDestroy()

    }



    fun onCodeScanned(result: String) : Boolean{
        val message = SiriusSDK.getInstance().invitationUseCase.parseInvitationLink(result)
        if (message != null) {
            SiriusSDK.getInstance().invitationUseCase.startConnectionWithInvitation(message)
            return true
        } else {
            val textError: String ="The scanned QR code is not an invitation, please scan another QR code."
            Toast.makeText(context,textError,Toast.LENGTH_SHORT).show()
            return false
        }
    }

    override fun handleResult(rawResult: Result?) {
        Log.d("mylog290","rawResult="+rawResult)
        rawResult?.let { onCodeScanned(it.text.orEmpty()) }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.onRequestPermissionsResult(
            requestCode,
            1098,
            permissions,
            grantResults,
            object :
                PermissionHelper.OnRequestPermissionListener {
                override fun onRequestFail() {
                    Toast.makeText(
                        context,
                        "For scan Qr you need to give permissions",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onRequestSuccess() {
                    startCamera()
                }
            })
    }

}