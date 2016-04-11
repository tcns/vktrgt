package ru.tcns.vktrgt.web.rest;

import ru.tcns.vktrgt.Application;
import ru.tcns.vktrgt.domain.Tag;
import ru.tcns.vktrgt.repository.TagRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TagResource REST controller.
 *
 * @see TagResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TagResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private TagRepository tagRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTagMockMvc;

    private Tag tag;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TagResource tagResource = new TagResource();
        ReflectionTestUtils.setField(tagResource, "tagRepository", tagRepository);
        this.restTagMockMvc = MockMvcBuilders.standaloneSetup(tagResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tagRepository.deleteAll();
        tag = new Tag();
        tag.setName(DEFAULT_NAME);
    }

    @Test
    public void createTag() throws Exception {
        int databaseSizeBeforeCreate = tagRepository.findAll().size();

        // Create the Tag

        restTagMockMvc.perform(post("/api/tags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tag)))
                .andExpect(status().isCreated());

        // Validate the Tag in the database
        List<Tag> tags = tagRepository.findAll();
        assertThat(tags).hasSize(databaseSizeBeforeCreate + 1);
        Tag testTag = tags.get(tags.size() - 1);
        assertThat(testTag.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getAllTags() throws Exception {
        // Initialize the database
        tagRepository.save(tag);

        // Get all the tags
        restTagMockMvc.perform(get("/api/tags?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    public void getTag() throws Exception {
        // Initialize the database
        tagRepository.save(tag);

        // Get the tag
        restTagMockMvc.perform(get("/api/tags/{id}", tag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tag.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    public void getNonExistingTag() throws Exception {
        // Get the tag
        restTagMockMvc.perform(get("/api/tags/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateTag() throws Exception {
        // Initialize the database
        tagRepository.save(tag);

		int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Update the tag
        tag.setName(UPDATED_NAME);

        restTagMockMvc.perform(put("/api/tags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tag)))
                .andExpect(status().isOk());

        // Validate the Tag in the database
        List<Tag> tags = tagRepository.findAll();
        assertThat(tags).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tags.get(tags.size() - 1);
        assertThat(testTag.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void deleteTag() throws Exception {
        // Initialize the database
        tagRepository.save(tag);

		int databaseSizeBeforeDelete = tagRepository.findAll().size();

        // Get the tag
        restTagMockMvc.perform(delete("/api/tags/{id}", tag.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Tag> tags = tagRepository.findAll();
        assertThat(tags).hasSize(databaseSizeBeforeDelete - 1);
    }
}
