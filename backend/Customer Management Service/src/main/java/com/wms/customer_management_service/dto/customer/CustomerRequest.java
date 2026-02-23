package com.wms.customer_management_service.dto.customer;


import com.wms.customer_management_service.enums.AddressType;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for customer registration/update requests.
 */
@Data
@Schema(description = "Customer registration/update request")
public class CustomerRequest {
	@Schema(description = "Customer name", example = "Saman Kumara")
	@NotBlank(message = "Customer name is required")
	private String customerName;

	@Schema(description = "Customer email", example = "saman@email.com")
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@Schema(description = "Customer phone", example = "0771234567")
	@NotBlank(message = "Phone is required")
	private String phone;

	@Schema(description = "List of addresses")
	@NotNull(message = "Addresses are required")
	private List<AddressRequest> addresses;

	@Data
	@Schema(description = "Address request")
	public static class AddressRequest {
		@Schema(description = "Address type", example = "BILLING")
		@NotNull(message = "Address type is required")
		private AddressType type;

		@Schema(description = "Line 1", example = "No 123, Orchid Lane")
		@NotBlank(message = "Line1 is required")
		private String line1;

		@Schema(description = "Line 2", example = "Apt 4")
		private String line2;

		@Schema(description = "City", example = "Colombo")
		@NotBlank(message = "City is required")
		private String city;

		@Schema(description = "District", example = "Western")
		@NotBlank(message = "District is required")
		private String district;

		@Schema(description = "Postal code", example = "10100")
		@NotBlank(message = "Postal code is required")
		private String postalCode;

		@Schema(description = "Country", example = "Sri Lanka")
		@NotBlank(message = "Country is required")
		private String country;
	}
}
