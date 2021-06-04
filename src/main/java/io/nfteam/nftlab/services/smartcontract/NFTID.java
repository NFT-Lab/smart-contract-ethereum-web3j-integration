package io.nfteam.nftlab.services.smartcontract;

import java.math.BigInteger;
import java.util.Objects;

public record NFTID(String hash, BigInteger tokenId) {
  public NFTID {
    Objects.requireNonNull(hash);
    Objects.requireNonNull(tokenId);
  }
}
