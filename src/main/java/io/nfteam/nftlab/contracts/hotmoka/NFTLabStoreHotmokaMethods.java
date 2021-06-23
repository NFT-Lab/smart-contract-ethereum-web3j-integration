package io.nfteam.nftlab.contracts.hotmoka;

import io.hotmoka.beans.signatures.ConstructorSignature;
import io.hotmoka.beans.signatures.NonVoidMethodSignature;
import io.hotmoka.beans.types.BasicTypes;
import io.hotmoka.beans.types.ClassType;

public class NFTLabStoreHotmokaMethods {
  public static final ClassType NFTLabStore = new ClassType("io.nfteam.nftlab.nftlabstore.NFTLabStore");
  public static final ClassType NFTLab = new ClassType("io.nfteam.nftlab.nftlabstore.NFTLab");
  public static final ClassType NFTTransaction = new ClassType("io.nfteam.nftlab.nftlabstore.NFTTransaction");

  public static final ConstructorSignature CONSTRUCTOR_NFTLABSTORE_STR_STR = new ConstructorSignature(NFTLabStore, ClassType.STRING, ClassType.STRING);

  public static final NonVoidMethodSignature mint = new NonVoidMethodSignature(
    NFTLabStore,
    "mint",
    ClassType.BIG_INTEGER,
    ClassType.CONTRACT,
    ClassType.BIG_INTEGER,
    ClassType.STRING,
    ClassType.STRING
  );

  public static final NonVoidMethodSignature transfer = new NonVoidMethodSignature(
    NFTLabStore,
    "transfer",
    BasicTypes.BOOLEAN,
    ClassType.BIG_INTEGER,
    ClassType.CONTRACT,
    ClassType.BIG_INTEGER,
    ClassType.CONTRACT,
    ClassType.BIG_INTEGER,
    ClassType.STRING,
    ClassType.STRING
  );

  public static final NonVoidMethodSignature getHistory = new NonVoidMethodSignature(
    NFTLabStore,
    "getHistory",
    ClassType.STORAGE_LINKED_LIST,
    ClassType.BIG_INTEGER
  );

  public static final NonVoidMethodSignature getNFTByHash = new NonVoidMethodSignature(
    NFTLabStore,
    "getNFTByHash",
    NFTLab,
    ClassType.STRING
  );

  public static final NonVoidMethodSignature getNFTById = new NonVoidMethodSignature(
    NFTLabStore,
    "getNFTById",
    NFTLab,
    ClassType.BIG_INTEGER
  );
}
