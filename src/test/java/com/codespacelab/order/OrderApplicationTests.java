package com.codespacelab.order;

import com.codespacelab.order.config.OrderWebSecurity;
import com.codespacelab.order.model.OrderDto;
import com.codespacelab.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
@WebMvcTest(properties={"spring.datasource.url= jdbc:mysql://testhost:3307"})
@AutoConfigureMockMvc
@Import(OrderWebSecurity.class)
class OrderApplicationTests {

	@MockBean
	OrderService orderService;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	private List<OrderDto> orders;
    private OrderDto orderDto;
    private OrderDto orderDtoToPost;
    private OrderDto orderDtoToUpdate;
    private String username = "admin";
    private String password = "admin";
    private String wrongPassword = "wrong_password";
    private Long orderId = 123L;
    private Long userId = 123L;

    @BeforeEach
	void setUp(){
		orders = new ArrayList<>();
		orderDto = new OrderDto(orderId, Arrays.asList("Meal"), "Collected");
		orders.add(orderDto);
		orderDtoToPost = new OrderDto(100L, Arrays.asList("cookies"), "Collected");
		orderDtoToUpdate = orderDto;
	}
	@Test
	void getOrdersTest() throws Exception {

		when(orderService.getOrders(userId)).thenReturn(orders);
		mockMvc.perform(get("/order/all?userId=123")
				.with(httpBasic(username,password))).andExpect(status().isOk()).andReturn();
        verify(orderService,times(1)).getOrders(123L);
	}

	@Test
	void getOrdersTest_unauthorized() throws Exception {

		when(orderService.getOrders(userId)).thenReturn(orders);
		mockMvc.perform(get("/order/all?userId=123")
				.with(httpBasic(username,wrongPassword))).andExpect(status().isUnauthorized()).andReturn();
		verify(orderService,times(0)).getOrders(123L);
	}

	@Test
	void getOrderTest() throws Exception{

		when(orderService.getOrder(orderId, userId)).thenReturn(orderDto);
		mockMvc.perform(get("/order?id=123&userId=123")
				.with(httpBasic(username,password))).andExpect(status().isOk()).andReturn();
		verify(orderService,times(1)).getOrder(123L,123L);
	}

	@Test
	void getOrderTest_unauthorized() throws Exception{

		when(orderService.getOrder(orderId, userId)).thenReturn(orderDto);
		mockMvc.perform(get("/order?id=123&userId=123")
				.with(httpBasic(username,wrongPassword))).andExpect(status().isUnauthorized()).andReturn();
		verify(orderService,times(0)).getOrder(123L,123L);
	}

	@Test
	void addOrderTest() throws Exception{
        String order_json  = objectMapper.writeValueAsString(orderDtoToPost);
		when(orderService.addOrder(any(OrderDto.class), anyLong())).thenReturn(orderDtoToPost);
		mockMvc.perform(post("/order?userId=123").contentType(MediaType.APPLICATION_JSON).content(order_json)
				.with(httpBasic(username,password))).andExpect(status().isOk()).andReturn();
		verify(orderService,times(1)).addOrder(any(OrderDto.class),anyLong());
	}

	@Test
	void addOrderTest_unauthorized() throws Exception{
		String order_json  = objectMapper.writeValueAsString(orderDtoToPost);
		when(orderService.addOrder(any(OrderDto.class), anyLong())).thenReturn(orderDtoToPost);
		mockMvc.perform(post("/order?userId=123").contentType(MediaType.APPLICATION_JSON).content(order_json)
				.with(httpBasic(username,wrongPassword))).andExpect(status().isUnauthorized()).andReturn();
		verify(orderService,times(0)).addOrder(any(OrderDto.class),anyLong());
	}

	@Test
	void updateOrderTest() throws Exception{
		String order_json  = objectMapper.writeValueAsString(orderDtoToUpdate);
		when(orderService.updateOrder(any(OrderDto.class), anyLong())).thenReturn(orderDtoToUpdate);
		mockMvc.perform(put("/order?userId=123").contentType(MediaType.APPLICATION_JSON).content(order_json)
				.with(httpBasic(username,password))).andExpect(status().isOk()).andReturn();
		verify(orderService,times(1)).updateOrder(any(OrderDto.class),anyLong());
	}

	@Test
	void updateOrderTest_unAuthorized() throws Exception{
		String order_json  = objectMapper.writeValueAsString(orderDtoToUpdate);
		when(orderService.updateOrder(any(OrderDto.class), anyLong())).thenReturn(orderDtoToUpdate);
		mockMvc.perform(put("/order?userId=123").contentType(MediaType.APPLICATION_JSON).content(order_json)
				.with(httpBasic(username,wrongPassword))).andExpect(status().isUnauthorized()).andReturn();
		verify(orderService,times(0)).updateOrder(any(OrderDto.class),anyLong());
	}

	@Test
	void deleteOrderTest() throws Exception{

		when(orderService.deleteOrder(orderId, userId)).thenReturn(true);
		mockMvc.perform(delete("/order?id=123&userId=123")
				.with(httpBasic(username,password))).andExpect(status().isOk()).andReturn();
		verify(orderService,times(1)).deleteOrder(orderId,userId);
	}

	@Test
	void deleteOrderTest_unAuthorized() throws Exception{

		when(orderService.deleteOrder(orderId, userId)).thenReturn(true);
		mockMvc.perform(delete("/order?id=123&userId=123")
				.with(httpBasic(username,wrongPassword))).andExpect(status().isUnauthorized()).andReturn();
		verify(orderService,times(0)).deleteOrder(orderId,userId);
	}
}
