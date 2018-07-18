package com.gether.research.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User implements Serializable {

  private Long id;
  private String name;
  private String email;
  private String password;

}