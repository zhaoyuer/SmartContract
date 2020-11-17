package com;

import com.aw.MultiSend;
import com.mighty.MightyTransactionManager;
import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc
 * @Author Yu Zhao
 * @Date 2020/11/12 14:47
 * @Version 1.0
 */

@Slf4j
public class Smart {

    private static Web3j web3j = null;
    private static String address;

    private static DefaultGasProvider defaultGasProvider = new DefaultGasProvider();

    private static final int SLEEP_DURATION = 15000;
    private static final int ATTEMPTS = 40;
    private static Credentials credentials;
    private static MightyTransactionManager mightyTransactionManager;

    public static void main(String[] args) {

        try {
            //连接私链
            web3j = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
            //自定义合约调用类
          //  mightyTransactionManager = new MightyTransactionManager(web3j, 666);
            //非冷钱包加载账户密码
            credentials = WalletUtils.loadCredentials("1", "D:\\soft\\eth\\keystore\\UTC--2020-11-16T06-58-52.160862200Z--894c8470615037a04d86fbf9b395214564eb6b0b");
            address = credentials.getAddress();
            log.info("address:" + address);
            //获取私链gasPrice
            BigInteger gasPrice = web3j.ethGasPrice().sendAsync().get().getGasPrice();
            log.info("gasPrice:{}", gasPrice);
            log.info("blockNumber:{}", web3j.ethBlockNumber().sendAsync().get().getBlockNumber());
            String contractAddress = "0x2c61d46677a9a16842bb79198b263258d6eff42a";

            //部署合约
            //  com.mighty.MultiSend multiSend = com.mighty.MultiSend.deploy(web3j, credentials,new DefaultGasProvider()).send();
            //    MultiSend multiSend = MultiSend.deploy(web3j, credentials,new DefaultGasProvider()).send();
            //加载已存在的合约
            MultiSend multiSend = MultiSend.load(contractAddress, web3j, credentials, new DefaultGasProvider());

            String add1 = "0x0d70552bc01772e0663bd34fc0bbc88f70af974b";
            String add2 = "0xb0cb082606f1e43375ad3b9b7398d22fe16bf249";
            log.info("Smart contract loaded 获取合约地址：" + multiSend.isValid());
            log.info("Smart contract loaded 获取合约地址：" + multiSend.getContractAddress());
            log.info("转账前地址1余额：" + web3j.ethGetBalance(add1, DefaultBlockParameterName.LATEST).send().getBalance());
            log.info("转账前地址2余额：" + web3j.ethGetBalance(add2, DefaultBlockParameterName.LATEST).send().getBalance());

            List<String> addresses = new ArrayList<>();
            addresses.add(add1);
            addresses.add(add2);
            List<BigInteger> counts = new ArrayList<>();
            counts.add(BigInteger.valueOf(1));
            counts.add(BigInteger.valueOf(2));
            BigInteger weiValue = BigInteger.valueOf(1000000);

            //魔改合约调用
            //  multiSend.multiSendEth(addresses, counts, weiValue, mightyTransactionManager, new DefaultGasProvider()).sendAsync().get();
            //初始合约调用
            multiSend.multiSendEth(addresses, counts, weiValue).sendAsync().get();
            log.info("转账后地址1余额：" + web3j.ethGetBalance(add1, DefaultBlockParameterName.LATEST).sendAsync().get().getBalance());
            log.info("转账后地址2余额：" + web3j.ethGetBalance(add2, DefaultBlockParameterName.LATEST).sendAsync().get().getBalance());

        } catch (Exception e) {
            log.error("", e);
        }

    }

    /*
     * @param rawTransaction a RawTransaction istance to be signed
     * @return The transaction signed and encoded without ever broadcasting it
     */
    public static String sign(long chainId, RawTransaction rawTransaction) {

        byte[] signedMessage;

        signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        //  signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);


        return Numeric.toHexString(signedMessage);
    }

    public static BigInteger getNonce() throws IOException {
        EthGetTransactionCount ethGetTransactionCount =
                web3j.ethGetTransactionCount(
                        address, DefaultBlockParameterName.PENDING)
                        .send();

        return ethGetTransactionCount.getTransactionCount();
    }


}
