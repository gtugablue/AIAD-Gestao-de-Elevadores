package lift_management.onto;

import jade.content.AgentAction;
import lift_management.calls.Call;

public class ServiceExecutionRequest implements AgentAction {
	
	private static final long serialVersionUID = 1L;
	
	private String serviceName;
	private Call call;

	public ServiceExecutionRequest() {
	}
	
	public ServiceExecutionRequest(String serviceName, Call call) {
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
