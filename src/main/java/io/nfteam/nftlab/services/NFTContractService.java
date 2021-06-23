package io.nfteam.nftlab.services;

import io.nfteam.nftlab.contracts.NFTLabStoreEthereum;
import io.nfteam.nftlab.contracts.NFTLabStoreHotmoka;
import io.nfteam.nftlab.services.smartcontract.*;
import org.springframework.core.io.ByteArrayResource;

import java.math.BigInteger;
import java.util.List;

public interface NFTContractService {
    NFTID mint(UserTuple artist, ByteArrayResource file) throws Exception;

    void transfer(
            BigInteger tokenId,
            UserTuple seller,
            UserTuple buyer,
            String price,
            String timestamp) throws Exception;

    List<INFTTransaction> getHistory(BigInteger tokenId) throws Exception;

    INFTLab getNFTById(BigInteger tokenId) throws Exception;

    INFTLab getNFTByHash(String hash) throws Exception;
}
