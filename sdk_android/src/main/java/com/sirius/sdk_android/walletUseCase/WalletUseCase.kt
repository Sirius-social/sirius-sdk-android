package com.sirius.sdk_android.walletUseCase

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sirius.sdk_android.IndyWallet
import com.sirius.sdk_android.models.KeyDidRecord
import com.sirius.sdk_android.models.WalletRecordSearch
import com.sirius.sdk_android.utils.FileUtils
import com.sirius.sdk_android.utils.HashUtils

import org.hyperledger.indy.sdk.IndyException
import org.hyperledger.indy.sdk.non_secrets.WalletRecord
import org.hyperledger.indy.sdk.non_secrets.WalletSearch
import org.hyperledger.indy.sdk.wallet.Wallet
import java.io.File
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.*
import java.util.concurrent.ExecutionException


class WalletUseCase constructor(

) {

    companion object {
        var instanceWalletUseCase: WalletUseCase? = null

        @JvmStatic
        fun getInstance(): WalletUseCase {
            if (instanceWalletUseCase == null) {
                instanceWalletUseCase = WalletUseCase()
            }
            return instanceWalletUseCase!!
        }
    }


    val OPEN_WALLET_REQUEST_CODE = 1007
    val SCAN_INVITATION_WALLET_REQUEST_CODE = 1009
    var myWallet: Wallet? = null

    private var dirPath: String = "wallet"
    private var exportdirPath: String = "export"
    private var genesisPath: String = "genesis"
    fun setDirPath(context: Context) {
        dirPath = context.filesDir.absolutePath + File.separator + "wallet"
        exportdirPath = context.filesDir.absolutePath + File.separator + "export"
        genesisPath = context.filesDir.absolutePath + File.separator + "genesis"
    }

    fun exportWallet(userJid: String) {
        try {
            val alias = HashUtils.generateHash(userJid)
            val pass = HashUtils.generateHashWithoutStoredSalt("1234", alias)
            val projDir = File(exportdirPath)
            if (!projDir.exists()) {
                projDir.mkdirs()
            }
            val walletId = alias.substring(IntRange(0, 8))
            val path =
                "{\"path\":\"" + projDir.absolutePath + File.separator + "wallet_" + walletId + "\",\"key\":\"$pass\"}"

            Wallet.exportWallet(IndyWallet.getMyWallet(), path).get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteExportedWallet(userJid: String) {
        try {
            //TODO REFACTOR WITH LAYERS
            val alias = HashUtils.generateHash(userJid)
            val walletId = alias.substring(IntRange(0, 8))
            val projDir = File(exportdirPath + File.separator + "wallet_" + walletId)
            FileUtils.forceDelete(projDir)
            //   FileUtils.deleteDirectory(projDir)
        } catch (e: java.lang.Exception) {
            Log.d("mylog2080", "deleteWallet dirPath + File.separator + alias=" + e.message)
            e.printStackTrace()
        }
    }


    fun isExportedWalletExist(jid: String): Boolean {
        val exist = false
        //TODO REFACTOR WITH LAYERS
        try {
            val alias = HashUtils.generateHash(jid)
            val walletId = alias.substring(IntRange(0, 8))
            val projDir = File(exportdirPath + File.separator + "wallet_" + walletId)
            return projDir.exists()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return exist
    }

    fun importWallet(jid: String, pinForImport: String = "1234", pinForNew: String): Boolean {
        try {
            val alias = HashUtils.generateHash(jid)
            val pass = HashUtils.generateHashWithoutStoredSalt(pinForNew, alias)
            val projDir = File(dirPath)
            if (!projDir.exists()) {
                projDir.mkdirs()
            }
            val path = "{\"path\":\"" + projDir.absolutePath + "\"}"
            val myWalletConfig = "{\"id\":\"$alias\" , \"storage_config\":$path}"
            val myWalletCredentials = "{\"key\":\"$pass\"}"


            val projDirImport = File(exportdirPath)
            if (!projDirImport.exists()) {
                projDirImport.mkdirs()
            }
            val passToImport = HashUtils.generateHashWithoutStoredSalt(pinForImport, alias)
            val walletId = alias.substring(IntRange(0, 8))
            val pathToImport =
                "{\"path\":\"" + projDirImport.absolutePath + File.separator + "wallet_" + walletId + "\",\"key\":\"$passToImport\"}"

            Wallet.importWallet(myWalletConfig, myWalletCredentials, pathToImport).get()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    fun createWallet(userJid: String?, pin: String?): Boolean {
        try {
            val alias = HashUtils.generateHash(userJid)
            val pass = HashUtils.generateHashWithoutStoredSalt(pin, alias)
            val projDir = File(dirPath)
            if (!projDir.exists()) {
                projDir.mkdirs()
            }
            val path = "{\"path\":\"" + projDir.absolutePath + "\"}"
            val myWalletConfig = "{\"id\":\"$alias\" , \"storage_config\":$path}"
            val myWalletCredentials = "{\"key\":\"$pass\"}"
            Wallet.createWallet(myWalletConfig, myWalletCredentials).get()
            return true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }


        return false
    }


    fun isWalletExist(jid: String): Boolean {
        val exist = false
        //TODO REFACTOR WITH LAYERS
        try {
            val alias = HashUtils.generateHash(jid)
            val projDir = File(dirPath + File.separator + alias)
            return projDir.exists()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return exist
    }


    fun setUserResourses(resources: String, afterSet: (sessionId: String) -> Unit) {
        var userSessionId = resources
        Log.d("mylog900", "setUserResourses from Prefs userSessionId=$userSessionId")
        if (TextUtils.isEmpty(userSessionId)) {
            try {
                val userSessionIdJson =
                    WalletRecord.get(IndyWallet.myWallet, "user_session_id", "my_session", "{}")
                        .get()
                val gson = Gson()
                val didRecord = gson.fromJson(userSessionIdJson, KeyDidRecord::class.java)
                userSessionId = didRecord.value
                Log.d("mylog900", "setUserResourses from Wallet userSessionId=$userSessionId")
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: IndyException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                if (TextUtils.isEmpty(userSessionId)) {
                    userSessionId = UUID.randomUUID().toString()
                    WalletRecord.add(
                        IndyWallet.myWallet,
                        "user_session_id",
                        "my_session",
                        userSessionId,
                        "{}"
                    ).get()
                }
                Log.d("mylog900", "setUserResourses generateNew =$userSessionId")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            afterSet.invoke(userSessionId)
        }
    }

    fun getRecordFrom(wallet: Wallet?, type: String?, key: String?): String? {
        val did: String? = null
        try {
            val didJson = WalletRecord.get(wallet, type, key, "{}").get()
            if (didJson != null) {
                val gson = Gson()
                val didRecord = gson.fromJson(didJson, KeyDidRecord::class.java)
                return didRecord.value
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: IndyException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return did
    }


    private fun searchResultParse(searchResult: String): Pair<List<String>, Int> {
        val searchRecord =
            Gson().fromJson<WalletRecordSearch>(searchResult, WalletRecordSearch::class.java)
        val valueList: MutableList<String> = mutableListOf()
        var countAll = searchRecord.totalCount ?: 0
        searchRecord.records?.forEach {
            it.value?.let { value ->
                valueList.add(value)
            }
        }
        return Pair<List<String>, Int>(valueList, countAll)
    }

    fun getSearchResultFrom(
        wallet: Wallet?,
        type: String,
        query: String = "{}",
        count: Int,
        queryAll: Boolean = true
    ): List<String> {
        var result: MutableList<String> = mutableListOf()
        if (wallet == null) {
            return result
        }
        try {
            val walletSearch = searchResultFor(wallet, type, query)
            walletSearch?.let {
                if (queryAll) {
                    val searchResult = walletSearch.fetchNextRecords(wallet, count).get()
                    val tempResult = searchResultParse(searchResult)
                    result.addAll(tempResult.first)
                    while (result.size < tempResult.second) {
                        val tempSearchResult = walletSearch.fetchNextRecords(wallet, count).get()
                        val tempTempResult = searchResultParse(tempSearchResult)
                        result.addAll(tempTempResult.first)
                    }
                    walletSearch.closeSearch()
                } else {
                    val searchResult = walletSearch.fetchNextRecords(wallet, count).get()
                    walletSearch.closeSearch()
                    return searchResultParse(searchResult).first
                }
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: IndyException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun searchResultFor(
        wallet: Wallet?, type: String, query: String = "{}", options: String = "{\n" +
                "  \"retrieveTotalCount\": true\n" +
                "}"
    ): WalletSearch? {
        if (wallet == null) {
            return null
        }
        try {
            val walletSearch = WalletSearch.open(wallet, type, query, options).get()
            return walletSearch
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: IndyException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun addOrUpdateKeyValue(wallet: Wallet?, type: String, id: String, value: String) {
        if (wallet == null) {
            return
        }
        try {
            val gson = GsonBuilder().create()
            if (TextUtils.isEmpty(getRecordFrom(wallet, type, id))) {
                WalletRecord.add(wallet, type, id, value, "{}").get()
            } else {
                WalletRecord.updateValue(wallet, type, id, value).get()
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: IndyException) {
            e.printStackTrace()
        }
    }


}