package com.codespacelab.order;

import com.codespacelab.order.config.JmsConfig;
import com.codespacelab.order.config.TaskConfig;
import com.codespacelab.order.model.OrderDto;
import com.codespacelab.order.service.MenuService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//In this test, the order service will not connect to the config server for the datasource url,
//specify any value for spring.datasource.url , so that it won't throw an exception
//when creating OrderController
@SpringBootTest(properties= {"spring.datasource.url= jdbc:mysql://testhost:3307"})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class OrderApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void getOrdersTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/order/all")).andReturn();
		String jsonResult = result.getResponse().getContentAsString();
		List<OrderDto> orders = objectMapper.readValue(jsonResult, new TypeReference<List<OrderDto>>() {
		});
		OrderDto order0 = orders.get(0);
		OrderDto order1 = orders.get(1);
		assertTrue(order0.getId() == 123);
		assertTrue(order1.getId() == 456);
		assertTrue(order0.getItems().contains("Burger"));
		assertTrue(order0.getStatus().equals("Collected"));
		assertTrue(order1.getItems().contains("Meal"));
		assertTrue(order1.getStatus().equals("Pick-up"));
	}

	@Test
	void getOrderTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/order?id=123")).andReturn();
		String jsonResult = result.getResponse().getContentAsString();
		OrderDto order = objectMapper.readValue(jsonResult, OrderDto.class);
		assertTrue(order.getId()==123);
	}

	@Test
	void addOrderTest() throws Exception{
		OrderDto myOrder = new OrderDto();
		myOrder.setItems(Arrays.asList("Cookies", "Cheese burger"));
		String orderJson = objectMapper.writeValueAsString(myOrder);
		MvcResult result = mockMvc.perform(post("/order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(orderJson)).andReturn();
		String resultJson = result.getResponse().getContentAsString();
		OrderDto orderAdded = objectMapper.readValue(resultJson, OrderDto.class);
		assertTrue(orderAdded.getId() == 102L);
		assertTrue(orderAdded.getStatus().equals("Pending"));
		assertTrue(orderAdded.getItems().size()==2);
		assertTrue(getCurrentOrders().size()==3);
	}



	@Test
	void updateOrderTest() throws Exception{
		OrderDto myOrderUpdated = new OrderDto();
		myOrderUpdated.setId(123L);
		myOrderUpdated.setItems(Arrays.asList("Cookies", "tomato burger"));
		myOrderUpdated.setStatus("Pick-up");
		String orderJson = objectMapper.writeValueAsString(myOrderUpdated);
		MvcResult result = mockMvc.perform(put("/order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(orderJson)).andReturn();
		String resultJson = result.getResponse().getContentAsString();
		OrderDto orderUpdated = objectMapper.readValue(resultJson, OrderDto.class);
		assertTrue(orderUpdated.getId() == 123L);
		assertTrue(orderUpdated.getStatus().equals("Pick-up"));
	}



	@Test
	void deleteOrderTest() throws Exception{
		MvcResult result = mockMvc.perform(delete("/order?id=123")).andReturn();
		String deleted = result.getResponse().getContentAsString();
		assertTrue(deleted.equals("true"));
		assertTrue(getCurrentOrders().size()==1);
	}


	private List<OrderDto> getCurrentOrders() throws Exception{
		MvcResult result = mockMvc.perform(get("/order/all")).andReturn();
		String jsonResult = result.getResponse().getContentAsString();
		List<OrderDto> orders = objectMapper.readValue(jsonResult, new TypeReference<List<OrderDto>>() {
		});
		return orders;
	}

}
