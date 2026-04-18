package tripora.api.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "guides")
@Getter
@Setter
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 255)
    private String bio;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(length = 15, unique = true)
    private String phone;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;
}