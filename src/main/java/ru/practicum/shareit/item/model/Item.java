package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "items")
public class Item {
    //уникальный идентификатор вещи.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //краткое название.
    @NotBlank
    @Column(length = 100, nullable = false)
    private String name;

    //развёрнутое описание.
    @NotBlank
    @Column(length = 420, nullable = false)
    private String description;

    //статус о том, доступна или нет вещь для аренды.
    @NotNull
    @EqualsAndHashCode.Exclude
    @Column(name = "is_available")
    private Boolean available;

    //владелец вещи.
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private User owner;

    /*если вещь была создана по запросу другого пользователя, то в этом
      поле будет храниться ссылка на соответствующий запрос*/
    @Transient
    private ItemRequest request;
}