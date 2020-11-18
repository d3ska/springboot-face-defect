package pl.deska.springbootfacedefect;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import pl.deska.springbootfacedefect.model.FaceObject;
import pl.deska.springbootfacedefect.model.ImageUrl;

import java.net.URI;
import java.net.URISyntaxException;


@Controller
public class FaceApiClient {

    @Value("${Ocp-Apim-Subscription-Key}")
    private String ocpApimSubscriptionKey;
    private static final String FACE__BASE_API_URL = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect?";

    public FaceObject[] getInfoByPhotoUrl(String url){
//        "https://dawidpartnerzy.com/media/zespol/IMG_9206.jpg"
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ImageUrl> imageUrlHttpEntity = getHttpEntity(url);

        ResponseEntity<FaceObject[]> exchange = restTemplate.exchange(
                getApiUrl(),
                HttpMethod.POST,
                imageUrlHttpEntity,
                FaceObject[].class);

        return exchange.getBody();
    }


    private HttpEntity<ImageUrl> getHttpEntity(String url) {
        ImageUrl imageUrl = new ImageUrl(url);
        HttpHeaders httpHeaders = getHttpHeaders();
        HttpEntity<ImageUrl> imageUrlHttpEntity = new HttpEntity<>(imageUrl, httpHeaders);
        return imageUrlHttpEntity;
    }


    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Ocp-Apim-Subscription-Key",ocpApimSubscriptionKey);
        return httpHeaders;
    }


    private URI getApiUrl() {

        URI uri = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(FACE__BASE_API_URL);
            uri = uriBuilder.addParameter("returnFaceId", "true")
                    .addParameter("returnFaceLandmarks", "true")
                    .addParameter("returnFaceAttributes", "age, gender, headPose, smile, facialHair, glasses, emotion, hair, makeup, occlusion, accessories, blur, exposure, noise")
                    .addParameter("recognitionModel", "recognition_03")
                    .addParameter("returnRecognitionModel", "false")
                    .addParameter("detectionModel", "detection_01")
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return uri;
    }
}
