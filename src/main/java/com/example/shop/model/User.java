package com.example.shop.model;

import com.example.shop.util.MapJsonConverter;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "delete_time is null")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 行号就是 id 值
    private Long id;
    private String openid;
    private String nickname;
    private Long unifyUid;
    private String email;
    private String password;
    private String mobile;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @Convert(converter = MapJsonConverter.class)
    private Map<String, Object> wxProfile;

    // coupon
    // order
}
