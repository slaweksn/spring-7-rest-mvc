package guru.springframework.spring7restmvc.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	
	private UUID id;
    private Integer version;
	private String customerName;
	private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    
}
