package be.pxl.clubs.integration;

import be.pxl.clubs.domain.Owner;
import be.pxl.clubs.domain.dto.OwnerRequest;
import be.pxl.clubs.repository.OwnerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ContainerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private OwnerRepository ownerRepository;

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8:0:18");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Test
    public void addOwner() throws Exception{
        OwnerRequest ownerRequest = OwnerRequest.builder().name("input").build();
        String requestString = mapper.writeValueAsString(ownerRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clubowner")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString)
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isCreated());

        Owner owner = ownerRepository.findAll().stream().findFirst().orElse(null);
        assertEquals("input", owner.getName());
    }
}
