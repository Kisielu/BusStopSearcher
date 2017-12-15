package com.kisielewicz.BusStopSearcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kisielewicz.BusStopSearcher.domain.BusStop;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebMvc
public class BusStopSearcherApplicationTests {

	private static final String URL = "http://localhost:8080/";

	@Autowired
	WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void before() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();

	}

	@Test
	public void shouldGetBusStop() throws Exception {
		String latitude = "16.90";
		String longitude = "52.40";

		MvcResult mvcResult = mockMvc.perform(
						get(URL + "/stops/closest")
								.param("latitude", latitude)
								.param("longitude", longitude))
				.andExpect(status().isOk())
				.andReturn();
		ObjectMapper objectMapper = new ObjectMapper();
		BusStop result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BusStop.class);
		assertThat(result).isNotNull();
	}

	@Test
	public void shouldReturn4xxForWrongTypeOfParameters() throws Exception {
		String latitude = "a";
		String longitude = "b";

		mockMvc.perform(
						get(URL + "/stops/closest")
								.param("latitude", latitude)
								.param("longitude", longitude))
				.andExpect(status().is4xxClientError())
				.andReturn();
	}


}
