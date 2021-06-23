package io.nfteam.nftlab.contracts.hotmoka;

import io.hotmoka.beans.CodeExecutionException;
import io.hotmoka.beans.SignatureAlgorithm;
import io.hotmoka.beans.TransactionException;
import io.hotmoka.beans.TransactionRejectedException;
import io.hotmoka.beans.references.TransactionReference;
import io.hotmoka.beans.requests.*;
import io.hotmoka.beans.signatures.CodeSignature;
import io.hotmoka.beans.signatures.ConstructorSignature;
import io.hotmoka.beans.signatures.MethodSignature;
import io.hotmoka.beans.values.BigIntegerValue;
import io.hotmoka.beans.values.StorageReference;
import io.hotmoka.beans.values.StorageValue;
import io.hotmoka.beans.values.StringValue;
import io.hotmoka.crypto.SignatureAlgorithmForTransactionRequests;
import io.hotmoka.nodes.Node;
import io.hotmoka.remote.RemoteNode;
import io.hotmoka.remote.RemoteNodeConfig;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class HotmokaIntegration {
  protected static final BigInteger _50_000 = BigInteger.valueOf(50_000);
  protected static final BigInteger _100_000 = BigInteger.valueOf(100_000);
  protected static final BigInteger _500_000 = BigInteger.valueOf(500_000);
  protected static final BigInteger _1_000_000 = BigInteger.valueOf(1_000_000);
  protected static final BigInteger _10_000_000 = BigInteger.valueOf(10_000_000);
  protected static final BigInteger _1_000_000_000 = BigInteger.valueOf(1_000_000_000);
  protected static final BigInteger _10_000_000_000 = BigInteger.valueOf(10_000_000_000L);

  protected StorageReference account;
  protected Node node;
  protected String chainId;
  private final SignedTransactionRequest.Signer signer;

  protected HotmokaIntegration (String url, String accountAddress) throws Exception {
    this.account = new StorageReference(accountAddress);
    this.node = getNode(url);
    this.chainId = getChainId();
    this.signer = getSigner(accountAddress);
  }

  private static final ConcurrentMap<StorageReference, BigInteger> nonces = new ConcurrentHashMap<>();

  protected final TransactionReference addJarStoreInitialTransaction(byte[] jar, TransactionReference... dependencies) throws TransactionRejectedException {
    return node.addJarStoreInitialTransaction(new JarStoreInitialTransactionRequest(jar, dependencies));
  }

  /**
   * Takes care of computing the next nonce.
   */
  protected final TransactionReference addJarStoreTransaction(BigInteger gasLimit, BigInteger gasPrice, TransactionReference classpath, byte[] jar, TransactionReference... dependencies) throws TransactionException, TransactionRejectedException, InvalidKeyException, SignatureException {
    return node.addJarStoreTransaction(new JarStoreTransactionRequest(signer, account, getNonceOf(account), chainId, gasLimit, gasPrice, classpath, jar, dependencies));
  }

  /**
   * Takes care of computing the next nonce.
   */
  protected final StorageReference addConstructorCallTransaction(BigInteger gasLimit, BigInteger gasPrice, TransactionReference classpath, ConstructorSignature constructor, StorageValue... actuals) throws TransactionException, CodeExecutionException, TransactionRejectedException, InvalidKeyException, SignatureException {
    return node.addConstructorCallTransaction(new ConstructorCallTransactionRequest(signer, account, getNonceOf(account), chainId, gasLimit, gasPrice, classpath, constructor, actuals));
  }

  /**
   * Takes care of computing the next nonce.
   */
  protected final StorageValue addInstanceMethodCallTransaction(BigInteger gasLimit, BigInteger gasPrice, TransactionReference classpath, MethodSignature method, StorageReference receiver, StorageValue... actuals) throws TransactionException, CodeExecutionException, TransactionRejectedException, InvalidKeyException, SignatureException {
    return node.addInstanceMethodCallTransaction(new InstanceMethodCallTransactionRequest(signer, account, getNonceOf(account), chainId, gasLimit, gasPrice, classpath, method, receiver, actuals));
  }

  /**
   * Takes care of computing the next nonce.
   */
  protected final StorageValue addStaticMethodCallTransaction(BigInteger gasLimit, BigInteger gasPrice, TransactionReference classpath, MethodSignature method, StorageValue... actuals) throws TransactionException, CodeExecutionException, TransactionRejectedException, InvalidKeyException, SignatureException {
    return node.addStaticMethodCallTransaction(new StaticMethodCallTransactionRequest(signer, account, getNonceOf(account), chainId, gasLimit, gasPrice, classpath, method, actuals));
  }

  /**
   * Takes care of computing the next nonce.
   */
  protected final StorageValue runInstanceMethodCallTransaction(BigInteger gasLimit, TransactionReference classpath, MethodSignature method, StorageReference receiver, StorageValue... actuals) throws TransactionException, CodeExecutionException, TransactionRejectedException {
    return node.runInstanceMethodCallTransaction(new InstanceMethodCallTransactionRequest(account, gasLimit, classpath, method, receiver, actuals));
  }

  /**
   * Takes care of computing the next nonce.
   */
  protected final StorageValue runStaticMethodCallTransaction(BigInteger gasLimit, TransactionReference classpath, MethodSignature method, StorageValue... actuals) throws TransactionException, CodeExecutionException, TransactionRejectedException {
    return node.runStaticMethodCallTransaction(new StaticMethodCallTransactionRequest(account, gasLimit, classpath, method, actuals));
  }

  /**
   * Takes care of computing the next nonce.
   */
  protected final Node.JarSupplier postJarStoreTransaction(BigInteger gasLimit, BigInteger gasPrice, TransactionReference classpath, byte[] jar, TransactionReference... dependencies) throws TransactionRejectedException, InvalidKeyException, SignatureException {
    return node.postJarStoreTransaction(new JarStoreTransactionRequest(signer, account, getNonceOf(account), chainId, gasLimit, gasPrice, classpath, jar, dependencies));
  }

  /**
   * Takes care of computing the next nonce.
   */
  protected final Node.CodeSupplier<StorageValue> postInstanceMethodCallTransaction(BigInteger gasLimit, BigInteger gasPrice, TransactionReference classpath, MethodSignature method, StorageReference receiver, StorageValue... actuals) throws TransactionRejectedException, InvalidKeyException, SignatureException {
    return node.postInstanceMethodCallTransaction(new InstanceMethodCallTransactionRequest(signer, account, getNonceOf(account), chainId, gasLimit, gasPrice, classpath, method, receiver, actuals));
  }

  /**
   * Takes care of computing the next nonce.
   */
  protected final Node.CodeSupplier<StorageReference> postConstructorCallTransaction(BigInteger gasLimit, BigInteger gasPrice, TransactionReference classpath, ConstructorSignature constructor, StorageValue... actuals) throws TransactionRejectedException, InvalidKeyException, SignatureException {
    return node.postConstructorCallTransaction(new ConstructorCallTransactionRequest(signer, account, getNonceOf(account), chainId, gasLimit, gasPrice, classpath, constructor, actuals));
  }

  protected final TransactionReference takamakaCode() {
    return node.getTakamakaCode();
  }

  private KeyPair loadKeys(String account) throws Exception {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./" + account + ".keys"))) {
      return (KeyPair) ois.readObject();
    }
  }

  private SignedTransactionRequest.Signer getSigner(String accountAddress) throws Exception {
    SignatureAlgorithm<SignedTransactionRequest> signature
      = SignatureAlgorithmForTransactionRequests.mk(node.getNameOfSignatureAlgorithmForRequests());
    KeyPair keys = loadKeys(accountAddress);

    return SignedTransactionRequest.Signer.with(signature, keys.getPrivate());
  }

  private static Node getNode(String url) {
    RemoteNodeConfig config = new RemoteNodeConfig.Builder()
      .setURL(url)
      .build();

    return RemoteNode.of(config);
  }

  private String getChainId() throws TransactionException, TransactionRejectedException, CodeExecutionException {
    return ((StringValue) node
      .runInstanceMethodCallTransaction(new InstanceMethodCallTransactionRequest
        (account,
          _500_000,
          node.getTakamakaCode(),
          CodeSignature.GET_CHAIN_ID,
          node.getManifest())))
      .value;
  }

  private BigInteger getNonceOf(StorageReference account) throws TransactionRejectedException {
    try {
      BigInteger nonce = nonces.get(account);

      if (nonce != null) {
        nonce = nonce.add(BigInteger.ONE);
      }
      else {
        nonce = ((BigIntegerValue) node.runInstanceMethodCallTransaction(new InstanceMethodCallTransactionRequest
          (account, _100_000, node.getClassTag(account).jar, CodeSignature.NONCE, account))).value;
      }

      nonces.put(account, nonce);

      return nonce;
    }
    catch (Exception e) {
      throw new TransactionRejectedException("cannot compute the nonce of " + account);
    }
  }
}
