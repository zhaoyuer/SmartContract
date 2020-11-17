package com.mighty;

import com.Smart;
import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.exceptions.TxHashMismatchException;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.RevertReasonExtractor;
import org.web3j.utils.TxHashVerifier;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @Desc
 * @Author Yu Zhao
 * @Date 2020/11/16 14:12
 * @Version 1.0
 */

@Slf4j
public class MightyTransactionManager {

    private Web3j web3j;
    private long chainId;
    private TxHashVerifier txHashVerifier = new TxHashVerifier();

    private static final long POLLING_FREQUENCY = 15000;

    public static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 40;
    private TransactionReceiptProcessor receiptProcessor;

    public MightyTransactionManager(Web3j web3j, long chainId) {
        this.web3j = web3j;
        this.chainId = chainId;
        receiptProcessor = new PollingTransactionReceiptProcessor(
                web3j, POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);
    }


    protected MightyTransactionManager(TransactionReceiptProcessor transactionReceiptProcessor, String fromAddress) {
    }

    public EthSendTransaction sendTransaction(BigInteger gasPrice,
                                              BigInteger gasLimit,
                                              String to,
                                              String data,
                                              BigInteger value,
                                              boolean constructor) throws IOException {
        //测试
        BigInteger nonce = Smart.getNonce();
        log.info("获取的nonce:"+nonce);
        RawTransaction rawTransaction =
                RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);

        return signAndSend(rawTransaction);
    }

    //mighty 离线签名
    public EthSendTransaction sendTransaction(BigInteger gasPrice,
                                              BigInteger gasLimit,
                                              String to,
                                              String data,
                                              BigInteger value
                                             ) throws IOException {
        //测试
        String hexValue =MightySign.sign(gasPrice,gasLimit,to,value,data);
        log.info("签名:"+hexValue);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

        if (ethSendTransaction != null && !ethSendTransaction.hasError()) {
            String txHashLocal = Hash.sha3(hexValue);
            String txHashRemote = ethSendTransaction.getTransactionHash();
            if (!txHashVerifier.verify(txHashLocal, txHashRemote)) {
                throw new TxHashMismatchException(txHashLocal, txHashRemote);
            }
        }

        return ethSendTransaction;
    }



    public EthSendTransaction signAndSend(RawTransaction rawTransaction) throws IOException {
        //测试
        String hexValue = Smart.sign(chainId,rawTransaction);
        log.info("签名:"+hexValue);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

        if (ethSendTransaction != null && !ethSendTransaction.hasError()) {
            String txHashLocal = Hash.sha3(hexValue);
            String txHashRemote = ethSendTransaction.getTransactionHash();
            if (!txHashVerifier.verify(txHashLocal, txHashRemote)) {
                throw new TxHashMismatchException(txHashLocal, txHashRemote);
            }
        }

        return ethSendTransaction;
    }


    /*
     * @param rawTransaction a RawTransaction istance to be signed
     * @return The transaction signed and encoded without ever broadcasting it
     */
    public String sign(RawTransaction rawTransaction) {

//        byte[] signedMessage;
//
//        if (chainId > ChainId.NONE) {
//            signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
//        } else {
//            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//        }
//
//        return Numeric.toHexString(signedMessage);
        return "";
    }


    public RemoteFunctionCall<TransactionReceipt> executeRemoteCallTransaction(Function function, BigInteger weiValue, String contractAddress, ContractGasProvider contractGasProvider) {
        return new RemoteFunctionCall(function, () -> this.executeTransaction(function, weiValue,contractAddress,contractGasProvider));
    }

    private TransactionReceipt executeTransaction(Function function, BigInteger weiValue, String contractAddress, ContractGasProvider contractGasProvider) throws IOException, TransactionException {
        return this.executeTransaction(FunctionEncoder.encode(function), weiValue, function.getName(),contractAddress,contractGasProvider);
    }


    TransactionReceipt executeTransaction(String data, BigInteger weiValue, String funcName, String contractAddress, ContractGasProvider contractGasProvider) throws TransactionException, IOException {
        return this.executeTransaction(data, weiValue, funcName, false,contractAddress,contractGasProvider);
    }

    TransactionReceipt executeTransaction(String data, BigInteger weiValue, String funcName, boolean constructor, String contractAddress, ContractGasProvider contractGasProvide) throws TransactionException, IOException {
        TransactionReceipt receipt = this.send(contractAddress, data, weiValue, contractGasProvide.getGasPrice(funcName), contractGasProvide.getGasLimit(funcName), constructor);
        if (!receipt.isStatusOK()) {
            throw new TransactionException(String.format("Transaction %s has failed with status: %s. Gas used: %s. Revert reason: '%s'.", receipt.getTransactionHash(), receipt.getStatus(), receipt.getGasUsedRaw() != null ? receipt.getGasUsed().toString() : "unknown", RevertReasonExtractor.extractRevertReason(receipt, data, this.web3j, true)), receipt);
        } else {
            return receipt;
        }
    }

    protected TransactionReceipt send(String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit, boolean constructor) throws IOException, TransactionException {
        return executeTransaction(gasPrice, gasLimit, to, data, value, constructor);
    }

    protected TransactionReceipt executeTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value, boolean constructor) throws IOException, TransactionException {
        EthSendTransaction ethSendTransaction = this.sendTransaction(gasPrice, gasLimit, to, data, value, constructor);
        return this.processResponse(ethSendTransaction);
    }


    private TransactionReceipt processResponse(EthSendTransaction transactionResponse) throws IOException, TransactionException {
        if (transactionResponse.hasError()) {
            throw new RuntimeException("Error processing transaction request: " + transactionResponse.getError().getMessage());
        } else {
            String transactionHash = transactionResponse.getTransactionHash();
            return receiptProcessor.waitForTransactionReceipt(transactionHash);
        }
    }
}
