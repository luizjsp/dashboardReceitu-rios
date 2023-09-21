package br.com.mv.clinic.domain.prescription;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;

import br.com.mv.clinic.domain.AbstractAuditingEntity;
import br.com.mv.clinic.domain.attendance.Attendance;
import br.com.mv.clinic.domain.ehr.Segment;

@Entity
@Table(name = "prescription")
@SuppressWarnings("serial")
public class Prescription extends AbstractAuditingEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	
	@ManyToOne
	@JoinColumn(name = "id_attendance", nullable = false)
	private Attendance attendance;
	
	@Column(name="active",  nullable = false)
	private boolean active=true;
	
	@CreatedDate
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "created_date", updatable = true)
	private DateTime createdDate = DateTime.now();
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "segment_id")
	private Segment segment;
	
	@Column(nullable=false)
	private Integer version;
	
	@Column(name="hash_key", unique = true)
	private String hashKey;

	public Prescription(){}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Attendance getAttendance() {
		return attendance;
	}

	public void setAttendance(Attendance attendance) {
		this.attendance = attendance;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getHashKey() {
		return hashKey;
	}

	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}
}
