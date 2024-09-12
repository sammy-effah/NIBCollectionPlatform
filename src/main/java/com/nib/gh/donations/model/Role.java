package com.nib.gh.donations.model;

import lombok.Data;
import java.util.Optional;
import jakarta.persistence.*;

@Entity
@Data
// @Table(name = "role", schema = "funeral_collections")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    public Optional<Users> stream() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stream'");
    }

}
