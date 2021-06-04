package io.nfteam.nftlab.services.ipfs;

import java.io.IOException;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

//@Component("IPFSPinataService")
public class IPFSPinataService implements IPFSService {
  private final String baseUrl;
  private final String apiKey;
  private final String secretKey;
  private final RestTemplate restTemplate;

  public IPFSPinataService(String baseUrl, String apiKey, String secretKey, RestTemplate restTemplate) {
    this.baseUrl = baseUrl;
    this.apiKey = apiKey;
    this.secretKey = secretKey;
    this.restTemplate = restTemplate;
  }

  public IPFSResponses.UploadImage uploadImage(FileSystemResource file) throws IOException
  {
    HttpHeaders headers = getHeader();

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    body.add("file", file);

    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    ResponseEntity<PinataUploadImage> response = restTemplate
      .postForEntity(baseUrl + "/pinning/pinFileToIPFS", requestEntity, PinataUploadImage.class);

    return response.getBody();
  }

  private HttpHeaders getHeader() {
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    headers.set("pinata_api_key", this.apiKey);
    headers.set("pinata_secret_api_key", this.secretKey);
    headers.setAccessControlAllowCredentials(true);

    return headers;
  }
}