package com.mighty;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc 离线钱包签名类
 * @Author Yu Zhao
 * @Date 2020/11/16 15:14
 * @Version 1.0
 */
public class MightySign {

    public static Sign.SignatureData createEip155SignatureData(Sign.SignatureData signatureData, long chainId) {
        BigInteger v = Numeric.toBigInt(signatureData.getV());
        v = v.subtract(BigInteger.valueOf(27L));
        v = v.add(BigInteger.valueOf(chainId * 2L));
        v = v.add(BigInteger.valueOf(35L));
        return new Sign.SignatureData(v.toByteArray(), signatureData.getR(), signatureData.getS());
    }

    public static byte[] encode(RawTransaction rawTransaction, long chainId) {
        Sign.SignatureData signatureData = new Sign.SignatureData(longToBytes(chainId), new byte[0], new byte[0]);
        return encode(rawTransaction, signatureData);
    }

    private static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(x);
        return buffer.array();
    }

    private static byte[] encode(RawTransaction rawTransaction, Sign.SignatureData signatureData) {
        List<RlpType> values = asRlpValues(rawTransaction, signatureData);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    public static String sign(BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data) {
        //TODO 离线获取nonce chainid
        BigInteger nonce = null;
        long chainId = -1;
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);
        return Numeric.toHexString(signMessage(rawTransaction, chainId));
    }

    public static byte[] signMessage(RawTransaction rawTransaction, long chainId) {
        //TODO 获取离线钱包私钥
        ECKeyPair ecKeyPair = null;
        return signMessage(rawTransaction, chainId, ecKeyPair);
    }

    public static byte[] signMessage(RawTransaction rawTransaction, long chainId, ECKeyPair ecKeyPair) {
        byte[] encodedTransaction = encode(rawTransaction, chainId);
        Sign.SignatureData signatureData = Sign.signMessage(encodedTransaction, ecKeyPair);
        Sign.SignatureData eip155SignatureData = createEip155SignatureData(signatureData, chainId);
        return encode(rawTransaction, eip155SignatureData);
    }


    public static List<RlpType> asRlpValues(RawTransaction rawTransaction, Sign.SignatureData signatureData) {
        List<RlpType> result = new ArrayList<>();
        result.add(RlpString.create(rawTransaction.getNonce()));
        result.add(RlpString.create(rawTransaction.getGasPrice()));
        result.add(RlpString.create(rawTransaction.getGasLimit()));
        String to = rawTransaction.getTo();
        if (to != null && to.length() > 0) {
            result.add(RlpString.create(Numeric.hexStringToByteArray(to)));
        } else {
            result.add(RlpString.create(""));
        }

        result.add(RlpString.create(rawTransaction.getValue()));
        byte[] data = Numeric.hexStringToByteArray(rawTransaction.getData());
        result.add(RlpString.create(data));
        if (rawTransaction.isEIP1559Transaction()) {
            result.add(RlpString.create(rawTransaction.getGasPremium()));
            result.add(RlpString.create(rawTransaction.getFeeCap()));
        }

        if (signatureData != null) {
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getV())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        }

        return result;
    }
}
