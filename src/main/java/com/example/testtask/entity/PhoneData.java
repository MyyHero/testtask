package com.example.testtask.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "phone_data", uniqueConstraints = @UniqueConstraint(columnNames = "phone"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Телефон пользователя")
public class PhoneData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_seq")
    @SequenceGenerator(name = "phone_seq", sequenceName = "phone_seq", allocationSize = 1)
    @Schema(description = "ID телефона", example = "5")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "Пользователь, владеющий этим телефоном")
    private User user;

    @Column(nullable = false, length = 13, unique = true)
    @Schema(description = "Номер телефона (только цифры, без плюса)", example = "79207865432")
    private String phone;
}
