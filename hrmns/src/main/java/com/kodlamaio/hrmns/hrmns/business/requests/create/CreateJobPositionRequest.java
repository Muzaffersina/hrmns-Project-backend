package com.kodlamaio.hrmns.hrmns.business.requests.create;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobPositionRequest {
	
	@NotNull
	@Size(min = 1 )
	private String name;


}
