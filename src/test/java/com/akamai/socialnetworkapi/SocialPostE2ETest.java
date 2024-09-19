package com.akamai.socialnetworkapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureMockMvc
public class SocialPostControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private List<SocialNetworkPost> posts;

    @BeforeEach
    public void setup() {
        posts = Arrays.asList(
                new SocialNetworkPost(1L, LocalDate.parse("2024-01-15"), "Music", "Alice", "Check out my new song!", 1570),
                new SocialNetworkPost(2L, LocalDate.parse("2024-02-22"), "Gaming", "Bob", "Just finished an epic gaming session!", 2215),
                new SocialNetworkPost(3L, LocalDate.parse("2024-03-10"), "News", "Charlie", "Breaking news: Major event happening now!", 3050)
        );
    }

    @Test
    public void testGetTopPostsByCategory() throws Exception {
//        Page<SocialNetworkPost> page = new PageImpl<>(posts, PageRequest.of(0, 10), posts.size());
//
//        Mockito.when(service.getTopPostsForEachCategory(eq("Music"), eq(0), eq(10))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/endpoint/post/author")
                        .param("author", "Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(posts.size()))
                .andExpect(jsonPath("$.content[0].author").value("Alice"))
                .andExpect(jsonPath("$.content[0].viewCount").value(1570L));
    }

    @Test
    public void testSearchPostsByAuthor() throws Exception {
        Page<SocialNetworkPost> page = new PageImpl<>(posts, PageRequest.of(0, 10), posts.size());

//        Mockito.when(service.searchPostsByAuthor(eq("Alice"), eq(0), eq(10))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/search")
                        .param("author", "Alice")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(posts.size()))
                .andExpect(jsonPath("$.content[0].author").value("Alice"))
                .andExpect(jsonPath("$.content[0].viewCount").value(1570L));
    }

    @Test
    public void testGetTopPostsByCategory_EmptyResult() throws Exception {
        Page<SocialNetworkPost> emptyPage = Page.empty(PageRequest.of(0, 10));

//        Mockito.when(service.getTopPostsForEachCategory(eq("UnknownCategory"), eq(0), eq(10))).thenReturn(emptyPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/top/UnknownCategory")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    public void testSearchPostsByAuthor_EmptyResult() throws Exception {
        Page<SocialNetworkPost> emptyPage = Page.empty(PageRequest.of(0, 10));


        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/search")
                        .param("author", "UnknownAuthor")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }
}


