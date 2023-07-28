package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

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
    @Column(length = 100, nullable = false)
    private String name;

    //развёрнутое описание.
    @Column(length = 420, nullable = false)
    private String description;

    //статус о том, доступна или нет вещь для аренды.
    @Column(name = "is_available")
    private Boolean available;

    //владелец вещи.
    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private User owner;

    /*если вещь была создана по запросу другого пользователя, то в этом
      поле будет храниться ссылка на соответствующий запрос*/
    @Transient
    private ItemRequest request;
}