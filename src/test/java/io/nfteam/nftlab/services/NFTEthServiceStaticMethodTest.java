package io.nfteam.nftlab.services;

import io.nfteam.nftlab.contracts.NFTLabStore;
import io.nfteam.nftlab.services.smartcontract.NFTLabETHContractService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NFTEthServiceStaticMethodTest {

  @Test
  public void loadContract() throws Exception {
    String contractAddress = "address_of_contract";
    NFTLabStore expectedLoadedContract = mock(NFTLabStore.class);

    try(MockedStatic<NFTLabStore> nftLabStoreMockedStatic = mockStatic(NFTLabStore.class)) {
      nftLabStoreMockedStatic.when(() -> NFTLabStore.load(
        contractAddress, any(Web3j.class), any(Credentials.class), any(ContractGasProvider.class)))
        .thenReturn(expectedLoadedContract);

      NFTLabStore actualLoadedContract =  NFTEthService.loadContract(contractAddress, mock(Web3j.class), mock(Credentials.class), mock(ContractGasProvider.class));

      assertEquals(expectedLoadedContract, actualLoadedContract);
    }
  }
}
