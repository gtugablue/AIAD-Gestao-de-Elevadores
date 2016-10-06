package lift_management;

import repast.simphony.context.Context;
import sajas.core.Agent;
import sajas.sim.repasts.RepastSLauncher;

/**
 * Created by Gustavo on 06/10/2016.
 */
public class World extends RepastSLauncher {
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

    }
}
