package com.tcc.jogodememoria.backend.teacher.models;

import com.tcc.jogodememoria.backend.user.models.UserModel;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "table_teacher")
public class TeacherModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String subject;

  @OneToOne
  @JoinColumn(name = "user_id")
  private UserModel userModel;
}
