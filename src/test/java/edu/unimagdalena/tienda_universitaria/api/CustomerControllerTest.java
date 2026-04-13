package edu.unimagdalena.tienda_universitaria.api;

import edu.unimagdalena.tienda_universitaria.api.dto.CustomerDtos.*;
import edu.unimagdalena.tienda_universitaria.services.CustomerService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    //@MockitoBean
}
