package com.aw;

import com.mighty.MightyTransactionManager;
import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.5.
 */
@SuppressWarnings("rawtypes")
public class MultiSend extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610475806100206000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680639ec68f0f14610051578063e702d7af1461011a575b600080fd5b34801561005d57600080fd5b50610118600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290803590602001908201803590602001908080602002602001604051908101604052809392919081815260200183836020028082843782019150505050505091929192905050506101b6565b005b6101b460048036038101908080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290803590602001908201803590602001908080602002602001604051908101604052809392919081815260200183836020028082843782019150505050505091929192905050506103b3565b005b6000806000859150600090505b8451811015610340578173ffffffffffffffffffffffffffffffffffffffff166323b872dd3387848151811015156101f757fe5b90602001906020020151878581518110151561020f57fe5b906020019060200201516040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b1580156102d157600080fd5b505af11580156102e5573d6000803e3d6000fd5b505050506040513d60208110156102fb57600080fd5b8101908080519060200190929190505050151561031757600080fd5b838181518110151561032557fe5b906020019060200201518301925080806001019150506101c3565b7f04afd2ce457d973046bd54f5d7d36368546da08b88be1bca8ae50e32b451da178387604051808381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019250505060405180910390a1505050505050565b60008090505b82518110156104445782818151811015156103d057fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff166108fc838381518110151561040157fe5b906020019060200201519081150290604051600060405180830381858888f19350505050158015610436573d6000803e3d6000fd5b5080806001019150506103b9565b5050505600a165627a7a723058206436ce0e66d5db2de269fc4d279f70e2d2c05af28df2bc4e4aea34491d8660ee0029";

    public static final String FUNC_MULTISEND = "multiSend";

    public static final String FUNC_MULTISENDETH = "multiSendEth";

    public static final Event MULTISENDED_EVENT = new Event("Multisended", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected MultiSend(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected MultiSend(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected MultiSend(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected MultiSend(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> multiSend(String _token, List<String> addresses, List<BigInteger> counts) {
        final Function function = new Function(
                FUNC_MULTISEND, 
                Arrays.<Type>asList(new Address(160, _token),
                new org.web3j.abi.datatypes.DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(addresses, Address.class)),
                new org.web3j.abi.datatypes.DynamicArray<Uint256>(
                        Uint256.class,
                        org.web3j.abi.Utils.typeMap(counts, Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> multiSendEth(List<String> addresses, List<BigInteger> counts, BigInteger weiValue, MightyTransactionManager mightyTransactionManager,ContractGasProvider contractGasProvider) {
        final Function function = new Function(
                FUNC_MULTISENDETH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(addresses, Address.class)),
                new org.web3j.abi.datatypes.DynamicArray<Uint256>(
                        Uint256.class,
                        org.web3j.abi.Utils.typeMap(counts, Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return mightyTransactionManager.executeRemoteCallTransaction(function, weiValue,contractAddress,contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> multiSendEth(List<String> addresses, List<BigInteger> counts, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_MULTISENDETH,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Address>(
                                Address.class,
                                org.web3j.abi.Utils.typeMap(addresses, Address.class)),
                        new org.web3j.abi.datatypes.DynamicArray<Uint256>(
                                Uint256.class,
                                org.web3j.abi.Utils.typeMap(counts, Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public List<MultisendedEventResponse> getMultisendedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(MULTISENDED_EVENT, transactionReceipt);
        ArrayList<MultisendedEventResponse> responses = new ArrayList<MultisendedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MultisendedEventResponse typedResponse = new MultisendedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.total = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<MultisendedEventResponse> multisendedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, MultisendedEventResponse>() {
            @Override
            public MultisendedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MULTISENDED_EVENT, log);
                MultisendedEventResponse typedResponse = new MultisendedEventResponse();
                typedResponse.log = log;
                typedResponse.total = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.tokenAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<MultisendedEventResponse> multisendedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MULTISENDED_EVENT));
        return multisendedEventFlowable(filter);
    }

    @Deprecated
    public static MultiSend load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MultiSend(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static MultiSend load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MultiSend(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static MultiSend load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new MultiSend(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static MultiSend load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new MultiSend(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<MultiSend> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MultiSend.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<MultiSend> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MultiSend.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<MultiSend> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MultiSend.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<MultiSend> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MultiSend.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class MultisendedEventResponse extends BaseEventResponse {
        public BigInteger total;

        public String tokenAddress;
    }
}
