package com.sirius.sample.ui.qrcode

import com.google.zxing.Result
import com.sirius.sample.R
import com.sirius.sample.base.App
import com.sirius.sample.base.ui.BaseFragment

import com.sirius.sample.databinding.FragmentScanQrBinding
import com.sirius.sample.design.views.SiriusScannerView
import com.sirius.sample.utils.PermissionHelper


class ScanQrFragment : BaseFragment<FragmentScanQrBinding, ScanQrViewModel>(), SiriusScannerView.ResultHandler {

    override fun setupViews() {
        super.setupViews()
        if (PermissionHelper.checkPermissionsForCamera(activity, 1098)) {
            startCamera()
        }

    }

    private fun startCamera() {
        dataBinding.mScannerView.setResultHandler(this)
        dataBinding. mScannerView.startCamera()
    }

    override fun onResume() {
        super.onResume()
        dataBinding.mScannerView.resumeCameraPreview(this)
    }

    override fun onPause() {
        super.onPause()
        dataBinding.mScannerView.stopCameraPreview()
    }

    override fun onDestroy() {
        dataBinding.mScannerView.stopCamera()
        super.onDestroy()

    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_scan_qr
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun subscribe() {


    }

    override fun setModel() {
        dataBinding.viewModel = model
    }


    override fun handleResult(rawResult: Result?) {
        rawResult?.let { model.onCodeScanned(it.text.orEmpty()) }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.onRequestPermissionsResult(requestCode, 1098, permissions, grantResults, object :
            PermissionHelper.OnRequestPermissionListener {
            override fun onRequestFail() {
                model.onShowToastLiveData.postValue(resources.getString(R.string.camera_qr_scan_permission))
            }

            override fun onRequestSuccess() {
                startCamera()
            }
        })
    }

}