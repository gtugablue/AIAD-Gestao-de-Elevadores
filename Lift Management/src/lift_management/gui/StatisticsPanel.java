package lift_management.gui;

import java.awt.EventQueue;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.GradientPaintTransformType;

import java.awt.*;
import javax.swing.*;

import org.jfree.chart.plot.dial.*;
import org.jfree.ui.StandardGradientPaintTransformer;


import lift_management.agents.Lift;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

public class StatisticsPanel {
	//Requests by lift
	XYSeriesCollection datasetRequestsByLift = new XYSeriesCollection();

	//Load by Lift
	DefaultValueDataset datasetLiftLoad = new DefaultValueDataset(0);
	JLabel lbAvgLoad = new JLabel("Average load: 0");
	JSpinner spinnerLoadLift = new JSpinner();
	JLabel lblAverageOccupation = new JLabel("Average Occupation: 0%");
	
	//Average Waiting Time
	XYSeriesCollection datasetAvgWaitingTime = new XYSeriesCollection();

	private static StatisticsPanel window = null;
	List<Lift> lifts;
	Color colors[] = {Color.BLACK, Color.BLUE, Color.CYAN, Color.GRAY,
					Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.ORANGE, Color.RED};

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

		tabbedPane.addTab("Requests by Lift", null, requestsByLiftPane(), null);

		tabbedPane.addTab("Information by Lift", null, loadByLiftPane(), null);

		tabbedPane.addTab("Wait time", null, waitTimePane(), null);

		frmPerformance.setVisible(true);
	}
	

	private Component loadByLiftPane() {
		DialPlot dialplot = new DialPlot();

		dialplot.setView(0.0D, -0.05D, 1.0D, 1.0D);
		dialplot.setDataset(0, datasetLiftLoad);

		StandardDialFrame standarddialframe = new StandardDialFrame();
		standarddialframe.setBackgroundPaint(Color.lightGray);
		standarddialframe.setForegroundPaint(Color.darkGray);
		dialplot.setDialFrame(standarddialframe);

		GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(170, 170, 220));
		DialBackground dialbackground = new DialBackground(gradientpaint);

		dialbackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
		dialplot.setBackground(dialbackground);

		DialTextAnnotation dialtextannotation = new DialTextAnnotation("Current Lift Load");
		dialtextannotation.setFont(new Font("Dialog", 1, 14));
		dialtextannotation.setRadius(0.69999999999999996D);
		dialplot.addLayer(dialtextannotation);

		DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
		dialvalueindicator.setFont(new Font("Dialog", 0, 10));
		dialvalueindicator.setOutlinePaint(Color.darkGray);
		dialvalueindicator.setRadius(0.59999999999999998D);
		dialvalueindicator.setAngle(-103D);
		dialplot.addLayer(dialvalueindicator);

		StandardDialScale standarddialscale = new StandardDialScale(0D, lifts.get(0).getMaxWeight(), -120D, -300D, lifts.get(0).getMaxWeight() / 10, 4);
		standarddialscale.setTickRadius(0.88D);
		standarddialscale.setTickLabelOffset(0.14999999999999999D);
		standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
		dialplot.addScale(0, standarddialscale);

		org.jfree.chart.plot.dial.DialPointer.Pin pin = new org.jfree.chart.plot.dial.DialPointer.Pin(1);
		pin.setRadius(0.55000000000000004D);
		dialplot.addPointer(pin);

		org.jfree.chart.plot.dial.DialPointer.Pointer pointer = new org.jfree.chart.plot.dial.DialPointer.Pointer(0);
		dialplot.addPointer(pointer);

		DialCap dialcap = new DialCap();
		dialcap.setRadius(0.10000000000000001D);
		dialplot.setCap(dialcap);

		JFreeChart jfreechart = new JFreeChart(dialplot);

		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setPreferredSize(new Dimension(560, 370));
		chartpanel.setLayout(null);

		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.anchor = GridBagConstraints.NORTH;
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 0;
		spinnerLoadLift.setBounds(31, 5, 55, 20);
		spinnerLoadLift.setModel(new SpinnerNumberModel(0, 0, lifts.size()-1, 1));
		chartpanel.add(spinnerLoadLift);
		lbAvgLoad.setBounds(96, 8, 126, 14);

		chartpanel.add(lbAvgLoad);
		
		JLabel lblLift = new JLabel("Lift");
		lblLift.setBounds(10, 8, 27, 14);
		chartpanel.add(lblLift);
		
		lblAverageOccupation.setBounds(245, 8, 147, 14);
		chartpanel.add(lblAverageOccupation);

		return chartpanel;
	}

	private Component waitTimePane() {
		datasetAvgWaitingTime.addSeries(new XYSeries("Average Waiting Time"));

		//create the chart
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Average Waiting Time",
				"Ticks",
				"Time Ticks",
				datasetAvgWaitingTime,             
				PlotOrientation.VERTICAL, true, false, false);

		final XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		
		renderer.setSeriesPaint(0, Color.RED);

		plot.setRenderer(renderer);

		//chart pane size
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 370));
		chartPanel.setMouseZoomable(true, false);

		return chartPanel;
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

	private void updateRequestsByLiftDataset(long ticks) {
		for (int i = 0; i < lifts.size(); i++) {
			datasetRequestsByLift.getSeries(i).add(ticks, lifts.get(i).getTasks().size());
		}
	}

	private void updateLoadByLift(long ticks) {
		int liftIndex = (Integer) spinnerLoadLift.getValue();
		datasetLiftLoad.setValue(lifts.get(liftIndex).getCurrentWeight());
		lbAvgLoad.setText("Average load: " + Math.round(lifts.get(liftIndex).getAvgLoad()));
		double operationTime = lifts.get(liftIndex).getOperatingTime();
		lblAverageOccupation.setText("Average Occupation: " + Math.round((operationTime / ticks) * 100) + "%");
	}

	private void updateAvgWaitingTime(double avgWaitingTime, long ticks) {
		if (datasetAvgWaitingTime.getSeries().isEmpty())
			return;
		
		datasetAvgWaitingTime.getSeries(0).add(ticks, avgWaitingTime);
	}

	public void incTick(long ticksToNextRun, double avgWaitingTime) {
		updateRequestsByLiftDataset(ticksToNextRun);
		updateLoadByLift(ticksToNextRun);
		updateAvgWaitingTime(avgWaitingTime, ticksToNextRun);
	}

	public void updateLiftTimes() {
		for (Lift lf : lifts)
			lf.updateOperatingTime();
	}
}
