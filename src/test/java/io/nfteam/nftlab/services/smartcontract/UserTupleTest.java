package io.nfteam.nftlab.services.smartcontract;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class UserTupleTest {
  @Test
  public void defaultConstructor() {
    final String expectedWallet = "0x51b1D3246fc4D665A75F77599c82419ab11dEAc4";
    final BigInteger expectedId = BigInteger.valueOf(1);

    UserTuple userTuple = new UserTuple(expectedWallet, expectedId);

    assertEquals(expectedWallet, userTuple.wallet());
    assertEquals(expectedId, userTuple.id());
  }
}
