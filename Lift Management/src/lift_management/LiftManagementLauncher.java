package lift_management;

import jade.core.Profile;
import jade.core.ProfileImpl;
import repast.simphony.context.Context;
import sajas.core.Agent;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

/**
 * Created by Gustavo on 06/10/2016.
 */
public class LiftManagementLauncher extends RepastSLauncher {
    private ContainerController mainContainer;

    public static void main(String[] args) {
        return;
    }

    @Override
    public Context build(Context<Object> context) {
        super.build(context);
        return context;
    }

    public String getName() {
        return null;
    }

    protected void launchJADE() {
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        mainContainer = rt.createMainContainer(p1);
    }
}
