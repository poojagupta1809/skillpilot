package com.thoughtworks.skillpilot.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private int userId;

  @Column(name = "user_name", nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  private Integer status;

  @OneToMany(mappedBy = "enrollmentId", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Enrollment> enrollments;

  private String role;

  public User(String username, String password) {
    this.password = password;
    this.username = username;
  }

  public User() {}

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Set<Enrollment> getEnrollments() {
    return enrollments;
  }

  public void setEnrollments(Set<Enrollment> enrollments) {
    this.enrollments = enrollments;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
