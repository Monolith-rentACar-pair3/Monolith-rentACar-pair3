package com.etiya.rentACar.business.request.rentingBillRequests;

import java.sql.Date;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRentingBillRequest {
	
	@NotNull
	@ApiModelProperty(example = "1970-01-01")
	private Date rentingStartDate;
	
	@NotNull
	@ApiModelProperty(example = "1970-01-01")
	private Date rentingEndDate;
	
	@NotNull
	private int rentingPrice;
	
	@NotNull
	private int userId;
	
	@NotNull
	private int billId;

	@NotNull
	private int rentalId;
}
