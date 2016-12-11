package lift_management.onto;

import jade.content.Predicate;
import lift_management.calls.Call;

public class ServiceProposalRequest implements Predicate {
	
	private static final long serialVersionUID = 1L;
	
	private String serviceName;
	private Call call;
	
	public ServiceProposalRequest() {
		
	}

	public ServiceProposalRequest(String serviceName, Call call) {
		this.serviceName = serviceName;
		this.call = call;
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
}
