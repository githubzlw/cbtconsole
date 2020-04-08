package com.importExpress.pojo;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransportMethod {
	private String time;
	private List<String> method;

}
