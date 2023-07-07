
package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumPublishService extends AbstractService<Company, Practicum> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Practicum practicum;

		practicum = this.repository.findPracticumById(super.getRequest().getData("id", int.class));

		status = practicum != null && super.getRequest().getPrincipal().hasRole(practicum.getCompany());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Practicum object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findPracticumById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Practicum object) {
		assert object != null;

		super.bind(object, "code", "title", "anAbstract", "goals", "draftMode", "estimatedTime");
	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Practicum existing;

			existing = this.repository.findPracticumByCode(object.getCode());
			super.state(existing == null || existing.getId() == object.getId(), "code", "company.practicum.error.code.duplicated");
		}

	}
	@Override
	public void perform(final Practicum object) {
		assert object != null;

		object.setDraftMode(false);

		this.repository.save(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		Double estimatedTime;
		Tuple tuple;
		Collection<PracticumSession> sessions;

		sessions = this.repository.findManyPracticumSessionsByPracticumId(object.getId());
		estimatedTime = 0.;
		if (!sessions.isEmpty())
			estimatedTime = object.estimatedTime(sessions);

		tuple = super.unbind(object, "code", "title", "abstraction", "goals", "draftMode");
		tuple.put("estimatedTime", estimatedTime);

		super.getResponse().setData(tuple);
	}

}
