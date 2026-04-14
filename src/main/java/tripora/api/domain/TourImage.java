package tripora.api.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "tour_images",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tour_id", "sort_order"}))
@Getter
@Setter
@NoArgsConstructor
public class TourImage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 500, nullable = false)
    private String imageUrl;


    @Column(nullable = false)
    private Integer sortOrder;


    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;


}
