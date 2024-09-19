package com.akamai.socialnetworkapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SocialNetworkPostService {

    @Autowired
    private SocialNetworkPostRepository socialNetworkPostRepository;


    public Map<String, List<SocialNetworkPost>> getTopPostsForEachCategory(String category) {
        List<SocialNetworkPost> all = socialNetworkPostRepository.findAll();
        Map<String, List<SocialNetworkPost>> map = new HashMap<>();
        for(SocialNetworkPost socialNetworkPost : all){
            List<SocialNetworkPost> socialNetworkPosts = map.get(socialNetworkPost.getPostCategory());
            socialNetworkPosts.add(socialNetworkPost);
        }

        for(Map.Entry<String, List<SocialNetworkPost>> entry : map.entrySet()){
            entry.getValue().sort(Comparator.comparing(SocialNetworkPost::getViewCount));
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    public List<SocialNetworkPost> searchPostsByAuthor(String author) {
        List<SocialNetworkPost> all = socialNetworkPostRepository.findAll();
        ArrayList<SocialNetworkPost> socialNetworkPosts = new ArrayList<>();
        for(SocialNetworkPost post : all){
            if(post.getAuthor() == author){
                socialNetworkPosts.add(post);
            }
        }
        return all;
    }


}
