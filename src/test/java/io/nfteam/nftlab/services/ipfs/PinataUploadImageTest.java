package io.nfteam.nftlab.services.ipfs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PinataUploadImageTest {
  @Test
  public void defaultConstructor() {
    String expectedHash = "";
    int expectedPinSize = 0;
    String expectedTimestamp = "";

    PinataUploadImage uploadImage = new PinataUploadImage();

    assertNull(uploadImage.getHash());
    assertEquals(expectedPinSize, uploadImage.getSize());
    assertNull(uploadImage.getTimestamp());
  }

  @Test
  public void getHash() {
    String expectedHash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";

    PinataUploadImage uploadImage = new PinataUploadImage();

    uploadImage.IpfsHash = expectedHash;

    assertEquals(expectedHash, uploadImage.getHash());
  }

  @Test
  public void getSize() {
    int expectedPinSize = 12;

    PinataUploadImage uploadImage = new PinataUploadImage();

    uploadImage.PinSize = expectedPinSize;

    assertEquals(expectedPinSize, uploadImage.getSize());
  }

  @Test
  public void getTimestamp() {
    String expectedTimestamp = "2021";

    PinataUploadImage uploadImage = new PinataUploadImage();

    uploadImage.Timestamp = expectedTimestamp;

    assertEquals(expectedTimestamp, uploadImage.getTimestamp());
  }

  @Test
  public void toString_Object_StringOfObject() {
    String hash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";
    int pinSize = 12;
    String timestamp = "2021";

    String expectedToString =  hash + " - " + pinSize + " - " + timestamp;

    PinataUploadImage uploadImage = new PinataUploadImage();

    uploadImage.IpfsHash = hash;
    uploadImage.PinSize = pinSize;
    uploadImage.Timestamp = timestamp;

    assertEquals(expectedToString, uploadImage.toString());
  }
}
