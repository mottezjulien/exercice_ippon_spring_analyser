package com.example.springanalyser;

import com.example.springanalyser.proxy.ConfigurationProxy;
import org.hamcrest.Matcher;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
@ContextConfiguration(classes = ConfigurationProxy.class)
class SpringConfigurationWebRequestTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void verifyRequestInjectionIsAnonymousClass() throws Exception {
        MvcResult result = getResult("/className");
        assertThat(result.getResponse().getContentAsString())
                .startsWith("MyProperty$$EnhancerBySpringCGLIB$$");
    }

    @Test
    void verifyBuildNewInstanceOfRequestInjectionForEachCall() throws Exception {
        MvcResult result = getResult("/id");
        String firstId = result.getResponse().getContentAsString();
        result = getResult("/id");
        String secondId = result.getResponse().getContentAsString();
        assertThat(firstId).isNotEqualTo(secondId);
    }

    private MvcResult getResult(String url) throws Exception {
        return mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

}

