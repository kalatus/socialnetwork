package com.akamai.socialnetworkapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/endpoint_post")
public class SocialPostController {

    @Autowired
    public SocialNetworkPostService socialNetworkPostService;

    @GetMapping("/author")
    public ResponseEntity<Map<String, Object>> getAuthor(@RequestParam("author") String author) {
        List<SocialNetworkPost> socialNetworkPosts = socialNetworkPostService.searchPostsByAuthor(author);
        Map<String, Object> r = Map.of(
                "author", author,
                "posts", socialNetworkPosts.stream()
                        .map(p -> Map.of("postDate", p.getPostDate(),
                                "postCategory", p.getPostCategory(),
                                "author", p.getAuthor(),
                                "views", p.getViewCount())
                        ).collect(Collectors.toList())
        );

        return ResponseEntity.ok(r);

    }

    @GetMapping("/category")
    public ResponseEntity<Map<String, Object>> getCategory(String category) {
        Map<String, List<SocialNetworkPost>> topPostsForEachCategory = socialNetworkPostService.getTopPostsForEachCategory(category);

        Map<String, Object> r = Map.of(

                "postPerCategory", topPostsForEachCategory.entrySet().stream()
                        .map(p -> Map.of("category", p.getKey(),
                                "posts", p.getValue().stream()
                                        .map(e -> Map.of("postDate", e.getPostDate(),
                                                "postCategory", e.getPostCategory(),
                                                "author", e.getAuthor(),
                                                "views", e.getViewCount())
                                        ).collect(Collectors.toList())
                        )).collect(Collectors.toList()));


        return ResponseEntity.ok(r);

    }
}
