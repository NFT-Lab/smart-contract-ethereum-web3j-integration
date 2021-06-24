package io.nfteam.nftlab.contracts;

import io.hotmoka.beans.CodeExecutionException;
import io.hotmoka.beans.TransactionException;
import io.hotmoka.beans.TransactionRejectedException;
import io.hotmoka.beans.references.LocalTransactionReference;
import io.hotmoka.beans.references.TransactionReference;
import io.hotmoka.beans.signatures.NonVoidMethodSignature;
import io.hotmoka.beans.types.BasicTypes;
import io.hotmoka.beans.types.ClassType;
import io.hotmoka.beans.values.*;
import io.nfteam.nftlab.contracts.hotmoka.HotmokaIntegration;
import io.nfteam.nftlab.contracts.hotmoka.NFTLabStoreHotmokaMethods;
import io.nfteam.nftlab.services.smartcontract.INFTLab;
import io.nfteam.nftlab.services.smartcontract.INFTTransaction;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.LinkedList;
import java.util.List;

import static io.nfteam.nftlab.contracts.hotmoka.NFTLabStoreHotmokaMethods.*;

public class NFTLabStoreHotmoka extends HotmokaIntegration {
  private final BigInteger defaultGasLimit;
  private final BigInteger defaultGasPrice;

  private TransactionReference classpath;
  private StorageReference nftLabStore;

  public NFTLabStoreHotmoka(
    String url,
    String accountAddress,
    BigInteger defaultGasLimit,
    BigInteger defaultGasPrice
  ) throws Exception {
    super(url, accountAddress);

    this.defaultGasLimit = defaultGasLimit;
    this.defaultGasPrice = defaultGasPrice;
  }

  public BigInteger mint(
    String artist,
    BigInteger artistId,
    String hash,
    String timestamp
  ) throws TransactionRejectedException, SignatureException, InvalidKeyException, TransactionException, CodeExecutionException {
    return mint(artist, artistId, hash, timestamp, defaultGasLimit, defaultGasPrice);
  }

  public BigInteger mint(
    String artist,
    BigInteger artistId,
    String hash,
    String timestamp,
    BigInteger gasLimit,
    BigInteger gasPrice
  ) throws TransactionRejectedException, SignatureException, InvalidKeyException, TransactionException, CodeExecutionException {
    BigIntegerValue artistIdRef = new BigIntegerValue(artistId);
    StorageReference artistRef = new StorageReference(artist);

    BigIntegerValue newTokenId = (BigIntegerValue) addInstanceMethodCallTransaction(
      gasLimit,
      gasPrice,
      classpath,
      NFTLabStoreHotmokaMethods.mint,
      nftLabStore,
      artistRef,
      artistIdRef,
      new StringValue(hash),
      new StringValue(timestamp)
    );

    return newTokenId.value;
  }

  public boolean transfer(
    BigInteger tokenId,
    String seller,
    BigInteger sellerId,
    String buyer,
    BigInteger buyerId,
    String price,
    String timestamp
  ) throws TransactionRejectedException, SignatureException, InvalidKeyException, TransactionException, CodeExecutionException {
    return transfer(tokenId, seller, sellerId, buyer, buyerId, price, timestamp, defaultGasLimit, defaultGasPrice);
  }

  public boolean transfer(
    BigInteger tokenId,
    String seller,
    BigInteger sellerId,
    String buyer,
    BigInteger buyerId,
    String price,
    String timestamp,
    BigInteger gasLimit,
    BigInteger gasPrice
  ) throws TransactionRejectedException, SignatureException, InvalidKeyException, TransactionException, CodeExecutionException {
    StringValue priceRef = new StringValue(price);
    StringValue timestampRef = new StringValue(timestamp);
    StorageReference sellerRef = new StorageReference(seller);
    StorageReference buyerRef = new StorageReference(buyer);

    BigIntegerValue sellerIdRef = new BigIntegerValue(sellerId);
    BigIntegerValue buyerIdRef = new BigIntegerValue(buyerId);

    BigIntegerValue tokenIdRef = new BigIntegerValue(tokenId);

    BooleanValue transferred = (BooleanValue) addInstanceMethodCallTransaction(
        gasLimit,
        gasPrice,
        classpath,
        transfer,
        nftLabStore,
        tokenIdRef,
        sellerRef,
        sellerIdRef,
        buyerRef,
        buyerIdRef,
        priceRef,
        timestampRef
      );

    return transferred.value;
  }

  public List<NFTTransaction> getHistory(BigInteger tokenId) throws TransactionRejectedException, SignatureException, InvalidKeyException, TransactionException, CodeExecutionException {
    return getHistory(tokenId, defaultGasLimit, defaultGasPrice);
  }

    public List<NFTTransaction> getHistory(BigInteger tokenId, BigInteger gasLimit, BigInteger gasPrice) throws TransactionRejectedException, SignatureException, InvalidKeyException, TransactionException, CodeExecutionException {
    BigIntegerValue tokenIdRef = new BigIntegerValue(tokenId);
    List<NFTTransaction> history =
      new LinkedList<>();

    StorageReference historyRef = (StorageReference) addInstanceMethodCallTransaction(
      gasLimit,
      gasPrice,
      classpath,
      getHistory,
      nftLabStore,
      tokenIdRef
    );

    int size = ((IntValue) runInstanceMethodCallTransaction(
      defaultGasLimit,
      classpath,
      new NonVoidMethodSignature(
        ClassType.STORAGE_LIST,
        "size",
        BasicTypes.INT
      ),
      historyRef
    )).value;

    for(int i = 0; i < size; ++i) {

      StorageReference transactionRef = (StorageReference) runInstanceMethodCallTransaction(
        defaultGasLimit,
        classpath,
        new NonVoidMethodSignature(
          ClassType.STORAGE_LINKED_LIST,
          "get",
          ClassType.OBJECT,
          BasicTypes.INT
        ),
        historyRef,
        new IntValue(i)
      );

      history.add(new NFTTransaction(transactionRef));
    }

    return history;
  }

  public NFTLab getNFTByHash(String hash) throws TransactionRejectedException, SignatureException, InvalidKeyException, TransactionException, CodeExecutionException {
    return getNFTByHash(hash, defaultGasLimit, defaultGasPrice);
  }

  public NFTLab getNFTByHash(String hash, BigInteger gasLimit, BigInteger gasPrice) throws TransactionRejectedException, SignatureException, InvalidKeyException, TransactionException, CodeExecutionException {
    StringValue hashRef = new StringValue(hash);

    StorageReference nft = (StorageReference) addInstanceMethodCallTransaction(
      gasLimit,
      gasPrice,
      classpath,
      getNFTByHash,
      nftLabStore,
      hashRef
    );

    return new NFTLab(nft);
  }

  public NFTLab getNFTById(BigInteger tokenId) throws TransactionRejectedException, SignatureException, InvalidKeyException, TransactionException, CodeExecutionException {
    return getNFTById(tokenId, defaultGasLimit, defaultGasPrice);
  }

    public NFTLab getNFTById(BigInteger tokenId, BigInteger gasLimit, BigInteger gasPrice) throws TransactionRejectedException, SignatureException, InvalidKeyException, TransactionException, CodeExecutionException {
    BigIntegerValue tokenIdRef = new BigIntegerValue(tokenId);

    StorageReference nft = (StorageReference) addInstanceMethodCallTransaction(
      gasLimit,
      gasPrice,
      classpath,
      getNFTById,
      nftLabStore,
      tokenIdRef
    );

    return new NFTLab(nft);
  }

  protected void setClasspath(TransactionReference classpath) {
    this.classpath = classpath;
  }

  protected void setNftLabStore(StorageReference nftLabStore) {
    this.nftLabStore = nftLabStore;
  }

  public TransactionReference getClasspath() {
    return classpath;
  }

  public StorageReference getNftLabStore() {
    return this.nftLabStore;
  }

  public static NFTLabStoreHotmoka load(
    String url,
    String accountAddress,
    BigInteger defaultGasLimit,
    BigInteger defaultGasPrice,
    String classpath,
    String nftLabStoreAddress
  ) throws Exception {
    NFTLabStoreHotmoka nftLabStore = new NFTLabStoreHotmoka(
      url,
      accountAddress,
      defaultGasLimit,
      defaultGasPrice
    );

    nftLabStore.setNftLabStore(new StorageReference(nftLabStoreAddress));
    nftLabStore.setClasspath(new LocalTransactionReference(classpath));

    return nftLabStore;
  }

  public static NFTLabStoreHotmoka deploy(
    String url,
    String accountAddress,
    BigInteger defaultGasLimit,
    BigInteger defaultGasPrice,
    Path smartContractCodePath,
    Path erc721CodePath,
    String name,
    String symbol
  ) throws Exception {
    NFTLabStoreHotmoka nftLabStore = new NFTLabStoreHotmoka(
      url,
      accountAddress,
      defaultGasLimit,
      defaultGasPrice
    );

    TransactionReference erc721Ref = nftLabStore.addJarStoreTransaction(
      defaultGasLimit,
      defaultGasPrice,
      nftLabStore.takamakaCode(),
      Files.readAllBytes(erc721CodePath),
      nftLabStore.takamakaCode()
    );

    TransactionReference classpath = nftLabStore.addJarStoreTransaction(
      defaultGasLimit,
      defaultGasPrice,
      nftLabStore.takamakaCode(),
      Files.readAllBytes(smartContractCodePath),
      erc721Ref
    );

    StringValue nameValue = new StringValue(name);
    StringValue symbolValue = new StringValue(symbol);

    StorageReference nftLabStoreRef = nftLabStore.addConstructorCallTransaction(
      defaultGasLimit,
      defaultGasPrice,
      classpath,
      NFTLabStoreHotmokaMethods.CONSTRUCTOR_NFTLABSTORE_STR_STR,
      nameValue,
      symbolValue
    );

    nftLabStore.setClasspath(classpath);
    nftLabStore.setNftLabStore(nftLabStoreRef);

    return nftLabStore;
  }

  public class NFTLab implements INFTLab {
    private final StorageReference artist;
    private final BigInteger artistId;
    private final String hash;
    private final String timestamp;

    public NFTLab(StorageReference artist, BigInteger artistId, String hash, String timestamp) {
      this.artist = artist;
      this.artistId = artistId;
      this.hash = hash;
      this.timestamp = timestamp;
    }

    public NFTLab(StorageReference nft) throws TransactionRejectedException, SignatureException, InvalidKeyException, TransactionException, CodeExecutionException {
      StorageReference artist = (StorageReference) runInstanceMethodCallTransaction(
        defaultGasLimit,
        classpath,
        new NonVoidMethodSignature(
          NFTLab,
          "getArtist",
          ClassType.CONTRACT
        ),
        nft
      );

      BigIntegerValue artistId = (BigIntegerValue) runInstanceMethodCallTransaction(
        defaultGasLimit,
        classpath,
        new NonVoidMethodSignature(
          NFTLab,
          "getArtistId",
          ClassType.BIG_INTEGER
        ),
        nft
      );

      StringValue hash = (StringValue) runInstanceMethodCallTransaction(
        defaultGasLimit,
        classpath,
        new NonVoidMethodSignature(
          NFTLab,
          "getHash",
          ClassType.STRING
        ),
        nft
      );

      StringValue timestamp = (StringValue) runInstanceMethodCallTransaction(
        defaultGasLimit,
        classpath,
        new NonVoidMethodSignature(
          NFTLab,
          "getTimestamp",
          ClassType.STRING
        ),
        nft
      );

      this.artist = artist;
      this.artistId = artistId.value;
      this.hash = hash.value;
      this.timestamp = timestamp.value;
    }

    public StorageReference getArtist() { return artist; }

    public BigInteger getArtistId() { return artistId; }

    public String getHash() { return hash; }

    public String getTimestamp() { return timestamp; }

    @Override
    public String toString() {
      return "NFTLab{" +
        "artist=" + artist.toString() +
        ", artistId=" + artistId.toString() +
        ", hash='" + hash + '\'' +
        ", timestamp='" + timestamp + '\'' +
        '}';
    }
  }

  public class NFTTransaction implements INFTTransaction {
    private final BigInteger tokenId;
    private final StorageReference seller;
    private final BigInteger sellerId;
    private final StorageReference buyer;
    private final BigInteger buyerId;
    private final String price;
    private final String timestamp;

    public NFTTransaction(BigInteger tokenId, StorageReference seller, BigInteger sellerId, StorageReference buyer, BigInteger buyerId, String price, String timestamp) {
      this.tokenId = tokenId;
      this.seller = seller;
      this.sellerId = sellerId;
      this.buyer = buyer;
      this.buyerId = buyerId;
      this.price = price;
      this.timestamp = timestamp;
    }

    public NFTTransaction(StorageReference transaction) throws TransactionRejectedException, TransactionException, CodeExecutionException {
      BigIntegerValue tokenId = (BigIntegerValue) runInstanceMethodCallTransaction(
        defaultGasLimit,
        classpath,
        new NonVoidMethodSignature(
          NFTTransaction,
          "getTokenId",
          ClassType.BIG_INTEGER
        ),
        transaction
      );

      StorageReference seller = (StorageReference) runInstanceMethodCallTransaction(
        defaultGasLimit,
        classpath,
        new NonVoidMethodSignature(
          NFTTransaction,
          "getSeller",
          ClassType.CONTRACT
        ),
        transaction
      );

      BigIntegerValue sellerId = (BigIntegerValue) runInstanceMethodCallTransaction(
        defaultGasLimit,
        classpath,
        new NonVoidMethodSignature(
          NFTTransaction,
          "getSellerId",
          ClassType.BIG_INTEGER
        ),
        transaction
      );

      StorageReference buyer = (StorageReference) runInstanceMethodCallTransaction(
        defaultGasLimit,
        classpath,
        new NonVoidMethodSignature(
          NFTTransaction,
          "getBuyer",
          ClassType.CONTRACT
        ),
        transaction
      );

      BigIntegerValue buyerId = (BigIntegerValue) runInstanceMethodCallTransaction(
        defaultGasLimit,
        classpath,
        new NonVoidMethodSignature(
          NFTTransaction,
          "getBuyerId",
          ClassType.BIG_INTEGER
        ),
        transaction
      );

      StringValue price = (StringValue) runInstanceMethodCallTransaction(
        defaultGasLimit,
        classpath,
        new NonVoidMethodSignature(
          NFTTransaction,
          "getPrice",
          ClassType.STRING
        ),
        transaction
      );

      StringValue timestamp = (StringValue) runInstanceMethodCallTransaction(
        defaultGasLimit,
        classpath,
        new NonVoidMethodSignature(
          NFTTransaction,
          "getTimestamp",
          ClassType.STRING
        ),
        transaction
      );

      this.tokenId = tokenId.value;
      this.seller = seller;
      this.sellerId = sellerId.value;
      this.buyer = buyer;
      this.buyerId = buyerId.value;
      this.price = price.value;
      this.timestamp = timestamp.value;
    }

    public BigInteger getTokenId() { return tokenId; }

    public StorageReference getSeller() { return seller; }

    public BigInteger getSellerId() { return sellerId; }

    public StorageReference getBuyer() { return buyer; }

    public BigInteger getBuyerId() { return buyerId; }

    public String getPrice() { return price; }

    public String getTimestamp() { return timestamp; }

    @Override
    public String toString() {
      return "NFTTransaction{" +
        "tokenId=" + tokenId.toString() +
        ", seller=" + seller.toString() +
        ", sellerId=" + sellerId.toString() +
        ", buyer=" + buyer.toString() +
        ", buyerId=" + buyerId.toString() +
        ", price='" + price + '\'' +
        ", timestamp='" + timestamp + '\'' +
        '}';
    }
  }
}
