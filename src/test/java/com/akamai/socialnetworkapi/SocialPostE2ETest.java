package com.akamai.socialnetworkapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureMockMvc
public class SocialPostE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testGetTopTenPostsForEachCategory() throws Exception {

        //given
        String api = "/endpoint_post/category";

        //when
        MvcResult actual = mockMvc.perform(MockMvcRequestBuilders.get(api))
                .andExpect(status().isOk())
                .andReturn();

        //then
        MockHttpServletResponse response = actual.getResponse();
        LinkedHashMap<String, List> responseObject = objectMapper.readValue(response.getContentAsString(), LinkedHashMap.class);
        List<LinkedHashMap<String, Object>> postPerCategory = responseObject.get("postPerCategory");
        assertThat(postPerCategory).isNotEmpty();
        assertThat(postPerCategory).allMatch(postsPerCategory -> ((List)postsPerCategory.get("posts")).size() <= 10)
                .noneMatch(postsPerCategory -> ((List)postsPerCategory.get("posts")).size() > 0)
                .allMatch(postsPerCategory -> postsPerCategory.get("category") != null);
    }

    @Test
    public void testThatIncludesTopPostForGamingCategory() throws Exception {

        //given
        String api = "/endpoint_post/category";

        //when
        MvcResult actual = mockMvc.perform(MockMvcRequestBuilders.get(api))
                .andExpect(status().isOk())
                .andReturn();

        //then
        MockHttpServletResponse response = actual.getResponse();
        LinkedHashMap<String, List> responseObject = objectMapper.readValue(response.getContentAsString(), LinkedHashMap.class);
        List<LinkedHashMap<String, Object>> postPerCategory = responseObject.get("postPerCategory");
        List<LinkedHashMap<String, Object>> gamingPosts = postPerCategory.stream()
                .filter(postsPerCategory -> postsPerCategory.get("category").equals("Gaming"))
                .map(postsPerCategory -> (List<LinkedHashMap<String, Object>>) postsPerCategory.get("posts"))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        assertThat(gamingPosts).anyMatch(post -> post.get("views").equals(23200000000L));
    }

    @Test
    public void testSearchPostsByAuthor() throws Exception {

        //given
        String author = "Alice";
        String api = "/endpoint_post/author";

        //when
        MvcResult actual = mockMvc.perform(MockMvcRequestBuilders.get(api)
                        .param("author", author))
                .andExpect(status().isOk())
                .andReturn();

        //then
        MockHttpServletResponse response = actual.getResponse();
        LinkedHashMap<String, Object> responseObject = objectMapper.readValue(response.getContentAsString(), LinkedHashMap.class);
    }


    @Test
    public void testSearchPostsByAuthor_EmptyResult() throws Exception {

        //given
        String author = "NonExistingAuthor";
        String api = "/endpoint_post/author";

        //when
        MvcResult actual = mockMvc.perform(MockMvcRequestBuilders.get(api)
                        .param("author", author))
                .andExpect(status().isOk())
                .andReturn();

        //then
        MockHttpServletResponse response = actual.getResponse();
        LinkedHashMap<String, Object> responseObject = objectMapper.readValue(response.getContentAsString(), LinkedHashMap.class);

        assertThat(responseObject.get("author")).isEqualTo(author);
        assertThat((List)responseObject.get("posts")).isEmpty();
    }
}


