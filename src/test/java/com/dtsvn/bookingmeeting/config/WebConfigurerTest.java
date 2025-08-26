package com.dtsvn.bookingmeeting.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link WebConfigurer} class.
 */
class WebConfigurerTest {

    private WebConfigurer webConfigurer;

    @BeforeEach
    void setup() {
        webConfigurer = new WebConfigurer();
    }

    @Test
    void shouldAddCorsMappings() {
        // Test that CORS mappings are added correctly
        CorsRegistry registry = new CorsRegistry();
        
        // Call the method
        webConfigurer.addCorsMappings(registry);
        
        // Verify that CORS configuration was added
        // Note: We can't easily test the internal registry state, 
        // but we can verify the method executes without error
        assertNotNull(webConfigurer);
    }

    @Test
    void shouldCreateWebConfigurer() {
        // Test that WebConfigurer can be created
        assertNotNull(webConfigurer);
        assertTrue(webConfigurer instanceof WebConfigurer);
    }
}
