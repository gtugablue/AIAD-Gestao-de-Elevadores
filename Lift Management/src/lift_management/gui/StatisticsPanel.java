package lift_management.gui;

import java.awt.EventQueue;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.Series;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import lift_management.agents.Lift;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;

public class StatisticsPanel {
	XYSeriesCollection datasetRequestsByLift = new XYSeriesCollection();
	private static StatisticsPanel window = null;
	List<Lift> lifts;
	long ticks = 0;
	//TODO it should be used something else
	Color colors[] = {Color.BLACK, Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.ORANGE, Color.RED};

	private StatisticsPanel() {
	}

	public static StatisticsPanel getInstance() {
		if (window == null) {
			window = new StatisticsPanel();
		}

		return window;
	}

	/**
	 * Launch the application.
	 * @param lifts 
	 */
	public void run(List<Lift> lifts) {
		this.lifts = lifts;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		JFrame frmPerformance = new JFrame();
		frmPerformance.setTitle("Performance");
		frmPerformance.setBounds(100, 100, 450, 493);
		frmPerformance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmPerformance.getContentPane().add(tabbedPane, BorderLayout.NORTH);

		//TODO show a graph detailing the number of requests completed by each lift
		tabbedPane.addTab("Requests by Lift", null, requestsByLiftPane(), null);

		//TODO show the capacity of each lift
		tabbedPane.addTab("Load by Lift", null, loadByLiftPane(), null);

		//TODO show the mean waiting time (also max and min)
		tabbedPane.addTab("Wait time", null, waitTimePane(), null);

		//TODO show current and total number of requests, (no) use time of the lift, distance traveled, min/max/avg load
		tabbedPane.addTab("Information by lift", null, informationByLiftPane(), null);
		
		frmPerformance.setVisible(true);
	}

	private Component loadByLiftPane() {
		return null;
	}

	private Component informationByLiftPane() {
		return null;
	}

	private Component waitTimePane() {
		return null;
	}

	private Component requestsByLiftPane() {
		//each lift has a series
		for (int i = 0; i < lifts.size(); i++) {
			datasetRequestsByLift.addSeries(new XYSeries("Lift " + i));
		}
		
		//create the chart
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Lift Performance",
				"Ticks",
				"Number of Tasks",
				datasetRequestsByLift,             
				PlotOrientation.VERTICAL, true, false, false);

		final XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		
		//different colors for different lifts
		for (int i = 0; i < lifts.size(); i++) {
			renderer.setSeriesPaint(i, colors[i % colors.length]);
		}
		plot.setRenderer(renderer);

		//chart pane size
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 370));
		chartPanel.setMouseZoomable(true, false);

		return chartPanel;
	}
	
	private void updateRequestsByLiftDataset() {
		for (int i = 0; i < lifts.size(); i++) {
			datasetRequestsByLift.getSeries(i).add(ticks, lifts.get(i).getTasks().size());
		}
	}

	public void incTick(long ticksToNextRun) {
		ticks += ticksToNextRun;
		//TODO update info of the dataset
		updateRequestsByLiftDataset();
	}

}
