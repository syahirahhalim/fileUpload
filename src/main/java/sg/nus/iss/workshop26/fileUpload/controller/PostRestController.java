package sg.nus.iss.workshop26.fileUpload.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.nus.iss.workshop26.fileUpload.models.Post;
import sg.nus.iss.workshop26.fileUpload.repository.PostRepository;

@RestController
@RequestMapping(path="/post")
public class PostRestController {

    @Autowired
    private PostRepository postRepo;
    
    @GetMapping(path="/{postId}/image")
    public ResponseEntity<byte[]> getPostImage(@PathVariable Integer postId) {
        
        Optional<Post> opt = postRepo.getPostById(postId);
        if(opt.isEmpty())
         
            return ResponseEntity.notFound().build();
        
            final Post postVal= opt.get();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", postVal.getImageType());
            headers.add("Cache-Control", "max-age=604800");
    
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(postVal.getImage());
            
    }
}
