package lift_management.onto;

import java.util.List;

import jade.content.Predicate;
import lift_management.Call;
import lift_management.Human;

public class ServiceProposalRequest implements Predicate {
	
	private static final long serialVersionUID = 1L;
	
	private String serviceName;
	private Call call;
	private List<Human> humans;
	
	public ServiceProposalRequest() {
		
	}

	public ServiceProposalRequest(String serviceName, Call call, List<Human> humans) {
		this.serviceName = serviceName;
		this.call = call;
		this.humans = humans;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Call getCall() {
		return call;
	}

	public void setCall(Call call) {
		this.call = call;
	}

	public List<Human> getHumans() {
		return humans;
	}

	public void setHumans(List<Human> humans) {
		this.humans = humans;
	}
}
