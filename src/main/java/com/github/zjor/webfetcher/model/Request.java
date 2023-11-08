package com.github.zjor.webfetcher.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.zjor.webfetcher.enumeration.RequestStatus;
import com.github.zjor.webfetcher.model.base.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @JsonIgnore
  @Column(name = "url")
  private String url;

  @Column(name = "content_length")
  private Integer contentLength;

  @Column(name = "download_url")
  private String downloadUrl;

  @JsonIgnore
  @Column(name = "webhook_url")
  private String webhookUrl;

  @Column(name = "last_status")
  @Enumerated(value = EnumType.STRING)
  private RequestStatus lastStatus;

  @Column(name = "error")
  private String error;
}
