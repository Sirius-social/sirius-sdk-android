package com.sirius.sdk_android.wallet.impl;

import android.util.Log;

import com.sirius.sdk.agent.RemoteParams;
import com.sirius.sdk.agent.connections.BaseAgentConnection;
import com.sirius.sdk.agent.connections.RemoteCallWrapper;
import com.sirius.sdk.agent.wallet.abstract_wallet.AbstractCrypto;
import com.sirius.sdk_android.walletUseCase.WalletUseCase;

import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.crypto.Crypto;
import org.hyperledger.indy.sdk.did.DidResults;
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MobileCryptoProxy extends AbstractCrypto   {

    BaseAgentConnection rpc;

    public MobileCryptoProxy() {

    }


    @Override
    public String createKey(String seed, String cryptoType) {
       DidResults.CreateAndStoreMyDidResult result =  WalletUseCase.getInstance().createAndStoreMyDid();
       if(result!=null){
           return   result.getVerkey();
       }
        return null;
    }

    @Override
    public void setKeyMetadata(String verkey, String metadata) {
         new RemoteCallWrapper<String>(rpc){}.
                remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/sirius_rpc/1.0/set_key_metadata",
                        RemoteParams.RemoteParamsBuilder.create()
                                .add("verkey", verkey).
                                add("metadata", metadata));
    }

    @Override
    public String getKeyMetadata(String verkey) {
        return new RemoteCallWrapper<String>(rpc){}.
                remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/sirius_rpc/1.0/get_key_metadata",
                        RemoteParams.RemoteParamsBuilder.create()
                                .add("verkey", verkey));
    }

    @Override
    public byte[] cryptoSign(String signerVk, byte[] msg) {
        return new RemoteCallWrapper<byte[]>(rpc){}.
                remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/sirius_rpc/1.0/crypto_sign",
                        RemoteParams.RemoteParamsBuilder.create()
                                .add("signer_vk", signerVk)
                                .add("msg", msg));
    }

    @Override
    public boolean cryptoVerify(String signerVk, byte[] msg, byte[] signature) {
        return new RemoteCallWrapper<Boolean>(rpc){}.
                remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/sirius_rpc/1.0/crypto_verify",
                        RemoteParams.RemoteParamsBuilder.create()
                                .add("signer_vk", signerVk)
                                .add("msg", msg)
                                .add("signature", signature));
    }

    @Override
    public byte[] anonCrypt(String recipentVk, byte[] msg) {
        return new RemoteCallWrapper<byte[]>(rpc){}.
                remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/sirius_rpc/1.0/anon_crypt",
                        RemoteParams.RemoteParamsBuilder.create()
                                .add("recipient_vk", recipentVk).add("msg",msg));
    }

    @Override
    public byte[] anonDecrypt(String recipientVk, byte[] encryptedMsg) {
        return new RemoteCallWrapper<byte[]>(rpc){}.
                remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/sirius_rpc/1.0/anon_decrypt",
                        RemoteParams.RemoteParamsBuilder.create()
                                .add("recipient_vk", recipientVk).add("encrypted_msg",encryptedMsg));
    }

    @Override
    public byte[] packMessage(Object message, List<String> recipentVerkeys, String senderVerkey) {
        String listString= recipentVerkeys.toString();
        Log.d("mylog2090","listString="+listString.toString());
        try {
            byte[]  byteTotransfer = Crypto.packMessage(WalletUseCase.getInstance().getMyWallet(), "[\"" + recipentVerkeys.get(0) + "\"]", senderVerkey, message.toString().getBytes(StandardCharsets.UTF_8)).get();
            return byteTotransfer;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IndyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String unpackMessage(byte[] jwe) {
                try{
                    String jweString = new String(jwe, StandardCharsets.UTF_8);
                    JSONObject jsonObject = new JSONObject(jweString );
                    Wallet wallet = WalletUseCase.getInstance().getMyWallet();
                    byte[] byteMess = Crypto.unpackMessage(wallet, jsonObject.toString().getBytes(StandardCharsets.UTF_8)).get();
                    String unpackedMess = new String(byteMess);
                    Log.d("mylog900", "unpackedMess=" + unpackedMess);
                    return unpackedMess;
                }catch (Exception e){
                    e.printStackTrace();
                }

           return null;
    }
}
