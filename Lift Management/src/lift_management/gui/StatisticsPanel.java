package lift_management.gui;

import java.awt.EventQueue;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.Series;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.BorderLayout;
import javax.swing.JButton;

public class StatisticsPanel {

	private JFrame frmPerformance;
	private XYSeries series;
	private static StatisticsPanel window = null;
	long ticks = 0;

	public static StatisticsPanel getInstance() {
		if (window == null) {
			window = new StatisticsPanel();
		}

		return window;
	}

	/**
	 * Launch the application.
	 */
	public void run(/*info required by arg*/) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
					window.frmPerformance.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	private StatisticsPanel() {
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPerformance = new JFrame();
		frmPerformance.setTitle("Performance");
		frmPerformance.setBounds(100, 100, 450, 493);
		frmPerformance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmPerformance.getContentPane().add(tabbedPane, BorderLayout.NORTH);

		JButton btnNewButton = new JButton("New button");
		JButton btnNewButton1 = new JButton("New button1");
		JButton btnNewButton2 = new JButton("New button2");
		JButton btnNewButton3 = new JButton("New button3");

		series = new XYSeries( "Random Data" );         
		Second current = new Second( );         
		double value = 100.0;         
		for (int i = 0; i < 4000; i++)    
		{
			try 
			{
				value = value + Math.random( ) - 0.5;                 
				series.add(current.getSecond(), new Double( value ) );                 
				current = ( Second ) current.next( ); 
			}
			catch ( SeriesException e ) 
			{
				System.err.println("Error adding to series");
			}
		}



		final XYSeriesCollection dataset = new XYSeriesCollection();        
		dataset.addSeries(series);
		final JFreeChart chart = ChartFactory.createTimeSeriesChart(             
				"Lift Performance", 
				"Ticks",              
				"# requests",              
				dataset,             
				false,              
				false,              
				false);

		final ChartPanel chartPanel = new ChartPanel( chart );         
		chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 370 ) );         
		chartPanel.setMouseZoomable( true , false );   


		//TODO show a graph detailing the number of requests completed by each lift
		tabbedPane.addTab("Requests by Lift", null, chartPanel, null);

		//TODO show the capacity of each lift
		tabbedPane.addTab("Load by Lift", null, btnNewButton1, null);

		//TODO show the mean waiting time (also max and min)
		tabbedPane.addTab("Wait time", null, btnNewButton2, null);

		//TODO show current and total number of requests, (no) use time of the lift, distance traveled, min/max/avg load
		tabbedPane.addTab("Information by lift", null, btnNewButton3, null);
	}

	public void incOneTick() {
		ticks++;
		//TODO update info of the dataset
	}

}
