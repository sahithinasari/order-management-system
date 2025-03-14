package com.redshift.order_management_system.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
    private String recipient;
    public String toString(NotificationDto notification){
        return notification.getMessage();
    }
}
