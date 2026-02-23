package com.wms.customer_management_service.dto.customer;

import com.wms.customer_management_service.enums.CustomerStatus;
import com.wms.customer_management_service.enums.AddressType;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

/**
 * DTO for customer API responses.
 */
@Data
@Schema(description = "Customer response")
public class CustomerResponse {
	@Schema(description = "Customer ID", example = "3a6e5f84-5717-4562-b3fc-2c3f9636afa6")
	private UUID customerId;

	@Schema(description = "Customer name", example = "Saman Kumara")
	private String customerName;

	@Schema(description = "Customer email", example = "saman@email.com")
	private String email;

	@Schema(description = "Customer phone", example = "0771234567")
	private String phone;

	@Schema(description = "Customer status", example = "ACTIVE")
	private CustomerStatus status;

	@Schema(description = "List of addresses")
	private List<AddressResponse> addresses;

	@Data
	@Schema(description = "Address response")
	public static class AddressResponse {
		@Schema(description = "Address ID", example = "3a6e5f84-5717-4562-b3fc-2c3f9636afa6")
		private UUID addressId;

		@Schema(description = "Address type", example = "BILLING")
		private AddressType type;

		@Schema(description = "Line 1", example = "No 123, Orchid Lane")
		private String line1;

		@Schema(description = "Line 2", example = "Apt 4")
		private String line2;

		@Schema(description = "City", example = "Colombo")
		private String city;

		@Schema(description = "District", example = "Western")
		private String district;

		@Schema(description = "Postal code", example = "10100")
		private String postalCode;

		@Schema(description = "Country", example = "Sri Lanka")
		private String country;
	}
}
