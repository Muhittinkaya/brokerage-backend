package entity;

import enumaration.OrderSize;
import enumaration.OrderStatus;
import jakarta.persistence.Entity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Order {
    private Long id;
    private Long customerId;
    private String assetName;
    private OrderSize orderSize;
    private BigDecimal size;
    private BigDecimal price;
    private OrderStatus orderStatus;
    private LocalDateTime createDate;
}
