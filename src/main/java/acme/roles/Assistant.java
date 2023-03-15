
package acme.roles;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import acme.framework.data.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Assistant extends AbstractRole {
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Length(max = 76)
	protected String			supervisor;

	@NotBlank
	@Length(max = 101)
	protected String			expertiseFields;

	@NotBlank
	@Length(max = 101)
	protected String			résumé;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
