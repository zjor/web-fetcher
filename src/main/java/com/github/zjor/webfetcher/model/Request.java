package com.github.zjor.webfetcher.model;

import com.github.zjor.webfetcher.enumeration.RequestStatus;
import com.github.zjor.webfetcher.model.base.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request")
public class Request extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "url")
  private String url;

  @Column(name = "content_length")
  private Integer contentLength;

  @Column(name = "webhook_url")
  private String webhook_url;

  @Column(name = "last_status")
  @Enumerated(value = EnumType.STRING)
  private RequestStatus lastStatus;

  @Column(name = "error")
  private String error;
}
