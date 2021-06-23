package io.nfteam.nftlab.services.smartcontract;

import java.math.BigInteger;

public record UserTuple(String wallet, BigInteger id) {
  public UserTuple {
    java.util.Objects.requireNonNull(wallet);
    java.util.Objects.requireNonNull(id);

    if(!wallet.matches("^0x[a-fA-F0-9]{40}$") &&
      !wallet.matches("^\\w{64}#[0-9]$")
    ) {
      throw new IllegalArgumentException("Wallet not in exactly format.");
    }
  }
}
