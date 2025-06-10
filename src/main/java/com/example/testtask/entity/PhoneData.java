package com.example.testtask.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
