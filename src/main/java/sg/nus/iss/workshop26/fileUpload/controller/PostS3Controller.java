package sg.nus.iss.workshop26.fileUpload.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path="/post/s3")
public class PostS3Controller {

    @Autowired
    private AmazonS3 s3;

    @PostMapping
    public ResponseEntity<String> post(
        @RequestParam MultipartFile image,
        @RequestPart String comment, 
        @RequestPart String poster
    ){
        JsonObject result;
        String imageType = image.getContentType();
        byte[] buff = new byte[0];

        try {
            buff = image.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String uuid = UUID.randomUUID().toString().substring(0, 8);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(imageType);
        //metadata.setContentLength(buff.length);
        Map userCustomMetadata  = new HashMap();
        userCustomMetadata.put("uploader", poster);
        userCustomMetadata.put("fileSize", String.valueOf(buff.length));
        metadata.setUserMetadata(userCustomMetadata);
        
        try {
            PutObjectRequest putReq = new PutObjectRequest("vtt20222paf", 
                    "%s/images/%s".formatted(poster, uuid), image.getInputStream(), metadata);
            putReq.setCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putReq);
            result = Json.createObjectBuilder()
                .add("ObjId", uuid)
                .build();
            return ResponseEntity.ok(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
            result = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).body(result.toString());
        }

    }
}