package com.equalexperts.cart.service;

import java.util.Map.Entry;
import java.util.Optional;

import com.equalexperts.cart.domain.ItemVO;
import com.equalexperts.cart.domain.OrderVO;

public class AddToCartService implements ModifyCart {

	private OrderVO order;

	public AddToCartService(OrderVO order) {
		this.order = order;
	}

	public void add(final String upc, final Double price, final int quantity) {
		validateRequest(upc, price, quantity);
		Optional<Entry<String, ItemVO>> optional = this.order.getOrderItems().entrySet().stream()
				.filter(i -> i.getKey().equals(upc)).findFirst();
		if (optional.isPresent()) {
			ItemVO existingItem = this.order.getOrderItems().get(upc);
			existingItem.setQuantity(existingItem.getQuantity() + quantity);
			this.order.getOrderItems().put(upc, existingItem);
			return;
		}
		this.order.getOrderItems().put(upc, createLineItem(upc, price, quantity));
	}

	private void validateRequest(final String upc, final Double price, final int quantity) {
		if (price < 0) {
			throw new IllegalArgumentException("Invalid price: " + price + " for item:" + upc);
		} else if (quantity <= 0) {
			throw new IllegalArgumentException("Invalid quantity: " + quantity + " for item:" + upc);
		}
	}

	private ItemVO createLineItem(String itemName, Double unitPrice, int quantity) {
		return new ItemVO(itemName, unitPrice, quantity);
	}

	public OrderVO getOrder() {
		return this.order;
	}
}
