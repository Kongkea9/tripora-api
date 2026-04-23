package tripora.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "tours")
@Getter
@Setter
@NoArgsConstructor
public class Tour {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Positive
    private Integer durationDay;

    @Positive
    private Integer durationNight;

    @Column(columnDefinition = "TEXT")
    private String whatsIncluded;

    @Column(columnDefinition = "TEXT")
    private String whatExcluded;

    @Column(length = 255)
    private String province;

    @Column(length = 255)
    private String city;

    @Column(length = 500, nullable = false)
    private String coverImage;


    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private LocalDate createdAt;

     @ManyToOne
     @JoinColumn(name = "category_id")
     private Category category;

     @OneToMany(mappedBy = "tour")
     @OrderBy("sortOrder ASC")
     private Set<TourImage> tourImages = new HashSet<>();


     @OneToMany(mappedBy = "tour")
     @OrderBy("dayNumber ASC")
     private Set<Itinerary> itineraries = new HashSet<>();

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL , orphanRemoval = true)
    @OrderBy("minGuests ASC")
    private Set<TransportOption> transportOptions = new HashSet<>();


}
