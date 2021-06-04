package io.nfteam.nftlab.services.smartcontract;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class UserTupleTest {
  @Test
  public void defaultConstructor_ValidParameters_ObjectCreatedRight() {
    final String expectedWallet = "0x51b1D3246fc4D665A75F77599c82419ab11dEAc4";
    final BigInteger expectedId = BigInteger.valueOf(1);

    UserTuple userTuple = new UserTuple(expectedWallet, expectedId);

    assertEquals(expectedWallet, userTuple.wallet());
    assertEquals(expectedId, userTuple.id());
  }

  @Test
  public void defaultConstructor_NotValidWallet_ThrowException() {
    final String notValidWallet = "0x51b1D3246fc4D665A75F77599c8241";
    final BigInteger expectedId = BigInteger.valueOf(1);

    String expectedMessage = "Wallet not in exactly format.";

    Throwable thrown = assertThrows(IllegalArgumentException.class, () -> new UserTuple(notValidWallet, expectedId));

    assertEquals(expectedMessage, thrown.getMessage());
  }

  @Test
  public void defaultConstructor_NullWallet_ThrowException() {
    final String notValidWallet = null;
    final BigInteger expectedId = BigInteger.valueOf(1);

    assertThrows(NullPointerException.class, () -> new UserTuple(notValidWallet, expectedId));
  }

  @Test
  public void defaultConstructor_NullId_ThrowException() {
    final String notValidWallet = "0x51b1D3246fc4D665A75F77599c82419ab11dEAc4";
    final BigInteger expectedId = null;

    assertThrows(NullPointerException.class, () -> new UserTuple(notValidWallet, expectedId));
  }
}
